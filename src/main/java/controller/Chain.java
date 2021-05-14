package controller;

import controller.specialbilities.*;
import model.cards.monster.MonsterCards;
import model.cards.trap.TrapCards;
import model.game.*;
import model.tools.RegexPatterns;
import model.tools.StringMessages;
import view.PrinterAndScanner;

import java.util.ArrayList;
import java.util.regex.Matcher;
//TODO add conditions

public class Chain implements StringMessages {

    private static PrinterAndScanner printerAndScanner = null;
    private static PrintBuilderController printBuilderController = null;
    private static SpecialAbilityActivationController specialAbilityActivationController;

    private GamePlayController gamePlayController;
    private Place place;
    private int previousSpeed;
    boolean attackChain;

    static {
        printerAndScanner = PrinterAndScanner.getInstance();
        printBuilderController = PrintBuilderController.getInstance();
        specialAbilityActivationController = SpecialAbilityActivationController.getInstance();
    }

    public Chain (GamePlayController gamePlayController, Place place, int speed, boolean attackChain){
        this.gamePlayController = gamePlayController;
        this.place = place;
        this.previousSpeed = speed;
        this.attackChain = attackChain;
        run();
    }

    private void run(){
        ArrayList<Place> chainableCards = getChainableCards();
        if (!chainableCards.isEmpty()){
            printerAndScanner.printString(printBuilderController.turnComplete(
                    gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getName(),
                    gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getMyGameBoard(),
                    gamePlayController.getGamePlay().getMyGameBoard()
            ));
            printerAndScanner.printNextLine(askActivateChain);
            if (printerAndScanner.scanNextLine().equals("yes")){
                printerAndScanner.printNextLine(cardNumber);
                Place place = null;
                int cardNumber;
                while (true) {
                    cardNumber = printerAndScanner.scanNextInt();
                    if (cardNumber < 6 && cardNumber > 0){
                        place = gamePlayController.getGamePlay().getMyGameBoard().getPlace(cardNumber, PLACE_NAME.SPELL_AND_TRAP);
                        if (place.getCard() != null && chainableCards.contains(place))
                            break;
                    }
                    printerAndScanner.printNextLine(wrongCard);
                }
                if (place != null){
                    place.setAffect(this.place);
                    new Chain(gamePlayController.getGamePlay().getOpponentGamePlayController(), place, place.getCard().
                            getSpecialSpeed(), attackChain);
                    if (place.getCard() != null && place.getAffect() != null)
                        doChain();
                }
            }
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
            attack();
        else {
            for (SpecialAbility specialAbility : place.getCard().getSpecial()) {
                if (specialAbility instanceof ActivateChain || specialAbility instanceof ActivateNoChain)
                    specialAbility.run(gamePlayController, place);
            }
        }
    }

    private void attack(){
        MonsterZone attacker = (MonsterZone) place;
        MonsterZone defender = (MonsterZone) place.getAffect();
        attacker.addTemporaryFeatures(TEMPORARY_FEATURES.CARD_ATTACKED_IN_THIS_TURN);
        if (attacker.getStatus() == STATUS.ATTACK && defender.getAffect().getStatus() == STATUS.ATTACK){
            attackToAttackPosition(attacker, defender);
        } else {
            attackToDefensePosition(attacker, defender);
        }
    }

    private void attackToAttackPosition(MonsterZone attacker, MonsterZone defender) {
        int attackerAttack = attacker.getAttack();
        int defenderAttack = defender.getAttack();
        if (attackerAttack > defenderAttack){
            destroy(gamePlayController, defender,attacker, true, true);
            gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getMyGameBoard().
                    changeHealth(defenderAttack - attackerAttack);
            if (reduceHealth(defenderAttack - attackerAttack,
                    gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getMyGameBoard(),
                    gamePlayController.getGamePlay().getOpponentGamePlayController()))
            printerAndScanner.printString(printBuilderController
                    .attackToAttackResult(attackerAttack - defenderAttack, true));
        } else if (attackerAttack == defenderAttack){
            destroy(gamePlayController, defender, attacker, true, true);
            destroy(gamePlayController.getGamePlay().getOpponentGamePlayController(), attacker, defender, false, true);
            printerAndScanner.printNextLine(drawingAgainstOO);
        } else {
            destroy(gamePlayController.getGamePlay().getOpponentGamePlayController(), attacker, defender, false, false);
            gamePlayController.getGamePlay().getMyGameBoard().changeHealth(attackerAttack - defenderAttack);
            if (reduceHealth(attackerAttack - defenderAttack, gamePlayController.getGamePlay().getMyGameBoard(),
                    gamePlayController))
            printerAndScanner.printString(printBuilderController
                    .attackToAttackResult(defenderAttack - attackerAttack, false));
        }
    }

    private void attackToDefensePosition(MonsterZone attacker, MonsterZone defender){
        int attackerAttack = attacker.getAttack();
        int defenderDefense = defender.getDefense();
        boolean defenderWasHidden = defender.getStatus() == STATUS.SET;
        if (defenderWasHidden)
            gamePlayController.getGamePlay().getOpponentGamePlayController().flip(defender);
        if (defenderDefense < attackerAttack){
            destroy(gamePlayController, defender, attacker, true, true);
            printerAndScanner.printString(printBuilderController.
                    attackToDefenseResult(defenderWasHidden, defender.getCard().getName(), 1, 0));
        } else if (defenderDefense == attackerAttack)
            printerAndScanner.printString(printBuilderController.
                    attackToDefenseResult(defenderWasHidden, defender.getCard().getName(), 0, 0));
        else {
            destroy(gamePlayController.getGamePlay().getOpponentGamePlayController(), attacker, defender, false, false);
            if (reduceHealth(attackerAttack - defenderDefense, gamePlayController.getGamePlay().getMyGameBoard(),
                    gamePlayController))
            printerAndScanner.printString(printBuilderController.
                    attackToDefenseResult(defenderWasHidden, defender.getCard().getName(), -1,
                            defenderDefense - attackerAttack));
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
        GamePlayController opponentGamePlayController = gamePlayController.getGamePlay().getOpponentGamePlayController();
        for (String history : opponentGamePlayController.getGamePlay().getUniversalHistory()) {
            if (history.startsWith("getLPIfEnemySpellIsActivated")){
                Matcher matcher = RegexController.getMatcher(history, RegexPatterns.extractEndingNumber);
                opponentGamePlayController.getGamePlay().getMyGameBoard().changeHealth(Integer.parseInt(matcher.group(1)));
            }
        }
        specialAbilityActivationController.setGamePlayController(gamePlayController);
        specialAbilityActivationController.activateEffectWithoutChain(place);
        specialAbilityActivationController.runSuccessSpecialAbility(place);
    }
}
