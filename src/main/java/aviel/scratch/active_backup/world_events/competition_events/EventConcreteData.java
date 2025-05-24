package aviel.scratch.active_backup.world_events.competition_events;

import aviel.scratch.network_api.ActiveBackupCompetition;
import aviel.scratch.network_api.TopicWriter;

import java.util.*;

public class EventConcreteData {
    private final String site;
    private final long id;
    private int strength;
    private final TopicWriter<ActiveBackupCompetition> activeBackupCompetitionTopicWriter;
    private final SortedMap<Long, Integer> peersById;
    private final SortedMap<Integer, SortedSet<Long>> PeersByStrength;

    public EventConcreteData(String site,
                             long id,
                             int strength,
                             TopicWriter<ActiveBackupCompetition> activeBackupCompetitionTopicWriter) {
        this.site = site;
        this.id = id;
        this.strength = strength;
        this.activeBackupCompetitionTopicWriter = activeBackupCompetitionTopicWriter;
        peersById = new TreeMap<>();
        PeersByStrength = new TreeMap<>();
    }

    public void updatePeer(long id, int strength) {
        Integer prevStrength = peersById.put(id, strength);
        removeAssociatedPeerStrength(id, prevStrength);
        PeersByStrength.computeIfAbsent(strength, _ -> new TreeSet<>()).add(id);
    }

    public void updateSelf(int strength) {
        this.strength = strength;
        activeBackupCompetitionTopicWriter.sendMessage(new ActiveBackupCompetition(id, strength, site));
    }

    public boolean amStrongest() {
        Map.Entry<Integer, SortedSet<Long>> strongestPeers = PeersByStrength.lastEntry();
        return strongestPeers.getKey() < strength || strongestPeers.getKey() == strength && strongestPeers.getValue().last() < id;
    }

    public void removePeer(long id) {
        removeAssociatedPeerStrength(id, peersById.remove(id));
    }

    private void removeAssociatedPeerStrength(long id, Integer prevStrength) {
        if (prevStrength != null) {
            Set<Long> strengths = PeersByStrength.get(prevStrength);
            strengths.remove(id);
            if (strengths.isEmpty()) {
                PeersByStrength.remove(prevStrength);
            }
        }
    }
}
