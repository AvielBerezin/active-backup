package aviel.scratch.active_backup.world_events.competition_events;

import aviel.scratch.active_backup.world_events.WorldEvents;
import aviel.scratch.active_backup.competition_events.DormantWeak;
import aviel.scratch.active_backup.world_events.competition_events.data.EventConcreteData;
import aviel.scratch.active_backup.world_events.competition_events.data.StrengthHandoverModification;
import aviel.scratch.active_backup.world_events.competition_events.data.StrengthModification;
import aviel.scratch.network_api.ActiveBackupCompetition;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Instant;
import java.util.StringJoiner;

public class WorldEventsDormantWeak implements WorldEvents {
    private static final Logger LOGGER = LogManager.getLogger();

    private final DormantWeak dormantWeak;
    private final EventConcreteData data;

    private WorldEventsDormantWeak(DormantWeak dormantWeak, EventConcreteData data) {
        this.dormantWeak = dormantWeak;
        this.data = data;
    }

    public static WorldEvents create(DormantWeak dormantWeak, EventConcreteData data) {
        return new WorldEventsDormantWeak(dormantWeak, data)
                .onStrengthUpdate(dormantWeak.activeModification(), dormantWeak.handoverModification());
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
    public WorldEvents onStrengthUpdate(StrengthModification... modifications) {
        StringJoiner stringJoiner = new StringJoiner(", ", "onStrengthUpdate(", ")");
        for (StrengthModification modification : modifications) {
            stringJoiner.add(modification.toString());
        }
        LOGGER.info(stringJoiner);
        data.updateSelf(modifications);
        return decideFate();
    }

    private WorldEvents decideFate() {
        if (data.amStrongest()) {
            return WorldEventsDormantStrongest.create(dormantWeak.onAmStrongest(), data);
        }
        return this;
    }

    @Override
    public WorldEvents onAlarm(Instant triggerInstant) {
        LOGGER.info("onAlarm({})", triggerInstant);
        return WorldEventsAwakeWeak.create(dormantWeak.onWakeUp(), data);
    }

    @Override
    public WorldEvents onHandover(Instant instant) {
        LOGGER.info("onHandover({})", instant);
        return this.onStrengthUpdate(new StrengthHandoverModification());
    }
}
