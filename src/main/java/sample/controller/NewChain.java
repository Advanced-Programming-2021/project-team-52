package sample.controller;

import sample.controller.specialbilities.SpecialAbilityActivationController;
import sample.model.cards.spell.SpellCards;
import sample.model.cards.trap.TrapCards;
import sample.model.game.*;
import sample.model.tools.CHAIN_JOB;
import sample.model.tools.RegexPatterns;
import sample.model.tools.StringMessages;
import sample.view.PrinterAndScanner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.regex.Matcher;

public class NewChain implements StringMessages, RegexPatterns {

    private static final Set<String> TO_REMOVE = Collections.singleton("noHealthReduction");
    private static PrintBuilderController printBuilderController;
    private static PrinterAndScanner printerAndScanner;

    static {
        printerAndScanner = PrinterAndScanner.getInstance();
        printBuilderController = PrintBuilderController.getInstance();
    }

    private final ArrayList<Place> CHAINED_PLACES;
    private GamePlayController gamePlayController;
    private CHAIN_JOB chainJob;
    private Place place;
    private int previousSpeed;
    private SpecialAbilityActivationController specialAbilityActivationController;
    private String chainOwner;

    public NewChain(GamePlayController gamePlayController, Place place, CHAIN_JOB chainJob, int previousSpeed,
                    ArrayList<Place> CHAINED_PLACES) {
        this.gamePlayController = gamePlayController;
        this.place = place;
        this.chainJob = chainJob;
        this.previousSpeed = previousSpeed;
        this.CHAINED_PLACES = CHAINED_PLACES;
        this.specialAbilityActivationController = gamePlayController.getSpecialAbilityActivationController();
        this.chainOwner = gamePlayController.getGamePlay().getName();
        CHAINED_PLACES.add(place);
        run();
    }

    private void run() {
        ArrayList<Place> opponentChainable = new ArrayList<>();
        ArrayList<Place> myChainable = new ArrayList<>();
        if (chainJob == CHAIN_JOB.FLIP_SUMMON)
            doChain(false);
        searchForChainable(gamePlayController, myChainable);
        searchForChainable(gamePlayController.getGamePlay().getOpponentGamePlayController(), opponentChainable);
        if (myChainable.size() > 0)
            addChain(myChainable, gamePlayController);
        if (opponentChainable.size() > 0 && canChain()) {
            String myName = gamePlayController.getGamePlay().getName();
            String opponentName = gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getName();
            GameBoard myGameBoard = gamePlayController.getGamePlay().getMyGameBoard();
            GameBoard opponentGameBoard = gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getMyGameBoard();
            printerAndScanner.printString(printBuilderController.turnComplete(opponentName, myName, opponentGameBoard, myGameBoard));
            addChain(opponentChainable, gamePlayController.getGamePlay().getOpponentGamePlayController());
            if (!chainOwner.equals(myName)) {
                chainOwner = myName;
                printerAndScanner.printString(printBuilderController.turnComplete(myName, opponentName, myGameBoard, opponentGameBoard));
            }
        }
        if (!gamePlayController.getGameOver())
            doChain(true);
    }

    private boolean canChain() {
        if (gamePlayController.getPhase() == PHASE.MAIN || gamePlayController.getPhase() == PHASE.BATTLE)
            return true;
        GamePlayController opponentGamePlayController = gamePlayController.getGamePlay().getOpponentGamePlayController();
        return opponentGamePlayController.getPhase() == PHASE.MAIN || opponentGamePlayController.getPhase() == PHASE.BATTLE;
    }

    private void searchForChainable(GamePlayController gamePlayController, ArrayList<Place> chainable) {
        SpecialAbilityActivationController specialAbilityActivationController =
                gamePlayController.getSpecialAbilityActivationController();
        Place place;
        for (int i = 1; i < 6; i++) {
            place = gamePlayController.getGamePlay().getMyGameBoard().getPlace(i, PLACE_NAME.SPELL_AND_TRAP);
            if (place.getCard() != null)
                if (!CHAINED_PLACES.contains(place))
                    if (place.getCard().getSpecialSpeed() >= previousSpeed)
                        if (!(place.getCard() instanceof TrapCards && gamePlayController.getGamePlay()
                                .getUniversalHistory().contains("cannotActivateTrap"))) {
                            ArrayList<CHAIN_JOB> jobs = place.getCard() instanceof SpellCards
                                    ? ((SpellCards) place.getCard()).getChainJobs() : ((TrapCards) place.getCard()).getChainJobs();
                            if ((jobs.contains(chainJob) ||
                                    (jobs.size() == 0 && chainJob != CHAIN_JOB.BEFORE_SUMMON &&
                                            chainJob != CHAIN_JOB.BEFORE_SPECIAL_SUMMON))
                                    && !jobs.contains(CHAIN_JOB.ALONE) &&
                                    !place.getTemporaryFeatures().contains(TEMPORARY_FEATURES.SPELL_ACTIVATED)) {
                                if (specialAbilityActivationController.checkForConditions(place))
                                    chainable.add(place);
                            }
                        }
        }
    }

