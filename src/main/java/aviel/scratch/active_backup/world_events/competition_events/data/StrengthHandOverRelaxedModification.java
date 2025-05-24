package aviel.scratch.active_backup.world_events.competition_events.data;

public final class StrengthHandOverRelaxedModification implements StrengthModification {
    @Override
    public int modify(int strength) {
        return strength & ~0b10;
    }

    @Override
    public String toString() {
        return StrengthHandOverRelaxedModification.class.getSimpleName();
    }
}
