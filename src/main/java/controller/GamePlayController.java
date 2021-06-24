package controller;

import controller.specialbilities.*;
import model.cards.Cards;
import model.cards.monster.MonsterCards;
import model.cards.spell.SpellCards;
import model.cards.trap.TrapCards;
import model.game.*;
import model.tools.CHAIN_JOB;
import model.tools.RegexPatterns;
import model.tools.StringMessages;
import view.PrinterAndScanner;
//TODO some bug that i forgot about remove something from place special each round :|
import java.util.*;
import java.util.regex.Matcher;

public class GamePlayController extends RegexController implements RegexPatterns, StringMessages {

    //TODO write a place card function and add activateField to it
    //TODO move remove card from game board to here
    //TODO add onDeath to remove card function for fields
    //TODO flip-summon not showing error?

    private static final PrinterAndScanner printerAndScanner;
    private static final PrintBuilderController printBuilderController;
    private static final Set<String> endBattlePhase = Collections.singleton("endBattlePhase");
//    private static final SpecialAbilityActivationController specialAbilityActivationController;

    private final ArrayList<Place> CHAINED_PLACES;
    private SpecialAbilityActivationController specialAbilityActivationController;
    private PHASE phase;
    private GamePlay gamePlay;
    private boolean alreadySummonedOrSet = false;
    private boolean didBattlePhase = false;
    private boolean monsterCardDestroyed;

    static {
        printerAndScanner = PrinterAndScanner.getInstance();
        printBuilderController = PrintBuilderController.getInstance();
//        specialAbilityActivationController = SpecialAbilityActivationController.getInstance();
    }

    public GamePlayController(GamePlay gamePlay){
        this.gamePlay = gamePlay;
        CHAINED_PLACES = new ArrayList<>();
        specialAbilityActivationController = new SpecialAbilityActivationController(this);
    }

    public ArrayList<Place> sendChainedPlaces(){
        CHAINED_PLACES.clear();
        return CHAINED_PLACES;
    }

    public GamePlay getGamePlay() {
        return gamePlay;
    }

    public PHASE getPhase() {
        return phase;
    }

    public SpecialAbilityActivationController getSpecialAbilityActivationController() {
        return specialAbilityActivationController;
    }

    public void run(){
        didBattlePhase = false;
        drawPhase();
//        for (String command = printerAndScanner.scanNextLine(); true; command = printerAndScanner.scanNextLine()){
        String command;
        while (true){
            if (gamePlay.getUniversalHistory().contains("endBattlePhase") && phase == PHASE.BATTLE) {
                gamePlay.getUniversalHistory().removeAll(endBattlePhase);
                nextPhase();
                continue;
            }
            command = printerAndScanner.scanNextLine();
            handleCommands(command);
            if (phase == PHASE.END || gamePlay.getGameEnded())
                break;
        }
        if (!gamePlay.getGameEnded())
            end();
    }

