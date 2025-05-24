package aviel.scratch.active_backup.concrete_events.active_backup_events;

import aviel.scratch.active_backup.active_backup_events.Backup;
import aviel.scratch.active_backup.concrete_events.WokeAsStrongest;
import aviel.scratch.active_backup.concrete_events.WokeAsWeak;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WokeAsWeakBackup implements WokeAsWeak {
    private static final Logger LOGGER = LogManager.getLogger();

    private final Backup backup;

    public WokeAsWeakBackup(Backup backup) {
        this.backup = backup;
    }

    @Override
    public WokeAsStrongest onAmStrongest() {
        LOGGER.info("onAmStrongest()");
        return new WokeAsStrongestActive(backup.onActive());
    }
}
