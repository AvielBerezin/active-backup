package aviel.scratch.active_backup.world_events.competition_events.data;

public final class StrengthBackupModification implements StrengthActiveStateModification {
    @Override
    public int modify(int strength) {
        return strength & ~(1 << ACTIVE_POSITION);
    }

    @Override
    public String toString() {
        return StrengthBackupModification.class.getSimpleName();
    }
}
