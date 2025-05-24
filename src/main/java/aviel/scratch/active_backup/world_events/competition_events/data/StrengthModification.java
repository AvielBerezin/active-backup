package aviel.scratch.active_backup.world_events.competition_events.data;

public sealed interface StrengthModification
        permits StrengthActiveModification,
                StrengthBackupModification,
                StrengthHandOverModification,
                StrengthHandOverRelaxedModification,
                StrengthUserModification {
    int modify(int strength);
}
