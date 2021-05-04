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
            place.put(PLACE_NAME.HAND.getNumber() + i, new Place(PLACE_NAME.HAND));
            place.put(PLACE_NAME.MONSTER.getNumber() + i, new Place(PLACE_NAME.MONSTER));
            place.put(PLACE_NAME.SPELL_AND_TRAP.getNumber() + i, new Place(PLACE_NAME.SPELL_AND_TRAP));
        }
        place.put(PLACE_NAME.FUSION.getNumber(), new Place(PLACE_NAME.FUSION));
        graveyard = new ArrayList<>();
        this.mainCards = new ArrayList<>(mainCards);
        this.sideCards = new ArrayList<>(sideCards);
        lastNmCardNumberPicked = this.mainCards.size();
        cardsPicked = new ArrayList<>();
    }

    public STATUS getStatus(int placeNumber, PLACE_NAME name) {
        return place.get(placeNumber + name.getNumber()).getStatus();
    }

    public ArrayList<Cards> getGraveyard() {
        return graveyard;
    }

    public void shuffleCards() {
        Collections.shuffle(mainCards);
        lastNmCardNumberPicked = mainCards.size();
    }

    public void removeCard(Cards card, int placeNumber, PLACE_NAME name) {
        graveyard.add(card);
        place.get(placeNumber + name.getNumber()).setCard(null);
    }

    public void addCard(Cards card, int placeNumber, PLACE_NAME name, STATUS status) {
        place.get(placeNumber + name.getNumber()).setCard(card);
        place.get(placeNumber + name.getNumber()).setStatus(status);
    }

    public Cards drawCard() {
        if (mainCards.equals(cardsPicked))
            return null;
        while (cardsPicked.contains(mainCards.get(lastNmCardNumberPicked)))
            lastNmCardNumberPicked--;
        return (mainCards.get(lastNmCardNumberPicked--));
    }

    public Cards getCard(PLACE_NAME name, int number) {
        return place.get(name.getNumber() + number).getCard();
    }


    ///////////////////////////////////////////////
    // todo : fix equal() for Cards class
    public boolean isThisCardExistsInThisPlace(Cards card, PLACE_NAME placeName) {
        for (int i = 1; i < 6; i++) {
            if (place.get(placeName.getNumber() + i).getCard().equals(card))
                return true;
        }
        return false;
    }

    public int getNumberOfCardsInThisPlace(PLACE_NAME placeName) {
        int counter = 0;
        for (int i = 1; i < 6; i++) {
            if (place.get(placeName.getNumber() + i).getCard() != null)
                ++counter;
        }
        return counter;
    }

    public int getFirstEmptyPlace(PLACE_NAME placeName) {
        for (int i = 1; i < 6; i++) {
            if (place.get(placeName.getNumber() + i).getCard() == null)
                return i;
        }
        return -1;
    }

    public Cards getCardByAddressAndPlace(int placeNumber, PLACE_NAME name) {
        return place.get(placeNumber + name.getNumber()).getCard();
    }

    public STATUS getCardStatus(Cards card, PLACE_NAME placeName) {
        for (int i = 1; i < 6; i++) {
            if (place.get(placeName.getNumber() + i).getCard().equals(card))
                return place.get(placeName.getNumber() + i).getStatus();
        }
        return null;
    }

    public void changeStatusOfCard(int placeNumber, PLACE_NAME placeName, STATUS status) {
        if (place.get(placeNumber + placeName.getNumber()) != null)
            place.get(placeNumber + placeName.getNumber()).setStatus(status);
    }

    public int getPlaceNumberOfCard(Cards card, PLACE_NAME placeName) {
        for (int i = 1; i < 6; i++) {
            if (place.get(placeName.getNumber() + i).getCard().equals(card))
                return i;
        }
        return -1;
    }

    public boolean isCardSetOrSummonInThisTurn(int placeNumber, PLACE_NAME placeName) {
        return place.get(placeNumber + placeName.getNumber())
                .isTemporaryFeaturesContainsThisFeature(TEMPORARY_FEATURES.CARD_SET_OR_SUMMON_IN_THIS_TURN);
    }

    public boolean isCardAttackedInThisTurn(int placeNumber, PLACE_NAME placeName) {
        return place.get(placeNumber + placeName.getNumber())
                .isTemporaryFeaturesContainsThisFeature(TEMPORARY_FEATURES.CARD_ATTACKED_IN_THIS_TURN);
    }

    public boolean isCardPositionChangedInThisTurn(int placeNumber, PLACE_NAME placeName) {
        return place.get(placeNumber + placeName.getNumber())
                .isTemporaryFeaturesContainsThisFeature(TEMPORARY_FEATURES.CARD_POSITION_CHANGED_IN_THIS_TURN);
    }

    public void setCardSetOrSummonInThisTurn(int placeNumber, PLACE_NAME placeName) {
        place.get(placeNumber + placeName.getNumber()).addTemporaryFeatures
                (TEMPORARY_FEATURES.CARD_SET_OR_SUMMON_IN_THIS_TURN);
    }

    public void setCardAttackedInThisTurn(int placeNumber, PLACE_NAME placeName) {
        place.get(placeNumber + placeName.getNumber()).addTemporaryFeatures
                (TEMPORARY_FEATURES.CARD_ATTACKED_IN_THIS_TURN);
    }

    public void setCardPositionChangedInThisTurn(int placeNumber, PLACE_NAME placeName) {
        place.get(placeNumber + placeName.getNumber()).addTemporaryFeatures
                (TEMPORARY_FEATURES.CARD_POSITION_CHANGED_IN_THIS_TURN);
    }

    // todo : add this method at the first of each round
    public void clearAllTemporaryFeatures() {
        for (int i = 1; i < 6; i++) {
            place.get(PLACE_NAME.MONSTER.getNumber() + i).clearTemporaryFeatures();
        }
    }
    // agar card az graveyard bargasht, bazi tanzimat esh bayad reset beshe
    //////////////////////////////////////////////

    @Override
    public String toString() {
        //TODO change toString
        return "GameBoard";
    }
}
