package aviel.scratch.active_backup.world_events.competition_events.data;

public final class StrengthUserModification implements StrengthModification {
    private final int userComponent;

    public StrengthUserModification(int userComponent) {
        this.userComponent = userComponent;
    }

    @Override
    public int modify(int strength) {
        return (userComponent << USER_POSITION) | (strength & (0b11 << STATE_POSITION));
    }

    @Override
    public String toString() {
        return StrengthUserModification.class.getSimpleName() + "[" + userComponent + ']';
    }
}
