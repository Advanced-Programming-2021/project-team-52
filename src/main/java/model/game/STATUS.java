package model.game;

public enum STATUS {
    ATTACK,
    DEFENCE,
    SET;

    public static STATUS getStatusByString(String status){
        switch (status){
            case "attack" : return ATTACK;
            case "defence" : return DEFENCE;
            case "set" : return SET;
            default: return null;
        }
    }
}