    private void addChain(ArrayList<Place> chainAble, GamePlayController gamePlayController) {
        SpecialAbilityActivationController specialAbilityActivationController =
                gamePlayController.getSpecialAbilityActivationController();
        gamePlayController.getMyCommunicator().askOptions(askActivateChain, "yes", "no");
        if (gamePlayController.takeCommand().equals("yes")) {
            gamePlayController.getMyCommunicator().changeGameState("chain");
            chainOwner = gamePlayController.getGamePlay().getName();
            String command;
            for (command = gamePlayController.takeCommand(); !command.equals("cancel"); command = gamePlayController.takeCommand()) {
                if (handCommand(chainAble, gamePlayController, specialAbilityActivationController, command)) break;
            }
            if (command.equals("cancel"))
                chainOwner = gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getName();
        }
    }

    private boolean handCommand(ArrayList<Place> chainAble, GamePlayController gamePlayController,
                                SpecialAbilityActivationController specialAbilityActivationController, String command) {
        if (command.startsWith("select")) {
            Matcher matcher = RegexController.getMatcher(command, selectCardPattern);
            if (matcher != null)
                gamePlayController.selectCardCredibility(matcher);
            else printerAndScanner.printNextLine(invalidCommand);
        } else if (command.matches("activate effect")) {
            Place place = gamePlayController.getGamePlay().getSelectedCard();
            if (chainAble.contains(place) && !place.getTemporaryFeatures().contains(TEMPORARY_FEATURES.SPELL_ACTIVATED)) {
                if (specialAbilityActivationController.runUponActivation(place)) {
                    place.setAffect(this.place);
                    if (place.getStatus() == STATUS.SET)
                        gamePlayController.flip(place, STATUS.ATTACK, true, true, true);
                    new NewChain(gamePlayController, place, findChainJob(place),
                            place.getCard().getSpecialSpeed(), CHAINED_PLACES);
                }
                return true;
            } else printerAndScanner.printNextLine(notYourTurnToDoThat);
        } else if (command.startsWith("card show")) {
            Matcher matcher = RegexController.getMatcher(command, cardShowPattern);
            if (matcher != null)
                if (matcher.group("card").matches("^(?:--selected|-s)$"))
                    gamePlayController.showSelectedCard();
                else printerAndScanner.printNextLine(invalidCommand);
            else printerAndScanner.printNextLine(invalidCommand);
        } else if (command.equals("surrender")) {
            gamePlayController.setSurrendered(true);
            gamePlayController.setGameOver(true);
            gamePlayController.getGamePlay().getOpponentGamePlayController().setGameOver(true);
        } else printerAndScanner.printNextLine(notYourTurnToDoThat);
        return false;
    }

    public CHAIN_JOB findChainJob(Place place) {
        return place.getCard() instanceof SpellCards ? CHAIN_JOB.ACTIVATE_SPELL : CHAIN_JOB.ACTIVATE_TRAP;
    }

    private void doChain(boolean secondTimeDoingThisChain) {
        if (place != null)
            if (place.getCard() != null) {
                ArrayList<String> universalHistory = gamePlayController.getGamePlay().getUniversalHistory();
                if (chainJob == CHAIN_JOB.ATTACK_DIRECT) {
                    if (universalHistory.contains("preventAttack"))
                        universalHistory.remove("preventAttack");
                    else if (!place.getTemporaryFeatures().contains(TEMPORARY_FEATURES.CARD_ATTACKED_IN_THIS_TURN) &&
                            gamePlayController.checkIfMonstersCanAttack())
                        attackDirectly();
                } else if (chainJob == CHAIN_JOB.ATTACK_MONSTER) {
                    if (universalHistory.contains("preventAttack"))
                        universalHistory.remove("preventAttack");
                    else if (!place.getTemporaryFeatures().contains(TEMPORARY_FEATURES.CARD_ATTACKED_IN_THIS_TURN) &&
                            gamePlayController.checkIfMonstersCanAttack())
                        attackMonster();
                } else if (chainJob == CHAIN_JOB.ACTIVATE_SPELL || chainJob == CHAIN_JOB.ACTIVATE_TRAP)
                    activate();
                else if (chainJob == CHAIN_JOB.FLIP_SUMMON)
                    flipSummon(secondTimeDoingThisChain);
                checkIfTheGameEnded();
            }
    }

