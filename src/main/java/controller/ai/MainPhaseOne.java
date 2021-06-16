package controller.ai;

import model.cards.Cards;
import model.cards.monster.MonsterCards;
import model.game.*;

import java.util.ArrayList;
import java.util.Collections;

public class MainPhaseOne extends CommunicatorBetweenAIAndGameBoard {
    private static MainPhaseOne mainPhaseOne = null;
    private GameBoard AIGameBoard;
    private GameBoard opponentGameBoard;
    private CommunicatorBetweenAIAndGameBoard gameBoardCommunicator = CommunicatorBetweenAIAndGameBoard.getInstance();
    private CommunicatorBetweenAIAndGamePlay gamePlayCommunicator = CommunicatorBetweenAIAndGamePlay.getInstance();
    private ArrayList<Place> ignoreCards = new ArrayList<>(); // if get error since set or summon, add the card to this
    // initialize these Arraylists
    private ArrayList<String> goodSpellCards = new ArrayList<>();
    private ArrayList<String> normalSpellCards = new ArrayList<>();
    private ArrayList<String> badSpellCards = new ArrayList<>();
    private ArrayList<String> responderSpellCards = new ArrayList<>();
    private ArrayList<String> goodTrapCards = new ArrayList<>();
    private ArrayList<String> normalTrapCards = new ArrayList<>();
    private ArrayList<String> responderTrapCards = new ArrayList<>();
    private ArrayList<String> MonstersWithOneTribute = new ArrayList<>();
    private ArrayList<String> MonstersWithTwoTribute = new ArrayList<>();
    private ArrayList<String> MonstersWithThreeTribute = new ArrayList<>();

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
            activateOrSetAllSpellCardsInHand(spellCards);
            activateSpellsOfSpellZone(gameBoardCommunicator.getSpells
                    (gameBoardCommunicator.getSpellAndTrapZone(AIGameBoard)));
        }
        if (trapCards != null) {
            setAllTrapCards(trapCards);
        }
        if (monsterCards != null) {
            boolean isSetOrSummoned = true;
            while (isSetOrSummoned) {
                isSetOrSummoned = setOrSummonMonster();
            }
        }
        ignoreCards.clear();
        gamePlayCommunicator.endPhase();
    }

    public void initializeArrays() {
        Collections.addAll(goodSpellCards, "Monster Reborn", "Terraforming", "Pot of Greed", "Raigeki",
                "Change of Heart", "Swords of Revealing Light", "Harpie's Feather Duster", "Dark Hole", "Supply Squad",
                "Spell Absorption", "Mystical space typhoon", "Ring of defense");
        Collections.addAll(normalSpellCards, "Yami", "Forest", "Closed Forest", "Umiiruka");
        Collections.addAll(badSpellCards, "Messenger of peace", "Twin Twisters");
        Collections.addAll(responderSpellCards, "Monster Reborn", "Change of Heart", "Mystical space typhoon");

        Collections.addAll(MonstersWithOneTribute, "Crawling dragon", "Wattaildragon", "The Tricky");
        Collections.addAll(MonstersWithTwoTribute, "Suijin", "Dark magician", "Blue_Eyes white dragon",
                "Crab Turtle", "Skull Guardian", "Slot Machine", "Beast King Barbaros", "Spiral Serpent");
        Collections.addAll(MonstersWithThreeTribute, "Gate Guardian");
    }


    public boolean setOrSummonMonster() {
        ArrayList<Place> AIHand = new ArrayList<>();
        gameBoardCommunicator.getMonstersOfHand(AIHand, AIGameBoard);
        AIHand.removeIf(place -> ignoreCards.contains(place));
        if (AIHand.size() == 0) return true;

        Place AIBestCardPlace;
        AIBestCardPlace = gameBoardCommunicator.getBestCardByAttack(AIHand, true);
        String AIBestCardName = AIBestCardPlace.getCard().getName();
        ArrayList<Place> AIMonsterZone = gameBoardCommunicator.getMonsterZone(AIGameBoard);

        if (MonstersWithOneTribute.contains(AIBestCardName)) {
            if (AIMonsterZone.size() >= 1) {
                Place worstMonsterCard = gameBoardCommunicator.getBestCardByAttack(AIMonsterZone, false);
                // summon "AIBestCardPlace"
                // tribute "worstMonsterCard"
                return true;
            } else {
                ignoreCards.add(AIBestCardPlace);
                return false;
            }
        } else if (MonstersWithTwoTribute.contains(AIBestCardName)) {
            if (AIMonsterZone.size() >= 2) {
                ArrayList<Place> worstCardsPlaces = new ArrayList<>();
                for (int i = 0; i < 2; i++) {
                    AIMonsterZone.removeAll(worstCardsPlaces);
                    Place worstMonsterCard = gameBoardCommunicator.getBestCardByAttack(AIMonsterZone, false);
                    AIMonsterZone.addAll(worstCardsPlaces);
                    worstCardsPlaces.add(worstMonsterCard);
                }
                // summon "AIBestCardPlace"
                // tribute "worstCardsPlaces"
                return true;
            } else {
                ignoreCards.add(AIBestCardPlace);
                return false;
            }
        } else if (MonstersWithThreeTribute.contains(AIBestCardName)) {
            if (AIMonsterZone.size() >= 3) {
                ArrayList<Place> worstCardsPlaces = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    AIMonsterZone.removeAll(worstCardsPlaces);
                    Place worstMonsterCard = gameBoardCommunicator.getBestCardByAttack(AIMonsterZone, false);
                    AIMonsterZone.addAll(worstCardsPlaces);
                    worstCardsPlaces.add(worstMonsterCard);
                }
                // summon "AIBestCardPlace"
                // tribute "worstCardsPlaces"
                return true;
            } else {
                ignoreCards.add(AIBestCardPlace);
                return false;
            }
        } else {
            // check any other condition such as "ritual monsters"
            //summon "AIBestCardPlace"
            return true;
        }


