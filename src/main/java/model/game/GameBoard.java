package model.game;

import controller.SpecialAbility.SpecialAbilityController;
import model.cards.Cards;
import model.cards.monster.MonsterCards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class GameBoard {

    private HashMap<Integer, Place> place;
    private ArrayList<Cards> graveyard;
    private ArrayList<Cards> mainCards;
    private ArrayList<Cards> sideCards;
    private int lastCardNumberPicked;
    private ArrayList<Cards> cardsPicked;
    private GameBoard opponentGameBoard;

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
        lastCardNumberPicked = this.mainCards.size();
        cardsPicked = new ArrayList<>();
    }

    public STATUS getStatus(int placeNumber, PLACE_NAME name) {
        return getPlace(placeNumber, name).getStatus();
    }

    public ArrayList<Cards> getGraveyard() {
        return graveyard;
    }

    public void shuffleCards() {
        Collections.shuffle(mainCards);
        lastCardNumberPicked = mainCards.size();
    }

    public void removeCard(Cards card, int placeNumber, PLACE_NAME name) {
        graveyard.add(card);
        getPlace(placeNumber, name).setCard(null);
    }

    public void addCard(Cards card, int placeNumber, PLACE_NAME name, STATUS status) {
        getPlace(placeNumber, name).setCard(card);
        getPlace(placeNumber, name).setStatus(status);
    }

    public Cards drawCard() {
        if (mainCards.equals(cardsPicked))
            return null;
        while (cantPickAnyMoreOfThisCard())
            lastCardNumberPicked--;
        return (mainCards.get(lastCardNumberPicked--));
    }

    private boolean cantPickAnyMoreOfThisCard(){
        int amountInPickedCards = 0, amountInDeck = 0;
        Cards cardToCheck = mainCards.get(lastCardNumberPicked);
        for (Cards cards : cardsPicked) {
            if (cards == cardToCheck)
                amountInPickedCards++;
        }
        for (Cards mainCard : mainCards) {
            if (mainCard == cardToCheck)
                amountInDeck++;
        }
        if (amountInDeck == amountInPickedCards)
            return true;
        else return false;
    }

    public Cards getCard(PLACE_NAME name, int number) {
        return getPlace(number, name).getCard();
    }


    ///////////////////////////////////////////////
    // todo : fix equal() for Cards class
    public boolean isThisCardExistsInThisPlace(Cards card, PLACE_NAME placeName) {
        for (int i = 1; i < 6; i++) {
            if (getPlace(i, placeName).getCard().equals(card))
                return true;
        }
        return false;
    }

    public int getNumberOfCardsInThisPlace(PLACE_NAME placeName) {
        int counter = 0;
        for (int i = 1; i < 6; i++) {
            if (getPlace(i, placeName).getCard() != null)
                ++counter;
        }
        return counter;
    }

    public int getFirstEmptyPlace(PLACE_NAME placeName) {
        for (int i = 1; i < 6; i++) {
            if (getPlace(i, placeName).getCard() == null)
                return i;
        }
        return -1;
    }

    public Cards getCardByAddressAndPlace(int placeNumber, PLACE_NAME name) {
        return getPlace(placeNumber, name).getCard();
    }

    public STATUS getCardStatus(Cards card, PLACE_NAME placeName) {
        for (int i = 1; i < 6; i++) {
            if (getPlace(i, placeName).getCard().equals(card))
                return getPlace(i, placeName).getStatus();
        }
        return null;
    }

    public void changeStatusOfCard(int placeNumber, PLACE_NAME placeName, STATUS status) {
        if (getPlace(placeNumber, placeName) != null)
            getPlace(placeNumber, placeName).setStatus(status);

    }

    public int getPlaceNumberOfCard(Cards card, PLACE_NAME placeName) {
        for (int i = 1; i < 6; i++) {
            if (getPlace(i, placeName).getCard().equals(card))
                return i;
        }
        return -1;
    }

    public boolean isCardSetOrSummonInThisTurn(int placeNumber, PLACE_NAME placeName) {
        return getPlace(placeNumber, placeName)
                .isTemporaryFeaturesContainsThisFeature(TEMPORARY_FEATURES.CARD_SET_OR_SUMMON_IN_THIS_TURN);
    }

    public boolean isCardAttackedInThisTurn(int placeNumber, PLACE_NAME placeName) {
        return getPlace(placeNumber, placeName)
                .isTemporaryFeaturesContainsThisFeature(TEMPORARY_FEATURES.CARD_ATTACKED_IN_THIS_TURN);
    }

    public boolean isCardPositionChangedInThisTurn(int placeNumber, PLACE_NAME placeName) {
        return getPlace(placeNumber, placeName)
                .isTemporaryFeaturesContainsThisFeature(TEMPORARY_FEATURES.CARD_POSITION_CHANGED_IN_THIS_TURN);
    }

    public void setCardSetOrSummonInThisTurn(int placeNumber, PLACE_NAME placeName) {
        getPlace(placeNumber, placeName).addTemporaryFeatures
                (TEMPORARY_FEATURES.CARD_SET_OR_SUMMON_IN_THIS_TURN);
    }

    public void setCardAttackedInThisTurn(int placeNumber, PLACE_NAME placeName) {
        getPlace(placeNumber, placeName).addTemporaryFeatures
                (TEMPORARY_FEATURES.CARD_ATTACKED_IN_THIS_TURN);
    }

    public void setCardPositionChangedInThisTurn(int placeNumber, PLACE_NAME placeName) {
        getPlace(placeNumber, placeName).addTemporaryFeatures
                (TEMPORARY_FEATURES.CARD_POSITION_CHANGED_IN_THIS_TURN);
    }

    public Place getPlace(int placeNumber, PLACE_NAME placeName) {
        return place.get(placeName.getNumber() + placeNumber);
    }

    public void clearAllTemporaryFeatures() {
        for (int i = 1; i < 6; i++) {
            getPlace(i, PLACE_NAME.MONSTER).clearTemporaryFeatures();
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
