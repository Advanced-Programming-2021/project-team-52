package controller;

import controller.specialbilities.*;
import model.cards.monster.MonsterCards;
import model.cards.spell.SpellCards;
import model.cards.trap.TrapCards;
import model.game.*;
import model.tools.RegexPatterns;
import model.tools.StringMessages;
import view.PrinterAndScanner;

import java.util.ArrayList;
import java.util.regex.Matcher;
//TODO add conditions

public class OldChain implements StringMessages {

    //TODO remove this comment
    private static PrinterAndScanner printerAndScanner = null;
    private static PrintBuilderController printBuilderController = null;
    private static SpecialAbilityActivationController specialAbilityActivationController;

    private GamePlayController gamePlayController;
    private Place place;
    private int previousSpeed;
    boolean attackChain;
    boolean defenderWasHidden = false;

    static {
        printerAndScanner = PrinterAndScanner.getInstance();
        printBuilderController = PrintBuilderController.getInstance();
        specialAbilityActivationController = SpecialAbilityActivationController.getInstance();
    }

    public OldChain (GamePlayController gamePlayController, Place place, int speed, boolean attackChain){
        this.gamePlayController = gamePlayController;
        this.place = place;
        this.previousSpeed = speed;
        this.attackChain = attackChain;
        run();
    }

    private void run(){
        if (place.getCard() instanceof MonsterCards && place.getAffect().getCard() instanceof MonsterCards &&
                place.getAffect() != place)
            defenderWasHidden = place.getAffect().getStatus() == STATUS.SET;
        specialAbilityActivationController.setGamePlayController(gamePlayController.getGamePlay().getOpponentGamePlayController());
        ArrayList<Place> chainableCards = new ArrayList<>();
        getSpellChainableCards(chainableCards);
        getTrapChainableCards(chainableCards);
        if (!chainableCards.isEmpty()){
            printerAndScanner.printString(printBuilderController.turnComplete(
                    gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getName(),
                    gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getMyGameBoard(),
                    gamePlayController.getGamePlay().getMyGameBoard()
            ));
            printerAndScanner.printNextLine(askActivateChain);
            if (printerAndScanner.scanNextLine().equals("yes")) {
//                printerAndScanner.printNextLine(cardNumber);
//                Place place = null;
//                int cardNumber;
//                while (true) {
//                    cardNumber = printerAndScanner.scanNextInt();
//                    if (cardNumber < 6 && cardNumber > 0){
//                        place = gamePlayController.getGamePlay().getMyGameBoard().getPlace(cardNumber, PLACE_NAME.SPELL_AND_TRAP);
//                        if (place.getCard() != null && chainableCards.contains(place))
//                            break;
//                    }
//                    printerAndScanner.printNextLine(wrongCard);
//                }
//                if (place != null){
//                    place.setAffect(this.place);
//                    new Chain(gamePlayController.getGamePlay().getOpponentGamePlayController(), place, place.getCard().
//                            getSpecialSpeed(), attackChain);
//                    if (place.getCard() != null && place.getAffect() != null)
//                        doChain();
//                }
                getChainCommands(chainableCards);
                if (place.getCard() != null && place.getAffect() != null)
                    doChain();

            }
        }
    }

    private void getChainCommands(ArrayList<Place> chainable){
        for (String command = printerAndScanner.scanNextLine(); !command.equals("cancel")
                ; command = printerAndScanner.scanNextLine()){
            if (command.startsWith("select")){
                Matcher matcher = RegexController.getMatcher(command, RegexPatterns.selectCardPattern);
                if (matcher != null)
                    gamePlayController.getGamePlay().getOpponentGamePlayController().selectCardCredibility(matcher);
                else printerAndScanner.printNextLine(invalidCommand);
            } else if (command.equals("activate effect")){
                Place selectedCard = gamePlayController.getGamePlay().getSelectedCard();
                if (chainable.contains(selectedCard)) {
                    selectedCard.setAffect(place);
                    new OldChain(gamePlayController.getGamePlay().getOpponentGamePlayController(), selectedCard,
                            selectedCard.getCard().getSpecialSpeed(), false);
                }
                break;
            } else printerAndScanner.printNextLine(invalidCommand);
        }
    }

    public ArrayList<Place> getChainableCards(){
        ArrayList<Place> chainableCards = new ArrayList<>();
        boolean onlyAffectMonster = place.getCard() instanceof MonsterCards;
        Place place;
        specialAbilityActivationController.setGamePlayController(gamePlayController);
        for (int i = 1; i < 6; i++) {
            place = gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().
                    getMyGameBoard().getPlace(i, PLACE_NAME.SPELL_AND_TRAP);
            if (place.getCard() != null){
                if (checkForSpecialAbility(place, onlyAffectMonster) && place.getCard().getSpecialSpeed() >= previousSpeed)
                    chainableCards.add(place);
            }
        }
        return chainableCards;
    }

