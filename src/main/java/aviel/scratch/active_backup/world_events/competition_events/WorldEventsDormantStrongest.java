package aviel.scratch.active_backup.world_events.competition_events;

import aviel.scratch.active_backup.world_events.WorldEvents;
import aviel.scratch.active_backup.competition_events.DormantStrongest;
import aviel.scratch.active_backup.world_events.competition_events.data.EventConcreteData;
import aviel.scratch.active_backup.world_events.competition_events.data.StrengthHandoverModification;
import aviel.scratch.active_backup.world_events.competition_events.data.StrengthModification;
import aviel.scratch.network_api.ActiveBackupCompetition;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Instant;
import java.util.StringJoiner;

public class WorldEventsDormantStrongest implements WorldEvents {
    private static final Logger LOGGER = LogManager.getLogger();

    private final DormantStrongest dormantStrongest;
    private final EventConcreteData data;

    private WorldEventsDormantStrongest(DormantStrongest dormantStrongest, EventConcreteData data) {
        this.dormantStrongest = dormantStrongest;
        this.data = data;
    }

    public static WorldEvents create(DormantStrongest dormantStrongest, EventConcreteData data) {
        return new WorldEventsDormantStrongest(dormantStrongest, data)
                .onStrengthUpdate(dormantStrongest.activeModification(), dormantStrongest.handoverModification());
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
        if (!data.amStrongest()) {
            return WorldEventsDormantWeak.create(dormantStrongest.onMetStronger(), data);
        }
        return this;
    }

    @Override
    public WorldEvents onAlarm(Instant triggerInstant) {
        LOGGER.info("onAlarm({})", triggerInstant);
        return WorldEventsAwakeStrongest.create(dormantStrongest.onWakeUp(), data);
    }

    @Override
    public WorldEvents onHandover(Instant instant) {
        LOGGER.info("onHandover({})", instant);
        return this;
    }
}
