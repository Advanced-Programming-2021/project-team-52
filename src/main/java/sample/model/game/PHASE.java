package sample.model.game;

public enum PHASE {
    DRAW("phase : draw"),
    STAND_BY("phase : standby"),
    MAIN("phase : main"),
    BATTLE("phase : battle"),
    END("phase : end");

    private final String phaseName;

    PHASE(String phaseName) {
        this.phaseName = phaseName;
    }

    public String getValue() {
        return phaseName;
    }
}
