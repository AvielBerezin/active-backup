package aviel.scratch.active_backup.competition_events.active_backup_events;

import aviel.scratch.active_backup.active_backup_events.Backup;
import aviel.scratch.active_backup.competition_events.DormantStrongest;
import aviel.scratch.active_backup.competition_events.DormantWeak;
import aviel.scratch.active_backup.competition_events.WokeWeak;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DormantWeakBackup implements DormantWeak {
    private static final Logger LOGGER = LogManager.getLogger();

    private final Backup backup;

    public DormantWeakBackup(Backup backup) {
        this.backup = backup;
    }

    @Override
    public DormantStrongest onAmStrongest() {
        LOGGER.info("onAmStrongest()");
        return new DormantStrongestBackup(backup);
    }

    @Override
    public WokeWeak onWakeupCall() {
        LOGGER.info("onWakeupCall()");
        return new WokeWeakBackup(backup);
    }
}
