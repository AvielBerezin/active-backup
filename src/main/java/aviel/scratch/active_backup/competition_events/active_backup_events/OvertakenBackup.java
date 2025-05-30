package aviel.scratch.active_backup.competition_events.active_backup_events;

import aviel.scratch.active_backup.active_backup_events.Backup;
import aviel.scratch.active_backup.competition_events.AwakeWeak;
import aviel.scratch.active_backup.competition_events.Overtaken;
import aviel.scratch.active_backup.competition_events.Overtaking;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OvertakenBackup implements Overtaken {
    private static final Logger LOGGER = LogManager.getLogger();

    private final Backup backup;

    public OvertakenBackup(Backup backup) {
        this.backup = backup;
    }

    @Override
    public Overtaking onAmStrongest() {
        LOGGER.info("onAmStrongest()");
        return new OvertakingBackup(backup);
    }

    @Override
    public AwakeWeak onMetOvertook() {
        LOGGER.info("onMetOvertook()");
        return new AwakeWeakBackup(backup);
    }

    @Override
    public AwakeWeak onOvertakenTooLong() {
        LOGGER.info("onOvertakenTooLong()");
        return new AwakeWeakBackup(backup);
    }
}
