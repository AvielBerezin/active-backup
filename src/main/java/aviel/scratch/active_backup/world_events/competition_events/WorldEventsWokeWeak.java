package aviel.scratch.active_backup.world_events.competition_events;

import aviel.scratch.active_backup.competition_events.WokeWeak;
import aviel.scratch.active_backup.world_events.WorldEvents;
import aviel.scratch.active_backup.world_events.competition_events.data.EventConcreteData;
import aviel.scratch.active_backup.world_events.competition_events.data.StrengthActiveModification;
import aviel.scratch.active_backup.world_events.competition_events.data.StrengthModification;
import aviel.scratch.network_api.ActiveBackupCompetition;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WorldEventsWokeWeak implements WorldEvents {
    private static final Logger LOGGER = LogManager.getLogger();

    private final WokeWeak wokeWeak;
    private final EventConcreteData data;

    public WorldEventsWokeWeak(WokeWeak wokeWeak, EventConcreteData data) {
        this.wokeWeak = wokeWeak;
        this.data = data;
    }

    @Override
    public WorldEvents onPeerUpdate(ActiveBackupCompetition peer) {
        LOGGER.info("onPeerUpdate({})", peer);
        data.updatePeer(peer);
        return decideFate();
    }

    @Override
    public WorldEvents onPeerLost(long id) {
        LOGGER.info("onPeerLost({})", id);
        data.removePeer(id);
        return decideFate();
    }

    @Override
    public WorldEvents onStrengthChange(StrengthModification newStrength) {
        LOGGER.info("onStrengthChange({})", newStrength);
        data.updateSelf(newStrength);
        return decideFate();
    }

    private WorldEvents decideFate() {
        if (data.amStrongest()) {
            data.updateSelf(new StrengthActiveModification());
            return new WorldEventsWokeStrongest(wokeWeak.onAmStrongest(), data);
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
        return new WorldEventsDormantWeak(wokeWeak.onTakeANap(), data);
    }
}
