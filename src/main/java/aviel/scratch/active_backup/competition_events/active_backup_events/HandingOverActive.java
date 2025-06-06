package aviel.scratch.active_backup.competition_events.active_backup_events;

import aviel.scratch.active_backup.active_backup_events.Active;
import aviel.scratch.active_backup.competition_events.AwakeStrongest;
import aviel.scratch.active_backup.competition_events.AwakeWeak;
import aviel.scratch.active_backup.competition_events.HandingOver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HandingOverActive implements HandingOver {
    private static final Logger LOGGER = LogManager.getLogger();

    private final Active active;

    public HandingOverActive(Active active) {
        this.active = active;
    }

    @Override
    public AwakeWeak onMetActiveStronger() {
        LOGGER.info("onMetActiveStronger()");
        return new AwakeWeakBackup(active.onBackup());
    }

    @Override
    public AwakeStrongest onHandoverTooLong() {
        LOGGER.info("onHandoverTooLong()");
        return new AwakeStrongestActive(active);
    }
}
