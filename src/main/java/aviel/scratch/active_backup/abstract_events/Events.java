package aviel.scratch.active_backup.abstract_events;

public interface Events {
    Events onInstanceUpdate(long id, int strength);
    Events onInstanceLost(long id);
    Events onStrengthChange(int newStrength);
    Events onWakeupCall();
}
