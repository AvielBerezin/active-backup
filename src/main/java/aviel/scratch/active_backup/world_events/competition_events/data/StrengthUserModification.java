package aviel.scratch.active_backup.world_events.competition_events.data;

public final class StrengthUserModification implements StrengthModification {
    private final int userComponent;

    public StrengthUserModification(int userComponent) {
        this.userComponent = userComponent;
    }

    @Override
    public int modify(int strength) {
        return (userComponent << 1) | (strength & 1);
    }

    @Override
    public String toString() {
        return StrengthUserModification.class.getSimpleName() + "[" + userComponent + ']';
    }
}
