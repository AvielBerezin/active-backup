package aviel.scratch.active_backup.world_events.competition_events;

import aviel.scratch.active_backup.competition_events.OvertookCanceled;
import aviel.scratch.active_backup.world_events.WorldEvents;
import aviel.scratch.active_backup.world_events.competition_events.data.EventConcreteData;
import aviel.scratch.active_backup.world_events.competition_events.data.StrengthModification;
import aviel.scratch.network_api.ActiveBackupCompetition;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Instant;
import java.util.StringJoiner;

public class WorldEventsOvertookCanceled implements WorldEvents {
    private static final Logger LOGGER = LogManager.getLogger();

    private final OvertookCanceled overtookCanceled;
    private final EventConcreteData data;

    private WorldEventsOvertookCanceled(OvertookCanceled overtookCanceled, EventConcreteData data) {
        this.overtookCanceled = overtookCanceled;
        this.data = data;
    }

    public static WorldEvents create(OvertookCanceled overtookCanceled, EventConcreteData data) {
        return new WorldEventsOvertookCanceled(overtookCanceled, data)
                .onStrengthChange(overtookCanceled.activeModification(), overtookCanceled.handoverModification());
    }

    @Override
    public WorldEvents onPeerUpdate(ActiveBackupCompetition peer) {
        LOGGER.info("onPeerUpdate({})", peer);
        data.updatePeer(peer);
        if (data.noOvertaken()) {
            return WorldEventsAwakeWeak.create(overtookCanceled.onNoOvertaken(), data);
        }
        return this;
    }

    @Override
    public WorldEvents onPeerLost(long id) {
        LOGGER.info("onPeerLost({})", id);
        data.removePeer(id);
        if (data.noOvertaken()) {
            return WorldEventsAwakeWeak.create(overtookCanceled.onNoOvertaken(), data);
        }
        return this;
    }

    @Override
    public WorldEvents onStrengthChange(StrengthModification... modifications) {
        StringJoiner stringJoiner = new StringJoiner(", ", "onStrengthChange(", ")");
        for (StrengthModification modification : modifications) {
            stringJoiner.add(modification.toString());
        }
        LOGGER.info(stringJoiner);
        data.updateSelf(modifications);
        return this;
    }

    @Override
    public WorldEvents onAlarm(Instant triggerInstant) {
        LOGGER.info("onAlarm({})", triggerInstant);
        return this;
    }

    @Override
    public WorldEvents onHandover(Instant instant) {
        LOGGER.info("onHandover({})", instant);
        return this;
    }
}
