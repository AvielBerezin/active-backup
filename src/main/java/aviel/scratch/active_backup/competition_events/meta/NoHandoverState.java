package aviel.scratch.active_backup.competition_events.meta;

public interface NoHandoverState extends StateStrength {
    @Override
    default boolean isHandedOver() {
        return false;
    }
}
