package aviel.scratch.active_backup.competition_events.active_backup_events;

import aviel.scratch.active_backup.active_backup_events.Backup;
import aviel.scratch.active_backup.competition_events.DormantAsStrongest;
import aviel.scratch.active_backup.competition_events.DormantAsWeak;
import aviel.scratch.active_backup.competition_events.WokeAsStrongest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DormantAsStrongestBackup implements DormantAsStrongest {
    private static final Logger LOGGER = LogManager.getLogger();

    private final Backup backup;

    public DormantAsStrongestBackup(Backup backup) {
        this.backup = backup;
    }

    @Override
    public DormantAsWeak onMetStronger() {
        LOGGER.info("onMetStronger()");
        return new DormantAsWeakBackup(backup);
    }

    @Override
    public WokeAsStrongest onWakeupCall() {
        LOGGER.info("onWakeupCall()");
        return new WokeAsStrongestActive(backup.onActive());
    }
}
