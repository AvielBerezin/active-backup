package aviel.scratch.active_backup.active_backup_events.stateful;

import aviel.scratch.active_backup.active_backup_events.Active;
import aviel.scratch.active_backup.active_backup_events.Backup;
import aviel.scratch.active_backup.active_backup_events.StatefulActiveBackup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BackupStateful implements Backup {
    private static final Logger LOGGER = LogManager.getLogger();

    private final StatefulActiveBackup statefulActiveBackup;

    public BackupStateful(StatefulActiveBackup statefulActiveBackup) {
        this.statefulActiveBackup = statefulActiveBackup;
    }

    @Override
    public Active onActive() {
        LOGGER.info("onActive()");
        statefulActiveBackup.onActive();
        return new ActiveStateful(statefulActiveBackup);
    }
}
