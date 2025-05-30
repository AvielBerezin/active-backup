package aviel.scratch.active_backup.world_events;

import aviel.scratch.active_backup.world_events.competition_events.data.StrengthModification;
import aviel.scratch.network_api.ActiveBackupCompetition;

import java.time.Instant;

public interface WorldEvents {
    WorldEvents onPeerUpdate(ActiveBackupCompetition peer);
    WorldEvents onPeerLost(long id);
    WorldEvents onStrengthChange(StrengthModification... modifications);
    WorldEvents onAlarm(Instant triggerInstant);
    WorldEvents onHandover(Instant instant);
}