//        ArrayList<Place> opponentMonsterZone = gameBoardCommunicator.getMonsterZone(opponentGameBoard);
//        boolean opponentHasSetCard = gameBoardCommunicator.doesOpponentHaveSetCard(opponentMonsterZone);
//        if (opponentHasSetCard) {
//            AIBestCardPlace = gameBoardCommunicator.getBestCardByAttack(AIHand, true);
//
//            // summon AIBestCardPlace
//            // check for AIBestCardPlace being null
//            // attack to this card at first
//            return true;
//        } else {
//            Place weakestOpponentFaceUpMonsterPlace = gameBoardCommunicator
//                    .getWeakestOpponentFaceUpMonsterPlace(opponentMonsterZone);
//            if (weakestOpponentFaceUpMonsterPlace == null) {
//                AIBestCardPlace = gameBoardCommunicator.getBestCardByAttack(AIHand, true);
//                ignoreCards.add(AIBestCardPlace);
//                // summon best card
//                // check for AIBestCardPlace being null
//                //  direct attack
//                return true;
//            } else {
//                AIBestCardPlace = gameBoardCommunicator.getBestCardByAttack(AIHand, true);
//                if (AIBestCardPlace == null) {
//                    // there is nothing to set or summon
//                    return true;
//                } else if (((MonsterCards) AIBestCardPlace.getCard()).getAttack() > gameBoardCommunicator
//                        .getMonsterCardStrengthInMonsterZone(weakestOpponentFaceUpMonsterPlace)) {
//                    ignoreCards.add(AIBestCardPlace);
//                    // summon AIBestCardPlace
//                    // if summon successful --> return true;
//                    // else add AIBestCardPlace to ignoreCards and return false
//                    return false; // just to avoid syntax error
//                } else {
//                    AIBestCardPlace = gameBoardCommunicator.getBestCardByAttack(AIHand, false);
//                    ignoreCards.add(AIBestCardPlace);
//                    // set AIBestCardPlace
//                    return true;
//                }
//            }
//        }
    }

    public void activateOrSetAllSpellCardsInHand(ArrayList<Place> spellCards) {
        for (Place spellCardPlace : spellCards) {
            Cards spellCard = spellCardPlace.getCard();
            String cardName = spellCard.getName();
            boolean cardActivated = false;
            if (normalSpellCards.contains(cardName)) {
                boolean isConditionOkay = false;
                isConditionOkay = isSpellConditionOkay(cardName);

                if (isConditionOkay) {
                    gamePlayCommunicator.activeSpell(spellCardPlace);
                    cardActivated = true;
                } else {
                    gamePlayCommunicator.setSpell(spellCardPlace);
                }

            } else if (goodSpellCards.contains(cardName)) {
                gamePlayCommunicator.activeSpell(spellCardPlace);
                cardActivated = true;
            }

            if (cardActivated) {
                if (responderSpellCards.contains(cardName)) {
                    responseSpellTarget(cardName);
                }
            }
        }
    }

    private void responseSpellTarget(String cardName) {
        if (cardName.equals("Monster Reborn")) {
            monsterReborn();
        } else if (cardName.equals("Change of Heart")) {
            changeOfHeart();
        } else if (cardName.equals("Mystical space typhoon")) {
            mysticalSpaceTyphoon();
        }
    }

    private boolean isSpellConditionOkay(String cardName) {
        if (cardName.equals("Yami")) {
            return yami();
        } else if (cardName.equals("Forest")) {
            return forest();
        } else if (cardName.equals("Closed Forest")) {
            return closedForest();
        } else if (cardName.equals("Umiiruka")) {
            return umiiruka();
        } else return false;
    }

    public void activateSpellsOfSpellZone(ArrayList<Place> SpellPlaces) {
        for (Place spellPlace : SpellPlaces) {
            Cards spellCard = spellPlace.getCard();
            String cardName = spellCard.getName();
            boolean cardActivated = false;
            if (normalSpellCards.contains(cardName)) {

                boolean isConditionOkay = false;
                isConditionOkay = isSpellConditionOkay(cardName);
                if (isConditionOkay) {
                    gamePlayCommunicator.activeSpell(spellPlace);
                    cardActivated = true;
                }

            } else if (goodSpellCards.contains(cardName)) {
                gamePlayCommunicator.activeSpell(spellPlace);
                cardActivated = true;
            }

            if (cardActivated) {
                if (responderSpellCards.contains(cardName)) {
                    responseSpellTarget(cardName);
                }
            }
        }
    }


    public void setAllTrapCards(ArrayList<Place> trapCards) {
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
