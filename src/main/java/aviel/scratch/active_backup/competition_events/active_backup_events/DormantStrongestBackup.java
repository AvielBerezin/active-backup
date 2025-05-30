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
    private final Runnable onWakingUp;

    public DormantStrongestBackup(Backup backup, Runnable onWakingUp) {
        this.backup = backup;
        this.onWakingUp = onWakingUp;
    }

    @Override
    public DormantWeak onMetStronger() {
        LOGGER.info("onMetStronger()");
        return new DormantWeakBackup(backup, onWakingUp);
    }

    @Override
    public AwakeStrongest onWakeUp() {
        LOGGER.info("onWakeUp");
        onWakingUp.run();
        return new AwakeStrongestActive(backup.onActive());
    }
}
