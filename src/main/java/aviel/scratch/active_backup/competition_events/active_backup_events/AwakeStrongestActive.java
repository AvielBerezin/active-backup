package aviel.scratch.active_backup.competition_events.active_backup_events;

import aviel.scratch.active_backup.active_backup_events.Active;
import aviel.scratch.active_backup.competition_events.AwakeStrongest;
import aviel.scratch.active_backup.competition_events.AwakeWeak;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AwakeStrongestActive implements AwakeStrongest {
    private static final Logger LOGGER = LogManager.getLogger();

    private final Active active;

    public AwakeStrongestActive(Active active) {
        this.active = active;
    }

    @Override
    public AwakeWeak onMetStronger() {
        LOGGER.info("onMetStronger()");
        return new AwakeWeakBackup(active.onBackup());
    }
}
