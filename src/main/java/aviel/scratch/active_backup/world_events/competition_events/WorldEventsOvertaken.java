package aviel.scratch.active_backup.world_events.competition_events;

import aviel.scratch.active_backup.competition_events.Overtaken;
import aviel.scratch.active_backup.world_events.WorldEvents;
import aviel.scratch.active_backup.world_events.competition_events.data.EventConcreteData;
import aviel.scratch.active_backup.world_events.competition_events.data.StrengthModification;
import aviel.scratch.network_api.ActiveBackupCompetition;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Instant;
import java.util.StringJoiner;

public class WorldEventsOvertaken implements WorldEvents {
    private static final Logger LOGGER = LogManager.getLogger();

    private final Overtaken overtaken;
    private final EventConcreteData data;
    private final Instant handoverInstant;

    private WorldEventsOvertaken(Overtaken overtaken, EventConcreteData data, Instant handoverInstant) {
        this.data = data;
        this.overtaken = overtaken;
        this.handoverInstant = handoverInstant;
    }

    public static WorldEvents create(Overtaken overtaken, EventConcreteData data, Instant handoverInstant) {
        return new WorldEventsOvertaken(overtaken, data, handoverInstant)
                .onStrengthChange(overtaken.activeModification(), overtaken.handoverModification());
    }

    @Override
    public WorldEvents onPeerUpdate(ActiveBackupCompetition peer) {
        LOGGER.info("onPeerUpdate({})", peer);
        data.updatePeer(peer);
        if (data.amStrongest()) {
            return WorldEventsOvertaking.create(overtaken.onAmStrongest(), data, handoverInstant);
        }
        if (peer.isHandover() && peer.isActive()) {
            return WorldEventsAwakeWeak.create(overtaken.onMetOvertook(), data);
        }
        return this;
    }

    @Override
    public WorldEvents onPeerLost(long id) {
        LOGGER.info("onPeerLost({})", id);
        data.removePeer(id);
        if (data.amStrongest()) {
            return WorldEventsOvertaking.create(overtaken.onAmStrongest(), data, handoverInstant);
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
        if (data.amStrongest()) {
            return WorldEventsOvertaking.create(overtaken.onAmStrongest(), data, handoverInstant);
        }
        return this;
    }

    @Override
    public WorldEvents onAlarm(Instant triggerInstant) {
        LOGGER.info("onAlarm({})", triggerInstant);
        if (handoverInstant.equals(triggerInstant)) {
            return WorldEventsAwakeWeak.create(overtaken.onOvertakenTooLong(), data);
        }
        return this;
    }

    @Override
    public WorldEvents onHandover(Instant instant) {
        LOGGER.info("onHandover({})", instant);
        return this;
    }
}