    private boolean checkForSpecialAbility(Place place, boolean onlyAffectMonster){
        if ((place.getCard() instanceof TrapCards) && (gamePlayController.getGamePlay().
                getOpponentGamePlayController().getGamePlay().getUniversalHistory().contains("neutralizeTrap") ||
                gamePlayController.getGamePlay().getOpponentGamePlayController().
                        getGamePlay().getUniversalHistory().contains("cannotActivateTrap")))
            return false;
        if (specialAbilityActivationController.checkForConditions(place)) {
            for (SpecialAbility specialAbility : place.getCard().getSpecial()) {
                if (specialAbility instanceof ActivateChain || specialAbility instanceof ActivateNoChain) {
                    if ((specialAbility.getMethodName().equals("redirectAttack") && onlyAffectMonster) ||
                            specialAbility.getMethodName().equals("destroyAllEnemyMonstersInThisStatus") ||
                            specialAbility.getMethodName().equals("redirectAttack"))
                        return true;
                    if (!onlyAffectMonster)
                        if (specialAbility.getMethodName().equals("destroySpellWhileActivating") ||
                                specialAbility.getMethodName().equals("destroySpellAndTraps") ||
                                specialAbility.getMethodName().equals("killAllMonsters") ||
                                specialAbility.getMethodName().equals("destroyAllEnemySpellAndTrap"))
                            return true;
                    if (!attackChain) {
                        if (specialAbility.getMethodName().equals("spawnMonsterFromGraveYard"))
                            return true;
                    }
                }
            }
        }
        return false;
    }

    private void doChain(){
        if (place == place.getAffect()){
            if (place.getCard() instanceof MonsterCards)
                attackDirectly();
            else activateEffect(place);
        }
        else if (place.getCard() instanceof MonsterCards && place.getAffect().getCard() instanceof MonsterCards)
            attackMonster();
        else {
//            for (SpecialAbility specialAbility : place.getCard().getSpecial()) {
//                if (specialAbility instanceof ActivateChain || specialAbility instanceof ActivateNoChain)
//                    specialAbility.run(gamePlayController, place);
//            }
            activateEffect(place);
        }
    }

    private void attackMonster(){
        MonsterZone attacker = (MonsterZone) place;
        MonsterZone defender = (MonsterZone) place.getAffect();
        int attackerAttack = attacker.getAttack();
        int defenderAttack = defender.getAttack();
        int defenderDefense = defender.getDefense();
        String defenderName = defender.getCard().getName();
        boolean defenderInDefendingStatus = defender.getStatus() != STATUS.ATTACK;
        attacker.addTemporaryFeatures(TEMPORARY_FEATURES.CARD_ATTACKED_IN_THIS_TURN);
        Place previousAffect = defender.getAffect();
        defender.setAffect(attacker);
        if (defenderWasHidden)
            gamePlayController.getGamePlay().getOpponentGamePlayController().flip(defender);
        specialAbilityActivationController.setGamePlayController(gamePlayController.getGamePlay().getOpponentGamePlayController());
        specialAbilityActivationController.runAttackSpecial(defender, defenderWasHidden);
        defender.setAffect(previousAffect);
        if (!gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getHistory().get(attacker)
                .contains("neutralizeAttack")) {
            if (defenderInDefendingStatus) {
                attackToDefensePosition(attackerAttack, defenderDefense, defenderName,
                        attacker, defender, defenderWasHidden);
            } else {
                attackToAttackPosition(attackerAttack, defenderAttack, attacker, defender);
            }
        }
    }

    private void attackToAttackPosition(int attackerAttack, int defenderAttack,
                                        MonsterZone attacker, MonsterZone defender) {
        if (attackerAttack > defenderAttack){
            destroy(gamePlayController, defender,attacker, true, true);
            gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getMyGameBoard().
                    changeHealth(defenderAttack - attackerAttack);
            printerAndScanner.printString(printBuilderController
                    .attackToAttackResult(reduceHealth(defenderAttack - attackerAttack,
                            gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getMyGameBoard(),
                            gamePlayController.getGamePlay().getOpponentGamePlayController()) ? attackerAttack -
                            defenderAttack : 0, true));
        } else if (attackerAttack == defenderAttack){
            destroy(gamePlayController, defender, attacker, true, true);
            destroy(gamePlayController.getGamePlay().getOpponentGamePlayController(), attacker, defender, false, true);
            printerAndScanner.printNextLine(drawingAgainstOO);
        } else {
            destroy(gamePlayController.getGamePlay().getOpponentGamePlayController(), attacker, defender, false, false);
            gamePlayController.getGamePlay().getMyGameBoard().changeHealth(attackerAttack - defenderAttack);
            printerAndScanner.printString(printBuilderController
                    .attackToAttackResult(reduceHealth(attackerAttack - defenderAttack,
                            gamePlayController.getGamePlay().getMyGameBoard(),
                            gamePlayController) ? defenderAttack - attackerAttack : 0, false));
        }
    }

