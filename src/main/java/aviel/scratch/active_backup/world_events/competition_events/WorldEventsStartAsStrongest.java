package aviel.scratch.active_backup.world_events.competition_events;

import aviel.scratch.active_backup.world_events.WorldEvents;
import aviel.scratch.active_backup.competition_events.StartAsStrongest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WorldEventsStartAsStrongest implements WorldEvents {
    private static final Logger LOGGER = LogManager.getLogger();

    private final StartAsStrongest startAsStrongest;
    private final EventConcreteData data;

    public WorldEventsStartAsStrongest(StartAsStrongest startAsStrongest, EventConcreteData data) {
        this.startAsStrongest = startAsStrongest;
        this.data = data;
    }

    @Override
    public WorldEvents onPeerUpdate(long id, int strength) {
        LOGGER.info("onPeerUpdate({}, {})", id, strength);
        data.updatePeer(id, strength);
        if (!data.amStrongest()) {
            return new WorldEventsStartAsWeak(startAsStrongest.onMetStronger(), data);
        }
        return this;
    }

    @Override
    public WorldEvents onPeerLost(long id) {
        LOGGER.info("onPeerLost({})", id);
        data.removePeer(id);
        return new WorldEventsStartAsStrongest(startAsStrongest, data);
    }

    @Override
    public WorldEvents onStrengthChange(int newStrength) {
        LOGGER.info("onStrengthChange({})", newStrength);
        data.updateSelf(newStrength);
        if (!data.amStrongest()) {
            return new WorldEventsStartAsWeak(startAsStrongest.onMetStronger(), data);
        }
        return this;
    }

    @Override
    public WorldEvents onWakeupCall() {
        LOGGER.info("onWakeupCall()");
        return new WorldEventsWokeAsStrongest(startAsStrongest.onWakeupCall(), data);
    }
}
