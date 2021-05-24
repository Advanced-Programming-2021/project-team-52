package controller.ai;

import model.cards.Cards;
import model.cards.monster.MonsterCards;
import model.game.*;

import java.util.ArrayList;

public class MainPhaseOne extends CommunicatorBetweenAIAndGameBoard {
    private static MainPhaseOne mainPhaseOne = null;
    private GameBoard AIGameBoard;
    private GameBoard opponentGameBoard;
    private CommunicatorBetweenAIAndGameBoard gameBoardCommunicator = CommunicatorBetweenAIAndGameBoard.getInstance();
    private CommunicatorBetweenAIAndGamePlay gamePlayCommunicator = CommunicatorBetweenAIAndGamePlay.getInstance();
    private ArrayList<Place> ignoreCards = new ArrayList<>(); // if get error since set or summon, add the card to this
    // initialize these Arraylists
    private ArrayList<String> goodSpellCards;
    private ArrayList<String> normalSpellCards;
    private ArrayList<String> goodTrapCards;
    private ArrayList<String> normalTrapCards;
    private ArrayList<String> responderSpellCards;
    private ArrayList<String> responderTrapCards;

    private MainPhaseOne() {
    }

    public static MainPhaseOne getInstance() {
        if (mainPhaseOne == null)
            mainPhaseOne = new MainPhaseOne();
        return mainPhaseOne;
    }

    public void start() {
        ArrayList<Place> monsterCards = gameBoardCommunicator.getCardsOfHand(AIGameBoard, "monster");
        ArrayList<Place> spellCards = gameBoardCommunicator.getCardsOfHand(AIGameBoard, "spell");
        ArrayList<Place> trapCards = gameBoardCommunicator.getCardsOfHand(AIGameBoard, "trap");
        if (spellCards != null) {
            activateOrSetAllSpellCards(spellCards);
        }
        if (trapCards != null) {
            setAllTrapCars(trapCards);
        }
        if (monsterCards != null) {
            boolean isSetOrSummoned = true;
            while (isSetOrSummoned) {
                isSetOrSummoned = setOrSummonMonster();
            }
        }
        gamePlayCommunicator.endPhase();
    }


    public boolean setOrSummonMonster() {
        ArrayList<Place> AIHand = new ArrayList<>();
        gameBoardCommunicator.getMonstersOfHand(AIHand, AIGameBoard);
        AIHand.removeIf(place -> ignoreCards.contains(place));
        Place AIBestCardPlace;

        ArrayList<Place> opponentMonsterZone = gameBoardCommunicator.getMonsterZone(opponentGameBoard);


        boolean opponentHasSetCard = gameBoardCommunicator.doesOpponentHaveSetCard(opponentMonsterZone);
        if (opponentHasSetCard) {
            AIBestCardPlace = gameBoardCommunicator.getBestCardByAttack(AIHand, true);

            // summon AIBestCardPlace
            // check for AIBestCardPlace being null
            // attack to this card at first
            return true;
        } else {
            Place weakestOpponentFaceUpMonsterPlace = gameBoardCommunicator
                    .getWeakestOpponentFaceUpMonsterPlace(opponentMonsterZone);
            if (weakestOpponentFaceUpMonsterPlace == null) {
                AIBestCardPlace = gameBoardCommunicator.getBestCardByAttack(AIHand, true);
                ignoreCards.add(AIBestCardPlace);
                // summon best card
                // check for AIBestCardPlace being null
                //  direct attack
                return true;
            } else {
                AIBestCardPlace = gameBoardCommunicator.getBestCardByAttack(AIHand, true);
                if (AIBestCardPlace == null) {
                    // there is nothing to set or summon
                    return true;
                } else if (((MonsterCards) AIBestCardPlace.getCard()).getAttack() > gameBoardCommunicator
                        .getMonsterCardStrengthInMonsterZone(weakestOpponentFaceUpMonsterPlace)) {
                    ignoreCards.add(AIBestCardPlace);
                    // summon AIBestCardPlace
                    // if summon successful --> return true;
                    // else add AIBestCardPlace to ignoreCards and return false
                    return false; // just to avoid syntax error
                } else {
                    AIBestCardPlace = gameBoardCommunicator.getBestCardByAttack(AIHand, false);
                    ignoreCards.add(AIBestCardPlace);
                    // set AIBestCardPlace
                    return true;
                }
            }
        }
    }

    public void tribute(Place place) {
        ArrayList<Place> AIMonsterZonePlaces = gameBoardCommunicator.getMonsterZone(AIGameBoard);
        int monsterLevel = ((MonsterCards)place.getCard()).getLevel();


    }

    public void activateOrSetAllSpellCards(ArrayList<Place> spellCards) {
        for (Place spellCardPlace : spellCards) {
            Cards spellCard = spellCardPlace.getCard();
            String cardName = spellCard.getName();
            if (normalSpellCards.contains(cardName)) {
                boolean isConditionOkay = false;
                switch (cardName) {
                    // go to appropriate method
                    // evaluate isConditionOkay
                }
                if (isConditionOkay)
                    gamePlayCommunicator.activeSpell(spellCardPlace);
                else
                    gamePlayCommunicator.setSpell(spellCardPlace);
            } else
                gamePlayCommunicator.activeSpell(spellCardPlace);
            if (responderSpellCards.contains(cardName)) {
                switch (cardName) {
                    // go to appropriate method
                }
            }
        }
    }

