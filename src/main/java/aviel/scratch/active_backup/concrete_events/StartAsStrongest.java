package aviel.scratch.active_backup.concrete_events;

public interface StartAsStrongest {
    StartAsWeak onMetStronger();
    WokeAsStrongest onWakeupCall();
}
