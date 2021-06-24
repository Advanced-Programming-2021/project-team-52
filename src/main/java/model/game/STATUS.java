package model.game;

public enum STATUS {
    ATTACK,
    DEFENCE,
    SET;

    public static STATUS getStatusByString(String status){
        if (status != null)
        switch (status){
            case "attack" : return ATTACK;
            case "defense":
            case "defence" : return DEFENCE;
            case "set" : return SET;
        }
        return null;
    }
}
