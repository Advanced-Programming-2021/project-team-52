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
            case "MAIN_PHASE":
                return MAIN_PHASE;
            case "phase : battle":
            case "battlePhase":
            case "BATTLE_PHASE":
            case "battle":
                return BATTLE_PHASE;
            case "chain":
            case "CHAIN":
                return CHAIN;
            default:
                return OTHER;
        }
    }
}
