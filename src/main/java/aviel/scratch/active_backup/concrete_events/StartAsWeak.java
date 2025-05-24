package aviel.scratch.active_backup.concrete_events;

public interface StartAsWeak {
    StartAsStrongest onAmStrongest();
    WokeAsWeak onWakeupCall();
}
