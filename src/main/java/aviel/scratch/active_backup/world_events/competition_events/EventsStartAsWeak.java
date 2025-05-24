package aviel.scratch.active_backup.world_events.competition_events;

import aviel.scratch.active_backup.world_events.Events;
import aviel.scratch.active_backup.competition_events.StartAsWeak;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EventsStartAsWeak implements Events {
    private static final Logger LOGGER = LogManager.getLogger();

    private final StartAsWeak startAsWeak;
    private final EventConcreteData data;

    public EventsStartAsWeak(StartAsWeak startAsWeak, EventConcreteData data) {
        this.startAsWeak = startAsWeak;
        this.data = data;
    }

    @Override
    public Events onPeerUpdate(long id, int strength) {
        LOGGER.info("onPeerUpdate({}, {})", id, strength);
        data.updatePeer(id, strength);
        if (data.amStrongest()) {
            return new EventsStartAsStrongest(startAsWeak.onAmStrongest(), data);
        }
        return this;
    }

    @Override
    public Events onPeerLost(long id) {
        LOGGER.info("onPeerLost({})", id);
        data.removePeer(id);
        if (data.amStrongest()) {
            return new EventsStartAsStrongest(startAsWeak.onAmStrongest(), data);
        }
        return this;
    }

    @Override
    public Events onStrengthChange(int newStrength) {
        LOGGER.info("onStrengthChange({})", newStrength);
        data.updateSelf(newStrength);
        if (data.amStrongest()) {
            return new EventsStartAsStrongest(startAsWeak.onAmStrongest(), data);
        }
        return this;
    }

    @Override
    public Events onWakeupCall() {
        LOGGER.info("onWakeupCall()");
        return new EventsWokeAsWeak(startAsWeak.onWakeupCall(), data);
    }
}
