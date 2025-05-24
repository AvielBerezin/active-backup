package aviel.scratch.active_backup.abstract_events.concrete_events;

import aviel.scratch.active_backup.abstract_events.Events;
import aviel.scratch.active_backup.concrete_events.WokeAsStrongest;
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
    public Events onInstanceUpdate(long id, int strength) {
        LOGGER.info("onInstanceUpdate({}, {})", id, strength);
        data.updatePeer(id, strength);
        if (!data.amStrongest()) {
            return new EventsWokeAsWeak(wokeAsStrongest.onMetStronger(), data);
        }
        return this;
    }

    @Override
    public Events onInstanceLost(long id) {
        LOGGER.info("onInstanceLost({})", id);
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
