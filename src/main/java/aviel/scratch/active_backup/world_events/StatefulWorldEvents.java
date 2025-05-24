package aviel.scratch.active_backup.world_events;

import aviel.scratch.active_backup.world_events.competition_events.EventConcreteData;
import aviel.scratch.active_backup.world_events.competition_events.WorldEventsDormantAsStrongest;
import aviel.scratch.active_backup.active_backup_events.StatefulActiveBackup;
import aviel.scratch.active_backup.active_backup_events.stateful.BackupStateful;
import aviel.scratch.active_backup.competition_events.active_backup_events.DormantAsStrongestBackup;
import aviel.scratch.network_api.ActiveBackupCompetition;

public class StatefulWorldEvents {
    private WorldEvents worldEvents;

    public StatefulWorldEvents(StatefulActiveBackup statefulActiveBackup, EventConcreteData data) {
        worldEvents = new WorldEventsDormantAsStrongest(new DormantAsStrongestBackup(new BackupStateful(statefulActiveBackup)), data);
    }

    public void onPeerUpdate(ActiveBackupCompetition peer) {
        worldEvents = worldEvents.onPeerUpdate(new ActiveBackupCompetition(peer.id(), peer.strength(), ""));
    }

    public void onPeerLost(long id) {
        worldEvents = worldEvents.onPeerLost(id);
    }

    public void onStrengthChange(int newStrength) {
        worldEvents = worldEvents.onStrengthChange(newStrength);
    }

    public void onWakeupCall() {
        worldEvents = worldEvents.onWakeupCall();
    }
}
