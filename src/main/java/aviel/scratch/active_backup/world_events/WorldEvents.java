package aviel.scratch.active_backup.world_events;

import aviel.scratch.active_backup.world_events.competition_events.data.StrengthModification;
import aviel.scratch.network_api.ActiveBackupCompetition;

public interface WorldEvents {
    WorldEvents onPeerUpdate(ActiveBackupCompetition peer);
    WorldEvents onPeerLost(long id);
    WorldEvents onStrengthChange(StrengthModification newStrength);
    WorldEvents onWakeupCall();
    WorldEvents onTakeANap();
}
