package aviel.scratch.active_backup.competition_events;

import aviel.scratch.active_backup.competition_events.meta.ActiveState;
import aviel.scratch.active_backup.competition_events.meta.NoHandoverState;

public interface HandingOver extends NoHandoverState, ActiveState {
    AwakeWeak onMetActiveStronger();
    AwakeStrongest onHandoverTooLong();
}
