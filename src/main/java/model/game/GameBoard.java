package model.game;

import model.cards.Cards;
import java.util.ArrayList;
import java.util.HashMap;

public class GameBoard {

    private HashMap<Integer, Place> place;
    private ArrayList<Cards> graveyard;

    public GameBoard() {
        place = new HashMap<>();
        for (int i = 1; i < 6; i++) {
            place.put(PlaceName.HAND.getNumber() + i, new Place(PlaceName.HAND));
            place.put(PlaceName.MONSTER.getNumber() + i, new Place(PlaceName.MONSTER));
            place.put(PlaceName.SPELL_AND_TRAP.getNumber() + i, new Place(PlaceName.SPELL_AND_TRAP));
        }
        place.put(PlaceName.FUSION.getNumber(), new Place(PlaceName.FUSION));
    }

    public void removeCard(Cards card, int placeNumber, PlaceName name) {
        graveyard.add(card);
        place.get(placeNumber + name.getNumber()).setCard(null);
    }

    public void addCard(Cards card, int placeNumber, PlaceName name) {
        place.get(placeNumber + name.getNumber()).setCard(card);
    }

    public String getStatus(int placeNumber, PlaceName name) {
        return place.get(placeNumber + name.getNumber()).getStatus();
    }

    public String drawCard(boolean firstTurn) {
        //TODO
    }


    ///////////////////////////////////////////////
    public boolean isThisCardExistsInThisPlace(Cards card, PlaceName placeName){
        for (int i = 1; i < 6; i++) {
            if(place.get(placeName.getNumber() + i).getCard().equals(card))
                return true;
        }
        return false;
    }
    public int getNumberOfCardsInThisPlace(PlaceName placeName){
        int counter = 0;
        for (int i = 1; i < 6; i++) {
            if(place.get(placeName.getNumber() + i).getCard() != null)
                ++counter;
        }
        return counter;
    }

    public int getFirstEmptyPlace(PlaceName placeName){
        for (int i = 1; i < 6; i++) {
            if(place.get(placeName.getNumber() + i).getCard() == null)
                return i;
        }
    }
    //////////////////////////////////////////////

    @Override
    public String toString() {
        //TODO change toString
        return "GameBoard";
    }
}
