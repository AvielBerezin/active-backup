package aviel.scratch.active_backup.world_events.competition_events.data;

public final class StrengthHandoverModification implements StrengthHandoverStateModification {
    @Override
    public int modify(int strength) {
        return strength & ~(1 << HANDOVER_POSITION);
    }

    @Override
    public String toString() {
        return StrengthHandoverModification.class.getSimpleName();
    }
}
