package aviel.scratch.active_backup.competition_events.active_backup_events;

import aviel.scratch.active_backup.active_backup_events.Backup;
import aviel.scratch.active_backup.competition_events.DormantStrongest;
import aviel.scratch.active_backup.competition_events.DormantWeak;
import aviel.scratch.active_backup.competition_events.AwakeWeak;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DormantWeakBackup implements DormantWeak {
    private static final Logger LOGGER = LogManager.getLogger();

    private final Backup backup;
    private final Runnable onWakingUp;

    public DormantWeakBackup(Backup backup, Runnable onWakingUp) {
        this.backup = backup;
        this.onWakingUp = onWakingUp;
    }

    @Override
    public DormantStrongest onAmStrongest() {
        LOGGER.info("onAmStrongest()");
        return new DormantStrongestBackup(backup, onWakingUp);
    }

    @Override
    public AwakeWeak onWakeUp() {
        LOGGER.info("onWakeUp()");
        onWakingUp.run();
        return new AwakeWeakBackup(backup);
    }
}
