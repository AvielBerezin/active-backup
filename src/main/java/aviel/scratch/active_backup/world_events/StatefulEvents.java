package aviel.scratch.active_backup.world_events;

import aviel.scratch.active_backup.world_events.competition_events.EventConcreteData;
import aviel.scratch.active_backup.world_events.competition_events.EventsStartAsStrongest;
import aviel.scratch.active_backup.active_backup_events.StatefulActiveBackup;
import aviel.scratch.active_backup.active_backup_events.stateful.BackupStateful;
import aviel.scratch.active_backup.competition_events.active_backup_events.StartAsStrongestBackup;
import aviel.scratch.network_api.ActiveBackupCompetition;
import aviel.scratch.network_api.TopicWriter;

public class StatefulEvents {
    private Events events;

    public StatefulEvents(TopicWriter<ActiveBackupCompetition> activeBackupCompetitionTopicWriter,
                          long id,
                          int strength,
                          StatefulActiveBackup statefulActiveBackup) {
        events = new EventsStartAsStrongest(new StartAsStrongestBackup(new BackupStateful(statefulActiveBackup)),
                                            new EventConcreteData(id, strength, activeBackupCompetitionTopicWriter));
    }

    public void onPeerUpdate(long id, int strength) {
        events = events.onPeerUpdate(id, strength);
    }

    public void onPeerLost(long id) {
        events = events.onPeerLost(id);
    }

    public void onStrengthChange(int newStrength) {
        events = events.onStrengthChange(newStrength);
    }

    public void onWakeupCall() {
        events = events.onWakeupCall();
    }
}
