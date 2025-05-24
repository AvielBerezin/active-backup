package aviel.scratch.active_backup;

import aviel.scratch.active_backup.world_events.StatefulWorldEvents;
import aviel.scratch.active_backup.active_backup_events.StatefulActiveBackup;
import aviel.scratch.active_backup.world_events.competition_events.data.EventConcreteData;
import aviel.scratch.active_backup.world_events.competition_events.data.StrengthHandOverModification;
import aviel.scratch.active_backup.world_events.competition_events.data.StrengthHandOverRelaxedModification;
import aviel.scratch.active_backup.world_events.competition_events.data.StrengthUserModification;
import aviel.scratch.network_api.ActiveBackupCompetition;
import aviel.scratch.network_api.TopicListener;
import aviel.scratch.network_api.NetworkApi;
import aviel.scratch.network_api.TopicReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.*;

public class ActiveBackupProvider implements AutoCloseable {
    private static final Logger LOGGER = LogManager.getLogger();

    private final StatefulWorldEvents events;
    private final TopicReader topicReader;
    private volatile ScheduledFuture<?> wakeUpCallTask;
    private final ExecutorService activeBackupEventsExecutor;
    private final ScheduledExecutorService wakeUpCallScheduler;
    private final TopicReader handOverConsumer;

    public ActiveBackupProvider(NetworkApi networkApi,
                                String site,
                                long id,
                                StatefulActiveBackup activeBackupHandler) {
        events = new StatefulWorldEvents(activeBackupHandler, new EventConcreteData(site, id, networkApi.openActiveBackupCompetitionWriter()));
        activeBackupEventsExecutor = Executors.newSingleThreadExecutor(r -> {
            Thread thread = new Thread(r);
            thread.setName("activeBackupEventsThread");
            return thread;
        });
        activeBackupEventsExecutor.execute(activeBackupHandler::onBackup);
        wakeUpCallScheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread thread = new Thread(r);
            thread.setName("wakeUpCallScheduler");
            return thread;
        });
        wakeUpCallTask = wakeUpCallScheduler.schedule(() -> {
            activeBackupEventsExecutor.execute(events::onWakeUpCall);
        }, 5, TimeUnit.SECONDS);
        topicReader = networkApi.openActiveBackupCompetitionReader(new TopicListener<>() {
            @Override
            public void onReceivedMessage(ActiveBackupCompetition message) {
                activeBackupEventsExecutor.execute(() -> {
                    events.onPeerUpdate(new ActiveBackupCompetition(message.id(), message.strength(), ""));
                });
            }

            @Override
            public void onWriterLost(long id) {
                activeBackupEventsExecutor.execute(() -> {
                    events.onPeerLost(id);
                });
            }
        });
        handOverConsumer = networkApi.openHandOverProvider(() -> {
            activeBackupEventsExecutor.execute(() -> {
                events.onTakeANap();
                events.onStrengthChange(new StrengthHandOverModification());
            });
            wakeUpCallTask = wakeUpCallScheduler.schedule(() -> {
                activeBackupEventsExecutor.execute(() -> {
                    events.onStrengthChange(new StrengthHandOverRelaxedModification());
                });
            }, 5, TimeUnit.SECONDS);
        });
    }

    public void changeStrength(int userStrengthComponent) {
        activeBackupEventsExecutor.execute(() -> {
            events.onStrengthChange(new StrengthUserModification(userStrengthComponent));
        });
    }

    @Override
    public void close() {
        LOGGER.info("closing provider...");
        topicReader.close();
        handOverConsumer.close();
        wakeUpCallTask.cancel(false);
        wakeUpCallScheduler.close();
        activeBackupEventsExecutor.shutdown();
    }
}
