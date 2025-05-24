package aviel.scratch.active_backup.world_events.competition_events;

import aviel.scratch.active_backup.world_events.WorldEvents;
import aviel.scratch.active_backup.competition_events.DormantAsStrongest;
import aviel.scratch.active_backup.world_events.competition_events.data.EventConcreteData;
import aviel.scratch.active_backup.world_events.competition_events.data.StrengthModification;
import aviel.scratch.network_api.ActiveBackupCompetition;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WorldEventsDormantAsStrongest implements WorldEvents {
    private static final Logger LOGGER = LogManager.getLogger();

    private final DormantAsStrongest dormantAsStrongest;
    private final EventConcreteData data;

    public WorldEventsDormantAsStrongest(DormantAsStrongest dormantAsStrongest, EventConcreteData data) {
        this.dormantAsStrongest = dormantAsStrongest;
        this.data = data;
    }

    @Override
    public WorldEvents onPeerUpdate(ActiveBackupCompetition peer) {
        LOGGER.info("onPeerUpdate({})", peer);
        data.updatePeer(peer);
        if (!data.amStrongest()) {
            return new WorldEventsDormantAsWeak(dormantAsStrongest.onMetStronger(), data);
        }
        return this;
    }

    @Override
    public WorldEvents onPeerLost(long id) {
        LOGGER.info("onPeerLost({})", id);
        data.removePeer(id);
        return this;
    }

    @Override
    public WorldEvents onStrengthChange(StrengthModification newStrength) {
        LOGGER.info("onStrengthChange({})", newStrength);
        data.updateSelf(newStrength);
        if (!data.amStrongest()) {
            return new WorldEventsDormantAsWeak(dormantAsStrongest.onMetStronger(), data);
        }
        return this;
    }

    @Override
    public WorldEvents onWakeupCall() {
        LOGGER.info("onWakeupCall()");
        return new WorldEventsWokeAsStrongest(dormantAsStrongest.onWakeupCall(), data);
    }

    @Override
    public WorldEvents onTakeANap() {
        LOGGER.info("onTakeANap()");
        return this;
    }
}
