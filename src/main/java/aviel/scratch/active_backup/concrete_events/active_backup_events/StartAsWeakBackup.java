package aviel.scratch.active_backup.concrete_events.active_backup_events;

import aviel.scratch.active_backup.active_backup_events.Backup;
import aviel.scratch.active_backup.concrete_events.StartAsStrongest;
import aviel.scratch.active_backup.concrete_events.StartAsWeak;
import aviel.scratch.active_backup.concrete_events.WokeAsWeak;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StartAsWeakBackup implements StartAsWeak {
    private static final Logger LOGGER = LogManager.getLogger();

    private final Backup backup;

    public StartAsWeakBackup(Backup backup) {
        this.backup = backup;
    }

    @Override
    public StartAsStrongest onAmStrongest() {
        LOGGER.info("onAmStrongest()");
        return new StartAsStrongestBackup(backup);
    }

    @Override
    public WokeAsWeak onWakeupCall() {
        LOGGER.info("onWakeupCall()");
        return new WokeAsWeakBackup(backup);
    }
}
