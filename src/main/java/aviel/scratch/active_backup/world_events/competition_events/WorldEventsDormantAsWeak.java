package aviel.scratch.active_backup.world_events.competition_events;

import aviel.scratch.active_backup.world_events.WorldEvents;
import aviel.scratch.active_backup.competition_events.DormantAsWeak;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WorldEventsDormantAsWeak implements WorldEvents {
    private static final Logger LOGGER = LogManager.getLogger();

    private final DormantAsWeak dormantAsWeak;
    private final EventConcreteData data;

    public WorldEventsDormantAsWeak(DormantAsWeak dormantAsWeak, EventConcreteData data) {
        this.dormantAsWeak = dormantAsWeak;
        this.data = data;
    }

    @Override
    public WorldEvents onPeerUpdate(long id, int strength) {
        LOGGER.info("onPeerUpdate({}, {})", id, strength);
        data.updatePeer(id, strength);
        if (data.amStrongest()) {
            return new WorldEventsDormantAsStrongest(dormantAsWeak.onAmStrongest(), data);
        }
        return this;
    }

    @Override
    public WorldEvents onPeerLost(long id) {
        LOGGER.info("onPeerLost({})", id);
        data.removePeer(id);
        if (data.amStrongest()) {
            return new WorldEventsDormantAsStrongest(dormantAsWeak.onAmStrongest(), data);
        }
        return this;
    }

    @Override
    public WorldEvents onStrengthChange(int newStrength) {
        LOGGER.info("onStrengthChange({})", newStrength);
        data.updateSelf(newStrength);
        if (data.amStrongest()) {
            return new WorldEventsDormantAsStrongest(dormantAsWeak.onAmStrongest(), data);
        }
        return this;
    }

    @Override
    public WorldEvents onWakeupCall() {
        LOGGER.info("onWakeupCall()");
        return new WorldEventsWokeAsWeak(dormantAsWeak.onWakeupCall(), data);
    }

    @Override
    public WorldEvents onReceivedSwitchOver() {
        return this;
    }
}
