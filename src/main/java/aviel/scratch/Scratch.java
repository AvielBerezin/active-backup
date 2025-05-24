package aviel.scratch;

import aviel.scratch.active_backup.ActiveBackupProvider;
import aviel.scratch.active_backup.active_backup_events.StatefulActiveBackup;
import aviel.scratch.network_api.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicReference;

public class Scratch {
    private static final Logger LOGGER = LogManager.getLogger();

    public static void main(String[] args) throws InterruptedException {
        AtomicReference<TopicListener<ActiveBackupCompetition>> box = new AtomicReference<>();
        NetworkApi instance = new NetworkApi() {
            @Override
            public TopicReader openActiveBackupCompetitionReader(TopicListener<ActiveBackupCompetition> listener) {
                if (box.get() != null) {
                    throw new IllegalStateException("ActiveBackupCompetitionReader can only be opened once");
                }
                box.set(listener);
                return super.openActiveBackupCompetitionReader(listener);
            }
        };
        try (ActiveBackupProvider abProvider = new ActiveBackupProvider(instance, System.currentTimeMillis(), 10, new StatefulActiveBackup() {
            @Override
            public void onBackup() {
                LOGGER.info("backup");
            }

            @Override
            public void onActive() {
                LOGGER.info("active");
            }

        })) {
            TopicListener<ActiveBackupCompetition> activeBackupCompetitionTopicListener = box.get();
            activeBackupCompetitionTopicListener.onReceivedMessage(new ActiveBackupCompetition(1001L, 5));
            activeBackupCompetitionTopicListener.onReceivedMessage(new ActiveBackupCompetition(1002L, 15));
            Thread.sleep(6000);
            abProvider.modifyStrength(20);
            Thread.sleep(1000);
            activeBackupCompetitionTopicListener.onReceivedMessage(new ActiveBackupCompetition(1002L, 30));
            Thread.sleep(1000);
            activeBackupCompetitionTopicListener.onReceivedMessage(new ActiveBackupCompetition(1002L, 10));
            Thread.sleep(1000);
        }
    }
}
