package sample.controller;

import sample.controller.specialbilities.SpecialAbility;
import sample.controller.specialbilities.SpecialAbilityActivationController;
import sample.model.cards.Cards;
import sample.model.cards.monster.MonsterCards;
import sample.model.cards.spell.SpellCards;
import sample.model.cards.trap.TrapCards;
import sample.model.game.*;
import sample.model.tools.CHAIN_JOB;
import sample.model.tools.RegexPatterns;
import sample.model.tools.StringMessages;
import sample.view.PrinterAndScanner;
import sample.view.listener.Communicator;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.regex.Matcher;

public class GamePlayController extends RegexController implements RegexPatterns, StringMessages {

    private static final PrinterAndScanner printerAndScanner;
    private static final PrintBuilderController printBuilderController;
    private static final Set<String> endBattlePhase = Collections.singleton("endBattlePhase");

    private final ArrayList<Place> CHAINED_PLACES;
    private SpecialAbilityActivationController specialAbilityActivationController;
    private PHASE phase;
    private GamePlay gamePlay;
    private boolean alreadySummonedOrSet = false;
    private boolean didBattlePhase = false;
    private boolean monsterCardDestroyed;
    private boolean gameOver;
    private boolean surrendered;
    private ArrayBlockingQueue<String> commands;
    private Communicator myCommunicator, opponentCommunicator;
    private boolean run;

    static {
        printerAndScanner = PrinterAndScanner.getInstance();
        printBuilderController = PrintBuilderController.getInstance();
    }

    public GamePlayController(GamePlay gamePlay, Communicator myCommunicator, Communicator opponentCommunicator) {
        this.surrendered = false;
        this.gameOver = false;
        this.gamePlay = gamePlay;
        this.CHAINED_PLACES = new ArrayList<>();
        this.specialAbilityActivationController = new SpecialAbilityActivationController(this);
        this.commands = new ArrayBlockingQueue<>(1);
        this.myCommunicator = myCommunicator;
        this.opponentCommunicator = opponentCommunicator;
        this.run = true;
        startListening();
    }

    public ArrayList<Place> sendChainedPlaces() {
        CHAINED_PLACES.clear();
        return CHAINED_PLACES;
    }

    public void setRun(boolean run) {
        this.run = run;
    }

    public boolean getRun(){
        return this.run;
    }

    public GamePlay getGamePlay() {
        return gamePlay;
    }

    public PHASE getPhase() {
        return phase;
    }

    public Communicator getMyCommunicator() {
        return myCommunicator;
    }

    public Communicator getOpponentCommunicator() {
        return opponentCommunicator;
    }

