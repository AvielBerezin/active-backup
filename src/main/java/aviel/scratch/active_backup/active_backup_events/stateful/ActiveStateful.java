package aviel.scratch.active_backup.active_backup_events.stateful;

import aviel.scratch.active_backup.active_backup_events.Active;
import aviel.scratch.active_backup.active_backup_events.Backup;
import aviel.scratch.active_backup.active_backup_events.StatefulActiveBackup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ActiveStateful implements Active {
    private static final Logger LOGGER = LogManager.getLogger();

    private final StatefulActiveBackup statefulActiveBackup;

    public ActiveStateful(StatefulActiveBackup statefulActiveBackup) {
        this.statefulActiveBackup = statefulActiveBackup;
    }

    @Override
    public Backup onBackup() {
        LOGGER.info("onBackup()");
        statefulActiveBackup.onBackup();
        return new BackupStateful(statefulActiveBackup);
    }
}
