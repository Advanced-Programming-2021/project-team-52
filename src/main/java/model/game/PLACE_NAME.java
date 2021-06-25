package model.game;

public enum PLACE_NAME {
    HAND(0),
    MONSTER(10),
    SPELL_AND_TRAP(20),
    FIELD(40);


    private int number;

    PLACE_NAME(int number) {
        this.number = number;

    }

    public int getNumber() {
        return number;
    }

    public static PLACE_NAME getEnumByString(String enumString) {
        switch (enumString) {
            case "-m":
            case "--monster":
            case "monster":
                return MONSTER;
            case "-s":
            case "--spell":
            case "spell":
                return SPELL_AND_TRAP;
            case "hand":
                return HAND;
        }
        return null;
    }
}