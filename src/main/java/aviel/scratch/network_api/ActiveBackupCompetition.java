package aviel.scratch.network_api;

import aviel.scratch.Utils;
import aviel.scratch.active_backup.world_events.competition_events.data.StrengthModification;

public record ActiveBackupCompetition(long id, int strength) {
    public boolean isActive() {
        return (strength & (1 << StrengthModification.ACTIVE_POSITION)) != 0;
    }

    public boolean isHandover() {
        return (strength & (1 << StrengthModification.HANDOVER_POSITION)) != 0;
    }

    public boolean isWeek() {
        return !isActive() & !isHandover();
    }

    public boolean isOvertaken() {
        return !isActive() & isHandover();
    }

    @Override
    public String toString() {
        return "ActiveBackupCompetition[id=" + id + ", strength=hex(" + Utils.hexInt(strength) + ")]";
    }
}
