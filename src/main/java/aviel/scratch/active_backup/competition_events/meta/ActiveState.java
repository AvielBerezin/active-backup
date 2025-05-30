package aviel.scratch.active_backup.competition_events.meta;

public interface ActiveState extends StateStrength {
    @Override
    default boolean isActive() {
        return true;
    }
}
