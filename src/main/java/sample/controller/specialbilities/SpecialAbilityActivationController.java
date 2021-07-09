package sample.controller.specialbilities;

import sample.controller.GamePlayController;
import sample.controller.PrintBuilderController;
import sample.controller.RegexController;
import sample.model.cards.Cards;
import sample.model.cards.spell.SpellCards;
import sample.model.game.*;
import sample.model.tools.RegexPatterns;
import sample.model.tools.StringMessages;
import sample.view.PrinterAndScanner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;

public class SpecialAbilityActivationController implements StringMessages {

    private static PrinterAndScanner printerAndScanner;
    private static PrintBuilderController printBuilderController;

    private GamePlayController gamePlayController;

    static {
        printerAndScanner = PrinterAndScanner.getInstance();
        printBuilderController = PrintBuilderController.getInstance();
    }

    public SpecialAbilityActivationController(GamePlayController gamePlayController) {
        this.gamePlayController = gamePlayController;
    }

    public void runKillCardDeathWishes(Place place, boolean killAttacker) {
        for (SpecialAbility specialAbility : place.getCard().getSpecial()) {
            if (specialAbility instanceof DeathWish)
                if ((specialAbility.getMethodName().equals("killAttacker") && killAttacker) ||
                        specialAbility.getMethodName().equals("killDestroyer"))
                    specialAbility.run(gamePlayController, place);
        }
    }

    public void deathWishWithoutKillCard(Place place) {
        for (SpecialAbility specialAbility : place.getCard().getSpecial()) {
            if (specialAbility instanceof DeathWish)
                if (!specialAbility.getMethodName().equals("killAttacker") &&
                        !specialAbility.getMethodName().equals("killDestroyer"))
                    specialAbility.run(gamePlayController, place);
        }
    }

    public boolean summonOrSetWithTribute(Place place, boolean summon) {
        ArrayList<String> specials = new ArrayList<>();
        for (SpecialAbility specialAbility : place.getCard().getSpecial()) {
            if (specialAbility instanceof Tribute) {
                specials.add(specialAbility.getMethodName());
                ((Tribute) specialAbility).setStatus(summon ? STATUS.ATTACK : STATUS.DEFENCE);
            }
        }
        if (specials.contains("canSummonNormally") && checkForConditions(place)) {
            printerAndScanner.printNextLine(summonWithoutTribute);
            if (printerAndScanner.scanNextLine().equals("yes")) {
                for (SpecialAbility specialAbility : place.getCard().getSpecial()) {
                    if (specialAbility.getMethodName().equals("canSummonNormally")) {
                        specialAbility.run(gamePlayController, gamePlayController.getGamePlay().getSelectedCard());
                        break;
                    }
                }
            } else runTributeSummon(place);
        } else runTributeSummon(place);
        return specials.size() == 0;
    }

    private void runTributeSummon(Place place) {
        ArrayList<SpecialAbility> specials = place.getCard().getSpecial();
        for (int i = 0; i < specials.size(); i++) {
            if (specials.get(i).getMethodName().equals("summonWithTribute")) {
                specials.get(i).run(gamePlayController, place);
                break;
            }
        }
    }

    public boolean hasTributeMethod(Cards card) {
        for (SpecialAbility specialAbility : card.getSpecial()) {
            if (specialAbility instanceof Tribute)
                return true;
        }
        return false;
    }

    public void runFlipSpecial(Place place) {
        for (SpecialAbility specialAbility : place.getCard().getSpecial()) {
            if (specialAbility instanceof Flip)
                specialAbility.run(gamePlayController, place);
        }
    }

    public boolean checkForConditions(Place place) {
        ArrayList<Conditions> conditions = new ArrayList<>();
        for (SpecialAbility specialAbility : place.getCard().getSpecial()) {
            if (specialAbility instanceof Conditions)
                conditions.add((Conditions) specialAbility);
        }
        boolean met = true;
        for (Conditions condition : conditions) {
            condition.setMet(met);
            condition.run(gamePlayController, place);
            met = met && condition.getMet();
        }
        return met;
    }

    public void activateField() {
        Place myField = gamePlayController.getGamePlay().getMyGameBoard().getPlace(0, PLACE_NAME.FIELD);
        if (myField.getCard() != null && myField.getStatus() != STATUS.SET) {
            doField(myField, false);
        }
        Place enemyField = gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().
                getMyGameBoard().getPlace(0, PLACE_NAME.FIELD);
        if (enemyField.getCard() != null && enemyField.getStatus() != STATUS.SET) {
            doField(enemyField, false);
        }
    }

    public void deactivateField() {
        Place fieldToDeactivate = gamePlayController.getGamePlay().getMyGameBoard().getPlace(0, PLACE_NAME.FIELD);
        doField(fieldToDeactivate, true);
    }

    private void doField(Place field, boolean onDeath) {
        for (SpecialAbility specialAbility : field.getCard().getSpecial()) {
            if (specialAbility instanceof FieldSpecial) {
                ((FieldSpecial) specialAbility).setOnDeath(onDeath);
                specialAbility.run(gamePlayController, field);
            }
        }
    }

