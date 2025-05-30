package aviel.scratch.active_backup.world_events;

import aviel.scratch.active_backup.world_events.competition_events.WorldEventsDormantStrongest;
import aviel.scratch.active_backup.world_events.competition_events.data.EventConcreteData;
import aviel.scratch.active_backup.active_backup_events.StatefulActiveBackup;
import aviel.scratch.active_backup.active_backup_events.stateful.BackupStateful;
import aviel.scratch.active_backup.competition_events.active_backup_events.DormantStrongestBackup;
import aviel.scratch.active_backup.world_events.competition_events.data.StrengthModification;
import aviel.scratch.network_api.ActiveBackupCompetition;

import java.time.Instant;

public class StatefulWorldEvents {
    private WorldEvents worldEvents;

    public StatefulWorldEvents(StatefulActiveBackup statefulActiveBackup, EventConcreteData data) {
        worldEvents = WorldEventsDormantStrongest.create(new DormantStrongestBackup(new BackupStateful(statefulActiveBackup)), data);
    }

    public void onPeerUpdate(ActiveBackupCompetition peer) {
        worldEvents = worldEvents.onPeerUpdate(new ActiveBackupCompetition(peer.id(), peer.strength()));
    }

    public void onPeerLost(long id) {
        worldEvents = worldEvents.onPeerLost(id);
    }

    public void onStrengthUpdate(StrengthModification modification) {
        worldEvents = worldEvents.onStrengthUpdate(modification);
    }

    public void onAlarm(Instant triggerTime) {
        worldEvents = worldEvents.onAlarm(triggerTime);
    }

    public void onHandover(Instant instant) {
        worldEvents = worldEvents.onHandover(instant);
    }
}
