package aviel.scratch.active_backup.competition_events.active_backup_events;

import aviel.scratch.active_backup.active_backup_events.Active;
import aviel.scratch.active_backup.competition_events.AwakeStrongest;
import aviel.scratch.active_backup.competition_events.Overtook;
import aviel.scratch.active_backup.competition_events.OvertookCanceled;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OvertookActive implements Overtook {
    private static final Logger LOGGER = LogManager.getLogger();

    private final Active active;

    public OvertookActive(Active active) {
        this.active = active;
    }

    @Override
    public OvertookCanceled onMetActiveStronger() {
        LOGGER.info("onMetActiveStronger()");
        return new OvertookCanceledActive(active);
    }

    @Override
    public AwakeStrongest onNoOvertaken() {
        LOGGER.info("onNoOvertaken()");
        return new AwakeStrongestActive(active);
    }
}
