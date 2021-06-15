package controller;

import controller.specialbilities.SpecialAbilityActivationController;
import model.cards.spell.SpellCards;
import model.cards.trap.TrapCards;
import model.game.*;
import model.tools.CHAIN_JOB;
import model.tools.RegexPatterns;
import model.tools.StringMessages;
import view.PrinterAndScanner;

import java.util.ArrayList;
import java.util.regex.Matcher;

//TODO keep a history of cards in chain so they wont be used again
public class NewChain implements StringMessages, RegexPatterns {

    private static PrintBuilderController printBuilderController;
    private static PrinterAndScanner printerAndScanner;
    private static SpecialAbilityActivationController specialAbilityActivationController;

    static {
        printerAndScanner = PrinterAndScanner.getInstance();
        printBuilderController = PrintBuilderController.getInstance();
        specialAbilityActivationController = SpecialAbilityActivationController.getInstance();
    }

    private final ArrayList<Place> CHAINED_PLACES;
    private GamePlayController gamePlayController;
    private CHAIN_JOB chainJob;
    private boolean defenderWasHidden = false;
    private Place place;
    private int previousSpeed;

    public NewChain(GamePlayController gamePlayController, Place place, CHAIN_JOB chainJob, int previousSpeed,
                    ArrayList<Place> CHAINED_PLACES){
        this.gamePlayController = gamePlayController;
        this.place = place;
        this.chainJob = chainJob;
        this.previousSpeed = previousSpeed;
        this.CHAINED_PLACES = CHAINED_PLACES;
        CHAINED_PLACES.add(place);
        run();
    }

    private void run(){
        ArrayList<Place> opponentChainable = new ArrayList<>();
        ArrayList<Place> myChainable = new ArrayList<>();
//        findChainableCards(opponentChainable, myChainable);
        boolean doChainEarly = chainJob == CHAIN_JOB.SUMMON ||
                chainJob == CHAIN_JOB.SPECIAL_SUMMON || chainJob == CHAIN_JOB.FLIP_SUMMON;
        if (doChainEarly)
            doChain();
        searchForChainable(gamePlayController, myChainable);
        searchForChainable(gamePlayController.getGamePlay().getOpponentGamePlayController(), opponentChainable);
        if (myChainable.size() > 0)
            addChain(myChainable, gamePlayController);
        if (opponentChainable.size() > 0) {
            printerAndScanner.printString(printBuilderController.turnComplete(
                    gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getName(),
                    gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getMyGameBoard(),
                    gamePlayController.getGamePlay().getMyGameBoard()));
            addChain(opponentChainable, gamePlayController.getGamePlay().getOpponentGamePlayController());
            printerAndScanner.printString(printBuilderController.turnComplete(
                    gamePlayController.getGamePlay().getName(),
                    gamePlayController.getGamePlay().getMyGameBoard(),
                    gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getMyGameBoard()
            ));
            myChainable.clear();
            searchForChainable(gamePlayController, myChainable);
            if (myChainable.size() > 0)
                addChain(myChainable, gamePlayController);
        }
        if (!doChainEarly)
            if (gamePlayController.getGamePlay().getUniversalHistory().contains("preventChain"))
                gamePlayController.getGamePlay().getUniversalHistory().remove("preventChain");
            else if (!gamePlayController.getGamePlay().getGameEnded())
                doChain();
    }

//    private void findChainableCards(ArrayList<Place> opponentChainable, ArrayList<Place> myChainable) {
//        Place place;
//        specialAbilityActivationController.setGamePlayController(gamePlayController);
//        for (int i = 1; i < 6; i++) {
//            place = gamePlayController.getGamePlay().getMyGameBoard().getPlace(i, PLACE_NAME.SPELL_AND_TRAP);
//            if (place.getCard() != null)
//                if (place.getCard().getSpecialSpeed() >= previousSpeed)
//                    if (place.getCard().getChainAfter().contains(chainJob) || place.getCard().getChainAfter().size() == 0) {
//                        if (specialAbilityActivationController.checkForConditions(place))
//                            myChainable.add(place);
//                    }
//        }
//        for (int i = 1; i < 6; i++) {
//            place = gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().
//                    getMyGameBoard().getPlace(i, PLACE_NAME.SPELL_AND_TRAP);
//            if (place.getCard() != null)
//                if (place.getCard().getSpecialSpeed() >= previousSpeed)
//                    if (place.getCard().getChainAfter().contains(chainJob) || place.getCard().getChainAfter().size() == 0) {
//                        if (specialAbilityActivationController.checkForConditions(place))
//                            opponentChainable.add(place);
//                    }
//        }
//    }

