package aviel.scratch.active_backup.competition_events.meta;

public interface HandoverState extends StateStrength {
    default boolean isHandedOver() {
        return true;
    }
}