    private void attackToDefensePosition(int attackerAttack, int defenderDefense,
                                         String defenderName,
                                         MonsterZone attacker, MonsterZone defender,
                                         boolean defenderWasHidden) {
        if (defenderDefense < attackerAttack){
            destroy(gamePlayController, defender, attacker, true, true);
            printerAndScanner.printString(printBuilderController.
                    attackToDefenseResult(defenderWasHidden, defenderName, 1, 0));
        } else if (defenderDefense == attackerAttack)
            printerAndScanner.printString(printBuilderController.
                    attackToDefenseResult(defenderWasHidden, defenderName, 0, 0));
        else {
            destroy(gamePlayController.getGamePlay().getOpponentGamePlayController(), attacker, defender,
                    false, false);
            printerAndScanner.printString(printBuilderController.
                    attackToDefenseResult(defenderWasHidden, defenderName, -1,
                            reduceHealth(attackerAttack - defenderDefense,
                                    gamePlayController.getGamePlay().getMyGameBoard(),
                                    gamePlayController) ? defenderDefense - attackerAttack : 0));
        }
    }

    private void destroy(GamePlayController gamePlayController,
                         Place defender, Place attacker, boolean destroyDeathWish, boolean runKillAttacker){
        if (!gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().
                getHistory().get(defender).contains("cannotBeNormallyDestroyed")) {
            if (destroyDeathWish) {
                defender.setAffect(attacker);
                specialAbilityActivationController.setGamePlayController(gamePlayController);
                specialAbilityActivationController.runKillCardDeathWishes(defender, runKillAttacker);
            }
            gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getMyGameBoard().killCards(
                    gamePlayController.getGamePlay().getOpponentGamePlayController(), defender);
        }
    }

    private boolean reduceHealth(int amount, GameBoard board, GamePlayController gamePlayController){
        if (!gamePlayController.getGamePlay().getUniversalHistory().contains("noHealthReduction")){
            board.changeHealth(amount);
            return true;
        } else {
            gamePlayController.getGamePlay().getUniversalHistory().remove("noHealthReduction");
            return false;
        }
    }

    private void attackDirectly(){
        MonsterZone attacker = (MonsterZone) place;
        gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().
                getMyGameBoard().changeHealth(attacker.getAttack() * -1);
        printerAndScanner.printString(printBuilderController.attackDirectly(attacker.getAttack()));
    }

    private void activateEffect(Place place){
        if (place.getStatus() == STATUS.SET)
            gamePlayController.flip(place);
        GamePlayController opponentGamePlayController = gamePlayController.getGamePlay().getOpponentGamePlayController();
        for (String history : opponentGamePlayController.getGamePlay().getUniversalHistory()) {
            if (history.startsWith("getLPIfEnemySpellIsActivated")){
                Matcher matcher = RegexController.getMatcher(history, RegexPatterns.extractEndingNumber);
                opponentGamePlayController.getGamePlay().getMyGameBoard().changeHealth(Integer.parseInt(matcher.group(1)));
            }
        }
        specialAbilityActivationController.setGamePlayController(gamePlayController);
        specialAbilityActivationController.activateEffectWithChain(place);
        specialAbilityActivationController.activateEffectWithoutChain(place);
        specialAbilityActivationController.runSuccessSpecialAbility(place);
    }

    private void getSpellChainableCards(ArrayList<Place> chainable){
        Place place;
        for (int i = 1; i < 6; i++) {
            place = gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getMyGameBoard()
                    .getPlace(i, PLACE_NAME.SPELL_AND_TRAP);
            if (place.getCard() instanceof SpellCards) {
                Place previousAffect = place.getAffect();
                place.setAffect(this.place);
                if (specialAbilityActivationController.canActivateSpell(place, this.previousSpeed))
                    chainable.add(place);
                place.setAffect(previousAffect);
            }
        }
    }

    private void getTrapChainableCards(ArrayList<Place> chainable){
        Place place;
        for (int i = 1; i < 6; i++) {
            place = gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getMyGameBoard()
                    .getPlace(i, PLACE_NAME.SPELL_AND_TRAP);
            if (place.getCard() instanceof TrapCards) {
                Place previousAffect = place.getAffect();
                place.setAffect(this.place);
                if (specialAbilityActivationController.checkForConditions(place))
                    if (place.getCard().getSpecialSpeed() >= this.previousSpeed)
                        chainable.add(place);
                place.setAffect(previousAffect);
            }
        }
    }
}
