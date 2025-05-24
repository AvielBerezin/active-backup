package aviel.scratch.active_backup.world_events.competition_events;

import aviel.scratch.active_backup.world_events.WorldEvents;
import aviel.scratch.active_backup.competition_events.StartAsWeak;
import aviel.scratch.network_api.ActiveBackupCompetition;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WorldEventsStartAsWeak implements WorldEvents {
    private static final Logger LOGGER = LogManager.getLogger();

    private final StartAsWeak startAsWeak;
    private final EventConcreteData data;

    public WorldEventsStartAsWeak(StartAsWeak startAsWeak, EventConcreteData data) {
        this.startAsWeak = startAsWeak;
        this.data = data;
    }

    @Override
    public WorldEvents onPeerUpdate(ActiveBackupCompetition peer) {
        LOGGER.info("onPeerUpdate({})", peer);
        data.updatePeer(peer.id(), peer.strength());
        if (data.amStrongest()) {
            return new WorldEventsStartAsStrongest(startAsWeak.onAmStrongest(), data);
        }
        return this;
    }

    @Override
    public WorldEvents onPeerLost(long id) {
        LOGGER.info("onPeerLost({})", id);
        data.removePeer(id);
        if (data.amStrongest()) {
            return new WorldEventsStartAsStrongest(startAsWeak.onAmStrongest(), data);
        }
        return this;
    }

    @Override
    public WorldEvents onStrengthChange(int newStrength) {
        LOGGER.info("onStrengthChange({})", newStrength);
        data.updateSelf(newStrength);
        if (data.amStrongest()) {
            return new WorldEventsStartAsStrongest(startAsWeak.onAmStrongest(), data);
        }
        return this;
    }

    @Override
    public WorldEvents onWakeupCall() {
        LOGGER.info("onWakeupCall()");
        return new WorldEventsWokeAsWeak(startAsWeak.onWakeupCall(), data);
    }
}
