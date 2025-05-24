package aviel.scratch.active_backup.world_events;

import aviel.scratch.active_backup.world_events.competition_events.EventConcreteData;
import aviel.scratch.active_backup.world_events.competition_events.WorldEventsStartAsStrongest;
import aviel.scratch.active_backup.active_backup_events.StatefulActiveBackup;
import aviel.scratch.active_backup.active_backup_events.stateful.BackupStateful;
import aviel.scratch.active_backup.competition_events.active_backup_events.StartAsStrongestBackup;
import aviel.scratch.network_api.ActiveBackupCompetition;
import aviel.scratch.network_api.TopicWriter;

public class StatefulWorldEvents {
    private WorldEvents worldEvents;

    public StatefulWorldEvents(TopicWriter<ActiveBackupCompetition> activeBackupCompetitionTopicWriter,
                               long id,
                               int strength,
                               StatefulActiveBackup statefulActiveBackup) {
        worldEvents = new WorldEventsStartAsStrongest(new StartAsStrongestBackup(new BackupStateful(statefulActiveBackup)),
                                                      new EventConcreteData(id, strength, activeBackupCompetitionTopicWriter));
    }

    public void onPeerUpdate(long id, int strength) {
        worldEvents = worldEvents.onPeerUpdate(id, strength);
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
