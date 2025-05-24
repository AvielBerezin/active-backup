package aviel.scratch.active_backup.competition_events.active_backup_events;

import aviel.scratch.active_backup.active_backup_events.Backup;
import aviel.scratch.active_backup.competition_events.DormantWeak;
import aviel.scratch.active_backup.competition_events.WokeStrongest;
import aviel.scratch.active_backup.competition_events.WokeWeak;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WokeWeakBackup implements WokeWeak {
    private static final Logger LOGGER = LogManager.getLogger();

    private final Backup backup;

    public WokeWeakBackup(Backup backup) {
        this.backup = backup;
    }

    @Override
    public WokeStrongest onAmStrongest() {
        LOGGER.info("onAmStrongest()");
        return new WokeStrongestActive(backup.onActive());
    }

    @Override
    public DormantWeak onTakeANap() {
        LOGGER.info("onTakeANap()");
        return new DormantWeakBackup(backup);
    }
}
