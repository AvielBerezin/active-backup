package aviel.scratch.active_backup.world_events.competition_events;

import aviel.scratch.active_backup.competition_events.Overtook;
import aviel.scratch.active_backup.world_events.WorldEvents;
import aviel.scratch.active_backup.world_events.competition_events.data.EventConcreteData;
import aviel.scratch.active_backup.world_events.competition_events.data.StrengthModification;
import aviel.scratch.network_api.ActiveBackupCompetition;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Instant;
import java.util.StringJoiner;

public class WorldEventsOvertook implements WorldEvents {
    private static final Logger LOGGER = LogManager.getLogger();

    private final Overtook overtook;
    private final EventConcreteData data;

    private WorldEventsOvertook(Overtook overtook, EventConcreteData data) {
        this.overtook = overtook;
        this.data = data;
    }

    public static WorldEvents create(Overtook overtook, EventConcreteData data) {
        return new WorldEventsOvertook(overtook, data)
                .onStrengthUpdate(overtook.activeModification(), overtook.handoverModification());
    }

    @Override
    public WorldEvents onPeerUpdate(ActiveBackupCompetition peer) {
        LOGGER.info("onPeerUpdate({})", peer);
        data.updatePeer(peer);
        if (peer.isActive() && peer.strength() > data.myStrength()) {
            return WorldEventsOvertookCanceled.create(overtook.onMetActiveStronger(), data);
        }
        if (data.noOvertaken()) {
            return WorldEventsAwakeStrongest.create(overtook.onNoOvertaken(), data);
        }
        return this;
    }

    @Override
    public WorldEvents onPeerLost(long id) {
        LOGGER.info("onPeerLost({})", id);
        data.removePeer(id);
        if (data.noOvertaken()) {
            return WorldEventsAwakeStrongest.create(overtook.onNoOvertaken(), data);
        }
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
        if (data.metActiveStronger()) {
            return WorldEventsOvertookCanceled.create(overtook.onMetActiveStronger(), data);
        }
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
