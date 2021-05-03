package model.game;

import model.cards.Cards;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class GameBoard {

    private HashMap<Integer, Place> place;
    private ArrayList<Cards> graveyard;
    private ArrayList<Cards> mainCards;
    private ArrayList<Cards> sideCards;
    private int lastNmCardNumberPicked;
    private ArrayList<Cards> cardsPicked;

    public GameBoard(ArrayList<Cards> mainCards, ArrayList<Cards> sideCards) {
        place = new HashMap<>();
        for (int i = 1; i < 6; i++) {
            place.put(PlaceName.HAND.getNumber() + i, new Place(PlaceName.HAND));
            place.put(PlaceName.MONSTER.getNumber() + i, new Place(PlaceName.MONSTER));
            place.put(PlaceName.SPELL_AND_TRAP.getNumber() + i, new Place(PlaceName.SPELL_AND_TRAP));
        }
        place.put(PlaceName.FUSION.getNumber(), new Place(PlaceName.FUSION));
        graveyard = new ArrayList<>();
        this.mainCards = new ArrayList<>(mainCards);
        this.sideCards = new ArrayList<>(sideCards);
        lastNmCardNumberPicked = this.mainCards.size();
        cardsPicked = new ArrayList<>();
    }

    public String getStatus(int placeNumber, PlaceName name) {
        return place.get(placeNumber + name.getNumber()).getStatus();
    }

    public ArrayList<Cards> getGraveyard() {
        return graveyard;
    }

    public void shuffleCards(){
        Collections.shuffle(mainCards);
        lastNmCardNumberPicked = mainCards.size();
    }

    public void removeCard(Cards card, int placeNumber, PlaceName name) {
        graveyard.add(card);
        place.get(placeNumber + name.getNumber()).setCard(null);
    }

    public void addCard(Cards card, int placeNumber, PlaceName name) {
        place.get(placeNumber + name.getNumber()).setCard(card);
    }

    public Cards drawCard() {
        if (mainCards.equals(cardsPicked))
            return null;
        while (cardsPicked.contains(mainCards.get(lastNmCardNumberPicked)))
            lastNmCardNumberPicked--;
        return (mainCards.get(lastNmCardNumberPicked--));
    }

    public Cards getCard(PlaceName name, int number){
        return place.get(name.getNumber() + number).getCard();
    }

    @Override
    public String toString() {
        //TODO change toString
        return "GameBoard";
    }
}
