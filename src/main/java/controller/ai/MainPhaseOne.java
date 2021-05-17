package controller.ai;

import model.cards.Cards;
import model.cards.monster.MonsterCards;
import model.game.*;

import java.util.ArrayList;

public class MainPhaseOne extends CommunicatorBetweenAIAndGame{
    private static MainPhaseOne mainPhaseOne = null;
    private GameBoard AIGameBoard;
    private GameBoard opponentGameBoard;
    private CommunicatorBetweenAIAndGame communicator = CommunicatorBetweenAIAndGame.getInstance();
    private ArrayList<Place> ignoreCards = new ArrayList<>(); // if get error since set or summon, add the card to this
    private ArrayList<Cards> goodSpellCards;
    private ArrayList<Cards> normalSpellCards;
    private ArrayList<Cards> goodTrapCards;
    private ArrayList<Cards> normalTrapCards;

    private MainPhaseOne(){}

    public static MainPhaseOne getInstance(){
        if(mainPhaseOne == null)
            mainPhaseOne = new MainPhaseOne();
        return mainPhaseOne;
    }

    public void a() {
        ArrayList<Place> monsterCards = communicator.getCardsOfHand(AIGameBoard, "monster");
        ArrayList<Place> spellCards = communicator.getCardsOfHand(AIGameBoard, "spell");
        ArrayList<Place> trapCards = communicator.getCardsOfHand(AIGameBoard, "trap");
        if (spellCards != null) {
            activateAllSpellCards(spellCards);
        }
        if (trapCards != null) {
            setAllTrapCars(trapCards);
        }
        for (Place ignoreCard : ignoreCards) {
            if (ignoreCard != null) // just to check, no exact reason
                monsterCards.remove(ignoreCard);
        }
        if (monsterCards != null) {

        }
    }


    public void setOrSummonMonster() {
        ArrayList<Place> AIHand = new ArrayList<>();
        communicator.getMonstersOfHand(AIHand, AIGameBoard);
        Place AIBestCard;

        ArrayList<Place> opponentMonsterZone = communicator.getMonsterZone(opponentGameBoard);


        boolean opponentHasSetCard = communicator.doesOpponentHaveSetCard(opponentMonsterZone);
        if (opponentHasSetCard) {
            AIBestCard = communicator.getBestCardByAttack(AIHand, true, ignoreCards);
            // summon AIBestCard
            // check for AIBestCard being null
            // attack to this card at first
        } else {
            Place weakestOpponentFaceUpMonsterPlace = communicator
                    .getWeakestOpponentFaceUpMonsterPlace(opponentMonsterZone);
            if (weakestOpponentFaceUpMonsterPlace == null) {
                AIBestCard = communicator.getBestCardByAttack(AIHand, true, ignoreCards);
                // summon best card
                // check for AIBestCard being null
                //  direct attack
            } else {
                AIBestCard = communicator.getBestCardByAttack(AIHand, true, ignoreCards);
                if (AIBestCard == null) {
                    // there is nothing to set or summon
                } else if (((MonsterCards) AIBestCard.getCard()).getAttack() > communicator
                        .getMonsterCardStrengthInMonsterZone(weakestOpponentFaceUpMonsterPlace)) {
                    // summon AIBestCard
                } else {
                    AIBestCard = communicator.getBestCardByAttack(AIHand, false, ignoreCards);
                    // set AIBestCard
                }
            }
        }
    }

    public void tribute(int numberOfCardsToTribute) {
        ArrayList<Place> AIMonsterZonePlaces = communicator.getMonsterZone(AIGameBoard);

    }

    public void activateAllSpellCards(ArrayList<Place> spellCards) {
        for (Place spellCardPlace : spellCards) {
            Cards spellCard = spellCardPlace.getCard();
            if (goodSpellCards.contains(spellCard)) {
                //activate spell
            } else if (normalSpellCards.contains(spellCard)) {
                // check condition
                // if it was ok -> activate spell
            }
        }
    }

