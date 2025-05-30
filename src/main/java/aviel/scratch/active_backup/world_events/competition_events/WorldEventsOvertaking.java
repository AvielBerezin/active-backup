package aviel.scratch.active_backup.world_events.competition_events;

import aviel.scratch.active_backup.competition_events.Overtaking;
import aviel.scratch.active_backup.world_events.WorldEvents;
import aviel.scratch.active_backup.world_events.competition_events.data.EventConcreteData;
import aviel.scratch.active_backup.world_events.competition_events.data.StrengthModification;
import aviel.scratch.network_api.ActiveBackupCompetition;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Instant;
import java.util.StringJoiner;

public class WorldEventsOvertaking implements WorldEvents {
    private static final Logger LOGGER = LogManager.getLogger();

    private final Overtaking overtaking;
    private final EventConcreteData data;
    private final Instant handoverInstant;

    private WorldEventsOvertaking(Overtaking overtaking, EventConcreteData data, Instant handoverInstant) {
        this.overtaking = overtaking;
        this.data = data;
        this.handoverInstant = handoverInstant;
    }

    public static WorldEvents create(Overtaking overtaking, EventConcreteData data, Instant handoverInstant) {
        return new WorldEventsOvertaking(overtaking, data, handoverInstant)
                .onStrengthUpdate(overtaking.activeModification(), overtaking.handoverModification());
    }

    @Override
    public WorldEvents onPeerUpdate(ActiveBackupCompetition peer) {
        LOGGER.info("onPeerUpdate({})", peer);
        data.updatePeer(peer);
        if (!data.amStrongest()) {
            return WorldEventsOvertaken.create(overtaking.onMetStronger(), data, handoverInstant);
        }
        if (data.noWeek()) {
            return WorldEventsOvertook.create(overtaking.onAmStrongestNoWeek(), data);
        }
        return this;
    }

    @Override
    public WorldEvents onPeerLost(long id) {
        LOGGER.info("onPeerLost({})", id);
        data.removePeer(id);
        if (data.noWeek()) {
            return WorldEventsOvertook.create(overtaking.onAmStrongestNoWeek(), data);
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
        if (!data.amStrongest()) {
            return WorldEventsOvertaken.create(overtaking.onMetStronger(), data, handoverInstant);
        }
        return this;
    }

    @Override
    public WorldEvents onAlarm(Instant triggerInstant) {
        LOGGER.info("onAlarm({})", triggerInstant);
        if (handoverInstant.equals(triggerInstant)) {
            return WorldEventsOvertook.create(overtaking.onAmStrongestOvertakingTooLong(), data);
        }
        return this;
    }

    @Override
    public WorldEvents onHandover(Instant instant) {
        LOGGER.info("onHandover({})", instant);
        return this;
    }
}
