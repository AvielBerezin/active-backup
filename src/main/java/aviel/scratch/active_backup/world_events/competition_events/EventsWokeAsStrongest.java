package aviel.scratch.active_backup.world_events.competition_events;

import aviel.scratch.active_backup.world_events.Events;
import aviel.scratch.active_backup.competition_events.WokeAsStrongest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EventsWokeAsStrongest implements Events {
    private static final Logger LOGGER = LogManager.getLogger();

    private final WokeAsStrongest wokeAsStrongest;
    private final EventConcreteData data;

    public EventsWokeAsStrongest(WokeAsStrongest wokeAsStrongest, EventConcreteData data) {
        this.wokeAsStrongest = wokeAsStrongest;
        this.data = data;
    }

    @Override
    public Events onPeerUpdate(long id, int strength) {
        LOGGER.info("onPeerUpdate({}, {})", id, strength);
        data.updatePeer(id, strength);
        if (!data.amStrongest()) {
            return new EventsWokeAsWeak(wokeAsStrongest.onMetStronger(), data);
        }
        return this;
    }

    @Override
    public Events onPeerLost(long id) {
        LOGGER.info("onPeerLost({})", id);
        data.removePeer(id);
        return new EventsWokeAsStrongest(wokeAsStrongest, data);
    }

    @Override
    public Events onStrengthChange(int newStrength) {
        LOGGER.info("onStrengthChange({})", newStrength);
        data.updateSelf(newStrength);
        if (!data.amStrongest()) {
            return new EventsWokeAsWeak(wokeAsStrongest.onMetStronger(), data);
        }
        return this;
    }

    @Override
    public Events onWakeupCall() {
        LOGGER.info("onWakeupCall()");
        return this;
    }
}
