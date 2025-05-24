package aviel.scratch.active_backup.abstract_events;

import aviel.scratch.active_backup.abstract_events.concrete_events.EventConcreteData;
import aviel.scratch.active_backup.abstract_events.concrete_events.EventsStartAsStrongest;
import aviel.scratch.active_backup.active_backup_events.StatefulActiveBackup;
import aviel.scratch.active_backup.active_backup_events.stateful.BackupStateful;
import aviel.scratch.active_backup.concrete_events.active_backup_events.StartAsStrongestBackup;
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

    public void onInstanceUpdate(long id, int strength) {
        events = events.onInstanceUpdate(id, strength);
    }

    public void onInstanceLost(long id) {
        events = events.onInstanceLost(id);
    }

    public void onStrengthChange(int newStrength) {
        events = events.onStrengthChange(newStrength);
    }

    public void onWakeupCall() {
        events = events.onWakeupCall();
    }
}