    public void runFacUpSpecial(Place place) {
        for (SpecialAbility specialAbility : place.getCard().getSpecial()) {
            if (specialAbility instanceof FaceUp)
                if (!specialAbility.getMethodName().equals("summonAMonster"))
                    specialAbility.run(gamePlayController, place);
        }
    }

    public void runContinuous(Place place) {
        if (!place.getTemporaryFeatures().contains(TEMPORARY_FEATURES.CONTINUOUS_ACTIVATED)) {
            for (SpecialAbility specialAbility : place.getCard().getSpecial()) {
                if (specialAbility instanceof Continuous)
                    specialAbility.run(gamePlayController, place);
            }
            place.addTemporaryFeatures(TEMPORARY_FEATURES.CONTINUOUS_ACTIVATED);
        }
    }

    public void activateEffectWithoutChain(Place place) {
        for (SpecialAbility specialAbility : place.getCard().getSpecial()) {
            if (specialAbility instanceof ActivateNoChain)
                specialAbility.run(gamePlayController, place);
        }
    }

    public void activateEffectWithChain(Place place) {
        for (SpecialAbility specialAbility : place.getCard().getSpecial()) {
            if (specialAbility instanceof ActivateChain)
                specialAbility.run(gamePlayController, place);
        }
    }

    public void removeMonsterFromEffect(Place place) {
//        Place field = gamePlayController.getGamePlay().getMyGameBoard().getPlace(0, PLACE_NAME.FIELD);
//        if (field.getCard() != null) {
//            ((Field) field).removeFromAffect(place);
//        }
        GameBoard myGameBoard = gamePlayController.getGamePlay().getMyGameBoard();
        GameBoard opponentGameBoard = gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getMyGameBoard();
        for (int i = 1; i < 6; i++) {
            checkEquip(place, myGameBoard.getPlace(i, PLACE_NAME.SPELL_AND_TRAP));
            checkEquip(place, opponentGameBoard.getPlace(i, PLACE_NAME.SPELL_AND_TRAP));
        }
    }

    private void checkEquip(Place place, Place equipCheck) {
        if (equipCheck.getCard() != null)
            if (equipCheck.getAffect() == place)
                if (equipCheck.getCard() instanceof SpellCards)
                    if (((SpellCards) equipCheck.getCard()).getIcon().equals("Equip"))
                        gamePlayController.killCard(equipCheck);
    }

    public void stopControl(String command) {
        GamePlayController opponentGamePlayController = gamePlayController.getGamePlay().getOpponentGamePlayController();
        Matcher matcher = RegexController.getMatcher(command, RegexPatterns.extractEndingNumber);
        Place opponent = opponentGamePlayController.getGamePlay().
                getMyGameBoard().getPlace(Integer.parseInt(matcher.group(1)), PLACE_NAME.MONSTER);
        if (opponentGamePlayController.getGamePlay().getHistory().get(opponent).contains("forEnemy")) {
            int myEmptyPlace = gamePlayController.getGamePlay().getMyGameBoard().getFirstEmptyPlace(PLACE_NAME.MONSTER);
            if (myEmptyPlace != -1) {
                Place toPlace = gamePlayController.getGamePlay().getMyGameBoard().getPlace(myEmptyPlace, PLACE_NAME.MONSTER);
                toPlace.setCard(opponent.getCard());
                toPlace.setStatus(opponent.getStatus());
            }
            opponentGamePlayController.killCard(opponent);
        }
    }

    public void handleScanner(Place place, PHASE phase) {
        STATUS status = place.getStatus();
        gamePlayController.killCard(place);
        place.setCard(Cards.getCard("Scanner"));
        place.setStatus(status);
        if (phase == PHASE.DRAW) {
            printerAndScanner.printNextLine(askActivateScanner);
            if (printerAndScanner.scanNextLine().equals("yes")) {
                place.getCard().getSpecial().get(0).run(gamePlayController, place);
                runFlipSpecial(place);
                runFacUpSpecial(place);
            }
        }
        gamePlayController.getGamePlay().getHistory().get(place).remove("scanner");
        gamePlayController.getGamePlay().getHistory().get(place).add("scanner");
    }

    public boolean getHealthOrDestroyCard(Place place, String command) {
        Matcher matcher = RegexController.getMatcher(command, RegexPatterns.extractEndingNumber);
        printerAndScanner.printString(printBuilderController.askForPayingLp(matcher.group(1), place.getCard().getName()));
        if (printerAndScanner.scanNextLine().equals("yes")) {
            gamePlayController.getGamePlay().getMyGameBoard().changeHealth(Integer.parseInt(matcher.group(1)) * -1);
            return false;
        } else {
            place.getTemporaryFeatures().add(TEMPORARY_FEATURES.FORCE_KILL);
            gamePlayController.killCard(place);
            return true;
        }
    }

    public void runSuccessSpecialAbility(Place place) {
        if (place.getCard() != null)
            for (SpecialAbility specialAbility : place.getCard().getSpecial()) {
                if (specialAbility instanceof Success)
                    specialAbility.run(gamePlayController, place);
            }
    }

