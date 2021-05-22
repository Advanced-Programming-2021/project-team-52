package model.game;

import java.util.ArrayList;

public class Field extends Place{

    public ArrayList<Place> affected;
    public int numberOfCardsAffected;

    public Field(){
        super(PLACE_NAME.FIELD);
        affected = new ArrayList<>();
        this.numberOfCardsAffected = 0;
    }

    public void setNumberOfCardsAffected(int numberOfCardsAffected) {
        this.numberOfCardsAffected = numberOfCardsAffected;
    }

    public int getNumberOfCardsAffected() {
        return numberOfCardsAffected;
    }

    public ArrayList<Place> getAffected() {
        return affected;
    }

    public void removeFromAffect(Place place){
        affected.remove(place);
    }
}
