package sample.model.tools;

//TODO add ritual summon ?
public enum CHAIN_JOB {
    ALONE,
    BEFORE_SUMMON,
    BEFORE_SPECIAL_SUMMON,
    SUMMON,
    FLIP_SUMMON,
    SPECIAL_SUMMON,
    ATTACK_DIRECT,
    ATTACK_MONSTER,
    ACTIVATE_SPELL,
    ACTIVATE_TRAP,
//    RITUAL_SUMMON,
    DRAW_PHASE;

//    ALONE("alone"),
//    SUMMON("summon"),
//    FLIP_SUMMON("flipSummon"),
//    SPECIAL_SUMMON("specialSummon"),
//    ATTACK_DIRECT("attackDirect"),
//    ATTACK_MONSTER("attackMonster"),
//    ACTIVATE_SPELL("activateSpell"),
//    ACTIVATE_TRAP("activateTrap"),
//    DRAW_PHASE("drawPhase");

//    private final String VALUE;
//
//    private CHAIN_JOB(String value){
//        this.VALUE = value;
//    }
//
//    public String getValue() {
//        return VALUE;
//    }

    public static CHAIN_JOB getValueByString(String value){
        switch (value){
            case "beforeSummon" : return BEFORE_SUMMON;
            case "beforeSpecialSummon" : return BEFORE_SPECIAL_SUMMON;
            case "alone" : return ALONE;
            case "summon" : return SUMMON;
            case "flipSummon" : return FLIP_SUMMON;
            case "specialSummon" : return SPECIAL_SUMMON;
            case "attackDirect" : return ATTACK_DIRECT;
            case "attackMonster" : return ATTACK_MONSTER;
            case "activateSpell" : return ACTIVATE_SPELL;
            case "activateTrap" : return ACTIVATE_TRAP;
            case "drawPhase" : return DRAW_PHASE;
//            case "ritualSummon": return RITUAL_SUMMON;
            default : return null;
        }
    }
}
