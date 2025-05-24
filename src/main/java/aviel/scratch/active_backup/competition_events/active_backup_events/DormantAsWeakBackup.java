package aviel.scratch.active_backup.competition_events.active_backup_events;

import aviel.scratch.active_backup.active_backup_events.Backup;
import aviel.scratch.active_backup.competition_events.DormantAsStrongest;
import aviel.scratch.active_backup.competition_events.DormantAsWeak;
import aviel.scratch.active_backup.competition_events.WokeAsWeak;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DormantAsWeakBackup implements DormantAsWeak {
    private static final Logger LOGGER = LogManager.getLogger();

    private final Backup backup;

    public DormantAsWeakBackup(Backup backup) {
        this.backup = backup;
    }

    @Override
    public DormantAsStrongest onAmStrongest() {
        LOGGER.info("onAmStrongest()");
        return new DormantAsStrongestBackup(backup);
    }

    @Override
    public WokeAsWeak onWakeupCall() {
        LOGGER.info("onWakeupCall()");
        return new WokeAsWeakBackup(backup);
    }
}
