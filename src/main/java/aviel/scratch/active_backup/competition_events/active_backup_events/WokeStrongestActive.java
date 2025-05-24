package aviel.scratch.active_backup.competition_events.active_backup_events;

import aviel.scratch.active_backup.active_backup_events.Active;
import aviel.scratch.active_backup.competition_events.WokeStrongest;
import aviel.scratch.active_backup.competition_events.WokeWeak;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WokeStrongestActive implements WokeStrongest {
    private static final Logger LOGGER = LogManager.getLogger();

    private final Active active;

    public WokeStrongestActive(Active active) {
        this.active = active;
    }

    @Override
    public WokeWeak onMetStronger() {
        LOGGER.info("onMetStronger()");
        return new WokeWeakBackup(active.onBackup());
    }
}
