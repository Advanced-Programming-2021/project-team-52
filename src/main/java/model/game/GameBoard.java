package model.game;

import controller.GamePlayController;
import controller.specialbilities.SpecialAbilityActivationController;
import model.cards.Cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class GameBoard {

    private HashMap<Integer, Place> place;
    private ArrayList<Cards> graveyard;
    private ArrayList<Cards> mainCards;
    private ArrayList<Cards> sideCards;
    private ArrayList<Integer> cardsPicked;
    private int lastCardNumberPicked;
    private int health;
    private boolean monsterCardDestroyed = false;

    public GameBoard(ArrayList<Cards> mainCards, ArrayList<Cards> sideCards) {
        place = new HashMap<>();
        place.put(0, new Place(PLACE_NAME.HAND));
        for (int i = 1; i < 6; i++) {
            place.put(PLACE_NAME.HAND.getNumber() + i, new Place(PLACE_NAME.HAND));
            place.put(PLACE_NAME.MONSTER.getNumber() + i, new Place(PLACE_NAME.MONSTER));
            place.put(PLACE_NAME.SPELL_AND_TRAP.getNumber() + i, new Place(PLACE_NAME.SPELL_AND_TRAP));//TODO instantiate History arrayList From place
        }
        place.put(PLACE_NAME.FIELD.getNumber(), new Place(PLACE_NAME.FIELD));
        graveyard = new ArrayList<>();
        this.mainCards = new ArrayList<>(mainCards);
        this.sideCards = new ArrayList<>(sideCards);
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

    //TODO move shuffling to
    public void shuffleCards() {
        if (cardsPicked.isEmpty())
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
        while (cardsPicked.contains(lastCardNumberPicked))
            lastCardNumberPicked--;
        if (lastCardNumberPicked == 0 && cardsPicked.size() != mainCards.size()){
                lastCardNumberPicked = mainCards.size() - 1;
                return drawCard();
        } else {
            cardsPicked.add(lastCardNumberPicked);
            return (mainCards.get(lastCardNumberPicked--));
        }
    }

    public boolean noCardToDraw(){
        return mainCards.size() == cardsPicked.size();
    }

    public Cards getCard(PLACE_NAME name, int number) {
        return getPlace(number, name).getCard();
    }

    public ArrayList<Cards> getMainCards() {
        return mainCards;
    }

    public void setMainCards(ArrayList<Cards> mainCards) {
        this.mainCards = mainCards;
        lastCardNumberPicked = mainCards.size() - 1;
    }

    public ArrayList<Integer> getCardsPicked() {
        return cardsPicked;
    }

    public void setCardsPicked(ArrayList<Integer> cardsPicked) {
        this.cardsPicked = cardsPicked;
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

    public void setMonsterCardDestroyed(boolean monsterCardDestroyed) {
        this.monsterCardDestroyed = monsterCardDestroyed;
    }

    public boolean getMonsterCardDestroyed(){
        return monsterCardDestroyed;
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
                .isTemporaryFeaturesContainsThisFeature(TEMPORARY_FEATURES.CARD_SET_OR_SUMMONED_IN_THIS_TURN);
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
                (TEMPORARY_FEATURES.CARD_SET_OR_SUMMONED_IN_THIS_TURN);
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

    public Cards getACardByType(String type){
        int fieldNum = lastCardNumberPicked;
        while (fieldNum >= 0){
            if (mainCards.get(fieldNum).getType().equals(type) && !cardsPicked.contains(fieldNum)){
                cardsPicked.add(fieldNum);
                return mainCards.get(fieldNum);
            }
            fieldNum --;
        }
        return null;
    }

    public void killCards(GamePlayController gamePlayController, Place place){
        SpecialAbilityActivationController specialAbilityActivationController = SpecialAbilityActivationController.getInstance();
        specialAbilityActivationController.setGamePlayController(gamePlayController);
        specialAbilityActivationController.deathWishWithoutKillCard(place);
        if (place instanceof MonsterZone) {
            specialAbilityActivationController.removeMonsterFromFieldAndEffect(place);
            specialAbilityActivationController.runAttackAmountByQuantifier();
            monsterCardDestroyed = true;
        } else if (place instanceof Field)
            specialAbilityActivationController.deactivateField();
        graveyard.add(place.getCard());
        place.killCard();
    }

     public void changeHealth(int amount){
        health += amount;
     }

     public boolean fromThisGameBoard(Place place){
        return this.place.containsValue(place);
     }

    @Override
    public String toString() {
        //TODO change toString
        return "GameBoard";
    }
}
