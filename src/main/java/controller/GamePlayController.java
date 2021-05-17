package controller;

import controller.specialbilities.*;
import model.cards.Cards;
import model.cards.monster.MonsterCards;
import model.cards.spell.SpellCards;
import model.game.*;
import model.tools.CHAIN_JOB;
import model.tools.RegexPatterns;
import model.tools.StringMessages;
import view.PrinterAndScanner;
//TODO some bug that i forgot about remove something from place special each round :|
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;

public class GamePlayController extends RegexController implements RegexPatterns, StringMessages {

    //TODO write a place card function and add activateField to it
    //TODO move remove card from game board to here
    //TODO add onDeath to remove card function for fields

    private static PrinterAndScanner printerAndScanner;
    private static PrintBuilderController printBuilderController;
    private static SpecialAbilityActivationController specialAbilityActivationController;

    private GamePlay gamePlay;
    private PHASE phase;
    private boolean alreadySummonedOrSet = false;
    private boolean didBattlePhase = false;

    static {
        printerAndScanner = PrinterAndScanner.getInstance();
        printBuilderController = PrintBuilderController.getInstance();
        specialAbilityActivationController = SpecialAbilityActivationController.getInstance();
    }

    public GamePlayController(GamePlay gamePlay){
        this.gamePlay = gamePlay;
    }

    public GamePlay getGamePlay() {
        return gamePlay;
    }

    public void run(){
        didBattlePhase = false;
        drawPhase();
        for (String command = ""; phase != PHASE.END; command = printerAndScanner.scanNextLine()){
            if (gamePlay.getUniversalHistory().contains("endBattlePhase") && phase == PHASE.BATTLE)
                nextPhase();
            handleCommands(command);
        }
        end();
    }

