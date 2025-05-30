package aviel.scratch.active_backup.competition_events.active_backup_events;

import aviel.scratch.active_backup.active_backup_events.Active;
import aviel.scratch.active_backup.competition_events.AwakeWeak;
import aviel.scratch.active_backup.competition_events.OvertookCanceled;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OvertookCanceledActive implements OvertookCanceled {
    private static final Logger LOGGER = LogManager.getLogger();

    private final Active active;

    public OvertookCanceledActive(Active active) {
        this.active = active;
    }

    @Override
    public AwakeWeak onNoOvertaken() {
        LOGGER.info("onNoOvertaken()");
        return new AwakeWeakBackup(active.onBackup());
    }
}
