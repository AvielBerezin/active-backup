package aviel.scratch.active_backup.world_events.competition_events;

import aviel.scratch.active_backup.competition_events.WokeStrongest;
import aviel.scratch.active_backup.world_events.WorldEvents;
import aviel.scratch.active_backup.world_events.competition_events.data.EventConcreteData;
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
        if (!data.amStrongest()) {
            return new WorldEventsWokeWeak(wokeStrongest.onMetStronger(), data);
        }
        return this;
    }

    @Override
    public WorldEvents onPeerLost(long id) {
        LOGGER.info("onPeerLost({})", id);
        data.removePeer(id);
        return new WorldEventsWokeStrongest(wokeStrongest, data);
    }

    @Override
    public WorldEvents onStrengthChange(StrengthModification newStrength) {
        LOGGER.info("onStrengthChange({})", newStrength);
        data.updateSelf(newStrength);
        if (!data.amStrongest()) {
            return new WorldEventsWokeWeak(wokeStrongest.onMetStronger(), data);
        }
        return this;
    }

    @Override
    public WorldEvents onWakeupCall() {
        LOGGER.info("onWakeupCall()");
        return this;
    }

    @Override
    public WorldEvents onTakeANap() {
        LOGGER.info("onTakeANap()");
        return this;
    }
}
