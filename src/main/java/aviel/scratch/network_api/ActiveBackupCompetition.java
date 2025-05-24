package aviel.scratch.network_api;

public class ActiveBackupCompetition {
    private final long id;
    private final int strength;

    public ActiveBackupCompetition(long id, int strength) {
        this.id = id;
        this.strength = strength;
    }

    public int strength() {
        return strength;
    }

    public long id() {
        return id;
    }
}