    public void setAllTrapCars(ArrayList<Place> trapCards) {
        for (Place trapCardPlace : trapCards) {
            Cards trapCard = trapCardPlace.getCard();
            String cardName = trapCard.getName();
            if (normalTrapCards.contains(cardName)) {
                boolean isConditionOkay = false;
                switch (cardName) {
                    // go to appropriate method
                    // evaluate isConditionOkay
                }
                if (isConditionOkay)
                    gamePlayCommunicator.setTrap(trapCardPlace);
            } else
                gamePlayCommunicator.setTrap(trapCardPlace);
            if (responderTrapCards.contains(cardName)) {
                switch (cardName) {
                    // go to appropriate method
                }
            }
        }
    }

    /// Spell cards

    // responder
    public void monsterReborn() {
        ArrayList<Cards> AIGraveyard = AIGameBoard.getGraveyard();
        ArrayList<Cards> opponentGraveyard = opponentGameBoard.getGraveyard();
        MonsterCards bestAICard = gameBoardCommunicator.findBestMonsterCardByCard(AIGraveyard);
        MonsterCards bestOpponentCard = gameBoardCommunicator.findBestMonsterCardByCard(opponentGraveyard);
        MonsterCards bestCard = gameBoardCommunicator.cardsComparatorByAttack(bestAICard, bestOpponentCard);
        // select bestCard and send it
    }

    // responder
    public void changeOfHeart() {
        ArrayList<Place> opponentMonsterZone = gameBoardCommunicator.getMonsterZone(opponentGameBoard);
        Place bestMonsterPlace = gameBoardCommunicator.findBestMonsterCardByAttackByPlace(opponentMonsterZone);
        if (bestMonsterPlace != null) {
            // send message by bestMonsterPlace card
        }
    }

    // responder
    public void mysticalSpaceTyphoon() {
        ArrayList<Place> opponentSpellAndTrapPlace = gameBoardCommunicator.getSpellAndTrapZone(opponentGameBoard);
        for (Place place : opponentSpellAndTrapPlace) {
            if (place != null) {
                // send this card to destroy
                return;
            }
        }
        ArrayList<Place> AISpellAndTrapPlace = gameBoardCommunicator.getSpellAndTrapZone(AIGameBoard);
        for (Place place : AISpellAndTrapPlace) {
            if (place != null) {
                // send this card to destroy
                return;
            }
        }
    }

    // checker
    public boolean yami() {
        ArrayList<String> goodCards = new ArrayList<>();
        goodCards.add("Fiend");
        goodCards.add("Spellcaster");
        ArrayList<String> badCards = new ArrayList<>();
        badCards.add("Fairy");
        if (weightedCardCounter(goodCards, badCards, AIGameBoard, opponentGameBoard)) {
            return true;
        }
        return false;
    }

    // checker
    public boolean forest() {
        ArrayList<String> goodCards = new ArrayList<>();
        goodCards.add("Insect");
        goodCards.add("Beast");
        goodCards.add("Beast-Warrior");
        ArrayList<String> badCards = new ArrayList<>();
        if (weightedCardCounter(goodCards, badCards, AIGameBoard, opponentGameBoard)) {
            return true;
        }
        return false;
    }

    // checker
    public boolean closedForest() {
        if (numberThisTypeMonsterInThisZone(AIGameBoard, "Beast-Type") > 0) {
            return true;
        }
        return false;

    }

    // checker
    public boolean umiiruka() {
        // most of our cards are in attack position and DEF point isn't as important as ATK point
        ArrayList<String> goodCards = new ArrayList<>();
        goodCards.add("Aqua");
        ArrayList<String> badCards = new ArrayList<>();
        if (weightedCardCounter(goodCards, badCards, AIGameBoard, opponentGameBoard)) {
            return true;
        }
        return false;
    }

    // checker
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

    // checker
    public boolean magnumShield() {
        ArrayList<String> goodCards = new ArrayList<>();
        goodCards.add("Warrior");
        ArrayList<String> badCards = new ArrayList<>();
        if (weightedCardCounter(goodCards, badCards, AIGameBoard, opponentGameBoard)) {
            return true;
        }
        return false;
    }


    /// Trap cards

    // responder
    public void mindCrush() {
        // send the best monster
    }

    // checker
    public boolean torrentialTribute() {
        ArrayList<Place> AIMonsterZoneCards = getMonsterZone(AIGameBoard);
        ArrayList<Place> opponentMonsterZone = getMonsterZone(opponentGameBoard);
        if (((MonsterCards) getWeakestOpponentFaceUpMonsterPlace(opponentMonsterZone).getCard()).getAttack() >=
                ((MonsterCards) findBestMonsterCardByAttackByPlace(AIMonsterZoneCards).getCard()).getAttack()) {
            return true;
        }
        return false;
    }

    // responder
    public void callOfTheHaunted() {
        ArrayList<Cards> AIGraveyard = AIGameBoard.getGraveyard();
        MonsterCards bestMonsterCard = findBestMonsterCardByCard(AIGraveyard);
        if (bestMonsterCard != null) {
            // activate trap
        }
    }
}
