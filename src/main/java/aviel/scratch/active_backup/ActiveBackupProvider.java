package aviel.scratch.active_backup;

import aviel.scratch.active_backup.world_events.StatefulWorldEvents;
import aviel.scratch.active_backup.active_backup_events.StatefulActiveBackup;
import aviel.scratch.active_backup.world_events.competition_events.data.EventConcreteData;
import aviel.scratch.active_backup.world_events.competition_events.data.StrengthUserModification;
import aviel.scratch.network_api.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Instant;
import java.util.concurrent.*;

public class ActiveBackupProvider implements AutoCloseable {
    private static final Logger LOGGER = LogManager.getLogger();

    private final StatefulWorldEvents events;
    private final TopicReader topicReader;
    private volatile ScheduledFuture<?> wakeUpCallTask;
    private final ScheduledExecutorService activeBackupEventsScheduler;
    private final TopicReader handOverConsumer;

    public ActiveBackupProvider(NetworkApi networkApi,
                                long id,
                                StatefulActiveBackup activeBackupHandler) {
        activeBackupEventsScheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread thread = new Thread(r);
            thread.setName("activeBackupEventsThread");
            return thread;
        });
        TopicWriter<ActiveBackupCompetition> selfTopicWriter = networkApi.openActiveBackupCompetitionWriter();
        EventConcreteData data = new EventConcreteData(id, selfTopicWriter);
        events = new StatefulWorldEvents(activeBackupHandler, data, data::wakeUp);
        Instant startupInstant = Instant.now();
        activeBackupEventsScheduler.execute(activeBackupHandler::onBackup);
        wakeUpCallTask = activeBackupEventsScheduler.schedule(() -> {
            activeBackupEventsScheduler.execute(() -> events.onAlarm(startupInstant));
        }, 5, TimeUnit.SECONDS);
        topicReader = networkApi.openActiveBackupCompetitionReader(new TopicListener<>() {
            @Override
            public void onReceivedMessage(ActiveBackupCompetition message) {
                activeBackupEventsScheduler.execute(() -> {
                    events.onPeerUpdate(new ActiveBackupCompetition(message.id(), message.strength()));
                });
            }

            @Override
            public void onWriterLost(long id) {
                activeBackupEventsScheduler.execute(() -> {
                    events.onPeerLost(id);
                });
            }
        });
        handOverConsumer = networkApi.openHandoverProvider(() -> {
            Instant handoverInstant = Instant.now();
            activeBackupEventsScheduler.execute(() -> events.onHandover(handoverInstant));
            wakeUpCallTask = activeBackupEventsScheduler.schedule(() -> {
                activeBackupEventsScheduler.execute(() -> {
                    events.onAlarm(handoverInstant);
                });
            }, 4, TimeUnit.SECONDS);
        });
    }

    public void changeStrength(int userStrengthComponent) {
        activeBackupEventsScheduler.execute(() -> {
            events.onStrengthUpdate(new StrengthUserModification(userStrengthComponent));
        });
    }

    @Override
    public void close() {
        LOGGER.info("closing provider...");
        topicReader.close();
        handOverConsumer.close();
        wakeUpCallTask.cancel(false);
        activeBackupEventsScheduler.close();
    }
}
