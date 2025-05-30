package aviel.scratch.active_backup.competition_events.meta;

public interface StateStrength {
    boolean isActive();

    boolean isHandedOver();

    default int strength() {
        return (isHandedOver() ? 0b10 : 0) |
               (isActive() ? 0b1 : 0);
    }
}
