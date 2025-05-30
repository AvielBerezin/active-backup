package aviel.scratch.active_backup.competition_events;

import aviel.scratch.active_backup.competition_events.meta.BackupState;
import aviel.scratch.active_backup.competition_events.meta.HandoverState;

public interface Overtaking extends HandoverState, BackupState {
    Overtaken onMetStronger();
    Overtook onAmStrongestNoWeek();
    Overtook onAmStrongestOvertakingTooLong();
}
