package aviel.scratch.active_backup.competition_events;

import aviel.scratch.active_backup.competition_events.meta.BackupState;
import aviel.scratch.active_backup.competition_events.meta.NoHandoverState;

public interface DormantStrongest extends NoHandoverState, BackupState {
    DormantWeak onMetStronger();
    AwakeStrongest onWakeUp();
}
