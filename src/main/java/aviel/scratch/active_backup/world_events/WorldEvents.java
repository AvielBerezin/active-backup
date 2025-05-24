package aviel.scratch.active_backup.world_events;

import aviel.scratch.network_api.ActiveBackupCompetition;

public interface WorldEvents {
    WorldEvents onPeerUpdate(ActiveBackupCompetition peer);
    WorldEvents onPeerLost(long id);
    WorldEvents onStrengthChange(int newStrength);
    WorldEvents onWakeupCall();
    WorldEvents onReceivedSwitchOver();
}
