package aviel.scratch.active_backup.active_backup_events;

public interface StatefulActiveBackup {
    void onBackup();
    void onActive();
}
