package sample.controller.ai;

import sample.controller.PrintBuilderController;
import sample.model.cards.Cards;
import sample.model.cards.monster.MonsterCards;
import sample.model.game.*;
import sample.model.tools.StringMessages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class AIController extends CommunicatorBetweenAIAndGameBoard implements StringMessages {
    private static AIController AIController = null;
    private GameBoard AIGameBoard;
    private GameBoard opponentGameBoard;
    private PrintBuilderController printBuilderController = PrintBuilderController.getInstance();
    private CommunicatorBetweenAIAndGameBoard gameBoardCommunicator = CommunicatorBetweenAIAndGameBoard.getInstance();
    private ArrayList<Place> ignoreCards = new ArrayList<>();

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

    private boolean isAttackOperationDone = false;

    private boolean isInMonsterProcess = false;
    private boolean isInSetSpellProcess = false;
    private boolean isInActivateSpellProcess = false;
    private boolean isInSetTrapProcess = false;
    private boolean isInActivateTrapProcess = false;

    private boolean isInDirectAttackProcess = false;
    private ArrayList<Integer> opponentMonsterCardToAttack = new ArrayList<>();

    private PHASE phase = PHASE.MAIN;

    private AIController() {
    }

    public static AIController getInstance() {
        if (AIController == null)
            AIController = new AIController();
        return AIController;
    }

    public String mainCommand() {
        if (phase == PHASE.MAIN) {
            return mainPhaseProcess();
        } else {
            return battlePhaseProcess();
        }
    }

    private String battlePhaseProcess() {
        if (isInDirectAttackProcess) {
            isInDirectAttackProcess = false;
            return directAttackAI;
        }
        if (opponentMonsterCardToAttack.size() > 0) {
            int numberToAttack = opponentMonsterCardToAttack.get(0);
            opponentMonsterCardToAttack.clear();
            return printBuilderController.attackToMonster(numberToAttack);
        }

        if (!isAttackOperationDone) {
            String response = attack();
            if (response.equals("done"))
                isAttackOperationDone = true;
            else
                return response;
        }

        opponentMonsterCardPlacesAlreadyHaveBeenAttacked.clear();
        AIMonsterCardPlacesAlreadyAttacked.clear();

        isAttackOperationDone = false;

        phase = PHASE.MAIN;
        return StringMessages.nextPhaseAI;
    }

    private String mainPhaseProcess() {
        ArrayList<Place> monsterCards = gameBoardCommunicator.getCardsOfHand(AIGameBoard, "monster");
        ArrayList<Place> spellCards = gameBoardCommunicator.getCardsOfHand(AIGameBoard, "spell");
        ArrayList<Place> trapCards = gameBoardCommunicator.getCardsOfHand(AIGameBoard, "trap");

        String putCardInFieldProcess = getPutCardInFieldProcess();
        if (putCardInFieldProcess != null) return putCardInFieldProcess;

        String response = selectCardForOperation(monsterCards, spellCards, trapCards);
        if (response != null) return response;

        ignoreCards.clear();
        worstCardsPlacesForTribute.clear();

        isMonsterOperationDone = false;
        isSpellOperationInHandDone = false;
        isSpellOperationInSpellZoneDone = false;
        isTrapOperationDone = false;

        phase = PHASE.BATTLE;
        return StringMessages.nextPhaseAI;
    }

    private String selectCardForOperation(ArrayList<Place> monsterCards, ArrayList<Place> spellCards,
                                          ArrayList<Place> trapCards) {
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
        return null;
    }

    private String getPutCardInFieldProcess() {
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
        return null;
    }

    public String exitGraveYard() {
        return "back";
    }

    public String askNewChain(ArrayList<String> cards) {
        if (cards.size() == 0)
            return noAI;
        ArrayList<Place> spellAndTrapZone = getSpellAndTrapZone(AIGameBoard);
        ArrayList<Place> trapCards = getTrapCards(spellAndTrapZone);
        for (Place trapCard : trapCards) {
            String name = trapCard.getCard().getName();
            if (goodTrapCards.contains(name) && cards.contains(name))
                return yesAI;
        }
        return noAI;
    }

    public String chainCommand(ArrayList<String> cards) {
        if (isInActivateTrapProcess) {
            isInActivateTrapProcess = false;
            return activateTrapAI;
        }
        return chooseCardInChain(cards);
    }

    private String chooseCardInChain(ArrayList<String> cards) {
        ArrayList<Place> spellAndTrapZone = getSpellAndTrapZone(AIGameBoard);
        ArrayList<Place> trapCards = getTrapCards(spellAndTrapZone);
        for (Place trapCard : trapCards) {
            String name = trapCard.getCard().getName();
            if (goodTrapCards.contains(name) && cards.contains(name)) {
                isInActivateTrapProcess = true;
                return selectProcess(trapCard, PLACE_NAME.SPELL_AND_TRAP);
            } else if (normalTrapCards.contains(name) && cards.contains(name)) {
                boolean isConditionOkay = false;
                if (name.equals("Torrential Tribute"))
                    isConditionOkay = torrentialTribute();
                if (isConditionOkay) {
                    isInActivateTrapProcess = true;
                    return selectProcess(trapCard, PLACE_NAME.SPELL_AND_TRAP);
                }
            }
        }
        return endAI;
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
        MonsterCards bestCard = getBestMonsterCardFromGraveYard();
        return bestCard.getName();
    }

    private MonsterCards getBestMonsterCardFromGraveYard() {
        ArrayList<Cards> AIGraveyard = AIGameBoard.getGraveyard();
        ArrayList<Cards> opponentGraveyard = opponentGameBoard.getGraveyard();
        MonsterCards bestAICard = gameBoardCommunicator.findBestMonsterCardByCard(AIGraveyard);
        MonsterCards bestOpponentCard = gameBoardCommunicator.findBestMonsterCardByCard(opponentGraveyard);
        MonsterCards bestCard = gameBoardCommunicator.cardsComparatorByAttack(bestAICard, bestOpponentCard);
        return bestCard;
    }

    public String getRitualCard() {
        ArrayList<Place> AIHand = new ArrayList<>();
        gameBoardCommunicator.getMonstersOfHand(AIHand, AIGameBoard, true);
        Place AIBestCardPlace = gameBoardCommunicator.getBestCardByAttack(AIHand, true);
        return String.valueOf(getNumberOfPlaceInGameBoard(AIGameBoard, AIBestCardPlace, PLACE_NAME.HAND));
    }

    public String getMonsterToEquip() {
        ArrayList<Place> AIMonsterZone = gameBoardCommunicator.getMonsterZone(AIGameBoard);
        Place AIBestCardPlace = gameBoardCommunicator.getBestCardByAttack(AIMonsterZone, true);
        return String.valueOf(getNumberOfPlaceInGameBoard(AIGameBoard, AIBestCardPlace, PLACE_NAME.MONSTER));
    }

    public String askWhereToSpecialSummonFrom() {
        return graveYardAI;
    }

    public String askCardToDestroy() {
        ArrayList<Place> opponentMonsterZone = gameBoardCommunicator.getMonsterZone(opponentGameBoard);
        Place opponentBestCardPlace = gameBoardCommunicator.getBestCardByAttack(opponentMonsterZone, true);
        return String.valueOf(getNumberOfPlaceInGameBoard(opponentGameBoard, opponentBestCardPlace,
                PLACE_NAME.MONSTER));
    }

    public String getTribute() {
        ArrayList<Place> AIMonsterZone = gameBoardCommunicator.getMonsterZone(AIGameBoard);
        AIMonsterZone.removeAll(worstCardsPlacesForTribute);
        Place worstMonsterCard = gameBoardCommunicator.getBestCardByAttack(AIMonsterZone, false);
        worstCardsPlacesForTribute.add(worstMonsterCard);
        return String.valueOf(getNumberOfPlaceInGameBoard(AIGameBoard, worstMonsterCard, PLACE_NAME.MONSTER));
    }

    public String askActivateSpecial() {
        return noAI;
    }

    public String askNormalSummon() {
        return noAI;
    }

    public String specialSummonFromGraveYard() {
        MonsterCards bestCard = getBestMonsterCardFromGraveYard();
        return bestCard.getName();
    }

    public String getMonsterToSummon(int maxLevel) {
        ArrayList<Place> AIHand = new ArrayList<>();
        ArrayList<Place> ignoreTheseCards = new ArrayList<>();
        gameBoardCommunicator.getMonstersOfHand(AIHand, AIGameBoard, true);
        gameBoardCommunicator.getMonstersOfHand(AIHand, AIGameBoard, false);
        while (AIHand.size() != 0) {
            AIHand.removeIf(ignoreTheseCards::contains);
            Place AIBestCardPlace = gameBoardCommunicator.getBestCardByAttack(AIHand, true);
            if (((MonsterCards) AIBestCardPlace.getCard()).getLevel() > maxLevel) {
                ignoreTheseCards.add(AIBestCardPlace);
            } else
                return String.valueOf(getNumberOfPlaceInGameBoard(AIGameBoard, AIBestCardPlace, PLACE_NAME.MONSTER));
        }
        return "cancel";
    }

    public void initializeArrays() {
        Collections.addAll(goodSpellCards, "Monster Reborn", "Terraforming", "Pot of Greed", "Raigeki",
                "Change of Heart", "Swords of Revealing Light", "Harpie's Feather Duster", "Dark Hole", "Supply Squad",
                "Spell Absorption", "Mystical space typhoon", "Ring of defense", "Sword of dark destruction",
                "Black Pendant", "United We Stand", "Magnum Shield", "Advanced Ritual Art");
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

        return findBestMonster(AIBestCardPlace, AIBestCardName, AIMonsterZone);
    }

    private String findBestMonster(Place AIBestCardPlace, String AIBestCardName, ArrayList<Place> AIMonsterZone) {
        if (MonstersWithOneTribute.contains(AIBestCardName)) {
            if (AIMonsterZone.size() >= 1) {
                return selectProcess(AIBestCardPlace, PLACE_NAME.HAND);
            } else {
                ignoreCards.add(AIBestCardPlace);
                return "";
            }
        } else if (MonstersWithTwoTribute.contains(AIBestCardName)) {
            if (AIMonsterZone.size() >= 2) {
                return selectProcess(AIBestCardPlace, PLACE_NAME.HAND);
            } else {
                ignoreCards.add(AIBestCardPlace);
                return "";
            }
        } else if (MonstersWithThreeTribute.contains(AIBestCardName)) {
            if (AIMonsterZone.size() >= 3) {
                return selectProcess(AIBestCardPlace, PLACE_NAME.HAND);
            } else {
                ignoreCards.add(AIBestCardPlace);
                return "";
            }
        } else {
            return selectProcess(AIBestCardPlace, PLACE_NAME.HAND);
        }
    }

    public String attack() {
        ArrayList<Place> AIMonsterCardPlaces = gameBoardCommunicator.getMonsterZone(AIGameBoard);
        ArrayList<Place> opponentMonsterCardPlaces = gameBoardCommunicator.getMonsterZone(opponentGameBoard);
        AIMonsterCardPlaces.removeIf(aiMonsterCardPlace
                -> AIMonsterCardPlacesAlreadyAttacked.contains(aiMonsterCardPlace));
        opponentMonsterCardPlaces.removeIf(opponentMonsterCardPlace
                -> opponentMonsterCardPlacesAlreadyHaveBeenAttacked.contains(opponentMonsterCardPlace));

        if (!AIMonsterCardPlaces.isEmpty()) {
            if (opponentMonsterCardPlaces.isEmpty()) {
                return opponentWithOutCardScenario(AIMonsterCardPlaces);
            } else {
                if (gameBoardCommunicator.doesOpponentHaveSetCard(opponentMonsterCardPlaces)) {
                    Place firstOpponentSetCard = gameBoardCommunicator.
                            getFirstSetMonsterCard(opponentMonsterCardPlaces);
                    if (firstOpponentSetCard != null) {
                        return opponentWithSetCardScenario(AIMonsterCardPlaces, firstOpponentSetCard);
                    }
                } else {
                    return opponentWithFaceDownScenario(AIMonsterCardPlaces, opponentMonsterCardPlaces);
                }
            }
        } else
            return "done";
        return "done";
    }

    private String opponentWithFaceDownScenario(ArrayList<Place> AIMonsterCardPlaces,
                                                ArrayList<Place> opponentMonsterCardPlaces) {
        Place cardPlaceForAttack = gameBoardCommunicator.
                findBestMonsterCardByAttackByPlace(AIMonsterCardPlaces);
        if (cardPlaceForAttack != null) {
            Place opponentCardPlaceToAttackTo = gameBoardCommunicator.
                    getBestMonsterCardStrengthInMonsterZoneForAttack
                            (opponentMonsterCardPlaces, cardPlaceForAttack);
            AIMonsterCardPlacesAlreadyAttacked.add(cardPlaceForAttack);
            opponentMonsterCardPlacesAlreadyHaveBeenAttacked.add(opponentCardPlaceToAttackTo);
            opponentMonsterCardToAttack.add(getNumberOfPlaceInGameBoard(opponentGameBoard,
                    opponentCardPlaceToAttackTo, PLACE_NAME.MONSTER));
            return selectProcess(cardPlaceForAttack, PLACE_NAME.MONSTER);
        } else
            return "done";
    }

    private String opponentWithSetCardScenario(ArrayList<Place> AIMonsterCardPlaces, Place firstOpponentSetCard) {
        Place cardPlaceForAttack = gameBoardCommunicator.
                findBestMonsterCardByAttackByPlace(AIMonsterCardPlaces);
        if (cardPlaceForAttack == null) {
            return "done";
        } else {
            AIMonsterCardPlacesAlreadyAttacked.add(cardPlaceForAttack);
            opponentMonsterCardPlacesAlreadyHaveBeenAttacked.add(firstOpponentSetCard);
            opponentMonsterCardToAttack.add(getNumberOfPlaceInGameBoard(opponentGameBoard,
                    firstOpponentSetCard, PLACE_NAME.MONSTER));
            return selectProcess(cardPlaceForAttack, PLACE_NAME.MONSTER);
        }
    }

    private String opponentWithOutCardScenario(ArrayList<Place> AIMonsterCardPlaces) {
        Place cardPlaceForAttack = gameBoardCommunicator.
                findBestMonsterCardByAttackByPlace(AIMonsterCardPlaces);
        if (cardPlaceForAttack == null) {
            return "done";
        } else {
            isInDirectAttackProcess = true;
            AIMonsterCardPlacesAlreadyAttacked.add(cardPlaceForAttack);
            return selectProcess(cardPlaceForAttack, PLACE_NAME.MONSTER);
        }
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

    public String activateOrSetAllSpellCardsInHand(ArrayList<Place> spellCards) {
        for (Place spellCardPlace : spellCards) {
            Cards spellCard = spellCardPlace.getCard();
            String cardName = spellCard.getName();
            if (normalSpellCards.contains(cardName)) {
                boolean isConditionOkay = false;
                isConditionOkay = isSpellConditionOkay(cardName);

                if (isConditionOkay) {
                    isInActivateSpellProcess = true;
                    return selectProcess(spellCardPlace, PLACE_NAME.HAND);

                } else {
                    isInSetSpellProcess = true;
                    return selectProcess(spellCardPlace, PLACE_NAME.HAND);
                }

            } else if (goodSpellCards.contains(cardName)) {
                isInActivateSpellProcess = true;
                return selectProcess(spellCardPlace, PLACE_NAME.HAND);
            }
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
            if (normalSpellCards.contains(cardName)) {

                boolean isConditionOkay = false;
                isConditionOkay = isSpellConditionOkay(cardName);
                if (isConditionOkay) {
                    isInActivateSpellProcess = true;
                    return selectProcess(spellPlace, PLACE_NAME.SPELL_AND_TRAP);
                }

            } else if (goodSpellCards.contains(cardName)) {
                isInActivateSpellProcess = true;
                return selectProcess(spellPlace, PLACE_NAME.SPELL_AND_TRAP);
            }
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
                    isInSetTrapProcess = true;
                    return selectProcess(trapCardPlace, PLACE_NAME.HAND);
                }
            } else if (goodTrapCards.contains(cardName)) {
                isInSetTrapProcess = true;
                return selectProcess(trapCardPlace, PLACE_NAME.HAND);
            }
        }
        return "done";
    }

/// Spell cards

    // responder
    public void monsterReborn() {
    }

    // responder
    public void changeOfHeart() {
        ArrayList<Place> opponentMonsterZone = gameBoardCommunicator.getMonsterZone(opponentGameBoard);
        Place bestMonsterPlace = gameBoardCommunicator.findBestMonsterCardByAttackByPlace(opponentMonsterZone);
        if (bestMonsterPlace != null) {
        }
    }

    // responder
    public void mysticalSpaceTyphoon() {
        ArrayList<Place> opponentSpellAndTrapPlace = gameBoardCommunicator.getSpellAndTrapZone(opponentGameBoard);
        for (Place place : opponentSpellAndTrapPlace) {
            if (place != null) {
                return;
            }
        }
        ArrayList<Place> AISpellAndTrapPlace = gameBoardCommunicator.getSpellAndTrapZone(AIGameBoard);
        for (Place place : AISpellAndTrapPlace) {
            if (place != null) {
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
        ArrayList<String> goodCards = new ArrayList<>();
        goodCards.add("Aqua");
        ArrayList<String> badCards = new ArrayList<>();
        if (weightedCardCounter(goodCards, badCards, AIGameBoard, opponentGameBoard)) {
            return true;
        }
        return false;
    }

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
        }
    }
}