    private void handleCommands(String command) {
        if (command.startsWith("select")){
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
        else if (command.startsWith("set")){
            Matcher matcher = RegexController.getMatcher(command, setAttackOrDefensePattern);
            if (matcher != null)
                changePosition(matcher);
            else printerAndScanner.printNextLine(invalidCommand);
        } else if (command.equals("flip-summon"))
            flipSummon();
        else if (command.startsWith("attack")){
            Matcher matcher = RegexController.getMatcher(command, attackPattern);
            if (matcher != null){
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
        else printerAndScanner.printNextLine(invalidCommand);
    }

    private void nextPhase(){
        if (phase == PHASE.MAIN){
            if (!didBattlePhase && canAttack() && !gamePlay.getUniversalHistory().contains("starter")) {
                phase = PHASE.BATTLE;
                printerAndScanner.printNextLine(phase.getValue());
                didBattlePhase = true;
            } else phase = PHASE.END;
        } else {
            phase = PHASE.MAIN;
            printerAndScanner.printNextLine(phase.getValue());
        }
    }

    private boolean canAttack(){
        for (int i = 1; i < 6; i++) {
            if (gamePlay.getMyGameBoard().getPlace(i, PLACE_NAME.MONSTER).getCard() != null)
                return true;
        }
        return false;
    }

    private void drawPhase(){
        monsterCardDestroyed = false;
        removeTemporaryFeatures();
        checkHistory();
        phase = PHASE.DRAW;
        printerAndScanner.printNextLine(phase.getValue());
        if (!gamePlay.getOpponentGamePlayController().getGamePlay().getUniversalHistory().contains("cannotDraw"))
            drawCard();
        new NewChain(this, null, CHAIN_JOB.DRAW_PHASE, 2, sendChainedPlaces());
        specialAbilityActivationController.handleMonstersThatCanBeActivated();
        standByPhase();
    }

    private void standByPhase(){
        phase = PHASE.STAND_BY;
        checkIndividualHistory();
        printerAndScanner.printNextLine(phase.getValue());
//        new NewChain(this, null, CHAIN_JOB.DRAW_PHASE, 2, sendChainedPlaces());
        nextPhase();
    }

    private void end(){
        printerAndScanner.printNextLine(phase.getValue());
        new NewChain(this, null, CHAIN_JOB.DRAW_PHASE, 2, sendChainedPlaces());
        gamePlay.getUniversalHistory().remove("starter");
        alreadySummonedOrSet = false;
        checkIndividualHistory();
    }

    public void drawCard(){
        if (gamePlay.getMyGameBoard().noCardToDraw()){
            gamePlay.getMyGameBoard().changeHealth(-8000);
        } else {
            Place emptyHandPlace = null;
            for (int i = 0; i < 6; i++) {
                Place place = gamePlay.getMyGameBoard().getPlace(i, PLACE_NAME.HAND);
                if (place.getCard() == null) {
                    emptyHandPlace = place;
                    break;
                }
            }
            if (emptyHandPlace != null) {
                emptyHandPlace.setCard(gamePlay.getMyGameBoard().drawCard());
                printerAndScanner.printString(printBuilderController.drawCard(emptyHandPlace.getCard().getName()));
            }
        }
    }

    public void selectCardCredibility(Matcher matcher){
        if (hasField(matcher, "typeHand")) {
            int number = Integer.parseInt(matcher.group("select"));
            if (number < 6 && number >= 0)
                if (gamePlay.getMyGameBoard().getPlace(number, PLACE_NAME.HAND).getCard() != null)
                    selectCard(matcher);
                else printerAndScanner.printNextLine(noCardInGivenPosition);
            else printerAndScanner.printNextLine(invalidSelection);
        }
        else if (hasField(matcher, "type")) {
            int number =  Integer.parseInt(matcher.group("select"));
            if (number < 6 && number > 0)
                if (gamePlay.getMyGameBoard().getPlace(number, PLACE_NAME.getEnumByString(matcher.group("type"))) != null)
                    selectCard(matcher);
                else printerAndScanner.printNextLine(noCardInGivenPosition);
            else printerAndScanner.printNextLine(invalidSelection);
        } else if (hasField(matcher, "delete"))
            if (gamePlay.getSelectedCard() != null)
                selectCard(matcher);
            else printerAndScanner.printNextLine(noCardsIsSelectedYet);
        else if (hasField(matcher, "typeField"))
            selectCard(matcher);
        else printerAndScanner.printNextLine(invalidSelection);
    }

    private void selectCard(Matcher matcher){
        if (hasField(matcher, "delete")){
            gamePlay.setSelectedCard(null);
            printerAndScanner.printNextLine(cardDeselected);
        }else {
            if (hasField(matcher, "typeHand"))
                gamePlay.setSelectedCard(gamePlay.getMyGameBoard().getPlace(Integer.parseInt(matcher.group("select")), PLACE_NAME.HAND));
            else {
                boolean selectOpponent = hasField(matcher, "opponent") || hasField(matcher, "opponent1");
                if (hasField(matcher, "typeField")){
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
        //TODO do something about next line
        if (gamePlay.getSelectedCard().getCard() != null) {
            System.out.println(gamePlay.getSelectedCard().getCard().getName());
            if (gamePlay.getSelectedCard() instanceof MonsterZone){
                System.out.println(((MonsterZone) gamePlay.getSelectedCard()).getAttack() + " " + ((MonsterZone) gamePlay.getSelectedCard()).getDefense());
            }
        }
    }

    private void summonCredibility(){
        if (!gamePlay.getUniversalHistory().contains("cannotSummon")) {
            Place selectedCard = gamePlay.getSelectedCard();
            if (selectedCard.getCard() != null) {
                if (gamePlay.getMyGameBoard().fromThisGameBoard(selectedCard) && selectedCard.getCard() instanceof MonsterCards
                    /*&& specialAbilityActivationController.checkIfItCanBeNormalSummoned(selectedCard)*/) {
                    if (phase == PHASE.MAIN) {
                        if (gamePlay.getMyGameBoard().getFirstEmptyPlace(PLACE_NAME.MONSTER) != -1 /*||
                                selectedCard.getCard().getType().equals("Ritual")*/) {
                            if (!alreadySummonedOrSet) {
                                if (specialAbilityActivationController.summonOrSetWithTribute(selectedCard, true)) {
                                    if (selectedCard.getCard().getType().equals("Ritual"))
                                        ritualSummon(selectedCard);
                                    else //summon(getGamePlay().getSelectedCard(), true);
//                                        new NewChain(this, selectedCard,
//                                                CHAIN_JOB.SUMMON, 0, sendChainedPlaces());
                                    placeCard(selectedCard, true, STATUS.ATTACK);
                                }
                            } else printerAndScanner.printNextLine(alreadySummonedORSetOnThisTurn);
                        } else printerAndScanner.printNextLine(monsterCardZoneIsFull);
                    } else printerAndScanner.printNextLine(actionNotAllowedInThisPhase);
                } else printerAndScanner.printNextLine(cantSummonThisCard);
            } else printerAndScanner.printNextLine(noCardsIsSelectedYet);
        }
    }

    /*public Place placeCard(Place place, boolean normalSummon, STATUS status){
        int emptyPlace = gamePlay.getMyGameBoard().getFirstEmptyPlace(PLACE_NAME.MONSTER);
        Place placeTo = gamePlay.getMyGameBoard().getPlace(emptyPlace, PLACE_NAME.MONSTER);
        placeTo.setCard(place.getCard());
        placeTo.setStatus(status);
        placeTo.addTemporaryFeatures(TEMPORARY_FEATURES.CARD_SET_OR_SUMMONED_IN_THIS_TURN);
//        killCard(place);
        place.setCard(null);
        if (normalSummon)
            alreadySummonedOrSet = true;
//        specialAbilityActivationController.setGamePlayController(this);
        specialAbilityActivationController.runFacUpSpecial(placeTo);
//        if (gamePlay.getUniversalHistory().contains("killThisCardUponSummon"))
//            specialAbilityActivationController.checkSummonDeactivation(placeTo);
        if (placeTo.getCard() != null) {
            printerAndScanner.printNextLine(summonedSuccessfully);
            specialAbilityActivationController.activateField();
            scannerHistoryHandler(placeTo);
        }
//        specialAbilityActivationController.setGamePlayController(this);
        specialAbilityActivationController.runAttackAmountByQuantifier();
        if (normalSummon)
            specialAbilityActivationController.checkSummonAMonsterUponNormalSummon(placeTo);
//        else place.getAffect().setAffect(placeTo);
        return placeTo;
    }*/

    //TODO merge with setCard ?
    public Place placeCard(Place place, boolean normalSummon, STATUS status){
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

    private void checkBeforeSet(){
        Place selectedCard = gamePlay.getSelectedCard();
        if (selectedCard.getCard() != null){
            if (gamePlay.getMyGameBoard().fromThisGameBoard(selectedCard) &&
                    selectedCard.getType() == PLACE_NAME.HAND /*&&
                    !specialAbilityActivationController.hasTributeMethod(selectedCard.getCard())*/) {
                if (phase == PHASE.MAIN) {
                    if (selectedCard.getCard() instanceof MonsterCards)
                        setMonsterCard(selectedCard);
                    else //setSpellOrTrap(selectedCard);
                    if (putSpellOrField(selectedCard, STATUS.SET) != null)
                        printerAndScanner.printNextLine(setSuccessfully);
                } else printerAndScanner.printNextLine(actionNotAllowedInThisPhase);
            } else printerAndScanner.printNextLine(cantSetThisCard);
        } else printerAndScanner.printNextLine(noCardsIsSelectedYet);
    }

    private void setMonsterCard(Place selectedCard){
        int emptyPlace = gamePlay.getMyGameBoard().getFirstEmptyPlace(PLACE_NAME.MONSTER);
        if (emptyPlace != -1) {
            if (!alreadySummonedOrSet) {
                if (specialAbilityActivationController.hasTributeMethod(selectedCard.getCard())) {
                    if (specialAbilityActivationController.summonOrSetWithTribute(selectedCard, false)) {
                        //TODO may need to change
                        if (selectedCard.getCard().getType().equals("Ritual"))
                            ritualSummon(selectedCard);
                        else //summon(getGamePlay().getSelectedCard(), true);
                            new NewChain(this, selectedCard,
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
        printerAndScanner.printNextLine(setSuccessfully);
        return placeTo;
    }

    /*private void setSpellOrTrap(Place selectedCard){
        int emptyPlace = gamePlay.getMyGameBoard().getFirstEmptyPlace(PLACE_NAME.SPELL_AND_TRAP);
        if (emptyPlace != -1) {
                Place placeTo = gamePlay.getMyGameBoard().getPlace(emptyPlace, PLACE_NAME.SPELL_AND_TRAP);
                placeTo.setCard(selectedCard.getCard());
                placeTo.setStatus(STATUS.SET);
//                placeTo.addTemporaryFeatures(TEMPORARY_FEATURES.SPELL_ACTIVATED);
            placeTo.addTemporaryFeatures(TEMPORARY_FEATURES.CARD_SET_OR_SUMMONED_IN_THIS_TURN);
                selectedCard.setCard(null);
                printerAndScanner.printNextLine(setSuccessfully);
        } else printerAndScanner.printNextLine(spellCardZoneIsFull);
    }*/

    //TODO bug when you change the position once and then change to the previous position again
    private void changePosition(Matcher matcher){
        Place selectedCard = gamePlay.getSelectedCard();
        if (selectedCard != null){
            if (selectedCard.getType() == PLACE_NAME.MONSTER){
                if (phase == PHASE.MAIN){
                    STATUS status = STATUS.getStatusByString(matcher.group("position"));
                    if ((status == STATUS.ATTACK && selectedCard.getStatus() == STATUS.DEFENCE) ||
                            (status == STATUS.DEFENCE && selectedCard.getStatus() == STATUS.ATTACK)){
                        if (!selectedCard.getTemporaryFeatures().contains(TEMPORARY_FEATURES.CARD_POSITION_CHANGED_IN_THIS_TURN)){
                            selectedCard.setStatus(STATUS.getStatusByString(matcher.group("position")));
                            if (selectedCard.getTemporaryFeatures().contains(TEMPORARY_FEATURES.EQUIPPED)){
//                                specialAbilityActivationController.setGamePlayController(this);
                                Place equippedBy = getEquip(selectedCard);
                                if (equippedBy != null)
                                    specialAbilityActivationController.dynamicEquipHandler(equippedBy,
                                            selectedCard.getStatus() == STATUS.ATTACK ? 0 : 1);
                            }
                            selectedCard.addTemporaryFeatures(TEMPORARY_FEATURES.CARD_POSITION_CHANGED_IN_THIS_TURN);
                        } else printerAndScanner.printNextLine(alreadyChangedThisCardPositionInThisTurn);
                    } else printerAndScanner.printNextLine(cardIsAlreadyInTheWantedPosition);
                } else printerAndScanner.printNextLine(actionNotAllowedInThisPhase);
            } else printerAndScanner.printNextLine(cantChangeThisCardPosition);
        } else  printerAndScanner.printNextLine(noCardsIsSelectedYet);
    }

    private void flipSummon(){
        Place selectedCard = gamePlay.getSelectedCard();
        if (selectedCard != null){
            if (selectedCard instanceof MonsterZone){
                if (phase == PHASE.MAIN){
                    if (selectedCard.getStatus() == STATUS.SET &&
                            !selectedCard.getTemporaryFeatures().contains(TEMPORARY_FEATURES.CARD_SET_OR_SUMMONED_IN_THIS_TURN)){
//                        selectedCard.setStatus(STATUS.ATTACK);
//                        selectedCard.addTemporaryFeatures(TEMPORARY_FEATURES.CARD_POSITION_CHANGED_IN_THIS_TURN);
//                        new NewChain(this, selectedCard, CHAIN_JOB.FLIP_SUMMON, 0);
//                        if (selectedCard.getCard() != null) {
//                            specialAbilityActivationController.setGamePlayController(this);
//                            specialAbilityActivationController.runFacUpSpecial(selectedCard);
//                            specialAbilityActivationController.runFlipSpecial(selectedCard);
//                        }
                        new NewChain(this, selectedCard, CHAIN_JOB.FLIP_SUMMON, 0, sendChainedPlaces());
                        if (selectedCard.getCard() != null)
                            specialAbilityActivationController.equipByControlledMonstersHandler();
                    }
                } else printerAndScanner.printNextLine(cantDoInThisPhase);
            } else printerAndScanner.printNextLine(cantChangeThisCardPosition);
        } else  printerAndScanner.printNextLine(noCardsIsSelectedYet);
    }

    private void attackMonster(int number){
        Place selectedCard = gamePlay.getSelectedCard();
        if (selectedCard != null){
            Place cardToAttack = gamePlay.getOpponentGamePlayController().getGamePlay().getMyGameBoard()
                    .getPlace(number, PLACE_NAME.MONSTER);
            if (cardToAttack instanceof MonsterZone && selectedCard instanceof MonsterZone){
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
        } else  printerAndScanner.printNextLine(noCardsIsSelectedYet);
    }

    private boolean checkCannotBeAttacked(Place place){
        if (gamePlay.getOpponentGamePlayController().getGamePlay().getHistory().get(place)
                .contains("cannotBeAttackedWhileThereAreOtherMonsters")){
            for (int i = 1; i < 6; i++) {
                if (gamePlay.getOpponentGamePlayController().getGamePlay().getMyGameBoard().
                        getPlace(i, PLACE_NAME.MONSTER).getCard() != null)
                    return false;
            }
        }
        return true;
    }

    private void attackDirect(){
        Place selectedCard = gamePlay.getSelectedCard();
        if (selectedCard != null){
            if (selectedCard instanceof MonsterZone){
                if (phase == PHASE.BATTLE){
                    if (!selectedCard.getTemporaryFeatures().contains(TEMPORARY_FEATURES.CARD_ATTACKED_IN_THIS_TURN)){
                        if (opponentHasMonsterCard()){
                            selectedCard.setAffect(selectedCard);
                            new NewChain(this, selectedCard,
                                    CHAIN_JOB.ATTACK_DIRECT, 0, sendChainedPlaces());
                        } else printerAndScanner.printNextLine(cantAttackTheOpponentDirectly);
                    } else printerAndScanner.printNextLine(cardAlreadyAttacked);
                } else printerAndScanner.printNextLine(cantDoInThisPhase);
            } else printerAndScanner.printNextLine(cantAttackWithThisCard);
        } else  printerAndScanner.printNextLine(noCardsIsSelectedYet);
    }

    private boolean opponentHasMonsterCard(){
        for (int i = 1; i < 6; i++)
            if (gamePlay.getOpponentGamePlayController().getGamePlay().getMyGameBoard().getPlace(i, PLACE_NAME.MONSTER) == null)
                return false;
        return true;
    }

    public void flip (Place toFlip, STATUS status, boolean runFlipSpecial, boolean runContinuous){
        toFlip.setStatus(status);
        if (runFlipSpecial) {
            specialAbilityActivationController.runFlipSpecial(toFlip);
            specialAbilityActivationController.runFacUpSpecial(toFlip);
            if (runContinuous)
                specialAbilityActivationController.runContinuous(toFlip);
//            specialAbilityActivationController.runSuccessSpecialAbility(toFlip);
        }
    }

    private void activateEffect(){
        Place selectedCard = gamePlay.getSelectedCard();
        if (selectedCard != null){
            if (gamePlay.getMyGameBoard().fromThisGameBoard(selectedCard) &&
                !isRitual(selectedCard)) {
                if (selectedCard.getCard() instanceof SpellCards) {
                    if (phase == PHASE.MAIN) {
                        //TODO remove true
                        if (/*!selectedCard.getTemporaryFeatures().contains(TEMPORARY_FEATURES.SPELL_ACTIVATED) &&
                            !selectedCard.getTemporaryFeatures().contains(TEMPORARY_FEATURES.CARD_SET_OR_SUMMONED_IN_THIS_TURN)*/ true) {
                            if (selectedCard.getType() != PLACE_NAME.HAND)
                                if (gamePlay.getHistory().get(selectedCard).contains("noSpecialThisRound"))
                                    return;
                            if ((selectedCard.getType() == PLACE_NAME.HAND &&
//                                    gamePlay.getMyGameBoard().getFirstEmptyPlace(PLACE_NAME.SPELL_AND_TRAP) != -1 ||
                                    selectedCard.getCard().getSpecialSpeed() >= 2) ||
                                    isField(selectedCard) ||
                                    selectedCard.getType() == PLACE_NAME.SPELL_AND_TRAP ){
//                                specialAbilityActivationController.setGamePlayController(this);
                                if (specialAbilityActivationController.checkForConditions(selectedCard) /*&&
                                    !gamePlay.getUniversalHistory().contains("cannotActivateTrap")*/){
                                    if (selectedCard.getType() == PLACE_NAME.HAND)
                                        selectedCard = putSpellOrField(selectedCard, STATUS.ATTACK);
                                    if (selectedCard != null) {
                                        if (selectedCard.getStatus() == STATUS.SET)
                                            flip(selectedCard, STATUS.ATTACK, true, false);
                                        printerAndScanner.printNextLine(spellActivated);
                                        if (isField(selectedCard))
                                            specialAbilityActivationController.activateField();
                                        else{
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
        } else  printerAndScanner.printNextLine(noCardsIsSelectedYet);
    }

    private Place putSpellOrField(Place toPlace, STATUS status){
        Place place = null;
        if (isField(toPlace)){
            place = gamePlay.getMyGameBoard().getPlace(0, PLACE_NAME.FIELD);
            killCard(place);
            place.setCard(toPlace.getCard());
            place.setStatus(status);
            toPlace.setCard(null);
        } else {
            int emptyPlace = gamePlay.getMyGameBoard().getFirstEmptyPlace(PLACE_NAME.SPELL_AND_TRAP);
            if (emptyPlace != -1){
                place = gamePlay.getMyGameBoard().getPlace(emptyPlace, PLACE_NAME.SPELL_AND_TRAP);
                place.setCard(toPlace.getCard());
                place.setStatus(status);
                toPlace.setCard(null);
            } else printerAndScanner.printNextLine(spellCardZoneIsFull);
        }
        if (place != null)
        place.addTemporaryFeatures(TEMPORARY_FEATURES.CARD_SET_OR_SUMMONED_IN_THIS_TURN);
        return place;
    }

    private void ritualSummon(Place place){
        if (checkBeforeRitualSummon(place)){
            Place ritualSpell = getRitualSpell();
            if (ritualSpell != null){
                ritualSpell.setAffect(gamePlay.getSelectedCard());
                new NewChain(this, ritualSpell, CHAIN_JOB.ACTIVATE_SPELL,
                        ritualSpell.getCard().getSpecialSpeed(), sendChainedPlaces());
//                if (ritualSpell.getCard() != null) {
//                    ritualSpell.getCard().getSpecial().get(0).run(this, ritualSpell);
//                    killCard(ritualSpell);
//                }
            } else printerAndScanner.printNextLine(cantRitualSummon);
        } else printerAndScanner.printNextLine(cantRitualSummon);
    }

    private boolean checkBeforeRitualSummon(Place place){
        boolean canRitual = false;
        Place ritualPlace;
        for (int i = 1; i < 6; i++) {
            ritualPlace = gamePlay.getMyGameBoard().getPlace(i, PLACE_NAME.SPELL_AND_TRAP);
            if (isRitual(ritualPlace)){
                canRitual = true;
                    break;
                }
        }
//        if (!canRitual){
//            for (int i = 0; i < 6 && !canRitual; i++) {
//                card = gamePlay.getMyGameBoard().getPlace(i, PLACE_NAME.HAND).getCard();
//                if (card != null)
//                    if (card.getType().equals("Ritual"))
//                        canRitual = true;
//            }
//        }
        if (canRitual)
            return calculateLevel(((MonsterCards) place.getCard()).getLevel(), sumOfAllLevels(), 5);
        else return false;
    }

    private boolean calculateLevel(int toReach, int sum, int upperLimit){
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

    private int sumOfAllLevels(){
        int sum = 0;
        Place place;
        for (int i = 1; i < 6; i++) {
            place = gamePlay.getMyGameBoard().getPlace(i, PLACE_NAME.MONSTER);
            if (place.getCard() != null)
                sum += ((MonsterCards) place.getCard()).getLevel();
        }
        return sum;
    }

    private Place getRitualSpell(){
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
//        if (ritual == null){
//            for (int i = 0; i < 6; i++) {
//                Place place = gamePlay.getMyGameBoard().getPlace(i, PLACE_NAME.HAND);
//                if (place.getCard() != null)
//                    if (place.getCard().getType().equals("Ritual"))
//                        ritual = gamePlay.getMyGameBoard().getPlace(i, PLACE_NAME.HAND);
//            }
//            if (ritual != null) {
//                int emptyPlace = gamePlay.getMyGameBoard().getFirstEmptyPlace(PLACE_NAME.SPELL_AND_TRAP);
//                if (emptyPlace != -1) {
//                    Place place = gamePlay.getMyGameBoard().getPlace(emptyPlace, PLACE_NAME.SPELL_AND_TRAP);
//                    place.setCard(ritual.getCard());
//                    place.setStatus(STATUS.ATTACK);
//                    ritual.setCard(null);
//                    ritual = place;//TODO chain placing cards;
//                } else ritual = null;
//            }
//        }
        return ritual;
    }

    public void showGraveYard(){
        printerAndScanner.printString(printBuilderController.buildGraveyard(gamePlay.getMyGameBoard().getGraveyard()));
        String command = printerAndScanner.scanNextLine();
        while (!command.equals("back")){
            printerAndScanner.printNextLine(invalidCommand);
            command = printerAndScanner.scanNextLine();
        }
    }

    private void checkHistory(){
        checkUniversalHistory();
        checkIndividualHistory();
    }

    private void checkUniversalHistory(){
        ArrayList<String> universalHistory = gamePlay.getUniversalHistory();
        for (int i = 0; i < universalHistory.size(); i++) {
            if (universalHistory.get(i).startsWith("MyMonsterCard")){//TODO round or turn?
//                specialAbilityActivationController.setGamePlayController(this);
                specialAbilityActivationController.stopControl(universalHistory.get(i));
                universalHistory.remove(universalHistory.get(i));
            }
        }
        universalHistory.remove("endBattleHistory");
        universalHistory.remove("cannotDraw");
    }

    private void checkIndividualHistory(){
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

    private void manageHistory(Place place, ArrayList<String> historyArray){
        for (int i = 0; i < historyArray.size(); i++) {
            if (historyArray.get(i).equals("noSpecialThisRound"))
                historyArray.remove(historyArray.get(i));
            else if (historyArray.get(i).equals("scanner")){
//                specialAbilityActivationController.setGamePlayController(this);
                specialAbilityActivationController.handleScanner(place, phase);
                break;
            } else if (historyArray.get(i).startsWith("temporaryAttackBoost")){
                Matcher matcher = RegexController.getMatcher(historyArray.get(i), extractEndingNumber);
                ((MonsterZone) place).setAttackModifier(
                        ((MonsterZone) place).getAttackModifier() - Integer.parseInt(matcher.group(1)));
            } else if (historyArray.get(i).startsWith("turnsRemaining")){
                Matcher matcher = RegexController.getMatcher(historyArray.get(i), extractEndingNumber);
                if (matcher != null){
                    int number = Integer.parseInt(matcher.group(1));
                    if (number != 0){
                        historyArray.remove(historyArray.get(i));
                        historyArray.add("turnsRemaining" + (number -1));
                    } else {
                        killCard(place);
                        break;
                    }
                }
            } else if (historyArray.get(i).equals("drawCardIfAMonsterIsDestroyed") && monsterCardDestroyed)
                drawCard();
        }
    }

    public void placeCard(Place place, Cards card, STATUS status){
        place.setCard(card);
        place.setStatus(status);
//        specialAbilityActivationController.setGamePlayController(this);
        specialAbilityActivationController.activateField();
        if (status != STATUS.SET) {
            specialAbilityActivationController.runFacUpSpecial(place);
            specialAbilityActivationController.runContinuous(place);
        }
        if (card instanceof MonsterCards){
            applyUniversalEffectsToSummonCards(place);
        }
    }

    private void applyUniversalEffectsToSummonCards(Place place){
        if (place.getCard() instanceof MonsterCards){
            for (int i = 1; i < 6; i++) {
                Place toCheck = gamePlay.getMyGameBoard().getPlace(i, PLACE_NAME.MONSTER);
                for (SpecialAbility specialAbility : toCheck.getCard().getSpecial()) {
                    if (specialAbility.getMethodName().equals("attackAmountByQuantifier"))
                        specialAbility.run(this, toCheck);
                }
            }
        }
        for (String history : gamePlay.getUniversalHistory()) {
            if (history.startsWith("attackBoost")){
                Matcher matcher = RegexController.getMatcher(history, attackBoostPattern);
                ((MonsterZone) place).setAttackModifier(
                        ((MonsterZone) place).getAttackModifier() + Integer.parseInt(matcher.group(1)));

            } else if (history.startsWith("defenseBoost")){
                Matcher matcher = RegexController.getMatcher(history, attackBoostPattern);
                ((MonsterZone) place).setDefenseModifier(
                        ((MonsterZone) place).getDefenseModifier() + Integer.parseInt(matcher.group(1)));

            }
        }
    }

    public boolean checkIfMonstersCanAttack(){
        if (gamePlay.getUniversalHistory().contains("cannotAttack"))
            return false;
        Place selectedCard = gamePlay.getSelectedCard();
        if (!(selectedCard instanceof MonsterZone))
            return true;
        for (String history : gamePlay.getUniversalHistory()) {
            if (history.startsWith("monstersCannotAttack")){
                Matcher matcher = RegexController.getMatcher(history, extractEndingNumber);
                if (((MonsterCards) selectedCard.getCard()).getAttack() > Integer.parseInt(matcher.group(1)))
                    return false;
            }
        }
        return true;
    }

    private void checkStandByHistory(Place place, ArrayList<String> historyArray){
        for (String history : historyArray) {
            if (history.startsWith("payHealthEveryRound")){
//                specialAbilityActivationController.setGamePlayController(this);
                if (specialAbilityActivationController.getHealthOrDestroyCard(place, history))
                    break;
            }
        }
    }

    private void removeTemporaryFeatures(){
        Place place;
        ArrayList<TEMPORARY_FEATURES> temporaryFeatures;
        for (int i = 1; i < 6; i++) {
            place = gamePlay.getMyGameBoard().getPlace(i, PLACE_NAME.MONSTER);
            temporaryFeatures = place.getTemporaryFeatures();
            for (int j = 0; j < temporaryFeatures.size(); j++) {
                if (temporaryFeatures.get(j) != TEMPORARY_FEATURES.SPELL_ACTIVATED &&
                        (temporaryFeatures.get(j) != TEMPORARY_FEATURES.EQUIPPED ||
                        temporaryFeatures.get(j) == TEMPORARY_FEATURES.CARD_ATTACKED_IN_THIS_TURN ||
                        temporaryFeatures.get(j) == TEMPORARY_FEATURES.CARD_POSITION_CHANGED_IN_THIS_TURN)) {
                    temporaryFeatures.remove(temporaryFeatures.get(j));
                    j = 0;
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

    public void shuffleDeck(){
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

    private Place getEquip(Place place){
        for (int i = 1; i < 6; i++) {
            Place spell = gamePlay.getMyGameBoard().getPlace(i, PLACE_NAME.SPELL_AND_TRAP);
            if (spell.getCard() != null)
            if (((SpellCards) spell.getCard()).getIcon().equals("Equip") && spell.getAffect() == place)
                return spell;
        }
        return null;
    }

    public void killCard(Place place){
        if (checkIfItCanBeDestroyed(place)) {
            boolean wasMonster = false;
//            specialAbilityActivationController.setGamePlayController(this);
            specialAbilityActivationController.deathWishWithoutKillCard(place);
            if (place instanceof MonsterZone) {
                specialAbilityActivationController.removeMonsterFromFieldAndEffect(place);
                monsterCardDestroyed = true;
                wasMonster = true;
            } else if (place instanceof Field) {
                specialAbilityActivationController.deactivateField();
                ((Field) place).clear();
            } else if (isEquip(place.getCard()))
                specialAbilityActivationController.deactivateEquip(place);
//            specialAbilityActivationController.deathWishWithoutKillCard(place);
//            if (place instanceof MonsterZone) {
//                specialAbilityActivationController.removeMonsterFromFieldAndEffect(place);
//                specialAbilityActivationController.runAttackAmountByQuantifier();
//                monsterCardDestroyed = true;
//            } else if (place instanceof Field)
//                specialAbilityActivationController.deactivateField();
            gamePlay.getMyGameBoard().killCards(place);
            if (wasMonster){
                specialAbilityActivationController.runAttackAmountByQuantifier();
                specialAbilityActivationController.equipByControlledMonstersHandler();
            }
        }
    }

    public void scannerHistoryHandler(Place place){
        ArrayList<String> historyArray = gamePlay.getHistory().get(place);
        for (int i = 0; i < historyArray.size(); i++) {
            if (historyArray.get(i).equals("scanner")) {
                specialAbilityActivationController.handleScanner(place, phase);
                break;
            }
        }
    }

    public STATUS askStatus(){
        printerAndScanner.printNextLine(askStatus);
        STATUS status;
        for (String string = printerAndScanner.scanNextLine(); true; string = printerAndScanner.scanNextLine()){
//            if (string.equals("attack"))
//                return STATUS.ATTACK;
//            if (string.equals("defense"))
//                return STATUS.DEFENCE;
            status = STATUS.getStatusByString(string);
            if (status != null)
                break;
            printerAndScanner.printNextLine(wrongStatus);
        }
        return status;
    }

    private boolean isField(Place place){
        if (place.getCard() instanceof SpellCards)
            return ((SpellCards) place.getCard()).getIcon().equals("Field");
        return false;
    }

    private boolean checkIfItCanBeDestroyed(Place place){
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
        if (gamePlay.getHistory().containsKey(place)){
            for (String history : gamePlay.getHistory().get(place)) {
                if (history.startsWith("turnsRemaining"))
                    return history.equals("turnsRemaining0");
            }
        }
        return true;
    }

    public boolean isEquip(Cards card){
        try {
            return ((SpellCards) card).getIcon().equals("Equip");
        } catch (ClassCastException e){
            return false;
        }
    }

    public boolean isRitual(Place place){
        try {
            return ((SpellCards) place.getCard()).getIcon().equals("Ritual");
        } catch (ClassCastException | NullPointerException e){
            return false;
        }
    }
}
