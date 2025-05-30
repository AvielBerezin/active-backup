package aviel.scratch.active_backup.world_events.competition_events.data;

import aviel.scratch.Utils;
import aviel.scratch.network_api.ActiveBackupCompetition;
import aviel.scratch.network_api.TopicWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class EventConcreteData {
    private static final Logger LOGGER = LogManager.getLogger();

    private final long id;
    private int strength;
    private final TopicWriter<ActiveBackupCompetition> selfTopicWriter;
    private final Map<Long, ActiveBackupCompetition> peersById;
    private final SortedMap<Integer, SortedMap<Long, ActiveBackupCompetition>> peersByStrength;
    private final SortedMap<Integer, SortedMap<Long, ActiveBackupCompetition>> activePeersByStrength;
    /**
     * this counts all strengths that are 0b00 at the state bits.
     */
    private int weekPeersCount;
    /**
     * this counts all strengths that are 0b10 at the state bits.
     */
    private int overtakenPeerCount;

    public EventConcreteData(long id, TopicWriter<ActiveBackupCompetition> selfTopicWriter) {
        this.id = id;
        this.strength = 0;
        this.selfTopicWriter = selfTopicWriter;
        peersById = new HashMap<>();
        peersByStrength = new TreeMap<>();
        activePeersByStrength = new TreeMap<>();
        weekPeersCount = 0;
    }

    public int myStrength() {
        return strength;
    }

    public void updatePeer(ActiveBackupCompetition peer) {
        ActiveBackupCompetition prev = peersById.put(peer.id(), peer);
        if (prev != null) {
            removeAssociatedPeerStrength(prev);
            if (prev.isWeek()) {
                weekPeersCount -= 1;
            }
            if (prev.isOvertaken()) {
                overtakenPeerCount -= 1;
            }
        }
        peersByStrength.computeIfAbsent(peer.strength(), _ -> new TreeMap<>()).put(peer.id(), peer);
        if (peer.isActive()) {
            activePeersByStrength.computeIfAbsent(peer.strength(), _ -> new TreeMap<>()).put(peer.id(), peer);
        }
        if (peer.isWeek()) {
            weekPeersCount += 1;
        }
        if (peer.isOvertaken()) {
            overtakenPeerCount += 1;
        }
    }

    public void updateSelf(StrengthModification ...modifications) {
        int prev = strength;
        for (StrengthModification modification : modifications) {
            int prevStrength = strength;
            strength = modification.modify(strength);
            if (prevStrength != strength) {
                LOGGER.info("strength changed: hex({}) -> hex({}) via {}", Utils.hexInt(prevStrength), Utils.hexInt(strength), modification);
            }
        }
        if (prev != strength) {
            selfTopicWriter.sendMessage(new ActiveBackupCompetition(id, strength));
        }
    }

    public boolean amStrongest() {
        Map.Entry<Integer, SortedMap<Long, ActiveBackupCompetition>> strongestPeers = peersByStrength.lastEntry();
        return strongestPeers == null ||
               strongestPeers.getKey() < strength ||
               strongestPeers.getKey() == strength && strongestPeers.getValue().lastKey() < id;
    }

    public boolean metActiveStronger() {
        Map.Entry<Integer, SortedMap<Long, ActiveBackupCompetition>> strongestPeers = activePeersByStrength.lastEntry();
        return strongestPeers != null &&
               (strongestPeers.getKey() > strength ||
                (strongestPeers.getKey() == strength && strongestPeers.getValue().lastKey() > id));
    }

    public void removePeer(long id) {
        ActiveBackupCompetition prev = peersById.remove(id);
        if (prev == null) {
            return;
        }
        removeAssociatedPeerStrength(prev);
        if (prev.isWeek()) {
            weekPeersCount -= 1;
        }
        if (prev.isOvertaken()) {
            overtakenPeerCount -= 1;
        }
    }

    private void removeAssociatedPeerStrength(ActiveBackupCompetition prev) {
        SortedMap<Long, ActiveBackupCompetition> strengths = peersByStrength.get(prev.strength());
        strengths.remove(prev.id());
        if (strengths.isEmpty()) {
            peersByStrength.remove(prev.strength());
        }
        if (prev.isActive()) {
            SortedMap<Long, ActiveBackupCompetition> activeStrengths = activePeersByStrength.get(prev.strength());
            activeStrengths.remove(prev.id());
            if (activeStrengths.isEmpty()) {
                activePeersByStrength.remove(prev.strength());
            }
        }
    }

    public boolean noWeek() {
        return weekPeersCount == 0;
    }

    public boolean noOvertaken() {
        return overtakenPeerCount == 0;
    }
}