    private void handleCommands(String command) {
        if (command.startsWith("select")){
            Matcher matcher = RegexController.getMatcher(command, selectCardPattern);
            if (matcher != null)
                selectCardCredibility(matcher);
            else printerAndScanner.printNextLine(invalidCommand);
        } else if (command.equals("nextPhase"))
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
                }
            } else printerAndScanner.printNextLine(invalidCommand);
        } else if (command.equals("activate effect"))
            activateEffect();
        else if (command.equals("show graveyard"))
            showGraveYard();
        else printerAndScanner.printNextLine(invalidCommand);
    }

    private void nextPhase(){
        if (phase == PHASE.MAIN){
            if (!didBattlePhase && canAttack()) {
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
        removeTemporaryFeatures();
        checkHistory();
        gamePlay.getMyGameBoard().setMonsterCardDestroyed(false);
        phase = PHASE.DRAW;
        printerAndScanner.printNextLine(phase.getValue());
        if (!gamePlay.getOpponentGamePlayController().getGamePlay().getUniversalHistory().contains("cannotDraw"))
            drawCard();
        standByPhase();
    }

    private void standByPhase(){
        checkIndividualHistory();
        phase = PHASE.STAND_BY;
        printerAndScanner.printNextLine(phase.getValue());
    }

    private void end(){
        printerAndScanner.printNextLine(phase.getValue());
        printerAndScanner.printString(printBuilderController.playerTurn(gamePlay.
                getOpponentGamePlayController().getGamePlay().getName()));
    }

    public void drawCard(){
        if (gamePlay.getMyGameBoard().noCardToDraw()){
            gamePlay.getMyGameBoard().changeHealth(-8000);
        } else {
            Place emptyHandPlace = null;
            for (int i = 0; i < 7; i++) {
                Place place = gamePlay.getMyGameBoard().getPlace(i, PLACE_NAME.HAND);
                if (place.getCard() != null)
                    emptyHandPlace = place;
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
            if (number < 7)
                if (gamePlay.getMyGameBoard().getPlace(number, PLACE_NAME.HAND) != null)
                    selectCard(matcher);
                else printerAndScanner.printNextLine(noCardInGivenPosition);
            else printerAndScanner.printNextLine(invalidSelection);
        }
        else if (hasField(matcher, "type")) {
            int number =  Integer.parseInt(matcher.group("select"));
            if (number < 6)
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
                    gamePlay.setSelectedCard(gamePlay.getMyGameBoard().
                            getPlace(Integer.parseInt(matcher.group("select")), placeName));
                }
            }
            printerAndScanner.printNextLine(cardSelected);
        }
    }

    private void summonCredibility(){
        if (!gamePlay.getUniversalHistory().contains("cannotSummon")) {
            Place selectedCard = gamePlay.getSelectedCard();
            if (selectedCard.getCard() != null) {
                if (gamePlay.getMyGameBoard().fromThisGameBoard(selectedCard) && selectedCard.getCard() instanceof MonsterCards) {
                    if (phase == PHASE.MAIN) {
                        if (gamePlay.getMyGameBoard().getFirstEmptyPlace(PLACE_NAME.MONSTER) != -1 /*||
                                selectedCard.getCard().getType().equals("Ritual")*/) {
                            if (!alreadySummonedOrSet) {
                                if (specialAbilityActivationController.summonWithTribute(selectedCard)) {
                                    if (selectedCard.getCard().getType().equals("Ritual"))
                                        ritualSummon(selectedCard);
                                    else //summon(getGamePlay().getSelectedCard(), true);
                                        new NewChain(this, selectedCard, CHAIN_JOB.SUMMON, 0);
                                }
                            } else printerAndScanner.printNextLine(alreadySummonedORSetOnThisTurn);
                        } else printerAndScanner.printNextLine(monsterCardZoneIsFull);
                    } else printerAndScanner.printNextLine(actionNotAllowedInThisPhase);
                } else printerAndScanner.printNextLine(cantSummonThisCard);
            } else printerAndScanner.printNextLine(noCardsIsSelectedYet);
        }
    }

    public void placeCard(Place place, boolean normalSummon, STATUS status){
        int emptyPlace = gamePlay.getMyGameBoard().getFirstEmptyPlace(PLACE_NAME.MONSTER);
        Place placeTo = gamePlay.getMyGameBoard().getPlace(emptyPlace, PLACE_NAME.MONSTER);
        placeTo.setCard(place.getCard());
        placeTo.setStatus(status);
        placeTo.addTemporaryFeatures(TEMPORARY_FEATURES.CARD_SET_OR_SUMMONED_IN_THIS_TURN);
        place.setCard(null);
        if (normalSummon)
            alreadySummonedOrSet = true;
        specialAbilityActivationController.setGamePlayController(this);
        specialAbilityActivationController.runFacUpSpecial(placeTo);
//        if (gamePlay.getUniversalHistory().contains("killThisCardUponSummon"))
//            specialAbilityActivationController.checkSummonDeactivation(placeTo);
        if (placeTo.getCard() != null) {
            printerAndScanner.printNextLine(summonedSuccessfully);
            specialAbilityActivationController.activateField();
        }
        specialAbilityActivationController.setGamePlayController(this);
        specialAbilityActivationController.runAttackAmountByQuantifier();
        if (normalSummon)
            specialAbilityActivationController.checkSummonAMonsterUponNormalSummon(placeTo);
    }

    private void checkBeforeSet(){
        Place selectedCard = gamePlay.getSelectedCard();
        if (selectedCard.getCard() != null){
            if (gamePlay.getMyGameBoard().fromThisGameBoard(selectedCard) &&
                    selectedCard.getType() == PLACE_NAME.HAND &&
                    specialAbilityActivationController.hasTributeMethod(selectedCard.getCard())) {
                if (phase == PHASE.MAIN) {
                    if (selectedCard.getCard() instanceof MonsterCards)
                        setMonsterCard(selectedCard);
                    else setSpellOrTrap(selectedCard);
                } else printerAndScanner.printNextLine(actionNotAllowedInThisPhase);
            } else printerAndScanner.printNextLine(cantSetThisCard);
        } else printerAndScanner.printNextLine(noCardsIsSelectedYet);
    }

    private void setMonsterCard(Place selectedCard){
        int emptyPlace = gamePlay.getMyGameBoard().getFirstEmptyPlace(PLACE_NAME.MONSTER);
        if (emptyPlace != -1) {
            if (!alreadySummonedOrSet) {
                Place placeTo = gamePlay.getMyGameBoard().getPlace(emptyPlace, PLACE_NAME.MONSTER);
                placeTo.setCard(selectedCard.getCard());
                placeTo.setStatus(STATUS.SET);
                selectedCard.setCard(null);
                placeTo.addTemporaryFeatures(TEMPORARY_FEATURES.CARD_SET_OR_SUMMONED_IN_THIS_TURN);
                alreadySummonedOrSet = true;
                printerAndScanner.printNextLine(setSuccessfully);
            } else printerAndScanner.printNextLine(alreadySummonedORSetOnThisTurn);
        } else printerAndScanner.printNextLine(monsterCardZoneIsFull);
    }

    private void setSpellOrTrap(Place selectedCard){
        int emptyPlace = gamePlay.getMyGameBoard().getFirstEmptyPlace(PLACE_NAME.SPELL_AND_TRAP);
        if (emptyPlace != -1) {
                Place placeTo = gamePlay.getMyGameBoard().getPlace(emptyPlace, PLACE_NAME.SPELL_AND_TRAP);
                placeTo.setCard(selectedCard.getCard());
                placeTo.setStatus(STATUS.SET);
                placeTo.addTemporaryFeatures(TEMPORARY_FEATURES.SPELL_ACTIVATED);
                selectedCard.setCard(null);
                printerAndScanner.printNextLine(setSuccessfully);
        } else printerAndScanner.printNextLine(spellCardZoneIsFull);
    }

    private void changePosition(Matcher matcher){
        Place selectedCard = gamePlay.getSelectedCard();
        if (selectedCard != null){
            if (selectedCard.getType() == PLACE_NAME.MONSTER){
                if (phase == PHASE.MAIN){
                    STATUS status = STATUS.getStatusByString(matcher.group("position"));
                    if ((status == STATUS.ATTACK && selectedCard.getStatus() == STATUS.DEFENCE) ||
                            (status == STATUS.DEFENCE && selectedCard.getStatus() == STATUS.ATTACK)){
                        if (selectedCard.getTemporaryFeatures().isEmpty()){
                            selectedCard.setStatus(STATUS.getStatusByString(matcher.group("position")));
                            if (selectedCard.getTemporaryFeatures().contains(TEMPORARY_FEATURES.EQUIPPED)){
                                specialAbilityActivationController.setGamePlayController(this);
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
                    if (selectedCard.getStatus() != STATUS.SET &&
                            !selectedCard.getTemporaryFeatures().contains(TEMPORARY_FEATURES.CARD_SET_OR_SUMMONED_IN_THIS_TURN)){
//                        selectedCard.setStatus(STATUS.ATTACK);
//                        selectedCard.addTemporaryFeatures(TEMPORARY_FEATURES.CARD_POSITION_CHANGED_IN_THIS_TURN);
//                        new NewChain(this, selectedCard, CHAIN_JOB.FLIP_SUMMON, 0);
//                        if (selectedCard.getCard() != null) {
//                            specialAbilityActivationController.setGamePlayController(this);
//                            specialAbilityActivationController.runFacUpSpecial(selectedCard);
//                            specialAbilityActivationController.runFlipSpecial(selectedCard);
//                        }
                        new NewChain(this, selectedCard, CHAIN_JOB.FLIP_SUMMON, 0);
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
                        if (selectedCard.getTemporaryFeatures().contains(TEMPORARY_FEATURES.CARD_ATTACKED_IN_THIS_TURN)) {
                            if (cardToAttack.getCard() != null) {
                                selectedCard.setAffect(cardToAttack);
                                new NewChain(this, selectedCard,CHAIN_JOB.ATTACK_MONSTER, 0);
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
                    if (selectedCard.getTemporaryFeatures().contains(TEMPORARY_FEATURES.CARD_ATTACKED_IN_THIS_TURN)){
                        if (opponentHasMonsterCard()){
                            selectedCard.setAffect(selectedCard);
                            new NewChain(this, selectedCard, CHAIN_JOB.ATTACK_DIRECT, 0);
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

    public void flip (Place toFlip, STATUS status){
        toFlip.setStatus(status);
        specialAbilityActivationController.runFlipSpecial(toFlip);
        specialAbilityActivationController.runFacUpSpecial(toFlip);
    }

    private void activateEffect(){
        Place selectedCard = gamePlay.getSelectedCard();
        if (selectedCard != null){
            if (gamePlay.getMyGameBoard().fromThisGameBoard(selectedCard)) {
                if (selectedCard.getCard() instanceof SpellCards) {
                    if (phase != PHASE.MAIN) {
                        if (!selectedCard.getTemporaryFeatures().contains(TEMPORARY_FEATURES.SPELL_ACTIVATED) &&
                            !gamePlay.getHistory().get(selectedCard).contains("noSpecialThisRound")) {
                            if ((selectedCard.getType() == PLACE_NAME.HAND &&
//                                    gamePlay.getMyGameBoard().getFirstEmptyPlace(PLACE_NAME.SPELL_AND_TRAP) != -1 ||
                                    selectedCard.getCard().getSpecialSpeed() >= 2) ||
                                    selectedCard.getCard().getType().equals("Field") ||
                                    selectedCard.getType() == PLACE_NAME.SPELL_AND_TRAP ){
                                specialAbilityActivationController.setGamePlayController(this);
                                if (specialAbilityActivationController.checkForConditions(selectedCard) &&
                                    !gamePlay.getUniversalHistory().contains("cannotActivateTrap")){
                                    if (selectedCard.getType() == PLACE_NAME.HAND)
                                        selectedCard = putSpellOrField(selectedCard);
                                    if (selectedCard != null) {
                                        printerAndScanner.printNextLine(spellActivated);
                                        if (selectedCard.getCard().getType().equals("Field"))
                                            specialAbilityActivationController.activateField();
                                        else {
                                            selectedCard.setAffect(selectedCard);
                                            new NewChain(this, selectedCard,
                                                    CHAIN_JOB.ACTIVATE_SPELL, selectedCard.getCard().getSpecialSpeed());
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

    private Place putSpellOrField(Place toPlace){
        Place place = null;
        if (toPlace.getCard().getType().equals("Field")){
            place = gamePlay.getMyGameBoard().getPlace(0, PLACE_NAME.FIELD);
            gamePlay.getMyGameBoard().killCards(this, place);
            place.setCard(toPlace.getCard());
            place.setStatus(STATUS.ATTACK);
            toPlace.setCard(null);
        } else {
            int emptyPlace = gamePlay.getMyGameBoard().getFirstEmptyPlace(PLACE_NAME.SPELL_AND_TRAP);
            if (emptyPlace != -1){
                place = gamePlay.getMyGameBoard().getPlace(emptyPlace, PLACE_NAME.SPELL_AND_TRAP);
                place.setCard(toPlace.getCard());
                place.setStatus(STATUS.ATTACK);
                toPlace.setCard(null);
            } else printerAndScanner.printNextLine(spellCardZoneIsFull);
        }
        return place;
    }

    private void ritualSummon(Place place){
        if (checkBeforeRitualSummon(place)){
            Place ritualSpell = getRitualSpell();
            if (ritualSpell != null){
                ritualSpell.getCard().getSpecial().get(0).run(this, ritualSpell);
            } else printerAndScanner.printNextLine(cantRitualSummon);
        } else printerAndScanner.printNextLine(cantRitualSummon);
    }

    private boolean checkBeforeRitualSummon(Place place){
        boolean canRitual = false;
        for (int i = 1; i < 6 && !canRitual; i++) {
            if (gamePlay.getMyGameBoard().getPlace(i, PLACE_NAME.SPELL_AND_TRAP).getCard().getType().equals("Ritual"))
                canRitual = true;
        }
        if (!canRitual){
            for (int i = 0; i < 6 && !canRitual; i++) {
                if (gamePlay.getMyGameBoard().getPlace(i, PLACE_NAME.HAND).getCard().getType().equals("Ritual"))
                    canRitual = true;
            }
        }
        if (!canRitual)
            return false;
        else {
            return calculateLevel(((MonsterCards) place.getCard()).getLevel(), sumOfAllLevels(), 5);
        }
    }

    private boolean calculateLevel(int toReach, int sum, int upperLimit){
        if (sum == toReach)
            return true;
        for (int i = upperLimit; i > 0; i--) {
            if (calculateLevel(toReach,
                    sum - ((MonsterCards) gamePlay.getMyGameBoard().getPlace(i, PLACE_NAME.MONSTER).getCard()).getLevel(),
                    upperLimit - 1))
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
                if (place.getCard().getType().equals("Ritual"))
                ritual = gamePlay.getMyGameBoard().getPlace(i, PLACE_NAME.SPELL_AND_TRAP);
        }
        if (ritual == null){
            for (int i = 0; i < 6; i++) {
                Place place = gamePlay.getMyGameBoard().getPlace(i, PLACE_NAME.HAND);
                if (place.getCard() != null)
                    if (place.getCard().getType().equals("Ritual"))
                        ritual = gamePlay.getMyGameBoard().getPlace(i, PLACE_NAME.HAND);
            }
            if (ritual != null) {
                int emptyPlace = gamePlay.getMyGameBoard().getFirstEmptyPlace(PLACE_NAME.SPELL_AND_TRAP);
                if (emptyPlace != -1) {
                    Place place = gamePlay.getMyGameBoard().getPlace(emptyPlace, PLACE_NAME.SPELL_AND_TRAP);
                    place.setCard(ritual.getCard());
                    place.setStatus(STATUS.ATTACK);
                    ritual.setCard(null);
                    ritual = place;//TODO chain placing cards;
                } else ritual = null;
            }
        }
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
        for (String history : universalHistory) {
            if (history.equals("endBattleHistory"))
                universalHistory.remove(history);
            else if (history.startsWith("MyMonsterCard")){//TODO round or turn?
                specialAbilityActivationController.setGamePlayController(this);
                specialAbilityActivationController.stopControl(history);
                universalHistory.remove(history);
            } else if (universalHistory.contains("cannotDraw"))
                universalHistory.remove(history);

        }
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
                    manageHistory(monster, gamePlay.getHistory().get(spell));
                else checkStandByHistory(monster, gamePlay.getHistory().get(spell));
        }
    }

    private void manageHistory(Place place, ArrayList<String> historyArray){
        for (String history : historyArray) {
            if (history.equals("noSpecialThisRound"))
                historyArray.remove(history);
            else if (history.equals("scanner")){
                specialAbilityActivationController.setGamePlayController(this);
                specialAbilityActivationController.handleScanner(place);
                break;
            } else if (history.startsWith("temporaryAttackBoost")){
                Matcher matcher = RegexController.getMatcher(history, extractEndingNumber);
                ((MonsterZone) place).setAttackModifier(
                        ((MonsterZone) place).getAttackModifier() - Integer.parseInt(matcher.group(1)));
            } else if (history.equals("drawCardIfAMonsterIsDestroyed") && gamePlay.getMyGameBoard().getMonsterCardDestroyed())
                drawCard();
            else if (history.startsWith("turnsRemaining")){
                Matcher matcher = RegexController.getMatcher(history, extractEndingNumber);
                if (matcher != null){
                    int number = Integer.parseInt(matcher.group(1));
                    if (number != 0){
                        historyArray.remove(history);
                        historyArray.add("turnsRemaining" + (number -1));
                    } else {
                        gamePlay.getMyGameBoard().killCards(this, place);
                        break;
                    }
                }
            }
        }
    }

    public void placeCard(Place place, Cards card, STATUS status){
        place.setCard(card);
        place.setStatus(status);
        specialAbilityActivationController.setGamePlayController(this);
        specialAbilityActivationController.activateField();
        if (status != STATUS.SET)
            specialAbilityActivationController.runFacUpSpecial(place);
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

    private boolean checkIfMonstersCanAttack(){
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
                specialAbilityActivationController.setGamePlayController(this);
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
                if (temporaryFeatures.get(j) != TEMPORARY_FEATURES.SPELL_ACTIVATED)
                    temporaryFeatures.remove(temporaryFeatures.get(j));
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
            if (spell.getCard().getType().equals("Equip") && spell.getAffect() == place)
                return spell;
        }
        return null;
    }
}
