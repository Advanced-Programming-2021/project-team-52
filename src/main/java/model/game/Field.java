package model.game;

import java.util.ArrayList;

public class Field extends Place{

    private ArrayList<Place> affectedAttack, affectedDefense;
    private int numberOfCardsAffected;

    public Field(){
        super(PLACE_NAME.FIELD);
        affectedAttack = new ArrayList<>();
        affectedDefense = new ArrayList<>();
        this.numberOfCardsAffected = 0;
    }

    public void setNumberOfCardsAffected(int numberOfCardsAffected) {
        this.numberOfCardsAffected = numberOfCardsAffected;
    }

    public int getNumberOfCardsAffected() {
        return numberOfCardsAffected;
    }

    public ArrayList<Place> getAffectedAttack() {
        return affectedAttack;
    }

    public ArrayList<Place> getAffectedDefense(){return  affectedDefense;}

    public void removeFromAffect(Place place){
        affectedAttack.remove(place);
        affectedDefense.remove(place);
    }

    public void clear(){
        affectedAttack.clear();
        affectedDefense.clear();
    }
}
