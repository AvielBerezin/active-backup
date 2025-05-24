package aviel.scratch.active_backup.world_events.competition_events.data;

import aviel.scratch.network_api.ActiveBackupCompetition;
import aviel.scratch.network_api.TopicWriter;

import java.util.*;

public class EventConcreteData {
    private final String site;
    private final long id;
    private int strength;
    private final TopicWriter<ActiveBackupCompetition> activeBackupCompetitionTopicWriter;
    private final Map<Long, Integer> peersById;
    private final SortedMap<Integer, SortedSet<Long>> PeersByStrength;
    private final Map<Long, String> peersToSites;

    public EventConcreteData(String site,
                             long id,
                             int strength,
                             TopicWriter<ActiveBackupCompetition> activeBackupCompetitionTopicWriter) {
        this.site = site;
        this.id = id;
        this.strength = strength;
        this.activeBackupCompetitionTopicWriter = activeBackupCompetitionTopicWriter;
        peersById = new HashMap<>();
        PeersByStrength = new TreeMap<>();
        peersToSites = new HashMap<>();
    }

    public void updatePeer(ActiveBackupCompetition peer) {
        Integer prevStrength = peersById.put(peer.id(), peer.strength());
        removeAssociatedPeerStrength(peer.id(), prevStrength);
        PeersByStrength.computeIfAbsent(peer.strength(), _ -> new TreeSet<>()).add(peer.id());
        String prevSite = peersToSites.put(peer.id(), peer.site());
        if (prevSite != null && !prevSite.equals(peer.site())) {
            throw new IllegalStateException("peer site cannot change: %s -> %s".formatted(prevSite, peer.site()));
        }
    }

    public void updateSelf(StrengthModification strength) {
        this.strength = strength.modify(this.strength);
        activeBackupCompetitionTopicWriter.sendMessage(new ActiveBackupCompetition(id, this.strength, site));
    }

    public boolean amStrongest() {
        Map.Entry<Integer, SortedSet<Long>> strongestPeers = PeersByStrength.lastEntry();
        return strongestPeers.getKey() < strength || strongestPeers.getKey() == strength && strongestPeers.getValue().last() < id;
    }

    public void removePeer(long id) {
        peersToSites.remove(id);
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
