package aviel.scratch.active_backup.competition_events;

import aviel.scratch.active_backup.competition_events.meta.BackupState;
import aviel.scratch.active_backup.competition_events.meta.HandoverState;

public interface Overtaken extends HandoverState, BackupState {
    Overtaking onAmStrongest();
    AwakeWeak onMetOvertook();
    AwakeWeak onOvertakenTooLong();
}
