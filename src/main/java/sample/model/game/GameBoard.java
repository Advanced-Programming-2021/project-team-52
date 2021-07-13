package sample.model.game;

import sample.model.cards.Cards;
import sample.model.cards.spell.SpellCards;

import java.util.ArrayList;
import java.util.HashMap;

public class GameBoard {

    private HashMap<Integer, Place> place;
    private ArrayList<Cards> graveyard;
    private ArrayList<Cards> mainCards;
    private ArrayList<Cards> sideCards;
    private ArrayList<Integer> cardsPicked;
    private int lastCardNumberPicked;
    private int health;

    public GameBoard(ArrayList<Cards> mainCards, ArrayList<Cards> sideCards) {
        place = new HashMap<>();
        place.put(0, new Place(PLACE_NAME.HAND, 0));
        for (int i = 1; i < 6; i++) {
            place.put(PLACE_NAME.HAND.getNumber() + i, new Place(PLACE_NAME.HAND, PLACE_NAME.HAND.getNumber() + i));
            place.put(PLACE_NAME.MONSTER.getNumber() + i, new MonsterZone(PLACE_NAME.MONSTER.getNumber() + i));
            place.put(PLACE_NAME.SPELL_AND_TRAP.getNumber() + i, new Place(PLACE_NAME.SPELL_AND_TRAP, PLACE_NAME.SPELL_AND_TRAP.getNumber() + i));
        }
        place.put(PLACE_NAME.FIELD.getNumber(), new Field(PLACE_NAME.FIELD.getNumber()));
        graveyard = new ArrayList<>();
        this.mainCards = mainCards;
        this.sideCards = sideCards;
        this.lastCardNumberPicked = this.mainCards.size() - 1;
        this.cardsPicked = new ArrayList<>();
        this.health = 8000;
    }

    public STATUS getStatus(int placeNumber, PLACE_NAME name) {
        return getPlace(placeNumber, name).getStatus();
    }

    public ArrayList<Cards> getGraveyard() {
        return graveyard;
    }

    public void addCard(Cards card, int placeNumber, PLACE_NAME name, STATUS status) {
        getPlace(placeNumber, name).setCard(card);
        getPlace(placeNumber, name).setStatus(status);
    }

    public Cards drawCard() {
        while (cardsPicked.contains(lastCardNumberPicked))
            lastCardNumberPicked--;
        if (lastCardNumberPicked < 0 && cardsPicked.size() != mainCards.size()) {
            lastCardNumberPicked = mainCards.size() - 1;
            return drawCard();
        } else {
            cardsPicked.add(lastCardNumberPicked);
            return (mainCards.get(lastCardNumberPicked--));
        }
    }

    public boolean noCardToDraw() {
        return mainCards.size() == cardsPicked.size();
    }

    public Cards getCard(PLACE_NAME name, int number) {
        return getPlace(number, name).getCard();
    }

    public ArrayList<Cards> getMainCards() {
        return mainCards;
    }

    public ArrayList<Cards> getSideCards() {
        return sideCards;
    }

    public void setMainCards(ArrayList<Cards> mainCards) {
        this.mainCards = mainCards;
        lastCardNumberPicked = mainCards.size() - 1;
    }

    public HashMap<Integer, Place> getPlace() {
        return place;
    }

    public ArrayList<Integer> getCardsPicked() {
        return cardsPicked;
    }

    public void setCardsPicked(ArrayList<Integer> cardsPicked) {
        this.cardsPicked = cardsPicked;
    }

    public int getFirstEmptyPlace(PLACE_NAME placeName) {
        for (int i = 1; i < 6; i++) {
            if (getPlace(i, placeName).getCard() == null)
                return i;
        }
        return -1;
    }

    public Place getPlace(int placeNumber, PLACE_NAME placeName) {
        return place.get(placeName.getNumber() + placeNumber);
    }

    public Place getPlace(int placeNumber){
        return place.get(placeNumber);
    }

    public Cards getACardByType(String type) {
        int fieldNum = mainCards.size() - 1;
        Cards card;
        while (fieldNum >= 0) {
            if (!cardsPicked.contains(fieldNum)) {
                card = mainCards.get(fieldNum);
                if (card instanceof SpellCards)
                    if (((SpellCards) card).getIcon().equals(type)) {
                        cardsPicked.add(fieldNum);
                        return mainCards.get(fieldNum);
                    }
            }
            fieldNum--;
        }
        return null;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void killCards(Place place) {
        graveyard.add(place.getCard());
        place.killCard();
    }

    public void changeHealth(int amount) {
        health += amount;
        if (health < 0)
            health = 0;
    }

    public boolean fromThisGameBoard(Place place) {
        return this.place.containsValue(place);
    }

    public int numberOfCardsRemainingToBePicked() {
        return mainCards.size() - cardsPicked.size();
    }

    public void clearArrayLists() {
        graveyard.clear();
        cardsPicked.clear();
        lastCardNumberPicked = mainCards.size() - 1;
    }
}
