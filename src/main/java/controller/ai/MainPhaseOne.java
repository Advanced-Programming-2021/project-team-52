package controller.ai;

import controller.PrintBuilderController;
import model.cards.Cards;
import model.cards.monster.MonsterCards;
import model.game.*;
import model.tools.StringMessages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MainPhaseOne extends CommunicatorBetweenAIAndGameBoard implements StringMessages {
    private static MainPhaseOne mainPhaseOne = null;
    private GameBoard AIGameBoard;
    private GameBoard opponentGameBoard;
    private PrintBuilderController printBuilderController = PrintBuilderController.getInstance();
    private CommunicatorBetweenAIAndGameBoard gameBoardCommunicator = CommunicatorBetweenAIAndGameBoard.getInstance();
    private CommunicatorBetweenAIAndGamePlay gamePlayCommunicator = CommunicatorBetweenAIAndGamePlay.getInstance();
    private ArrayList<Place> ignoreCards = new ArrayList<>(); // if get error since set or summon, add the card to this

    private ArrayList<String> goodSpellCards = new ArrayList<>();
    private ArrayList<String> normalSpellCards = new ArrayList<>();
    private ArrayList<String> badSpellCards = new ArrayList<>();
    private ArrayList<String> responderSpellCards = new ArrayList<>();

    private ArrayList<String> goodTrapCards = new ArrayList<>();
    private ArrayList<String> normalTrapCards = new ArrayList<>();
    private ArrayList<String> badTrapCards = new ArrayList<>();
    private ArrayList<String> responderTrapCards = new ArrayList<>();

    private ArrayList<String> MonstersWithOneTribute = new ArrayList<>();
    private ArrayList<String> MonstersWithTwoTribute = new ArrayList<>();
    private ArrayList<String> MonstersWithThreeTribute = new ArrayList<>();

    private ArrayList<Place> worstCardsPlacesForTribute = new ArrayList<>();

    ArrayList<Place> AIMonsterCardPlacesAlreadyAttacked = new ArrayList<>();
    ArrayList<Place> opponentMonsterCardPlacesAlreadyHaveBeenAttacked = new ArrayList<>();

    private boolean isMonsterOperationDone = false;
    private boolean isSpellOperationInHandDone = false;
    private boolean isSpellOperationInSpellZoneDone = false;
    private boolean isTrapOperationDone = false;

    private boolean isInMonsterProcess = false;
    private boolean isInSetSpellProcess = false;
    private boolean isInActivateSpellProcess = false;
    private boolean isInSetTrapProcess = false;

    private PHASE phase = PHASE.MAIN;

    private MainPhaseOne() {
    }

    public static MainPhaseOne getInstance() {
        if (mainPhaseOne == null)
            mainPhaseOne = new MainPhaseOne();
        return mainPhaseOne;
    }

    public String mainCommand() {
        if (phase == PHASE.MAIN) {
            ArrayList<Place> monsterCards = gameBoardCommunicator.getCardsOfHand(AIGameBoard, "monster");
            ArrayList<Place> spellCards = gameBoardCommunicator.getCardsOfHand(AIGameBoard, "spell");
            ArrayList<Place> trapCards = gameBoardCommunicator.getCardsOfHand(AIGameBoard, "trap");

            if (isInMonsterProcess) {
                isInMonsterProcess = false;
                return summonAI;
            }
            if (isInActivateSpellProcess) {
                isInActivateSpellProcess = false;
                return activateSpellAI;
            }
            if (isInSetSpellProcess) {
                isInSetSpellProcess = false;
                return setTrapAI;
            }
            if (isInSetTrapProcess) {
                isInSetTrapProcess = false;
                return setTrapAI;
            }

            if (!isSpellOperationInSpellZoneDone) {
                String response = activateSpellsOfSpellZone(gameBoardCommunicator.getSpells
                        (gameBoardCommunicator.getSpellAndTrapZone(AIGameBoard)));
                if (response.equals("done"))
                    isSpellOperationInSpellZoneDone = true;
                else
                    return response;
            }
            if (!isSpellOperationInHandDone) {
                String response = activateOrSetAllSpellCardsInHand(spellCards);
                if (response.equals("done"))
                    isSpellOperationInHandDone = true;
                else
                    return response;
            }
            if (!isTrapOperationDone) {
                String response = setAllTrapCards(trapCards);
                if (response.equals("done"))
                    isTrapOperationDone = true;
                else
                    return response;
            }
            if (!isMonsterOperationDone) {
                String monsterResponse = monsterOperation(monsterCards);
                if (!monsterResponse.equals("")) {
                    isInMonsterProcess = true;
                    return monsterResponse;
                }
            }

            ignoreCards.clear();
            worstCardsPlacesForTribute.clear();

            isMonsterOperationDone = false;
            isSpellOperationInHandDone = false;
            isSpellOperationInSpellZoneDone = false;
            isTrapOperationDone = false;

            phase = PHASE.BATTLE;
            return StringMessages.nextPhaseAI;
        }else{
            ArrayList<Place> monsterCardPlaces = gameBoardCommunicator.getMonsterZone(AIGameBoard);
            if (monsterCardPlaces != null) {
                boolean canAttack = true;
                while (canAttack) {
                    canAttack = attack();
                }
            }

            phase = PHASE.MAIN;
            return StringMessages.nextPhaseAI;
        }
    }

    public void exitGraveYard() {
        // what to do ???
    }

    public void askNewChain() {

    }

    public String askStatus() {
        return responseToStatusAI;
    }

    public String askCardNameToSacrificeFromHand() {
        ArrayList<Place> monsterZone = gameBoardCommunicator.getMonsterZone(AIGameBoard);
        Place place = getBestCardByAttack(monsterZone, false);
        return place.getCard().getName();
    }

    public String getEnemyMonsterCardToControl() {
        ArrayList<Place> monsterZone = gameBoardCommunicator.getMonsterZone(opponentGameBoard);
        Place place = getBestCardByAttack(monsterZone, true);
        return String.valueOf(getNumberOfPlaceInGameBoard(opponentGameBoard, place, PLACE_NAME.MONSTER));
    }

    public String getMindCrush() {
        return mindCrush().getName();
    }

    public String getScanner() {
        ArrayList<Cards> AIGraveyard = AIGameBoard.getGraveyard();
        ArrayList<Cards> opponentGraveyard = opponentGameBoard.getGraveyard();
        MonsterCards bestAICard = gameBoardCommunicator.findBestMonsterCardByCard(AIGraveyard);
        MonsterCards bestOpponentCard = gameBoardCommunicator.findBestMonsterCardByCard(opponentGraveyard);
        MonsterCards bestCard = gameBoardCommunicator.cardsComparatorByAttack(bestAICard, bestOpponentCard);
        return bestAICard.getName();
    }

    public String getRitualCard() {
        ArrayList<Place> AIHand = new ArrayList<>();
        gameBoardCommunicator.getMonstersOfHand(AIHand, AIGameBoard, true);
        Place AIBestCardPlace = gameBoardCommunicator.getBestCardByAttack(AIHand, true);
        return String.valueOf(getNumberOfPlaceInGameBoard(AIGameBoard, AIBestCardPlace, PLACE_NAME.HAND));
    }

    public String askWhereToSpecialSummonFrom() {
        return graveYardAI;
    }

    public String getTribute() {
        ArrayList<Place> AIMonsterZone = gameBoardCommunicator.getMonsterZone(AIGameBoard);
        AIMonsterZone.removeAll(worstCardsPlacesForTribute);
        Place worstMonsterCard = gameBoardCommunicator.getBestCardByAttack(AIMonsterZone, false);
        worstCardsPlacesForTribute.add(worstMonsterCard);
        return String.valueOf(getNumberOfPlaceInGameBoard(AIGameBoard, worstMonsterCard, PLACE_NAME.MONSTER));
    }

//    public void start() {
//        ArrayList<Place> monsterCards = gameBoardCommunicator.getCardsOfHand(AIGameBoard, "monster");
//        ArrayList<Place> spellCards = gameBoardCommunicator.getCardsOfHand(AIGameBoard, "spell");
//        ArrayList<Place> trapCards = gameBoardCommunicator.getCardsOfHand(AIGameBoard, "trap");
//        if (spellCards != null) {
//            activateOrSetAllSpellCardsInHand(spellCards);
//            activateSpellsOfSpellZone(gameBoardCommunicator.getSpells
//                    (gameBoardCommunicator.getSpellAndTrapZone(AIGameBoard)));
//        }
//        if (trapCards != null) {
//            setAllTrapCards(trapCards);
//        }
//        if (monsterCards != null) {
//            boolean isSetOrSummoned = true;
//            while (isSetOrSummoned) {
//                isSetOrSummoned = setOrSummonMonster();
//            }
//        }
//        ignoreCards.clear();
//        gamePlayCommunicator.endPhase();
//    }

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

        Collections.addAll(goodTrapCards, "Trap Hole", "Mirror Force", "Magic Cylinder", "Mind Crush",
                "Time Seal", "Negate Attack", "Call of The Haunted");
        Collections.addAll(normalTrapCards, "Torrential Tribute");
        Collections.addAll(badTrapCards, "Solemn Warning", "Magic Jamamer");
        Collections.addAll(responderTrapCards, "Mind Crush", "Call of The Haunted");
    }

    public String monsterOperation(ArrayList<Place> monsterCards) {
        if (monsterCards != null) {
            String response = setOrSummonMonster();
            while (response.equals("")) {
                response = setOrSummonMonster();
            }
            isMonsterOperationDone = true;
            if (response.equals("done"))
                return "";
            return response;
        } else
            return "";
    }


    public String setOrSummonMonster() {
        ArrayList<Place> AIHand = new ArrayList<>();
        gameBoardCommunicator.getMonstersOfHand(AIHand, AIGameBoard, false);
        AIHand.removeIf(place -> ignoreCards.contains(place));
        if (AIHand.size() == 0) return "done";

        Place AIBestCardPlace;
        AIBestCardPlace = gameBoardCommunicator.getBestCardByAttack(AIHand, true);
        String AIBestCardName = AIBestCardPlace.getCard().getName();
        ArrayList<Place> AIMonsterZone = gameBoardCommunicator.getMonsterZone(AIGameBoard);

        if (MonstersWithOneTribute.contains(AIBestCardName)) {
            if (AIMonsterZone.size() >= 1) {
                Place worstMonsterCard = gameBoardCommunicator.getBestCardByAttack(AIMonsterZone, false);
                // summon "AIBestCardPlace"
                // tribute "worstMonsterCard"
                return selectProcess(AIBestCardPlace, PLACE_NAME.HAND);
            } else {
                ignoreCards.add(AIBestCardPlace);
                return "";
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
                return selectProcess(AIBestCardPlace, PLACE_NAME.HAND);
            } else {
                ignoreCards.add(AIBestCardPlace);
                return "";
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
                return selectProcess(AIBestCardPlace, PLACE_NAME.HAND);
                // tribute "worstCardsPlaces"
            } else {
                ignoreCards.add(AIBestCardPlace);
                return "";
            }
        } else {
            // check any other condition such as "ritual monsters"
            return selectProcess(AIBestCardPlace, PLACE_NAME.HAND);
        }
    }

    public boolean attack() {
        ArrayList<Place> AIMonsterCardPlaces = gameBoardCommunicator.getMonsterZone(AIGameBoard);
        ArrayList<Place> opponentMonsterCardPlaces = gameBoardCommunicator.getMonsterZone(opponentGameBoard);
        AIMonsterCardPlaces.removeIf(aiMonsterCardPlace
                -> AIMonsterCardPlacesAlreadyAttacked.contains(aiMonsterCardPlace));
        opponentMonsterCardPlaces.removeIf(opponentMonsterCardPlace
                -> opponentMonsterCardPlacesAlreadyHaveBeenAttacked.contains(opponentMonsterCardPlace));

        if (!AIMonsterCardPlaces.isEmpty()) {
            if (opponentMonsterCardPlaces.isEmpty()) {

                // not considering attack modifier
                Place cardPlaceForAttack = gameBoardCommunicator.
                        findBestMonsterCardByAttackByPlace(AIMonsterCardPlaces);
                if (cardPlaceForAttack == null) {
                    return false;
                } else {
                    // direct attack with cardPlaceForAttack
                    AIMonsterCardPlacesAlreadyAttacked.add(cardPlaceForAttack);
                    return true;
                }
            } else {
                if (gameBoardCommunicator.doesOpponentHaveSetCard(opponentMonsterCardPlaces)) {
                    Place firstOpponentSetCard = gameBoardCommunicator.
                            getFirstSetMonsterCard(opponentMonsterCardPlaces);
                    if (firstOpponentSetCard != null) {
                        // not considering attack modifier
                        Place cardPlaceForAttack = gameBoardCommunicator.
                                findBestMonsterCardByAttackByPlace(AIMonsterCardPlaces);
                        if (cardPlaceForAttack == null) {
                            return false;
                        } else {
                            // attack firstOpponentSetCard.getName
                            AIMonsterCardPlacesAlreadyAttacked.add(cardPlaceForAttack);
                            opponentMonsterCardPlacesAlreadyHaveBeenAttacked.add(firstOpponentSetCard);
                            return true;
                        }
                    }
                } else {
                    // not considering attack modifier
                    Place cardPlaceForAttack = gameBoardCommunicator.
                            findBestMonsterCardByAttackByPlace(AIMonsterCardPlaces);
                    if (cardPlaceForAttack != null) {
                        Place opponentCardPlaceToAttackTo = gameBoardCommunicator.
                                getBestMonsterCardStrengthInMonsterZoneForAttack
                                        (opponentMonsterCardPlaces, cardPlaceForAttack);
                        // attack to opponentCardPlaceToAttackTo
                        AIMonsterCardPlacesAlreadyAttacked.add(cardPlaceForAttack);
                        opponentMonsterCardPlacesAlreadyHaveBeenAttacked.add(opponentCardPlaceToAttackTo);
                        return true;
                    } else
                        return false;
                }
            }
        } else
            return false;
        return false;
    }


    private String selectProcess(Place place, PLACE_NAME placeName) {
        int cardPlaceNumber = gameBoardCommunicator.getNumberOfPlaceInGameBoard(AIGameBoard,
                place, placeName);
        if (cardPlaceNumber == -1) {
            ignoreCards.add(place);
            return "";
        } else {
            return printBuilderController.selectCardAI(cardPlaceNumber, placeName);
        }
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


    public String activateOrSetAllSpellCardsInHand(ArrayList<Place> spellCards) {
        for (Place spellCardPlace : spellCards) {
            Cards spellCard = spellCardPlace.getCard();
            String cardName = spellCard.getName();
//            boolean cardActivated = false;
            if (normalSpellCards.contains(cardName)) {
                boolean isConditionOkay = false;
                isConditionOkay = isSpellConditionOkay(cardName);

                if (isConditionOkay) {
//                    gamePlayCommunicator.activeSpell(spellCardPlace);
//                    cardActivated = true;
                    isInActivateSpellProcess = true;
                    return selectProcess(spellCardPlace, PLACE_NAME.HAND);

                } else {
//                    gamePlayCommunicator.setSpell(spellCardPlace);
                    isInSetSpellProcess = true;
                    return selectProcess(spellCardPlace, PLACE_NAME.HAND);
                }

            } else if (goodSpellCards.contains(cardName)) {
//                gamePlayCommunicator.activeSpell(spellCardPlace);
//                cardActivated = true;
                isInActivateSpellProcess = true;
                return selectProcess(spellCardPlace, PLACE_NAME.HAND);
            }

//            if (cardActivated) {
//                if (responderSpellCards.contains(cardName)) {
//                    responseSpellTarget(cardName);
//                }
//            }
        }
        return "done";
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

    public String activateSpellsOfSpellZone(ArrayList<Place> SpellPlaces) {
        for (Place spellPlace : SpellPlaces) {
            Cards spellCard = spellPlace.getCard();
            String cardName = spellCard.getName();
//            boolean cardActivated = false;
            if (normalSpellCards.contains(cardName)) {

                boolean isConditionOkay = false;
                isConditionOkay = isSpellConditionOkay(cardName);
                if (isConditionOkay) {
//                    gamePlayCommunicator.activeSpell(spellPlace);
//                    cardActivated = true;
                    isInActivateSpellProcess = true;
                    return selectProcess(spellPlace, PLACE_NAME.SPELL_AND_TRAP);
                }

            } else if (goodSpellCards.contains(cardName)) {
//                gamePlayCommunicator.activeSpell(spellPlace);
//                cardActivated = true;
                isInActivateSpellProcess = true;
                return selectProcess(spellPlace, PLACE_NAME.SPELL_AND_TRAP);
            }

//            if (cardActivated) {
//                if (responderSpellCards.contains(cardName)) {
//                    responseSpellTarget(cardName);
//                }
//            }
        }
        return "done";
    }


    public String setAllTrapCards(ArrayList<Place> trapCards) {
        for (Place trapCardPlace : trapCards) {
            Cards trapCard = trapCardPlace.getCard();
            String cardName = trapCard.getName();
            if (normalTrapCards.contains(cardName)) {
                boolean isConditionOkay = false;
                if (cardName.equals("Torrential Tribute")) {
                    isConditionOkay = torrentialTribute();
                }
                if (isConditionOkay) {
//                    gamePlayCommunicator.setTrap(trapCardPlace);
                    isInSetTrapProcess = true;
                    return selectProcess(trapCardPlace, PLACE_NAME.HAND);
                }
            } else if (goodTrapCards.contains(cardName)) {
//                gamePlayCommunicator.setTrap(trapCardPlace);
                isInSetTrapProcess = true;
                return selectProcess(trapCardPlace, PLACE_NAME.HAND);
            }
//            if (responderTrapCards.contains(cardName)) {
//                switch (cardName) {
//                    // go to appropriate method
//                }
//            }
        }
        return "done";
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
    public Cards mindCrush() {
        Random random = new Random();
        int randomNumber = random.nextInt(5);
        if (randomNumber == 0)
            return Cards.getCard("Suijin");
        else if (randomNumber == 1)
            return Cards.getCard("Dark magician");
        else if (randomNumber == 2)
            return Cards.getCard("Gate Guardian");
        else if (randomNumber == 3)
            return Cards.getCard("Spiral Serpent");
        else return Cards.getCard("Slot Machine");
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
