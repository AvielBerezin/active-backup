package aviel.scratch.active_backup.competition_events.active_backup_events;

import aviel.scratch.active_backup.active_backup_events.Backup;
import aviel.scratch.active_backup.competition_events.DormantStrongest;
import aviel.scratch.active_backup.competition_events.DormantWeak;
import aviel.scratch.active_backup.competition_events.AwakeStrongest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DormantStrongestBackup implements DormantStrongest {
    private static final Logger LOGGER = LogManager.getLogger();

    private final Backup backup;

    public DormantStrongestBackup(Backup backup) {
        this.backup = backup;
    }

    @Override
    public DormantWeak onMetStronger() {
        LOGGER.info("onMetStronger()");
        return new DormantWeakBackup(backup);
    }

    @Override
    public AwakeStrongest onWakeUp() {
        LOGGER.info("onWakeUpCall()");
        return new AwakeStrongestActive(backup.onActive());
    }
}
