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
    private volatile ScheduledFuture<?> wakeupCallTask;
    private final ExecutorService activeBackupEventsExecutor;
    private final ScheduledExecutorService wakeupCallScheduler;
    private final TopicReader handOverConsumer;

    public ActiveBackupProvider(NetworkApi networkApi, String site, long id, int strength, StatefulActiveBackup activeBackupHandler) {
        events = new StatefulWorldEvents(activeBackupHandler, new EventConcreteData(site, id, strength, networkApi.openActiveBackupCompetitionWriter()));
        activeBackupEventsExecutor = Executors.newSingleThreadExecutor(r -> {
            Thread thread = new Thread(r);
            thread.setName("activeBackupEventsThread");
            return thread;
        });
        activeBackupEventsExecutor.execute(activeBackupHandler::onBackup);
        wakeupCallScheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread thread = new Thread(r);
            thread.setName("wakeupCallScheduler");
            return thread;
        });
        wakeupCallTask = wakeupCallScheduler.schedule(() -> {
            activeBackupEventsExecutor.execute(events::onWakeupCall);
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
            wakeupCallTask = wakeupCallScheduler.schedule(() -> {
                activeBackupEventsExecutor.execute(() -> {
                    events.onStrengthChange(new StrengthHandOverRelaxedModification());
                });
            }, 5, TimeUnit.SECONDS);
        });
    }

    public void userStrengthChange(int userStrengthComponent) {
        activeBackupEventsExecutor.execute(() -> {
            events.onStrengthChange(new StrengthUserModification(userStrengthComponent));
        });
    }

    @Override
    public void close() {
        LOGGER.info("closing provider...");
        topicReader.close();
        handOverConsumer.close();
        wakeupCallTask.cancel(false);
        wakeupCallScheduler.close();
        activeBackupEventsExecutor.shutdown();
    }
}
