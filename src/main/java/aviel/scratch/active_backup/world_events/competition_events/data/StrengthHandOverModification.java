package aviel.scratch.active_backup.world_events.competition_events.data;

public final class StrengthHandOverModification implements StrengthModification {
    @Override
    public int modify(int strength) {
        return strength | 0b1;
    }

    @Override
    public String toString() {
        return StrengthHandOverModification.class.getSimpleName();
    }
}
