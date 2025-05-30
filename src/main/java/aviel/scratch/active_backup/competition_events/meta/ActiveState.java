package aviel.scratch.active_backup.competition_events.meta;

import aviel.scratch.active_backup.world_events.competition_events.data.StrengthActiveModification;
import aviel.scratch.active_backup.world_events.competition_events.data.StrengthActiveStateModification;

public interface ActiveState extends StateStrength {
    @Override
    default StrengthActiveStateModification activeModification() {
        return new StrengthActiveModification();
    }
}