    public void setAllTrapCars(ArrayList<Place> trapCards) {
        for (Place trapCardPlace : trapCards) {
            Cards trapCard = trapCardPlace.getCard();
            if (goodTrapCards.contains(trapCard)) {
                // set trap
            } else if (normalTrapCards.contains(trapCard)) {
                // check condition
                // if it was ok -> set trap
            }
        }
    }

    /// Spell cards

    public void monsterReborn() {
        ArrayList<Cards> AIGraveyard = AIGameBoard.getGraveyard();
        ArrayList<Cards> opponentGraveyard = opponentGameBoard.getGraveyard();
        MonsterCards bestAICard = communicator.findBestMonsterCardByCard(AIGraveyard);
        MonsterCards bestOpponentCard = communicator.findBestMonsterCardByCard(opponentGraveyard);
        MonsterCards bestCard = communicator.cardsComparatorByAttack(bestAICard, bestOpponentCard);
        // select bestCard and send it
    }

    public void changeOfHeart() {
        ArrayList<Place> opponentMonsterZone = communicator.getMonsterZone(opponentGameBoard);
        Place bestMonsterPlace = communicator.findBestMonsterCardByAttackByPlace(opponentMonsterZone);
        if (bestMonsterPlace != null) {
            // send message by bestMonsterPlace card
        }
    }

    public void mysticalSpaceTyphoon() {
        ArrayList<Place> opponentSpellAndTrapPlace = communicator.getSpellAndTrapZone(opponentGameBoard);
        for (Place place : opponentSpellAndTrapPlace) {
            if (place != null) {
                // send this card to destroy
                return;
            }
        }
        ArrayList<Place> AISpellAndTrapPlace = communicator.getSpellAndTrapZone(AIGameBoard);
        for (Place place : AISpellAndTrapPlace) {
            if (place != null) {
                // send this card to destroy
                return;
            }
        }
    }

    public void yami(){
        ArrayList<String> goodCards = new ArrayList<>();
        goodCards.add("Fiend");
        goodCards.add("Spellcaster");
        ArrayList<String> badCards = new ArrayList<>();
        badCards.add("Fairy");
        if(weightedCardCounter(goodCards, badCards, AIGameBoard, opponentGameBoard)){
            // activate spell
        }

    }

    public void forest(){
        ArrayList<String> goodCards = new ArrayList<>();
        goodCards.add("Insect");
        goodCards.add("Beast");
        goodCards.add("Beast-Warrior");
        ArrayList<String> badCards = new ArrayList<>();
        if(weightedCardCounter(goodCards, badCards, AIGameBoard, opponentGameBoard)){
            // activate spell
        }
    }

    public void closedForest(){
        if(numberThisTypeMonsterInThisZone(AIGameBoard, "Beast-Type") > 0){
            // activate spell
        }
    }

    public void umiiruka(){
        // most of our cards are in attack position and DEF point isn't as important as ATK point
        ArrayList<String> goodCards = new ArrayList<>();
        goodCards.add("Aqua");
        ArrayList<String> badCards = new ArrayList<>();
        if(weightedCardCounter(goodCards, badCards, AIGameBoard, opponentGameBoard)){
            // activate spell
        }
    }

//    public void swordOfDarkDestruction(){
//        // most of our cards are in attack position and DEF point isn't as important as ATK point
//        ArrayList<String> goodCards = new ArrayList<>();
//        goodCards.add("Fiend");
//        goodCards.add("Spellcaster");
//        ArrayList<String> badCards = new ArrayList<>();
//        if(weightedCardCounter(goodCards, badCards, AIGameBoard, opponentGameBoard)){
//            // activate spell
//        }
//    }

    public void magnumShield(){
        ArrayList<String> goodCards = new ArrayList<>();
        goodCards.add("Warrior");
        ArrayList<String> badCards = new ArrayList<>();
        if(weightedCardCounter(goodCards, badCards, AIGameBoard, opponentGameBoard)){
            // activate spell
        }
    }


    /// Trap cards

    public void mindCrush(){
        // send s the best monster
    }




}
