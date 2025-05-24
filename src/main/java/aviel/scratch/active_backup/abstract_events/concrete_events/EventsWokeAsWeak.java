package aviel.scratch.active_backup.abstract_events.concrete_events;

import aviel.scratch.active_backup.abstract_events.Events;
import aviel.scratch.active_backup.concrete_events.WokeAsWeak;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EventsWokeAsWeak implements Events {
    private static final Logger LOGGER = LogManager.getLogger();

    private final WokeAsWeak wokeAsWeak;
    private final EventConcreteData data;

    public EventsWokeAsWeak(WokeAsWeak wokeAsWeak, EventConcreteData data) {
        this.wokeAsWeak = wokeAsWeak;
        this.data = data;
    }

    @Override
    public Events onInstanceUpdate(long id, int strength) {
        LOGGER.info("onInstanceUpdate({}, {})", id, strength);
        data.updatePeer(id, strength);
        if (data.amStrongest()) {
            return new EventsWokeAsStrongest(wokeAsWeak.onAmStrongest(), data);
        }
        return this;
    }

    @Override
    public Events onInstanceLost(long id) {
        LOGGER.info("onInstanceLost({})", id);
        data.removePeer(id);
        if (data.amStrongest()) {
            return new EventsWokeAsStrongest(wokeAsWeak.onAmStrongest(), data);
        }
        return this;
    }

    @Override
    public Events onStrengthChange(int newStrength) {
        LOGGER.info("onStrengthChange({})", newStrength);
        data.updateSelf(newStrength);
        if (data.amStrongest()) {
            return new EventsWokeAsStrongest(wokeAsWeak.onAmStrongest(), data);
        }
        return this;
    }

    @Override
    public Events onWakeupCall() {
        LOGGER.info("onWakeupCall()");
        return this;
    }
}
