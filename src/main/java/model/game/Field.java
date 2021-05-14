package model.game;

import java.util.ArrayList;

public class Field extends Place{

    public ArrayList<Place> affected;

    public Field(){
        super(PLACE_NAME.FIELD);
        affected = new ArrayList<>();
    }

    public ArrayList<Place> getAffected() {
        return affected;
    }

    public void removeFromAffect(Place place){
        affected.remove(place);
    }
}
