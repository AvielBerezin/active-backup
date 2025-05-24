package aviel.scratch.active_backup.abstract_events;

public interface Events {
    Events onPeerUpdate(long id, int strength);
    Events onPeerLost(long id);
    Events onStrengthChange(int newStrength);
    Events onWakeupCall();
}
