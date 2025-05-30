package aviel.scratch.active_backup.competition_events.meta;

public interface BackupState extends StateStrength {
    default boolean isActive() {
        return false;
    }
}
