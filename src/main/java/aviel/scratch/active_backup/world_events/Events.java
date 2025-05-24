package aviel.scratch.active_backup.world_events;

public interface Events {
    Events onPeerUpdate(long id, int strength);
    Events onPeerLost(long id);
    Events onStrengthChange(int newStrength);
    Events onWakeupCall();
}