    public SpecialAbilityActivationController getSpecialAbilityActivationController() {
        return specialAbilityActivationController;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public boolean getGameOver() {
        return gameOver;
    }

    public void setSurrendered(boolean surrendered) {
        this.surrendered = surrendered;
    }

    public boolean getSurrendered() {
        return surrendered;
    }

    private void startListening(){
        GamePlayControllerCommandReceiver gamePlayControllerCommandReceiver =
                new GamePlayControllerCommandReceiver(this, myCommunicator);
        Thread thread = new Thread(gamePlayControllerCommandReceiver);
        thread.setDaemon(true);
        thread.start();
    }

    public void run() {
        if (gameOver)
            return;
        didBattlePhase = false;
        drawPhase();
        String command;
        while (true) {
            myCommunicator.changeGameState(phase.getValue().toLowerCase());
            if (gamePlay.getUniversalHistory().contains("endBattlePhase") && phase == PHASE.BATTLE) {
                gamePlay.getUniversalHistory().removeAll(endBattlePhase);
                nextPhase();
                continue;
            }
            command = takeCommand();
            handleCommands(command);
            if (phase == PHASE.END || gameOver)
                break;
        }
        if (!gameOver)
            end();
    }

    private void handleCommands(String command) {
        if (command.startsWith("select")) {
            Matcher matcher = RegexController.getMatcher(command, selectCardPattern);
            if (matcher != null)
                selectCardCredibility(matcher);
            else printerAndScanner.printNextLine(invalidCommand);
        } else if (command.equals("next phase"))
            nextPhase();
        else if (command.equals("summon"))
            summonCredibility();
        else if (command.equals("set"))
            checkBeforeSet();
        else if (command.startsWith("set")) {
            Matcher matcher = RegexController.getMatcher(command, setAttackOrDefensePattern);
            if (matcher != null)
                changePosition(STATUS.getStatusByString(matcher.group("position")));
            else printerAndScanner.printNextLine(invalidCommand);
        } else if (command.equals("flip-summon"))
            flipSummon();
        else if (command.startsWith("attack")) {
            Matcher matcher = RegexController.getMatcher(command, attackPattern);
            if (matcher != null) {
                if (checkIfMonstersCanAttack()) {
                    if (matcher.group("type").equals("direct"))
                        attackDirect();
                    else attackMonster(Integer.parseInt(matcher.group("type")));
                } else printerAndScanner.printNextLine(StringMessages.cannotAttackRightNow);
            } else printerAndScanner.printNextLine(invalidCommand);
        } else if (command.equals("activate effect"))
            activateEffect();
        else if (command.equals("show graveyard"))
            showGraveYard();
        else if (command.startsWith("card show")) {
            Matcher matcher = RegexController.getMatcher(command, cardShowPattern);
            if (matcher != null)
                if (matcher.group("card").matches("^(?:--selected|-s)$"))
                    showSelectedCard();
                else printerAndScanner.printNextLine(invalidCommand);
            else printerAndScanner.printNextLine(invalidCommand);
        } else if (command.equals("surrender")) {
            surrendered = true;
            gameOver = true;
            gamePlay.getOpponentGamePlayController().setGameOver(true);
        } else printerAndScanner.printNextLine(invalidCommand);
    }

    public void showSelectedCard() {
        try {
            Place selectedCard = gamePlay.getSelectedCard();
            if (gamePlay.getMyGameBoard().fromThisGameBoard(selectedCard))
                printerAndScanner.printNextLine(printBuilderController.showOneCard(selectedCard));
            else if (selectedCard.getStatus() == STATUS.SET)
                printerAndScanner.printNextLine(cardIsNotVisible);
            else printerAndScanner.printNextLine(printBuilderController.showOneCard(selectedCard));
        } catch (NullPointerException e) {
            printerAndScanner.printNextLine(noCardsIsSelectedYet);
        }
    }

    private void nextPhase() {
        if (phase == PHASE.MAIN) {
            if (!didBattlePhase && canAttack() && !gamePlay.getUniversalHistory().contains("starter")) {
                phase = PHASE.BATTLE;
                myCommunicator.changePhase("BP", "false");
                opponentCommunicator.changePhase("BP", "true");
                didBattlePhase = true;
            } else phase = PHASE.END;
        } else {
            phase = PHASE.MAIN;
            String string = didBattlePhase ? "M2" : "M1";
            myCommunicator.changePhase(string, "false");
            opponentCommunicator.changePhase(string, "true");
        }
    }

    private boolean canAttack() {
        for (int i = 1; i < 6; i++) {
            if (gamePlay.getMyGameBoard().getPlace(i, PLACE_NAME.MONSTER).getCard() != null)
                return true;
        }
        return false;
    }

    private void drawPhase() {
        opponentCommunicator.changeGameState("other");
        if (gamePlay.getMyGameBoard().noCardToDraw()) {
            gameOver = true;
            gamePlay.getOpponentGamePlayController().setGameOver(true);
            return;
        }
        alreadySummonedOrSet = false;
        monsterCardDestroyed = false;
        removeTemporaryFeatures();
        checkHistory();
        phase = PHASE.DRAW;
        myCommunicator.changePhase("DP", "false");
        opponentCommunicator.changePhase("DP", "true");
        if (!gamePlay.getOpponentGamePlayController().getGamePlay().getUniversalHistory().contains("cannotDraw"))
            drawCard();
        new NewChain(this, null, CHAIN_JOB.DRAW_PHASE, 2, sendChainedPlaces());
        specialAbilityActivationController.handleMonstersThatCanBeActivated();
        try {
            Thread.sleep(500);
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
        standByPhase();
    }

    private void standByPhase() {
        phase = PHASE.STAND_BY;
        checkIndividualHistory();
        myCommunicator.changePhase("SB", "false");
        opponentCommunicator.changePhase("SB", "true");
        try {
            Thread.sleep(500);
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
        nextPhase();
    }

    private void end() {
        myCommunicator.changePhase("EP", "false");
        opponentCommunicator.changePhase("EP", "true");
        new NewChain(this, null, CHAIN_JOB.DRAW_PHASE, 2, sendChainedPlaces());
        gamePlay.getUniversalHistory().remove("starter");
        checkIndividualHistory();
        try {
            Thread.sleep(500);
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
    }

    public void drawCard() {
        Place emptyHandPlace = null;
        for (int i = 0; i < 6; i++) {
            Place place = gamePlay.getMyGameBoard().getPlace(i, PLACE_NAME.HAND);
            if (place.getCard() == null) {
                emptyHandPlace = place;
                break;
            }
        }
        if (emptyHandPlace != null) {
            Cards card = gamePlay.getMyGameBoard().drawCard();
            emptyHandPlace.setCard(card);
            myCommunicator.addToHand(emptyHandPlace.getNUM(), "false", card.getName(), card.getDescription());
            opponentCommunicator.addToHand(emptyHandPlace.getNUM(), "true", "UNKNOWN", "");
            try {
                Thread.sleep(500);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }

    }

    public void selectCardCredibility(Matcher matcher) {
        if (hasField(matcher, "typeHand")) {
            int number = Integer.parseInt(matcher.group("select"));
            if (number < 6 && number >= 0)
                if (gamePlay.getMyGameBoard().getPlace(number, PLACE_NAME.HAND).getCard() != null)
                    selectCard(matcher);
                else printerAndScanner.printNextLine(noCardInGivenPosition);
            else printerAndScanner.printNextLine(invalidSelection);
        } else if (hasField(matcher, "type")) {
            int number = Integer.parseInt(matcher.group("select"));
            if (number < 6 && number > 0) {
                Place place = RegexController.hasField(matcher, "opponent") ||
                        RegexController.hasField(matcher, "opponent2") ?
                        gamePlay.getOpponentGamePlayController().getGamePlay().getMyGameBoard().
                                getPlace(number, PLACE_NAME.getEnumByString(matcher.group("type"))) :
                        gamePlay.getMyGameBoard().getPlace(number, PLACE_NAME.getEnumByString(matcher.group("type")));
                if (place.getCard() != null)
                    selectCard(matcher);
                else printerAndScanner.printNextLine(noCardInGivenPosition);
            } else printerAndScanner.printNextLine(invalidSelection);
        } else if (hasField(matcher, "delete"))
            if (gamePlay.getSelectedCard() != null)
                selectCard(matcher);
            else printerAndScanner.printNextLine(noCardsIsSelectedYet);
        else if (hasField(matcher, "typeField")) {
            Place place = RegexController.hasField(matcher, "opponent") ||
                    RegexController.hasField(matcher, "opponent2") ?
                    gamePlay.getOpponentGamePlayController().getGamePlay().getMyGameBoard().
                            getPlace(0, PLACE_NAME.FIELD) :
                    gamePlay.getMyGameBoard().getPlace(0, PLACE_NAME.FIELD);
            if (place.getCard() != null)
                selectCard(matcher);
            else printerAndScanner.printNextLine(noCardInGivenPosition);
        } else printerAndScanner.printNextLine(invalidSelection);
    }

    private void selectCard(Matcher matcher) {
        if (hasField(matcher, "delete")) {
            gamePlay.setSelectedCard(null);
            printerAndScanner.printNextLine(cardDeselected);
        } else {
            if (hasField(matcher, "typeHand"))
                gamePlay.setSelectedCard(gamePlay.getMyGameBoard().getPlace(Integer.parseInt(matcher.group("select")), PLACE_NAME.HAND));
            else {
                boolean selectOpponent = hasField(matcher, "opponent") || hasField(matcher, "opponent2");
                if (hasField(matcher, "typeField")) {
                    if (selectOpponent)
                        gamePlay.setSelectedCard(gamePlay.getOpponentGamePlayController().getGamePlay().getMyGameBoard()
                                .getPlace(0, PLACE_NAME.FIELD));
                    else gamePlay.setSelectedCard(gamePlay.getMyGameBoard().getPlace(0, PLACE_NAME.FIELD));
                } else {
                    PLACE_NAME placeName = PLACE_NAME.getEnumByString(matcher.group("type"));
                    if (selectOpponent)
                        gamePlay.setSelectedCard(gamePlay.getOpponentGamePlayController().getGamePlay().getMyGameBoard().
                                getPlace(Integer.parseInt(matcher.group("select")), placeName));
                    else gamePlay.setSelectedCard(gamePlay.getMyGameBoard().
                            getPlace(Integer.parseInt(matcher.group("select")), placeName));
                }
            }
            printerAndScanner.printNextLine(cardSelected);
        }
    }

    private void summonCredibility() {
        if (!gamePlay.getUniversalHistory().contains("cannotSummon")) {
            Place selectedCard = gamePlay.getSelectedCard();
            if (selectedCard.getCard() != null) {
                if (gamePlay.getMyGameBoard().fromThisGameBoard(selectedCard) && selectedCard.getCard() instanceof MonsterCards) {
                    if (phase == PHASE.MAIN) {
                        if (gamePlay.getMyGameBoard().getFirstEmptyPlace(PLACE_NAME.MONSTER) != -1) {
                            if (!alreadySummonedOrSet) {
                                if (specialAbilityActivationController.summonOrSetWithTribute(selectedCard, true)) {
                                    if (selectedCard.getCard().getType().equals("Ritual"))
                                        ritualSummon(selectedCard);
                                    else placeCard(selectedCard, true, STATUS.ATTACK);
                                }
                            } else printerAndScanner.printNextLine(alreadySummonedORSetOnThisTurn);
                        } else printerAndScanner.printNextLine(monsterCardZoneIsFull);
                    } else printerAndScanner.printNextLine(actionNotAllowedInThisPhase);
                } else printerAndScanner.printNextLine(cantSummonThisCard);
            } else printerAndScanner.printNextLine(noCardsIsSelectedYet);
        }
    }

    public Place placeCard(Place place, boolean normalSummon, STATUS status) {
        Place placeTo = null;
        new NewChain(this, place, normalSummon ? CHAIN_JOB.BEFORE_SUMMON : CHAIN_JOB.BEFORE_SPECIAL_SUMMON,
                0, sendChainedPlaces());
        if (gamePlay.getUniversalHistory().contains("preventSummon"))
            gamePlay.getUniversalHistory().remove("preventSummon");
        else {
            int emptyPlace = gamePlay.getMyGameBoard().getFirstEmptyPlace(PLACE_NAME.MONSTER);
            placeTo = gamePlay.getMyGameBoard().getPlace(emptyPlace, PLACE_NAME.MONSTER);
            placeTo.setCard(place.getCard());
            placeTo.setStatus(status);
            placeTo.addTemporaryFeatures(TEMPORARY_FEATURES.CARD_SET_OR_SUMMONED_IN_THIS_TURN);
            placeTo.addTemporaryFeatures(TEMPORARY_FEATURES.CARD_POSITION_CHANGED_IN_THIS_TURN);
            place.setCard(null);
            applyUniversalEffectsToSummonCards(placeTo);
            myCommunicator.moveFromHandToBoard(place.getNUM() < 6 && place.getNUM() >= 0 ? place.getNUM() : 6,
                    placeTo.getNUM(), "false", status.name().toLowerCase(), placeTo.getCard().getName(), placeTo.getCard().getDescription());
            opponentCommunicator.moveFromHandToBoard(place.getNUM() < 6 && place.getNUM() >= 0 ? place.getNUM() : 6,
                    placeTo.getNUM(), "true", status.name().toLowerCase(), placeTo.getCard().getName(), placeTo.getCard().getDescription());
            if (normalSummon)
                alreadySummonedOrSet = true;
            if (status == STATUS.ATTACK || !normalSummon)
                new NewChain(this, placeTo, normalSummon ? CHAIN_JOB.SUMMON : CHAIN_JOB.SPECIAL_SUMMON,
                        0, sendChainedPlaces());
            if (placeTo.getCard() != null) {
                if (status != STATUS.SET) {
                    specialAbilityActivationController.runFacUpSpecial(placeTo);
                    specialAbilityActivationController.runContinuous(placeTo);
                }
                printerAndScanner.printNextLine(status == STATUS.ATTACK ? summonedSuccessfully : setSuccessfully);
                specialAbilityActivationController.activateField();
                scannerHistoryHandler(placeTo);
                specialAbilityActivationController.runAttackAmountByQuantifier();
                if (normalSummon)
                    specialAbilityActivationController.checkSummonAMonsterUponNormalSummon(placeTo);
                specialAbilityActivationController.equipByControlledMonstersHandler();
            }
        }
        return placeTo;
    }

    private void checkBeforeSet() {
        Place selectedCard = gamePlay.getSelectedCard();
        if (selectedCard.getCard() != null) {
            if (gamePlay.getMyGameBoard().fromThisGameBoard(selectedCard) &&
                    selectedCard.getType() == PLACE_NAME.HAND && !isRitual(selectedCard)/*&&
                    !specialAbilityActivationController.hasTributeMethod(selectedCard.getCard())*/) {
                if (phase == PHASE.MAIN) {
                    if (selectedCard.getCard() instanceof MonsterCards)
                        setMonsterCard(selectedCard);
                    else { //setSpellOrTrap(selectedCard);
                        Place place = putSpellOrField(selectedCard, STATUS.SET);
                        if (place != null) {
                            myCommunicator.moveFromHandToBoard(selectedCard.getNUM()
                                    , place.getNUM(), "false"
                                    , "set", place.getCard().getName(), place.getCard().getDescription());
                            opponentCommunicator.moveFromHandToBoard(selectedCard.getNUM()
                                    , place.getNUM(), "true"
                                    , "set", place.getCard().getName(), place.getCard().getDescription());
                        }
                    }
                } else printerAndScanner.printNextLine(actionNotAllowedInThisPhase);
            } else printerAndScanner.printNextLine(cantSetThisCard);
        } else printerAndScanner.printNextLine(noCardsIsSelectedYet);
    }

    private void setMonsterCard(Place selectedCard) {
        int emptyPlace = gamePlay.getMyGameBoard().getFirstEmptyPlace(PLACE_NAME.MONSTER);
        if (emptyPlace != -1) {
            if (!alreadySummonedOrSet) {
                if (specialAbilityActivationController.hasTributeMethod(selectedCard.getCard())) {
                    if (specialAbilityActivationController.summonOrSetWithTribute(selectedCard, false)) {
                        if (selectedCard.getCard().getType().equals("Ritual"))
                            ritualSummon(selectedCard);
                        else new NewChain(this, selectedCard,
                                CHAIN_JOB.SUMMON, 0, sendChainedPlaces());
                    }
                } else {
                    setCard(selectedCard, emptyPlace);
                }
            } else printerAndScanner.printNextLine(alreadySummonedORSetOnThisTurn);
        } else printerAndScanner.printNextLine(monsterCardZoneIsFull);
    }

    public Place setCard(Place selectedCard, int emptyPlace) {
        Place placeTo = gamePlay.getMyGameBoard().getPlace(emptyPlace, PLACE_NAME.MONSTER);
        placeTo.setCard(selectedCard.getCard());
        placeTo.setStatus(STATUS.SET);
        selectedCard.setCard(null);
        placeTo.addTemporaryFeatures(TEMPORARY_FEATURES.CARD_SET_OR_SUMMONED_IN_THIS_TURN);
        alreadySummonedOrSet = true;
//        printerAndScanner.printNextLine(setSuccessfully);
        myCommunicator.moveFromHandToBoard(selectedCard.getNUM() >= 0 && selectedCard.getNUM() < 6 ? selectedCard.getNUM() : 6,
                placeTo.getNUM(), "false", "set", placeTo.getCard().getName(), placeTo.getCard().getDescription());
        opponentCommunicator.moveFromHandToBoard(selectedCard.getNUM() >= 0 && selectedCard.getNUM() < 6 ? selectedCard.getNUM() : 6,
                placeTo.getNUM(), "true", "set", placeTo.getCard().getName(), placeTo.getCard().getDescription());
        return placeTo;
    }

    private void changePosition(STATUS status) {
        Place selectedCard = gamePlay.getSelectedCard();
        if (selectedCard != null) {
            if (selectedCard.getCard() != null) {
                if (selectedCard.getType() == PLACE_NAME.MONSTER) {
                    if (phase == PHASE.MAIN) {
                        if (status != selectedCard.getStatus()) {
                            if (!selectedCard.getTemporaryFeatures().contains(TEMPORARY_FEATURES.CARD_POSITION_CHANGED_IN_THIS_TURN)) {
                                selectedCard.setStatus(status);
                                if (selectedCard.getTemporaryFeatures().contains(TEMPORARY_FEATURES.EQUIPPED)) {
                                    Place equippedBy = getEquip(selectedCard);
                                    if (equippedBy != null)
                                        specialAbilityActivationController.dynamicEquipHandler(equippedBy,
                                                selectedCard.getStatus() == STATUS.ATTACK ? 0 : 1);
                                }
                                selectedCard.addTemporaryFeatures(TEMPORARY_FEATURES.CARD_POSITION_CHANGED_IN_THIS_TURN);
                                myCommunicator.changePosition(selectedCard.getNUM(), false, status.name());
                                opponentCommunicator.changePosition(selectedCard.getNUM(), true, status.name());
                            } else printerAndScanner.printNextLine(alreadyChangedThisCardPositionInThisTurn);
                        } else printerAndScanner.printNextLine(cardIsAlreadyInTheWantedPosition);
                    } else printerAndScanner.printNextLine(actionNotAllowedInThisPhase);
                } else printerAndScanner.printNextLine(cantChangeThisCardPosition);
            } else printerAndScanner.printNextLine(noCardsIsSelectedYet);
        } else printerAndScanner.printNextLine(noCardsIsSelectedYet);
    }

    private void flipSummon() {
        Place selectedCard = gamePlay.getSelectedCard();
        if (selectedCard != null) {
            if (selectedCard.getCard() != null) {
                if (selectedCard instanceof MonsterZone) {
                    if (phase == PHASE.MAIN) {
                        if (selectedCard.getStatus() == STATUS.SET &&
                                !selectedCard.getTemporaryFeatures().contains(TEMPORARY_FEATURES.CARD_SET_OR_SUMMONED_IN_THIS_TURN)) {
                            new NewChain(this, selectedCard, CHAIN_JOB.FLIP_SUMMON, 0, sendChainedPlaces());
                            if (selectedCard.getCard() != null) {
                                selectedCard.addTemporaryFeatures(TEMPORARY_FEATURES.CARD_POSITION_CHANGED_IN_THIS_TURN);
                                specialAbilityActivationController.equipByControlledMonstersHandler();
                                myCommunicator.flipSummon(selectedCard.getNUM(), false, selectedCard.getCard().getName());
                                opponentCommunicator.flipSummon(selectedCard.getNUM(), true, selectedCard.getCard().getName());
                            }
                        } else printerAndScanner.printNextLine(cantFlipSummon);
                    } else printerAndScanner.printNextLine(cantDoInThisPhase);
                } else printerAndScanner.printNextLine(cantChangeThisCardPosition);
            } else printerAndScanner.printNextLine(noCardsIsSelectedYet);
        } else printerAndScanner.printNextLine(noCardsIsSelectedYet);
    }

    private void attackMonster(int number) {
        Place selectedCard = gamePlay.getSelectedCard();
        if (selectedCard != null) {
            Place cardToAttack = gamePlay.getOpponentGamePlayController().getGamePlay().getMyGameBoard()
                    .getPlace(number, PLACE_NAME.MONSTER);
            if (cardToAttack instanceof MonsterZone && selectedCard instanceof MonsterZone) {
                if (checkCannotBeAttacked(cardToAttack)) {
                    if (phase == PHASE.BATTLE) {
                        if (!selectedCard.getTemporaryFeatures().contains(TEMPORARY_FEATURES.CARD_ATTACKED_IN_THIS_TURN)) {
                            if (cardToAttack.getCard() != null) {
                                if (selectedCard.getStatus() == STATUS.ATTACK) {
                                    selectedCard.setAffect(cardToAttack);
                                    new NewChain(this,
                                            selectedCard, CHAIN_JOB.ATTACK_MONSTER, 0, sendChainedPlaces());
                                } else printerAndScanner.printNextLine(cannotAttackWhileSet);
                            } else printerAndScanner.printNextLine(noCardToAttackHere);
                        } else printerAndScanner.printNextLine(cardAlreadyAttacked);
                    } else printerAndScanner.printNextLine(cantDoInThisPhase);
                } else printerAndScanner.printNextLine(cannotBeAttackedWhileThereAreMonsters);
            } else printerAndScanner.printNextLine(cantAttackWithThisCard);
        } else printerAndScanner.printNextLine(noCardsIsSelectedYet);
    }

    private boolean checkCannotBeAttacked(Place place) {
        if (gamePlay.getOpponentGamePlayController().getGamePlay().getHistory().get(place)
                .contains("cannotBeAttackedWhileThereAreOtherMonsters")) {
            for (int i = 1; i < 6; i++) {
                if (gamePlay.getOpponentGamePlayController().getGamePlay().getMyGameBoard().
                        getPlace(i, PLACE_NAME.MONSTER).getCard() != null)
                    return false;
            }
        }
        return true;
    }

    private void attackDirect() {
        Place selectedCard = gamePlay.getSelectedCard();
        if (selectedCard != null) {
            if (selectedCard instanceof MonsterZone) {
                if (phase == PHASE.BATTLE) {
                    if (!selectedCard.getTemporaryFeatures().contains(TEMPORARY_FEATURES.CARD_ATTACKED_IN_THIS_TURN)) {
                        if (opponentHasMonsterCard()) {
                            selectedCard.setAffect(selectedCard);
                            new NewChain(this, selectedCard,
                                    CHAIN_JOB.ATTACK_DIRECT, 0, sendChainedPlaces());
                        } else printerAndScanner.printNextLine(cantAttackTheOpponentDirectly);
                    } else printerAndScanner.printNextLine(cardAlreadyAttacked);
                } else printerAndScanner.printNextLine(cantDoInThisPhase);
            } else printerAndScanner.printNextLine(cantAttackWithThisCard);
        } else printerAndScanner.printNextLine(noCardsIsSelectedYet);
    }

    private boolean opponentHasMonsterCard() {
        for (int i = 1; i < 6; i++)
            if (gamePlay.getOpponentGamePlayController().getGamePlay().getMyGameBoard().getPlace(i, PLACE_NAME.MONSTER) == null)
                return false;
        return true;
    }

    public void flip(Place toFlip, STATUS status, boolean runFlipSpecial, boolean runContinuous, boolean showCard) {
        toFlip.setStatus(status);
        myCommunicator.changePosition(toFlip.getNUM(), false, status.name());
        opponentCommunicator.changePosition(toFlip.getNUM(), true, status.name());
        if (runFlipSpecial) {
            specialAbilityActivationController.runFlipSpecial(toFlip);
            specialAbilityActivationController.runFacUpSpecial(toFlip);
            if (runContinuous)
                specialAbilityActivationController.runContinuous(toFlip);
        }
        if (showCard) {
            myCommunicator.showCard(toFlip.getNUM(), toFlip.getCard().getName(), toFlip.getCard().getDescription(), false);
            opponentCommunicator.showCard(toFlip.getNUM(), toFlip.getCard().getName(), toFlip.getCard().getDescription(), true);
        }
    }

    private void activateEffect() {
        Place selectedCard = gamePlay.getSelectedCard();
        if (selectedCard != null) {
            if (selectedCard.getCard() != null) {
                if (gamePlay.getMyGameBoard().fromThisGameBoard(selectedCard) &&
                        !isRitual(selectedCard)) {
                    if (selectedCard.getCard() instanceof SpellCards) {
                        if (phase == PHASE.MAIN) {
                            if (!selectedCard.getTemporaryFeatures().contains(TEMPORARY_FEATURES.SPELL_ACTIVATED) &&
                                    !selectedCard.getTemporaryFeatures().contains(TEMPORARY_FEATURES.CARD_SET_OR_SUMMONED_IN_THIS_TURN)) {
                                if (selectedCard.getType() != PLACE_NAME.HAND)
                                    if (gamePlay.getHistory().get(selectedCard).contains("noSpecialThisRound"))
                                        return;
                                if ((selectedCard.getType() == PLACE_NAME.HAND &&
//                                    gamePlay.getMyGameBoard().getFirstEmptyPlace(PLACE_NAME.SPELL_AND_TRAP) != -1 ||
                                        selectedCard.getCard().getSpecialSpeed() >= 2) ||
                                        isField(selectedCard) ||
                                        selectedCard.getType() == PLACE_NAME.SPELL_AND_TRAP) {
//                                specialAbilityActivationController.setGamePlayController(this);
                                    if (specialAbilityActivationController.checkForConditions(selectedCard) /*&&
                                    !gamePlay.getUniversalHistory().contains("cannotActivateTrap")*/) {
                                        if (selectedCard.getType() == PLACE_NAME.HAND)
                                            selectedCard = putSpellOrField(selectedCard, STATUS.ATTACK);
                                        if (selectedCard != null) {
                                            if (selectedCard.getStatus() == STATUS.SET)
                                                flip(selectedCard, STATUS.ATTACK, true, false, true);
                                            printerAndScanner.printNextLine(spellActivated);
                                            if (isField(selectedCard))
                                                specialAbilityActivationController.activateField();
                                            else {
//                                            selectedCard.setAffect(selectedCard);
                                                if (specialAbilityActivationController.runUponActivation(selectedCard))
                                                    new NewChain(this, selectedCard,
                                                            CHAIN_JOB.ACTIVATE_SPELL,
                                                            selectedCard.getCard().getSpecialSpeed(), sendChainedPlaces());
                                            }
                                        }
                                    } else printerAndScanner.printNextLine(preparationsAreNotDoneYet);
                                } else printerAndScanner.printNextLine(spellCardZoneIsFull);
                            } else printerAndScanner.printNextLine(youHaveAlreadyActivatedThisCard);
                        } else printerAndScanner.printNextLine(youCantActivateOnThisTurn);
                    } else printerAndScanner.printNextLine(activateEffectIsOnlyForSpells);
                } else printerAndScanner.printNextLine(cantActivateThisCard);
            } else printerAndScanner.printNextLine(noCardsIsSelectedYet);
        } else printerAndScanner.printNextLine(noCardsIsSelectedYet);
    }

    private Place putSpellOrField(Place toPlace, STATUS status) {
        Place place = null;
        if (isField(toPlace)) {
            place = gamePlay.getMyGameBoard().getPlace(0, PLACE_NAME.FIELD);
            killCard(place);
            place.setCard(toPlace.getCard());
            place.setStatus(status);
            toPlace.setCard(null);
        } else {
            int emptyPlace = gamePlay.getMyGameBoard().getFirstEmptyPlace(PLACE_NAME.SPELL_AND_TRAP);
            if (emptyPlace != -1) {
                place = gamePlay.getMyGameBoard().getPlace(emptyPlace, PLACE_NAME.SPELL_AND_TRAP);
                place.setCard(toPlace.getCard());
                place.setStatus(status);
                toPlace.setCard(null);
            } else printerAndScanner.printNextLine(spellCardZoneIsFull);
        }
        if (place != null) {
            place.addTemporaryFeatures(TEMPORARY_FEATURES.CARD_SET_OR_SUMMONED_IN_THIS_TURN);
            myCommunicator.moveFromHandToBoard(toPlace.getNUM(), place.getNUM(), "false", status.name(),
                    place.getCard().getName(), place.getCard().getDescription());
            opponentCommunicator.moveFromHandToBoard(toPlace.getNUM(), place.getNUM(), "true", status.name(),
                    place.getCard().getName(), place.getCard().getDescription());
        }
        return place;
    }

    private void ritualSummon(Place place) {
        if (checkBeforeRitualSummon(place)) {
            Place ritualSpell = getRitualSpell();
            if (ritualSpell != null) {
                ritualSpell.setAffect(gamePlay.getSelectedCard());
                new NewChain(this, ritualSpell, CHAIN_JOB.ACTIVATE_SPELL,
                        ritualSpell.getCard().getSpecialSpeed(), sendChainedPlaces());
            } else printerAndScanner.printNextLine(cantRitualSummon);
        } else printerAndScanner.printNextLine(cantRitualSummon);
    }

    private boolean checkBeforeRitualSummon(Place place) {
        boolean canRitual = false;
        Place ritualPlace;
        for (int i = 1; i < 6; i++) {
            ritualPlace = gamePlay.getMyGameBoard().getPlace(i, PLACE_NAME.SPELL_AND_TRAP);
            if (isRitual(ritualPlace)) {
                canRitual = true;
                break;
            }
        }
        if (canRitual)
            return calculateLevel(((MonsterCards) place.getCard()).getLevel(), sumOfAllLevels(), 5);
        else return false;
    }

    private boolean calculateLevel(int toReach, int sum, int upperLimit) {
        Cards card;
        if (sum == toReach)
            return true;
        if (sum < toReach)
            return false;
        for (int i = upperLimit; i > 0; i--) {
            card = gamePlay.getMyGameBoard().getPlace(i, PLACE_NAME.MONSTER).getCard();
            if (card != null)
                if (calculateLevel(toReach, sum - ((MonsterCards) card).getLevel(), upperLimit - 1))
                    return true;
        }
        return false;
    }

    private int sumOfAllLevels() {
        int sum = 0;
        Place place;
        for (int i = 1; i < 6; i++) {
            place = gamePlay.getMyGameBoard().getPlace(i, PLACE_NAME.MONSTER);
            if (place.getCard() != null)
                sum += ((MonsterCards) place.getCard()).getLevel();
        }
        return sum;
    }

    private Place getRitualSpell() {
        Place ritual = null;
        for (int i = 1; i < 6; i++) {
            Place place = gamePlay.getMyGameBoard().getPlace(i, PLACE_NAME.SPELL_AND_TRAP);
            if (place.getCard() != null)
                if (place.getCard() instanceof SpellCards)
                    if (((SpellCards) place.getCard()).getIcon().equals("Ritual")) {
                        ritual = gamePlay.getMyGameBoard().getPlace(i, PLACE_NAME.SPELL_AND_TRAP);
                        break;
                    }
        }
        return ritual;
    }

    public void showGraveYard() {
        printerAndScanner.printString(printBuilderController.buildGraveyard(gamePlay.getMyGameBoard().getGraveyard()));
        String command = printerAndScanner.scanNextLine();
        while (!command.equals("back")) {
            printerAndScanner.printNextLine(invalidCommand);
            command = printerAndScanner.scanNextLine();
        }
    }

    private void checkHistory() {
        checkUniversalHistory();
        checkIndividualHistory();
    }

    private void checkUniversalHistory() {
        ArrayList<String> universalHistory = gamePlay.getUniversalHistory();
        for (int i = 0; i < universalHistory.size(); i++) {
            if (universalHistory.get(i).startsWith("MyMonsterCard")) {
                specialAbilityActivationController.stopControl(universalHistory.get(i));
                universalHistory.remove(universalHistory.get(i));
            }
        }
        universalHistory.remove("endBattleHistory");
        universalHistory.remove("cannotDraw");
    }

    private void checkIndividualHistory() {
        Place monster, spell;
        for (int i = 1; i < 6; i++) {
            monster = gamePlay.getMyGameBoard().getPlace(i, PLACE_NAME.MONSTER);
            spell = gamePlay.getMyGameBoard().getPlace(i, PLACE_NAME.SPELL_AND_TRAP);
            if (monster.getCard() != null)
                if (phase != PHASE.STAND_BY)
                    manageHistory(monster, gamePlay.getHistory().get(monster));
                else checkStandByHistory(monster, gamePlay.getHistory().get(monster));
            if (spell.getCard() != null)
                if (phase != PHASE.STAND_BY)
                    manageHistory(spell, gamePlay.getHistory().get(spell));
                else checkStandByHistory(spell, gamePlay.getHistory().get(spell));
        }
    }

    private void manageHistory(Place place, ArrayList<String> historyArray) {
        for (int i = 0; i < historyArray.size(); i++) {
            if (historyArray.get(i).equals("noSpecialThisRound"))
                historyArray.remove(historyArray.get(i));
            else if (historyArray.get(i).equals("scanner")) {
                specialAbilityActivationController.handleScanner(place, phase);
                break;
            } else if (historyArray.get(i).startsWith("temporaryAttackBoost")) {
                Matcher matcher = RegexController.getMatcher(historyArray.get(i), extractEndingNumber);
                ((MonsterZone) place).setAttackModifier(
                        ((MonsterZone) place).getAttackModifier() - Integer.parseInt(matcher.group(1)));
            } else if (historyArray.get(i).startsWith("turnsRemaining")) {
                Matcher matcher = RegexController.getMatcher(historyArray.get(i), extractEndingNumber);
                if (matcher != null) {
                    int number = Integer.parseInt(matcher.group(1));
                    if (number != 0) {
                        historyArray.remove(historyArray.get(i));
                        historyArray.add("turnsRemaining" + (number - 1));
                    } else {
                        killCard(place);
                        break;
                    }
                }
            } else if (historyArray.get(i).equals("drawCardIfAMonsterIsDestroyed") && monsterCardDestroyed)
                drawCard();
        }
    }

    private void applyUniversalEffectsToSummonCards(Place place) {
        if (place.getCard() instanceof MonsterCards) {
            for (int i = 1; i < 6; i++) {
                Place toCheck = gamePlay.getMyGameBoard().getPlace(i, PLACE_NAME.MONSTER);
                if (toCheck.getCard() != null)
                for (SpecialAbility specialAbility : toCheck.getCard().getSpecial()) {
                    if (specialAbility.getMethodName().equals("attackAmountByQuantifier"))
                        specialAbility.run(this, toCheck);
                }
            }
        }
        for (String history : gamePlay.getUniversalHistory()) {
            if (history.startsWith("attackBoost")) {
                Matcher matcher = RegexController.getMatcher(history, attackBoostPattern);
                ((MonsterZone) place).setAttackModifier(
                        ((MonsterZone) place).getAttackModifier() + Integer.parseInt(matcher.group(1)));

            } else if (history.startsWith("defenseBoost")) {
                Matcher matcher = RegexController.getMatcher(history, attackBoostPattern);
                ((MonsterZone) place).setDefenseModifier(
                        ((MonsterZone) place).getDefenseModifier() + Integer.parseInt(matcher.group(1)));

            }
        }
    }

    public boolean checkIfMonstersCanAttack() {
        if (gamePlay.getUniversalHistory().contains("cannotAttack"))
            return false;
        Place selectedCard = gamePlay.getSelectedCard();
        if (!(selectedCard instanceof MonsterZone))
            return true;
        for (String history : gamePlay.getUniversalHistory()) {
            if (history.startsWith("monstersCannotAttack")) {
                Matcher matcher = RegexController.getMatcher(history, extractEndingNumber);
                if (((MonsterCards) selectedCard.getCard()).getAttack() > Integer.parseInt(matcher.group(1)))
                    return false;
            }
        }
        return true;
    }

    private void checkStandByHistory(Place place, ArrayList<String> historyArray) {
        for (String history : historyArray) {
            if (history.startsWith("payHealthEveryRound")) {
                if (specialAbilityActivationController.getHealthOrDestroyCard(place, history))
                    break;
            }
        }
    }

    private void removeTemporaryFeatures() {
        Place place;
        ArrayList<TEMPORARY_FEATURES> temporaryFeatures;
        for (int i = 1; i < 6; i++) {
            place = gamePlay.getMyGameBoard().getPlace(i, PLACE_NAME.MONSTER);
            temporaryFeatures = place.getTemporaryFeatures();
            for (int j = 0; j < temporaryFeatures.size(); j++) {
                if ((temporaryFeatures.get(j) != TEMPORARY_FEATURES.SPELL_ACTIVATED &&
                        temporaryFeatures.get(j) != TEMPORARY_FEATURES.EQUIPPED) &&
                        (temporaryFeatures.get(j) == TEMPORARY_FEATURES.CARD_ATTACKED_IN_THIS_TURN ||
                                temporaryFeatures.get(j) == TEMPORARY_FEATURES.CARD_POSITION_CHANGED_IN_THIS_TURN ||
                                temporaryFeatures.get(j) == TEMPORARY_FEATURES.CARD_SET_OR_SUMMONED_IN_THIS_TURN)) {
                    temporaryFeatures.remove(temporaryFeatures.get(j));
                    j--;
                }
            }
            place = gamePlay.getMyGameBoard().getPlace(i, PLACE_NAME.SPELL_AND_TRAP);
            temporaryFeatures = place.getTemporaryFeatures();
            for (int j = 0; j < temporaryFeatures.size(); j++) {
                if (temporaryFeatures.get(j) != TEMPORARY_FEATURES.SPELL_ACTIVATED)
                    temporaryFeatures.remove(temporaryFeatures.get(j));
            }
        }
    }

    public void shuffleDeck() {
        ArrayList<Cards> mainCards = new ArrayList<>(gamePlay.getMyGameBoard().getMainCards());
        ArrayList<Integer> cardsPicked = new ArrayList<>(gamePlay.getMyGameBoard().getCardsPicked());
        ArrayList<Integer> cardsShuffled = new ArrayList<>();
        ArrayList<Cards> shuffledMainCards = new ArrayList<>();
        ArrayList<Integer> shuffledCardsPicked = new ArrayList<>();
        int mainCardsSize = mainCards.size();
        Random random = new Random();
        for (int i = 0; i < mainCardsSize; i++) {
            int cardToPut;
            do {
                cardToPut = random.nextInt(mainCardsSize);
            } while (cardsShuffled.contains(cardToPut));
            shuffledMainCards.add(mainCards.get(cardToPut));
            if (cardsPicked.contains(cardToPut))
                shuffledCardsPicked.add(i);
            cardsShuffled.add(cardToPut);
        }
        gamePlay.getMyGameBoard().setMainCards(shuffledMainCards);
        gamePlay.getMyGameBoard().setCardsPicked(shuffledCardsPicked);
    }

    private Place getEquip(Place place) {
        for (int i = 1; i < 6; i++) {
            Place spell = gamePlay.getMyGameBoard().getPlace(i, PLACE_NAME.SPELL_AND_TRAP);
            if (spell.getCard() != null)
                if (((SpellCards) spell.getCard()).getIcon().equals("Equip") && spell.getAffect() == place)
                    return spell;
        }
        return null;
    }

    public void killCard(Place place) {
        if (place.getCard() != null) {
            myCommunicator.moveToGraveyard(place.getNUM(), false, place.getCard().getName(), place.getCard().getDescription());
            opponentCommunicator.moveToGraveyard(place.getNUM(), true, place.getCard().getName(), place.getCard().getDescription());
        }
        if (checkIfItCanBeDestroyed(place)) {
            boolean wasMonster = false;
            specialAbilityActivationController.deathWishWithoutKillCard(place);
            if (place instanceof MonsterZone) {
                specialAbilityActivationController.removeMonsterFromEffect(place);
                monsterCardDestroyed = true;
                wasMonster = true;
            } else if (place instanceof Field) {
                specialAbilityActivationController.deactivateField();
            } else if (isEquip(place.getCard()))
                specialAbilityActivationController.deactivateEquip(place);
            gamePlay.getMyGameBoard().killCards(place);
            if (wasMonster) {
                specialAbilityActivationController.activateField();
                specialAbilityActivationController.runAttackAmountByQuantifier();
                specialAbilityActivationController.equipByControlledMonstersHandler();
            }
        }
    }

    public void scannerHistoryHandler(Place place) {
        ArrayList<String> historyArray = gamePlay.getHistory().get(place);
        for (int i = 0; i < historyArray.size(); i++) {
            if (historyArray.get(i).equals("scanner")) {
                specialAbilityActivationController.handleScanner(place, phase);
                break;
            }
        }
    }

    public STATUS askStatus() {
        myCommunicator.sendMessage(Communicator.askOption(askStatus, "attack", "defense"));
        STATUS status;
        for (String string = takeCommand(); true; string = takeCommand()) {
            status = STATUS.getStatusByString(string);
            if (status != null)
                break;
            printerAndScanner.printNextLine(wrongStatus);
        }
        return status;
    }

    private boolean isField(Place place) {
        if (place.getCard() instanceof SpellCards)
            return ((SpellCards) place.getCard()).getIcon().equals("Field");
        return false;
    }

    private boolean checkIfItCanBeDestroyed(Place place) {
        if (place.getCard() == null)
            return false;
        if (place.getTemporaryFeatures().contains(TEMPORARY_FEATURES.FORCE_KILL))
            return true;
        if (place.getCard() instanceof SpellCards) {
            if (((SpellCards) place.getCard()).getIcon().equals("Continuous"))
                return false;
        } else if (place.getCard() instanceof TrapCards)
            if (((TrapCards) place.getCard()).getIcon().equals("Continuous"))
                return false;
        if (gamePlay.getHistory().containsKey(place)) {
            for (String history : gamePlay.getHistory().get(place)) {
                if (history.startsWith("turnsRemaining"))
                    return history.equals("turnsRemaining0");
            }
        }
        return true;
    }

    public boolean isEquip(Cards card) {
        try {
            return ((SpellCards) card).getIcon().equals("Equip");
        } catch (ClassCastException e) {
            return false;
        }
    }

    public boolean isRitual(Place place) {
        try {
            return ((SpellCards) place.getCard()).getIcon().equals("Ritual");
        } catch (ClassCastException | NullPointerException e) {
            return false;
        }
    }

    public Action[] getPossibleAction(int placeNumber, boolean enemy, GameState gameState) {
        Action[] action = new Action[2];
        if (enemy || gameState == GameState.OTHER)
            action[0] = action[1] = Action.NOTHING;
        else {
            Place place = gamePlay.getMyGameBoard().getPlace(placeNumber);
            if (gameState == GameState.MAIN_PHASE)
                mainPhaseActions(place, action, gameState);
            else if (gameState == GameState.BATTLE_PHASE)
                battlePhaseActions(place, action);
            else chainActions(place, action, gameState);
        }
        return action;
    }

    private void mainPhaseActions(Place place, Action[] actions, GameState gameState) {
        if (place.getType() == PLACE_NAME.HAND)
            handAction(place, actions);
        else if (place.getType() == PLACE_NAME.SPELL_AND_TRAP)
            spellAndTrapMainPhaseActions(place, actions, gameState);
        else if (place.getType() == PLACE_NAME.MONSTER)
            monsterMainPhaseActions(place, actions);
        else actions[0] = actions[1] = Action.NOTHING;
    }

    private void handAction(Place place, Action[] action) {
        action[0] = (alreadySummonedOrSet && place.getCard() instanceof MonsterCards) || place.getCard() == null ?
                Action.NOTHING : (place.getCard() instanceof MonsterCards ? Action.NORMAL_SUMMON : getSpellOrTrapHandAction(place));
        action[1] = (alreadySummonedOrSet && place.getCard() instanceof MonsterCards) || place.getCard() == null ?
                Action.NOTHING : (place.getCard() instanceof MonsterCards ? Action.SET : Action.NOTHING);
    }

    private Action getSpellOrTrapHandAction(Place place){
        if (place.getCard() == null)
            return Action.NOTHING;
        if (place.getCard() instanceof TrapCards)
            return Action.SET;
        return ((SpellCards) place.getCard()).getIcon().equals("Field") ? Action.ACTIVATE_EFFECT : Action.SET;
    }

    private void spellAndTrapMainPhaseActions(Place place, Action[] actions, GameState gameState) {
        if (place.getCard() instanceof TrapCards)
            actions[0] = Action.NOTHING;
        else actions[0] = spellActivationAction(place.getTemporaryFeatures(), gameState);
        actions[1] = Action.NOTHING;
    }

    private Action spellActivationAction(ArrayList<TEMPORARY_FEATURES> temporaryFeatures, GameState gameState) {
        return temporaryFeatures.contains(TEMPORARY_FEATURES.CONTINUOUS_ACTIVATED) ||
                temporaryFeatures.contains(TEMPORARY_FEATURES.SPELL_ACTIVATED) ||
                (temporaryFeatures.contains(TEMPORARY_FEATURES.CARD_SET_OR_SUMMONED_IN_THIS_TURN) && gameState != GameState.CHAIN) ?
                Action.NOTHING :
                Action.ACTIVATE_EFFECT;
    }

    private void monsterMainPhaseActions(Place place, Action[] actions) {
        ArrayList<TEMPORARY_FEATURES> temporaryFeatures = place.getTemporaryFeatures();
        actions[0] = temporaryFeatures.contains(TEMPORARY_FEATURES.CARD_ATTACKED_IN_THIS_TURN) ||
                temporaryFeatures.contains(TEMPORARY_FEATURES.CARD_SET_OR_SUMMONED_IN_THIS_TURN) ||
                temporaryFeatures.contains(TEMPORARY_FEATURES.CARD_POSITION_CHANGED_IN_THIS_TURN) ?
                Action.NOTHING :
                (place.getStatus() == STATUS.SET ? Action.FLIP_SUMMON : Action.CHANGE_POSITION);
        actions[1] = Action.NOTHING;
    }

    private void battlePhaseActions(Place place, Action[] actions) {
        if (place.getType() == PLACE_NAME.MONSTER)
            monsterBattlePhaseActions(place, actions);
        else actions[0] = actions[1] = Action.NOTHING;
    }

    private void monsterBattlePhaseActions(Place place, Action[] actions) {
        if (place.getStatus() != STATUS.ATTACK)
            actions[0] = Action.NOTHING;
        else {
            ArrayList<TEMPORARY_FEATURES> temporaryFeatures = place.getTemporaryFeatures();
            actions[0] = temporaryFeatures.contains(TEMPORARY_FEATURES.CARD_ATTACKED_IN_THIS_TURN) ?
                    Action.NOTHING : (canDirectAttack() ? Action.DIRECT_ATTACK : Action.ATTACK);
        }
        actions[1] = Action.NOTHING;
    }

    private boolean canDirectAttack() {
        GameBoard gameBoard = gamePlay.getOpponentGamePlayController().getGamePlay().getMyGameBoard();
        for (int i = 1; i < 6; i++) {
            if (gameBoard.getPlace(i, PLACE_NAME.MONSTER).getCard() != null)
                return false;
        }
        return true;
    }

    private void chainActions(Place place, Action[] actions, GameState gameState) {
        if (place.getType() != PLACE_NAME.SPELL_AND_TRAP)
            actions[0] = Action.NOTHING;
        else actions[0] = spellActivationAction(place.getTemporaryFeatures(), gameState);
        actions[1] = Action.NOTHING;
    }

    public void putCommand(String command) {
        try {
            commands.put(command);
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
    }

    public String takeCommand() {
        try {
            return commands.take();
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
        return "";
    }
}
