package aviel.scratch.active_backup.competition_events.meta;

import aviel.scratch.active_backup.world_events.competition_events.data.StrengthActiveStateModification;
import aviel.scratch.active_backup.world_events.competition_events.data.StrengthHandoverStateModification;

public interface StateStrength {
    StrengthHandoverStateModification handoverModification();
    StrengthActiveStateModification activeModification();
}
