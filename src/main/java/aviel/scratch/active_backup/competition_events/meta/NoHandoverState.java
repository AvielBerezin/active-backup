package aviel.scratch.active_backup.competition_events.meta;

import aviel.scratch.active_backup.world_events.competition_events.data.StrengthHandoverRelaxedModification;
import aviel.scratch.active_backup.world_events.competition_events.data.StrengthHandoverStateModification;

public interface NoHandoverState extends StateStrength {
    @Override
    default StrengthHandoverStateModification handoverModification() {
        return new StrengthHandoverRelaxedModification();
    }
}
