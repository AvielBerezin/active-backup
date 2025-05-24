package aviel.scratch.active_backup.competition_events.active_backup_events;

import aviel.scratch.active_backup.active_backup_events.Backup;
import aviel.scratch.active_backup.competition_events.StartAsStrongest;
import aviel.scratch.active_backup.competition_events.StartAsWeak;
import aviel.scratch.active_backup.competition_events.WokeAsStrongest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StartAsStrongestBackup implements StartAsStrongest {
    private static final Logger LOGGER = LogManager.getLogger();

    private final Backup backup;

    public StartAsStrongestBackup(Backup backup) {
        this.backup = backup;
    }

    @Override
    public StartAsWeak onMetStronger() {
        LOGGER.info("onMetStronger()");
        return new StartAsWeakBackup(backup);
    }

    @Override
    public WokeAsStrongest onWakeupCall() {
        LOGGER.info("onWakeupCall()");
        return new WokeAsStrongestActive(backup.onActive());
    }
}
