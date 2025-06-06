package aviel.scratch.active_backup.competition_events;

import aviel.scratch.active_backup.competition_events.meta.ActiveState;
import aviel.scratch.active_backup.competition_events.meta.HandoverState;

import java.time.Instant;

public interface AwakeStrongest extends HandoverState, ActiveState {
    AwakeWeak onMetActiveStronger();
    HandingOver onHandover(Instant handoverInstant);
}
