package aviel.scratch.network_api;

public class ActiveBackupCompetition {
    private final long id;
    private final int strength;
    private final String site;

    public ActiveBackupCompetition(long id, int strength, String site) {
        this.id = id;
        this.strength = strength;
        this.site = site;
    }

    public int strength() {
        return strength;
    }

    public long id() {
        return id;
    }
}
