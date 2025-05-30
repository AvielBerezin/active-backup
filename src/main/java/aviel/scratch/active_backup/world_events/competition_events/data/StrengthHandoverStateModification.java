package aviel.scratch.active_backup.world_events.competition_events.data;

public sealed interface StrengthHandoverStateModification extends StrengthModification
        permits StrengthHandoverModification,
                StrengthHandoverRelaxedModification {
}