    private void searchForChainable(GamePlayController gamePlayController, ArrayList<Place> chainable){
        Place place;
        specialAbilityActivationController.setGamePlayController(gamePlayController);
        for (int i = 1; i < 6; i++) {
            place = gamePlayController.getGamePlay().getMyGameBoard().getPlace(i, PLACE_NAME.SPELL_AND_TRAP);
            if (place.getCard() != null)
                if (!CHAINED_PLACES.contains(place))
                    if (place.getCard().getSpecialSpeed() >= previousSpeed){
                        ArrayList<CHAIN_JOB> jobs = place.getCard() instanceof SpellCards
                            ? ((SpellCards) place.getCard()).getChainJobs() : ((TrapCards) place.getCard()).getChainJobs();//place.getCard().getChainJob();
                        if ((jobs.contains(chainJob) || jobs.size() == 0) && !jobs.contains(CHAIN_JOB.ALONE) &&
                                !place.getTemporaryFeatures().contains(TEMPORARY_FEATURES.SPELL_ACTIVATED)) {
                            if (specialAbilityActivationController.checkForConditions(place))
                                chainable.add(place);
                    }
            }
        }
    }

//    private CHAIN_AFTER findChainAfter(int chainJob){
//        switch (chainJob){
//            case 0 :
//            case 1 :
//                return CHAIN_AFTER.AFTER_SUMMON;
//            case 1 :
//            case 2 :
//                return CHAIN_AFTER.AFTER_ATTACK;
//            case 3 :
//                return CHAIN_AFTER.AFTER_SPELL;
//            case 4 :
//                return CHAIN_AFTER.AFTER_TRAP;
//            default :
//                return CHAIN_AFTER.AFTER_FLIP_SUMMON;
//        }
//    }

    private void addChain(ArrayList<Place> chainAble, GamePlayController gamePlayController){
        printerAndScanner.printNextLine(askActivateChain);
        if (printerAndScanner.scanNextLine().equals("yes")){
            for (String command = printerAndScanner.scanNextLine(); !command.equals("end"); command = printerAndScanner.scanNextLine()){
                if (command.startsWith("select")){
                    Matcher matcher = RegexController.getMatcher(command, selectCardPattern);
                    if (matcher != null)
                        gamePlayController.selectCardCredibility(matcher);
                    else printerAndScanner.printNextLine(invalidCommand);
                } else if (command.matches("activate effect")){
                    Place place = gamePlayController.getGamePlay().getSelectedCard();
                    if (chainAble.contains(place)) {
                        place.setAffect(this.place);
                        if (place.getStatus() == STATUS.SET)
                            gamePlayController.flip(place, STATUS.ATTACK);
                        specialAbilityActivationController.setGamePlayController(gamePlayController);
                        if (specialAbilityActivationController.runUponActivation(place))
                            new NewChain(gamePlayController, place, findChainJob(place),
                                    place.getCard().getSpecialSpeed(), CHAINED_PLACES);
                        break;
                    } else printerAndScanner.printNextLine(cannotDoThat);
                } else printerAndScanner.printNextLine(invalidCommand);
            }
        }
    }

    public CHAIN_JOB findChainJob(Place place){
        return place.getCard() instanceof SpellCards ? CHAIN_JOB.ACTIVATE_SPELL : CHAIN_JOB.ACTIVATE_TRAP;
    }

