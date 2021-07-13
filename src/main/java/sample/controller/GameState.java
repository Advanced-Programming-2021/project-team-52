package sample.controller;

public enum GameState {
    MAIN_PHASE,
    BATTLE_PHASE,
    CHAIN,
    OTHER;

    public static GameState getGameStateByString(String string) {
        switch (string) {
            case "main":
            case "mainPhase":
            case "phase : main":
                return MAIN_PHASE;
            case "phase : battle":
            case "battlePhase":
                return BATTLE_PHASE;
            case "chain":
                return CHAIN;
            default:
                return OTHER;
        }
    }
}
