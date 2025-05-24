package aviel.scratch.active_backup.world_events.competition_events;

import aviel.scratch.active_backup.world_events.WorldEvents;
import aviel.scratch.active_backup.competition_events.WokeAsStrongest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WorldEventsWokeAsStrongest implements WorldEvents {
    private static final Logger LOGGER = LogManager.getLogger();

    private final WokeAsStrongest wokeAsStrongest;
    private final EventConcreteData data;

    public WorldEventsWokeAsStrongest(WokeAsStrongest wokeAsStrongest, EventConcreteData data) {
        this.wokeAsStrongest = wokeAsStrongest;
        this.data = data;
    }

    @Override
    public WorldEvents onPeerUpdate(long id, int strength) {
        LOGGER.info("onPeerUpdate({}, {})", id, strength);
        data.updatePeer(id, strength);
        if (!data.amStrongest()) {
            return new WorldEventsWokeAsWeak(wokeAsStrongest.onMetStronger(), data);
        }
        return this;
    }

    @Override
    public WorldEvents onPeerLost(long id) {
        LOGGER.info("onPeerLost({})", id);
        data.removePeer(id);
        return new WorldEventsWokeAsStrongest(wokeAsStrongest, data);
    }

    @Override
    public WorldEvents onStrengthChange(int newStrength) {
        LOGGER.info("onStrengthChange({})", newStrength);
        data.updateSelf(newStrength);
        if (!data.amStrongest()) {
            return new WorldEventsWokeAsWeak(wokeAsStrongest.onMetStronger(), data);
        }
        return this;
    }

    @Override
    public WorldEvents onWakeupCall() {
        LOGGER.info("onWakeupCall()");
        return this;
    }

    @Override
    public WorldEvents onReceivedSwitchOver() {
        return this;
    }
}
