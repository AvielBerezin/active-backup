package aviel.scratch.active_backup.world_events.competition_events;

import aviel.scratch.active_backup.competition_events.AwakeStrongest;
import aviel.scratch.active_backup.world_events.WorldEvents;
import aviel.scratch.active_backup.world_events.competition_events.data.EventConcreteData;
import aviel.scratch.active_backup.world_events.competition_events.data.StrengthBackupModification;
import aviel.scratch.active_backup.world_events.competition_events.data.StrengthHandoverModification;
import aviel.scratch.active_backup.world_events.competition_events.data.StrengthModification;
import aviel.scratch.network_api.ActiveBackupCompetition;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Instant;
import java.util.StringJoiner;

public class WorldEventsAwakeStrongest implements WorldEvents {
    private static final Logger LOGGER = LogManager.getLogger();

    private final AwakeStrongest awakeStrongest;
    private final EventConcreteData data;

    private WorldEventsAwakeStrongest(AwakeStrongest awakeStrongest, EventConcreteData data) {
        this.awakeStrongest = awakeStrongest;
        this.data = data;
    }

    public static WorldEvents create(AwakeStrongest awakeStrongest, EventConcreteData data) {
        return new WorldEventsAwakeStrongest(awakeStrongest, data)
                .onStrengthUpdate(awakeStrongest.activeModification(), awakeStrongest.handoverModification());
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
            data.updateSelf(new StrengthBackupModification());
            return WorldEventsAwakeWeak.create(awakeStrongest.onMetStronger(), data);
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
        return this.onStrengthUpdate(new StrengthHandoverModification());
    }
}
