package model.game;

public enum PLACE_NAME {
    HAND(0),
    MONSTER(10),
    SPELL_AND_TRAP(20),
    FUSION(40);


    private int number;

    private PLACE_NAME(int number){
        this.number = number;

    }

    public int getNumber(){
        return number;
    }
}