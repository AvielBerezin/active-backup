package aviel.scratch.active_backup.world_events.competition_events;

import aviel.scratch.active_backup.competition_events.WokeStrongest;
import aviel.scratch.active_backup.world_events.WorldEvents;
import aviel.scratch.active_backup.world_events.competition_events.data.EventConcreteData;
import aviel.scratch.active_backup.world_events.competition_events.data.StrengthBackupModification;
import aviel.scratch.active_backup.world_events.competition_events.data.StrengthHandOverModification;
import aviel.scratch.active_backup.world_events.competition_events.data.StrengthModification;
import aviel.scratch.network_api.ActiveBackupCompetition;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WorldEventsWokeStrongest implements WorldEvents {
    private static final Logger LOGGER = LogManager.getLogger();

    private final WokeStrongest wokeStrongest;
    private final EventConcreteData data;

    public WorldEventsWokeStrongest(WokeStrongest wokeStrongest, EventConcreteData data) {
        this.wokeStrongest = wokeStrongest;
        this.data = data;
    }

    @Override
    public WorldEvents onPeerUpdate(ActiveBackupCompetition peer) {
        LOGGER.info("onPeerUpdate({})", peer);
        data.updatePeer(peer);
        return decideFate();
    }

    @Override
    public WorldEvents onPeerLost(long id) {
        LOGGER.info("onPeerLost({})", id);
        data.removePeer(id);
        return this;
    }

    @Override
    public WorldEvents onStrengthChange(StrengthModification newStrength) {
        LOGGER.info("onStrengthChange({})", newStrength);
        data.updateSelf(newStrength);
        return decideFate();
    }

    private WorldEvents decideFate() {
        if (!data.amStrongest()) {
            data.updateSelf(new StrengthBackupModification());
            return new WorldEventsWokeWeak(wokeStrongest.onMetStronger(), data);
        }
        return this;
    }

    @Override
    public WorldEvents onWakeUpCall() {
        LOGGER.info("onWakeUpCall()");
        return this;
    }

    @Override
    public WorldEvents onHandOver() {
        LOGGER.info("onTakeANap()");
        return this.onStrengthChange(new StrengthHandOverModification());
    }
}
