package aviel.scratch.active_backup.competition_events.active_backup_events;

import aviel.scratch.active_backup.active_backup_events.Backup;
import aviel.scratch.active_backup.competition_events.DormantWeak;
import aviel.scratch.active_backup.competition_events.AwakeStrongest;
import aviel.scratch.active_backup.competition_events.AwakeWeak;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AwakeWeakBackup implements AwakeWeak {
    private static final Logger LOGGER = LogManager.getLogger();

    private final Backup backup;

    public AwakeWeakBackup(Backup backup) {
        this.backup = backup;
    }

    @Override
    public AwakeStrongest onAmStrongest() {
        LOGGER.info("onAmStrongest()");
        return new AwakeStrongestActive(backup.onActive());
    }

    @Override
    public DormantWeak onTakeANap() {
        LOGGER.info("onTakeANap()");
        return new DormantWeakBackup(backup);
    }
}
