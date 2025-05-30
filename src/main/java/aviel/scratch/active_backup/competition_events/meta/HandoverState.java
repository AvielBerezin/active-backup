package aviel.scratch.active_backup.competition_events.meta;

import aviel.scratch.active_backup.world_events.competition_events.data.StrengthHandoverModification;
import aviel.scratch.active_backup.world_events.competition_events.data.StrengthHandoverStateModification;

public interface HandoverState extends StateStrength {
    @Override
    default StrengthHandoverStateModification handoverModification() {
        return new StrengthHandoverModification();
    }
}
