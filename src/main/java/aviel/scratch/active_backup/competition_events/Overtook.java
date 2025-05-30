package aviel.scratch.active_backup.competition_events;

import aviel.scratch.active_backup.competition_events.meta.ActiveState;
import aviel.scratch.active_backup.competition_events.meta.HandoverState;

public interface Overtook extends HandoverState, ActiveState {
    OvertookCanceled onMetActiveStronger();
    AwakeStrongest onNoOvertaken();
}
