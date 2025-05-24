package aviel.scratch.active_backup.abstract_events.concrete_events;

import aviel.scratch.active_backup.abstract_events.Events;
import aviel.scratch.active_backup.concrete_events.StartAsStrongest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EventsStartAsStrongest implements Events {
    private static final Logger LOGGER = LogManager.getLogger();

    private final StartAsStrongest startAsStrongest;
    private final EventConcreteData data;

    public EventsStartAsStrongest(StartAsStrongest startAsStrongest, EventConcreteData data) {
        this.startAsStrongest = startAsStrongest;
        this.data = data;
    }

    @Override
    public Events onPeerUpdate(long id, int strength) {
        LOGGER.info("onPeerUpdate({}, {})", id, strength);
        data.updatePeer(id, strength);
        if (!data.amStrongest()) {
            return new EventsStartAsWeak(startAsStrongest.onMetStronger(), data);
        }
        return this;
    }

    @Override
    public Events onPeerLost(long id) {
        LOGGER.info("onPeerLost({})", id);
        data.removePeer(id);
        return new EventsStartAsStrongest(startAsStrongest, data);
    }

    @Override
    public Events onStrengthChange(int newStrength) {
        LOGGER.info("onStrengthChange({})", newStrength);
        data.updateSelf(newStrength);
        if (!data.amStrongest()) {
            return new EventsStartAsWeak(startAsStrongest.onMetStronger(), data);
        }
        return this;
    }

    @Override
    public Events onWakeupCall() {
        LOGGER.info("onWakeupCall()");
        return new EventsWokeAsStrongest(startAsStrongest.onWakeupCall(), data);
    }
}
