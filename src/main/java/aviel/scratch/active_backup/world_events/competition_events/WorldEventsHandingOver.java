package aviel.scratch.active_backup.world_events.competition_events;

import aviel.scratch.active_backup.competition_events.HandingOver;
import aviel.scratch.active_backup.world_events.WorldEvents;
import aviel.scratch.active_backup.world_events.competition_events.data.EventConcreteData;
import aviel.scratch.active_backup.world_events.competition_events.data.StrengthModification;
import aviel.scratch.network_api.ActiveBackupCompetition;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Instant;
import java.util.StringJoiner;

public class WorldEventsHandingOver implements WorldEvents {
    private static final Logger LOGGER = LogManager.getLogger();

    private final HandingOver handingOverActive;
    private final EventConcreteData data;
    private final Instant instant;

    private WorldEventsHandingOver(HandingOver handingOverActive, EventConcreteData data, Instant instant) {
        this.handingOverActive = handingOverActive;
        this.data = data;
        this.instant = instant;
    }

    public static WorldEvents create(HandingOver handingOverActive, EventConcreteData data, Instant instant) {
        return new WorldEventsHandingOver(handingOverActive, data, instant)
                .onStrengthUpdate(handingOverActive.activeModification(), handingOverActive.handoverModification());
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
        return this;
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
        if (data.metActiveStronger()) {
            return WorldEventsAwakeWeak.create(handingOverActive.onMetActiveStronger(), data);
        }
        return this;
    }

    @Override
    public WorldEvents onAlarm(Instant triggerInstant) {
        LOGGER.info("onAlarm({})", triggerInstant);
        if (instant.equals(triggerInstant)) {
            return WorldEventsAwakeStrongest.create(handingOverActive.onHandoverTooLong(), data);
        }
        return this;
    }

    @Override
    public WorldEvents onHandover(Instant instant) {
        LOGGER.info("onHandover({})", instant);
        return create(handingOverActive, data, instant);
    }
}