    private void attackDirectly() {
        MonsterZone attacker = (MonsterZone) place;
        attacker.getTemporaryFeatures().add(TEMPORARY_FEATURES.CARD_ATTACKED_IN_THIS_TURN);
        GamePlayController opponentGamePlayController = gamePlayController.getGamePlay().getOpponentGamePlayController();
        gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().
                getMyGameBoard().changeHealth(attacker.getAttack() * -1);
        opponentGamePlayController.getMyCommunicator().reduceHealth(attacker.getAttack() * -1, false);
        gamePlayController.getMyCommunicator().reduceHealth(attacker.getAttack() * -1, true);
        printerAndScanner.printString(printBuilderController.attackDirectly(attacker.getAttack()));
    }

    private void attackMonster() {
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
        boolean defenderWasHidden = defender.getStatus() == STATUS.SET;
        if (defenderWasHidden)
            gamePlayController.getGamePlay().getOpponentGamePlayController().flip(defender, STATUS.DEFENCE, true, true, true);
        if (attacker.getCard() != null) {
            gamePlayController.getGamePlay().getOpponentGamePlayController().getSpecialAbilityActivationController()
                    .runAttackSpecial(defender);
            ArrayList<String> history = gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay()
                    .getHistory().get(defender);
            if (history.contains("neutralizeAttack"))
                history.remove("neutralizeAttack");
            else {
                if (defenderInDefendingStatus) {
                    attackToDefensePosition(attackerAttack, defenderDefense, defenderName,
                            attacker, defender, defenderWasHidden);
                } else {
                    attackToAttackPosition(attackerAttack, defenderAttack, attacker, defender);
                }
            }
            if (defenderWasHidden)
                gamePlayController.getGamePlay().getOpponentGamePlayController().getSpecialAbilityActivationController().
                        runReduceAttackerLpIfItWasFacingDown(defender);
        }
        removeUniversalNoHealthReductionHistories();
    }

    private void attackToAttackPosition(int attackerAttack, int defenderAttack,
                                        MonsterZone attacker, MonsterZone defender) {
        if (attackerAttack > defenderAttack) {
            destroy(gamePlayController.getGamePlay().getOpponentGamePlayController(), defender, attacker, true);
            printerAndScanner.printString(printBuilderController
                    .attackToAttackResult(reduceHealth(defenderAttack - attackerAttack,
                            gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getMyGameBoard(),
                            gamePlayController.getGamePlay().getOpponentGamePlayController()) ? attackerAttack -
                            defenderAttack : 0, defender.getCard() == null ? 1 : 0));
        } else if (attackerAttack == defenderAttack) {
            destroy(gamePlayController.getGamePlay().getOpponentGamePlayController(), defender, attacker, true);
            destroy(gamePlayController, attacker, defender, true);
            printerAndScanner.printNextLine(drawingAgainstOO);
        } else {
            destroy(gamePlayController, attacker, defender, false);
            printerAndScanner.printString(printBuilderController
                    .attackToAttackResult(reduceHealth(attackerAttack - defenderAttack,
                            gamePlayController.getGamePlay().getMyGameBoard(),
                            gamePlayController) ? defenderAttack - attackerAttack : 0, attacker.getCard() == null ? -1 : 0));
        }
    }

