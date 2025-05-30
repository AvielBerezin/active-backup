package aviel.scratch.active_backup.competition_events;

import aviel.scratch.active_backup.competition_events.meta.ActiveState;
import aviel.scratch.active_backup.competition_events.meta.HandoverState;

public interface OvertookCanceled extends HandoverState, ActiveState {
    AwakeWeak onNoOvertaken();
}
