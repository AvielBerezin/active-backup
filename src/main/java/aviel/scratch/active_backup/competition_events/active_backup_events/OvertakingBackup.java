package aviel.scratch.active_backup.competition_events.active_backup_events;

import aviel.scratch.active_backup.active_backup_events.Backup;
import aviel.scratch.active_backup.competition_events.Overtaken;
import aviel.scratch.active_backup.competition_events.Overtaking;
import aviel.scratch.active_backup.competition_events.Overtook;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OvertakingBackup implements Overtaking {
    private static final Logger LOGGER = LogManager.getLogger();

    private final Backup backup;

    public OvertakingBackup(Backup backup) {
        this.backup = backup;
    }

    @Override
    public Overtaken onMetStronger() {
        LOGGER.info("onMetStronger()");
        return new OvertakenBackup(backup);
    }

    @Override
    public Overtook onAmStrongestNoWeek() {
        LOGGER.info("onAmStrongestNoWeek()");
        return new OvertookActive(backup.onActive());
    }

    @Override
    public Overtook onAmStrongestOvertakingTooLong() {
        LOGGER.info("onAmStrongestOvertakingTooLong()");
        return new OvertookActive(backup.onActive());
    }
}
