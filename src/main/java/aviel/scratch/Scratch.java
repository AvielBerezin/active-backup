package aviel.scratch;

import aviel.scratch.active_backup.ActiveBackupProvider;
import aviel.scratch.active_backup.active_backup_events.StatefulActiveBackup;
import aviel.scratch.network_api.NetworkApiMock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Scratch {
    private static final Logger LOGGER = LogManager.getLogger();

    public static void main(String[] args) throws InterruptedException {
        NetworkApiMock networkApiMock = NetworkApiMock.create();
        try (ActiveBackupProvider abProvider = new ActiveBackupProvider(networkApiMock.networkApi(), System.currentTimeMillis(), 10, new StatefulActiveBackup() {
            @Override
            public void onBackup() {
                LOGGER.info("backup");
            }

            @Override
            public void onActive() {
                LOGGER.info("active");
            }
        })) {
            networkApiMock.triggerOnReceivedMessage(1001L, 5);
            networkApiMock.triggerOnReceivedMessage(1002L, 15);
            Thread.sleep(6000);
            abProvider.modifyStrength(20);
            Thread.sleep(1000);
            networkApiMock.triggerOnReceivedMessage(1002L, 30);
            Thread.sleep(1000);
            networkApiMock.triggerOnReceivedMessage(1002L, 10);
            Thread.sleep(1000);
            networkApiMock.triggerOnReceivedMessage(1001L, 30);
            Thread.sleep(1000);
            networkApiMock.triggerOnWriterLost(1001L);
            Thread.sleep(1000);
            networkApiMock.triggerOnWriterLost(1002L);
            Thread.sleep(1000);
        }
    }
}
