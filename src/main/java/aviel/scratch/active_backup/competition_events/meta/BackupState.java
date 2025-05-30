package aviel.scratch.active_backup.competition_events.meta;

import aviel.scratch.active_backup.world_events.competition_events.data.StrengthActiveStateModification;
import aviel.scratch.active_backup.world_events.competition_events.data.StrengthBackupModification;

public interface BackupState extends StateStrength {
    @Override
    default StrengthActiveStateModification activeModification() {
        return new StrengthBackupModification();
    }
}
