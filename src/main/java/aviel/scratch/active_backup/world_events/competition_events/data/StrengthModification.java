package aviel.scratch.active_backup.world_events.competition_events.data;

public sealed interface StrengthModification
        permits StrengthActiveStateModification,
                StrengthHandoverStateModification,
                StrengthUserModification {
    short STATE_POSITION = 3;
    short ACTIVE_POSITION = STATE_POSITION;
    short HANDOVER_POSITION = ACTIVE_POSITION + 1;
    short USER_POSITION = STATE_POSITION + 2;

    int modify(int strength);
}
