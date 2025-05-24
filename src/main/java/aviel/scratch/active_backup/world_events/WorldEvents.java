package aviel.scratch.active_backup.world_events;

public interface WorldEvents {
    WorldEvents onPeerUpdate(long id, int strength);
    WorldEvents onPeerLost(long id);
    WorldEvents onStrengthChange(int newStrength);
    WorldEvents onWakeupCall();
    WorldEvents onReceivedSwitchOver();
}
