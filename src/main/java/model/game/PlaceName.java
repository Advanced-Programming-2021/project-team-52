package model.game;

public enum PlaceName{
    HAND(0),
    MONSTER(10),
    SPELL_AND_TRAP(20),
    FUSION(40);


    private int number;

    private PlaceName(int number){
        this.number = number;

    }

    public int getNumber(){
        return number;
    }
}