    private void attackToDefensePosition(int attackerAttack, int defenderDefense,
                                         String defenderName,
                                         MonsterZone attacker, MonsterZone defender,
                                         boolean defenderWasHidden) {
        if (defenderDefense < attackerAttack) {
            destroy(gamePlayController.getGamePlay().getOpponentGamePlayController(), defender, attacker, true);
            printerAndScanner.printString(printBuilderController.
                    attackToDefenseResult(defenderWasHidden, defenderName, defender.getCard() == null ? 1 : 0, 0));
        } else if (defenderDefense == attackerAttack)
            printerAndScanner.printString(printBuilderController.
                    attackToDefenseResult(defenderWasHidden, defenderName, 0, 0));
        else {
            destroy(gamePlayController, attacker, defender, false);
            printerAndScanner.printString(printBuilderController.
                    attackToDefenseResult(defenderWasHidden, defenderName, attacker.getCard() == null ? -1 : 0,
                            reduceHealth(attackerAttack - defenderDefense,
                                    gamePlayController.getGamePlay().getMyGameBoard(),
                                    gamePlayController) ? defenderDefense - attackerAttack : 0));
        }
    }

    private void destroy(GamePlayController gamePlayController,
                         Place defender, Place attacker, boolean runKillAttacker) {
        if (!gamePlayController.getGamePlay().getHistory().get(defender).contains("cannotBeNormallyDestroyed")) {
            defender.setAffect(attacker);
            gamePlayController.getSpecialAbilityActivationController().runKillCardDeathWishes(defender, runKillAttacker);
            gamePlayController.killCard(defender);
        }
    }

    private boolean reduceHealth(int amount, GameBoard board, GamePlayController gamePlayController) {
        if (!gamePlayController.getGamePlay().getUniversalHistory().contains("noHealthReduction")) {
            board.changeHealth(amount);
            gamePlayController.getMyCommunicator().reduceHealth(amount * -1, false);
            gamePlayController.getOpponentCommunicator().reduceHealth(amount * -1, true);
            return true;
        } else return false;
    }

    private void activate() {
        place.getTemporaryFeatures().add(TEMPORARY_FEATURES.SPELL_ACTIVATED);
        if (place.getCard() instanceof SpellCards) {
            getLPIfSpellIsActivated(gamePlayController);
            getLPIfSpellIsActivated(gamePlayController.getGamePlay().getOpponentGamePlayController());
        }
        if (gamePlayController.isEquip(place.getCard())) {
            if (place.getAffect() != null)
                if (!place.getAffect().getTemporaryFeatures().contains(TEMPORARY_FEATURES.EQUIPPED))
                    place.setAffect(null);
            specialAbilityActivationController.activateEquip(place);
            if (place.getAffect() == null)
                gamePlayController.killCard(place);
        } else if (gamePlayController.isRitual(place)) {
            place.getCard().getSpecial().get(0).run(gamePlayController, place);
            gamePlayController.killCard(place);
        } else {
            specialAbilityActivationController.activateEffectWithChain(place);
            specialAbilityActivationController.activateEffectWithoutChain(place);
            specialAbilityActivationController.runContinuous(place);
            specialAbilityActivationController.runSuccessSpecialAbility(place);
            gamePlayController.killCard(place);
        }
    }

    private void getLPIfSpellIsActivated(GamePlayController gamePlayController) {
        for (String history : gamePlayController.getGamePlay().getUniversalHistory()) {
            if (history.startsWith("getLPIfSpellIsActivated")) {
                Matcher matcher = RegexController.getMatcher(history, RegexPatterns.extractEndingNumber);
                int amount = Integer.parseInt(matcher.group(1));
                gamePlayController.getGamePlay().getMyGameBoard().changeHealth(amount);
                gamePlayController.getMyCommunicator().reduceHealth(amount, false);
                gamePlayController.getOpponentCommunicator().reduceHealth(amount, true);
            }
        }
    }

    private void flipSummon(boolean runFlipSpecial) {
        place.removeTemporaryFeatures(TEMPORARY_FEATURES.CARD_POSITION_CHANGED_IN_THIS_TURN);
        place.addTemporaryFeatures(TEMPORARY_FEATURES.CARD_POSITION_CHANGED_IN_THIS_TURN);
        gamePlayController.flip(place, STATUS.ATTACK, runFlipSpecial, true, true);
    }

    private void checkIfTheGameEnded() {
        if (gamePlayController.getGamePlay().getMyGameBoard().getHealth() == 0 ||
                gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getMyGameBoard().getHealth() == 0) {
            gamePlayController.setGameOver(true);
            gamePlayController.getGamePlay().getOpponentGamePlayController().setGameOver(true);
        }
    }

    private void removeUniversalNoHealthReductionHistories() {
        gamePlayController.getGamePlay().getUniversalHistory().removeAll(TO_REMOVE);
        gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getUniversalHistory().removeAll(TO_REMOVE);
    }
}
