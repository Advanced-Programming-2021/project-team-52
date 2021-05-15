package model.game;

public enum PHASE {
    DRAW("phase : draw"),
    STAND_BY("phase : standby"),
    MAIN("phase : main"),
    BATTLE("phase : battle"),
    END("phase : end"),
    CHAIN("chain");

    private final String phaseName;

    PHASE(String phaseName){
        this.phaseName = phaseName;
    }

    public String getValue(){
        return phaseName;
    }
}