    private void doChain(){
        if (place != null)
        if (place.getCard() != null){
//            if (place.getCard() != null && place.getAffect() != null){
//                if (place.getCard() instanceof MonsterCards ){
//                    if (place == place.getAffect())
//                        attackDirectly();
//                    else attackMonster();
//                }
//            }
            if (chainJob == CHAIN_JOB.SUMMON)
                summon();
            else if (chainJob == CHAIN_JOB.SPECIAL_SUMMON)
                specialSummon();
            else if (chainJob == CHAIN_JOB.FLIP_SUMMON)
                flipSummon();
            else if (chainJob == CHAIN_JOB.ATTACK_DIRECT) {
                if (gamePlayController.getGamePlay().getUniversalHistory().contains("preventAttack"))
                    gamePlayController.getGamePlay().getUniversalHistory().remove("preventAttack");
                else if (!place.getTemporaryFeatures().contains(TEMPORARY_FEATURES.CARD_ATTACKED_IN_THIS_TURN))
                    attackDirectly();
            }
            else if (chainJob == CHAIN_JOB.ATTACK_MONSTER)
                if (gamePlayController.getGamePlay().getUniversalHistory().contains("preventAttack"))
                    gamePlayController.getGamePlay().getUniversalHistory().remove("preventAttack");
                else if (!place.getTemporaryFeatures().contains(TEMPORARY_FEATURES.CARD_ATTACKED_IN_THIS_TURN))
                    attackMonster();
            else if (chainJob == CHAIN_JOB.ACTIVATE_SPELL || chainJob == CHAIN_JOB.ACTIVATE_TRAP)
                activate();
        }
    }

    private void attackDirectly(){
        MonsterZone attacker = (MonsterZone) place;
        gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().
                getMyGameBoard().changeHealth(attacker.getAttack() * -1);
        printerAndScanner.printString(printBuilderController.attackDirectly(attacker.getAttack()));
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
            gamePlayController.getGamePlay().getOpponentGamePlayController().flip(defender, STATUS.DEFENCE);
        specialAbilityActivationController.setGamePlayController(gamePlayController.getGamePlay().getOpponentGamePlayController());
        specialAbilityActivationController.runAttackSpecial(defender, defenderWasHidden);
        defender.setAffect(previousAffect);
        if (!gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getHistory().get(defender)
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
            gamePlayController.getGamePlay().getOpponentGamePlayController().killCard(defender);
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

    private void summon(){
        gamePlayController.placeCard(place, true, STATUS.ATTACK);
    }

    private void activate(){
        GamePlayController opponentGamePlayController = gamePlayController.getGamePlay().getOpponentGamePlayController();
        for (String history : opponentGamePlayController.getGamePlay().getUniversalHistory()) {
            if (history.startsWith("getLPIfEnemySpellIsActivated")){
                Matcher matcher = RegexController.getMatcher(history, RegexPatterns.extractEndingNumber);
                opponentGamePlayController.getGamePlay().getMyGameBoard().changeHealth(Integer.parseInt(matcher.group(1)));
            }
        }
        if (place.getCard().getType().equals("equip"))
            specialAbilityActivationController.activateEquip(place);
        else {
            specialAbilityActivationController.setGamePlayController(gamePlayController);
            specialAbilityActivationController.activateEffectWithChain(place);
            specialAbilityActivationController.activateEffectWithoutChain(place);
            specialAbilityActivationController.runSuccessSpecialAbility(place);
            gamePlayController.killCard(place);
        }
    }

    private void specialSummon(){
        gamePlayController.placeCard(place, false, place.getStatus() != null ? place.getStatus() : askStatus());
    }

    private STATUS askStatus(){
        printerAndScanner.printNextLine(askStatus);
        for (String string = printerAndScanner.scanNextLine(); true; string = printerAndScanner.scanNextLine()){
            if (string.equals("attack"))
                return STATUS.ATTACK;
            if (string.equals("defense"))
                return STATUS.DEFENCE;
            printerAndScanner.printNextLine(wrongStatus);
        }
    }

    private void flipSummon(){
        place.addTemporaryFeatures(TEMPORARY_FEATURES.CARD_POSITION_CHANGED_IN_THIS_TURN);
        gamePlayController.flip(place, STATUS.ATTACK);
    }

    private void checkIfTheGameEnded(){
        if (gamePlayController.getGamePlay().getMyGameBoard().getHealth() == 0 ||
        gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getMyGameBoard().getHealth() == 0){
            gamePlayController.getGamePlay().setGameEnded(true);
            gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().setGameEnded(true);
        }
    }
}
