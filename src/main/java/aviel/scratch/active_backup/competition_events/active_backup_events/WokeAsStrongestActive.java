package aviel.scratch.active_backup.competition_events.active_backup_events;

import aviel.scratch.active_backup.active_backup_events.Active;
import aviel.scratch.active_backup.competition_events.WokeAsStrongest;
import aviel.scratch.active_backup.competition_events.WokeAsWeak;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WokeAsStrongestActive implements WokeAsStrongest {
    private static final Logger LOGGER = LogManager.getLogger();

    private final Active active;

    public WokeAsStrongestActive(Active active) {
        this.active = active;
    }

    @Override
    public WokeAsWeak onMetStronger() {
        LOGGER.info("onMetStronger()");
        return new WokeAsWeakBackup(active.onBackup());
    }
}
