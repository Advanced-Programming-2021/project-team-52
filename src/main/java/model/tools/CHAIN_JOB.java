package model.tools;

//TODO add ritual summon ?
public enum CHAIN_JOB {
    ALONE("alone"),
    SUMMON("summon"),
    FLIP_SUMMON("flipSummon"),
    SPECIAL_SUMMON("specialSummon"),
    ATTACK_DIRECT("attackDirect"),
    ATTACK_MONSTER("attackMonster"),
    ACTIVATE_SPELL("activateSpell"),
    ACTIVATE_TRAP("activateTrap"),
    DRAW_PHASE("drawPhase");

    private final String VALUE;

    private CHAIN_JOB(String value){
        this.VALUE = value;
    }

    public String getValue() {
        return VALUE;
    }

    public static CHAIN_JOB getValueByString(String value){
        switch (value){
            case "alone" : return ALONE;
            case "summon" : return SUMMON;
            case "flipSummon" : return FLIP_SUMMON;
            case "specialSummon" : return SPECIAL_SUMMON;
            case "attackDirect" : return ATTACK_DIRECT;
            case "attackMonster" : return ATTACK_MONSTER;
            case "activateSpell" : return ACTIVATE_SPELL;
            case "activateTrap" : return ACTIVATE_TRAP;
            case "drawPhase" : return DRAW_PHASE;
            default : return null;
        }
    }
}
