package aviel.scratch.active_backup;

import aviel.scratch.active_backup.abstract_events.StatefulEvents;
import aviel.scratch.active_backup.active_backup_events.StatefulActiveBackup;
import aviel.scratch.network_api.ActiveBackupCompetition;
import aviel.scratch.network_api.TopicListener;
import aviel.scratch.network_api.NetworkApi;
import aviel.scratch.network_api.TopicReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.*;

public class ActiveBackupProvider implements AutoCloseable {
    private static final Logger LOGGER = LogManager.getLogger();

    private final StatefulEvents events;
    private final TopicReader topicReader;
    private final ScheduledFuture<?> wakeupCallTask;
    private final ExecutorService activeBackupEventsExecutor;
    private final ScheduledExecutorService wakeupCallScheduler;

    public ActiveBackupProvider(NetworkApi networkApi, long id, int strength, StatefulActiveBackup activeBackupHandler) {
        events = new StatefulEvents(networkApi.openActiveBackupCompetitionWriter(), id, strength, activeBackupHandler);
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
                    events.onInstanceUpdate(message.id(), message.strength());
                });
            }

            @Override
            public void onWriterLost(long id) {
                activeBackupEventsExecutor.execute(() -> {
                    events.onInstanceLost(id);
                });
            }
        });
    }

    public void modifyStrength(int strength) {
        activeBackupEventsExecutor.execute(() -> {
            events.onStrengthChange(strength);
        });
    }

    @Override
    public void close() {
        LOGGER.info("closing provider...");
        topicReader.close();
        wakeupCallTask.cancel(false);
        wakeupCallScheduler.close();
        activeBackupEventsExecutor.shutdown();
    }
}