    public void runAttackSpecial(Place place) {
        for (SpecialAbility specialAbility : place.getCard().getSpecial()) {
            if (specialAbility instanceof AttackSpecial) {
                if (!specialAbility.getMethodName().equals("reduceAttackerLPIfItWasFacingDown")) {
                    specialAbility.run(gamePlayController, place);
                    runSuccessSpecialAbility(place);
                }
            }
        }
    }

    public void runAttackAmountByQuantifier() {
        Place place;
        for (int i = 1; i < 6; i++) {
            place = gamePlayController.getGamePlay().getMyGameBoard().getPlace(i, PLACE_NAME.MONSTER);
            if (place.getCard() != null)
                for (SpecialAbility specialAbility : place.getCard().getSpecial()) {
                    if (specialAbility.getMethodName().equals("attackAmountByQuantifier")) {
                        specialAbility.run(gamePlayController, place);
                        break;
                    }
                }
        }
    }

    public void checkSummonAMonsterUponNormalSummon(Place place) {
        for (SpecialAbility specialAbility : place.getCard().getSpecial()) {
            if (specialAbility.getMethodName().equals("summonAMonster")) {
                specialAbility.run(gamePlayController, place);
                break;
            }
        }
    }

    public void activateEquip(Place place) {
        for (SpecialAbility specialAbility : place.getCard().getSpecial()) {
            if (specialAbility instanceof Equip) {
                specialAbility.run(gamePlayController, place);
                break;
            }
        }
    }

    public void dynamicEquipHandler(Place place, int code) {
        for (SpecialAbility specialAbility : place.getCard().getSpecial()) {
            if (specialAbility instanceof Equip) {
                if (specialAbility.getMethodName().equals("dynamicEquip")) {
                    ((Equip) specialAbility).setQuantifier(code);
                    specialAbility.run(gamePlayController, place);
                }
                break;
            }
        }
    }

    public void equipByControlledMonstersHandler() {
        for (int i = 1; i < 6; i++) {
            MonsterZone monsterZone =
                    (MonsterZone) gamePlayController.getGamePlay().getMyGameBoard().getPlace(i, PLACE_NAME.MONSTER);
            if (monsterZone.getTemporaryFeatures().contains(TEMPORARY_FEATURES.EQUIPPED))
                checkForMultipliedByMonsterEquip(monsterZone);
        }
    }

    private void checkForMultipliedByMonsterEquip(MonsterZone monsterZone) {
        for (int i = 1; i < 6; i++) {
            Place spell = gamePlayController.getGamePlay().getMyGameBoard().getPlace(i, PLACE_NAME.SPELL_AND_TRAP);
            if (spell.getAffect() == monsterZone) {
                for (SpecialAbility specialAbility : spell.getCard().getSpecial()) {
                    if (specialAbility.getMethodName().equals("boostByControlledMonsters")) {
                        specialAbility.run(gamePlayController, spell);
                        break;
                    }
                }
            }
        }
    }

    public boolean runUponActivation(Place place) {
        for (SpecialAbility specialAbility : place.getCard().getSpecial()) {
            if (specialAbility instanceof UponActivation) {
                specialAbility.run(gamePlayController, place);
                return ((UponActivation) specialAbility).getMet();
            }
        }
        return true;
    }

    public void runReduceAttackerLpIfItWasFacingDown(Place place) {
        if (place.getCard() != null)
            for (SpecialAbility specialAbility : place.getCard().getSpecial()) {
                if (specialAbility instanceof AttackSpecial)
                    if (specialAbility.getMethodName().equals("reduceAttackerLPIfItWasFacingDown")) {
                        specialAbility.run(gamePlayController, place);
                        break;
                    }
            }
    }

    public void deactivateEquip(Place place) {
        if (place.getCard() != null)
            for (SpecialAbility specialAbility : place.getCard().getSpecial()) {
                if (specialAbility instanceof Equip) {
                    ((Equip) specialAbility).setOnDeath(true);
                    specialAbility.run(gamePlayController, place);
                    ((Equip) specialAbility).setOnDeath(false);
                }
            }
    }

    public void handleMonstersThatCanBeActivated() {
        Place place;
        GameBoard gameBoard = gamePlayController.getGamePlay().getMyGameBoard();
        HashMap<Place, ArrayList<String>> history = gamePlayController.getGamePlay().getHistory();
        for (int i = 1; i < 6; i++) {
            place = gameBoard.getPlace(i, PLACE_NAME.MONSTER);
            if (place.getCard() != null)
                if (history.get(place).contains("canBeActivated") && checkForConditions(place)) {
                    printerAndScanner.printNextLine(printBuilderController.askActivateMonster(place.getCard().getName()));
                    if (printerAndScanner.scanNextLine().equals("yes"))
                        if (runUponActivation(place)) {
                            activateEffectWithChain(place);
                            activateEffectWithoutChain(place);
                        }
                }
        }
    }
}
