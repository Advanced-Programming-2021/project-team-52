package controller.specialbilities;

import controller.NewChain;
import controller.GamePlayController;
import controller.PrintBuilderController;
import controller.RegexController;
import model.cards.Cards;
import model.cards.spell.SpellCards;
import model.cards.trap.TrapCards;
import model.game.*;
import model.tools.RegexPatterns;
import model.tools.StringMessages;
import view.PrinterAndScanner;

import java.util.ArrayList;
import java.util.regex.Matcher;

public class SpecialAbilityActivationController implements StringMessages {

    private static SpecialAbilityActivationController specialAbilityActivationController = null;

    private GamePlayController gamePlayController;
    private PrinterAndScanner printerAndScanner;
    private PrintBuilderController printBuilderController;

    private SpecialAbilityActivationController() {
        printerAndScanner = PrinterAndScanner.getInstance();
        printBuilderController = PrintBuilderController.getInstance();
    }

    public static SpecialAbilityActivationController getInstance() {
        if (specialAbilityActivationController == null)
            specialAbilityActivationController = new SpecialAbilityActivationController();
        return specialAbilityActivationController;
    }

    public void setGamePlayController(GamePlayController gamePlayController) {
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

    public boolean summonWithTribute(Place place) {
        ArrayList<String> specials = new ArrayList<>();
        for (SpecialAbility specialAbility : place.getCard().getSpecial()) {
            if (specialAbility instanceof Tribute)
                specials.add(specialAbility.getMethodName());
        }
        if (specials.contains("canSummonNormally")) {
            printerAndScanner.printNextLine(summonWithoutTribute);
            if (printerAndScanner.scanNextLine().equals("yes")) {
                for (SpecialAbility specialAbility : place.getCard().getSpecial()) {
                    if (specialAbility.getMethodName().equals("canSummonNormally")) {
                        specialAbility.run(gamePlayController, gamePlayController.getGamePlay().getSelectedCard());
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
//                for (int j = i + 1; j < specials.size(); j++) {
//                    if (specials.get(j) instanceof Success) {
//                        specials.get(j).run(gamePlayController, place);
//                        break;
//                    }
//                }
                runSuccessSpecialAbility(place);
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
        if (myField.getCard() != null) {
            doField(myField, false);
        }
        Place enemyField = gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().
                getMyGameBoard().getPlace(0, PLACE_NAME.FIELD);
        if (enemyField.getCard() != null) {
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
        runSuccessSpecialAbility(place);
    }

    public void activateEffectWithoutChain(Place place) {
        for (SpecialAbility specialAbility : place.getCard().getSpecial()) {
            if ((specialAbility instanceof Continuous) || (specialAbility instanceof ActivateNoChain))
                specialAbility.run(gamePlayController, place);
        }
    }

    public void activateEffectWithChain(Place place) {
        for (SpecialAbility specialAbility : place.getCard().getSpecial()) {
            if (specialAbility instanceof ActivateChain)
                specialAbility.run(gamePlayController, place);
        }
    }

    public void removeMonsterFromFieldAndEffect(Place place) {
        Place field = gamePlayController.getGamePlay().getMyGameBoard().getPlace(0, PLACE_NAME.FIELD);
        if (field.getCard() != null) {
            ((Field) field).removeFromAffect(place);
        }
        Place effectCheck;
        for (int i = 1; i < 6; i++) {
            effectCheck = gamePlayController.getGamePlay().getMyGameBoard().getPlace(i, PLACE_NAME.SPELL_AND_TRAP);
            if (effectCheck.getCard() != null)
                if (effectCheck.getAffect() == place)
                    gamePlayController.killCard(effectCheck);

        }
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

    public void handleScanner(Place place) {
        STATUS status = place.getStatus();
        gamePlayController.killCard(place);
        place.setCard(Cards.getCard("Scanner"));
        place.setStatus(status);
        printerAndScanner.printNextLine(askActivateScanner);
        if (printerAndScanner.scanNextLine().equals("yes")) {
            place.getCard().getSpecial().get(0).run(gamePlayController, place);
            runFlipSpecial(place);
            runFacUpSpecial(place);
        }
    }

    public boolean getHealthOrDestroyCard(Place place, String command) {
        Matcher matcher = RegexController.getMatcher(command, RegexPatterns.extractEndingNumber);
        printerAndScanner.printString(printBuilderController.askForPayingLp(matcher.group(1), place.getCard().getName()));
        if (printerAndScanner.scanNextLine().equals("yes")) {
            gamePlayController.getGamePlay().getMyGameBoard().changeHealth(Integer.parseInt(matcher.group(1)) * -1);
            return false;
        } else {
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

//    public void checkSummonDeactivation(Place placeToAffect) {
//        ArrayList<Place> doAble = new ArrayList<>();
//        for (int i = 1; i < 6; i++) {
//            Place candidate = gamePlayController.getGamePlay().getOpponentGamePlayController().
//                    getGamePlay().getMyGameBoard().getPlace(i, PLACE_NAME.SPELL_AND_TRAP);
//            if (placeToAffect.getCard() != null) {
//                if (candidate.getCard().getSpecialSpeed() == 3) {
//                    for (SpecialAbility specialAbility : candidate.getCard().getSpecial()) {
//                        if (specialAbility.getMethodName().equals("killThisCardUponSummon"))
//                            doAble.add(candidate);
//                    }
//                }
//            }
//        }
//        if (!doAble.isEmpty()) {
//            printerAndScanner.printNextLine(askActivateSpecial);
//            if (printerAndScanner.scanNextLine().equals("yes")) {
//                Place place;
//                while (true) {
//                    printerAndScanner.printNextLine(cardNumber);
//                    int placeNumber = printerAndScanner.scanNextInt();
//                    if (placeNumber > 5 || placeNumber < 1)
//                        continue;
//                    place = gamePlayController.getGamePlay().getOpponentGamePlayController().
//                            getGamePlay().getMyGameBoard().getPlace(placeNumber, PLACE_NAME.SPELL_AND_TRAP);
//                    if (!doAble.contains(place))
//                        continue;
//                    else break;
//                }
//                place.setAffect(placeToAffect);
//                new Chain(gamePlayController.getGamePlay().getOpponentGamePlayController(), place,
//                        place.getCard().getSpecialSpeed(), false);
//            }
//        }
//    }

//    public boolean spellAndTrapActivationChecker(Place place, boolean shouldChain, int previousSpeed, PHASE phase) {
////        if (selectedCard != null)
////            if (gamePlay.getMyGameBoard().fromThisGameBoard(selectedCard))
////                if (selectedCard.getCard() instanceof SpellCards)
////                    if (phase != PHASE.MAIN) { //TODO ?
////                            if ((selectedCard.getType() == PLACE_NAME.HAND &&
////                                    (gamePlay.getMyGameBoard().getFirstEmptyPlace(PLACE_NAME.SPELL_AND_TRAP) != -1 ||
////                                            selectedCard.getCard().getType().equals("Field"))) ||
////                                    selectedCard.getType() == PLACE_NAME.SPELL_AND_TRAP ){
//        if (place != null) {
//            if (place.getCard() != null) {
//                if (gamePlayController.getGamePlay().getMyGameBoard().fromThisGameBoard(place)) {
//                    if (phase == PHASE.MAIN || phase == PHASE.CHAIN)
//                        if (!gamePlayController.getGamePlay().getHistory().get(place).contains("noSpecialThisRound"))
//                            if (place.getCard() instanceof SpellCards || (place.getCard() instanceof TrapCards &&
//                                    !gamePlayController.getGamePlay().getUniversalHistory().contains("cannotActivateTrap")))
//                                if (!place.getTemporaryFeatures().contains(TEMPORARY_FEATURES.SPELL_ACTIVATED))
//                                    if (checkForConditions(place))
//                                        return true;
//                } else printerAndScanner.printNextLine(cantActivateThisCard);
//            } else printerAndScanner.printNextLine(noCardsIsSelectedYet);
//        } else printerAndScanner.printNextLine(noCardsIsSelectedYet);
//        return false;
//    }

    public boolean canActivateSpellOrTrap(int speedToCheck){
        for (int i = 1; i < 6; i++) {
            Place place = gamePlayController.getGamePlay().getMyGameBoard().getPlace(i, PLACE_NAME.SPELL_AND_TRAP);
            if (place.getCard() != null)
                if (place.getCard() instanceof SpellCards)
                    if (canActivateSpell(place, speedToCheck))
                        return true;
        }
        return false;
    }

    public boolean canActivateSpell(Place place, int speedToCheck){
        if (place.getCard().getSpecialSpeed() >= speedToCheck)
            if (!place.getTemporaryFeatures().contains(TEMPORARY_FEATURES.SPELL_ACTIVATED))
                if (checkForConditions(place))
                    return true;
        return false;
    }

    public void runAttackSpecial(Place place, boolean defenderWasHidden){

        for (SpecialAbility specialAbility : place.getCard().getSpecial()) {
            if (specialAbility instanceof AttackSpecial) {
                if (specialAbility.getMethodName().equals("reduceAttackerLPIfItWasFacingDown")) {
                    if (defenderWasHidden)
                        specialAbility.run(gamePlayController, place);
                } else specialAbility.run(gamePlayController, place);
                runSuccessSpecialAbility(place);
            }
        }
    }

    public void runAttackAmountByQuantifier(){
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

    public void checkSummonAMonsterUponNormalSummon(Place place){
        for (SpecialAbility specialAbility : place.getCard().getSpecial()) {
            if (specialAbility.getMethodName().equals("summonAMonster")){
                specialAbility.run(gamePlayController, place);
                break;
            }
        }
    }

    public void activateEquip(Place place){
        for (SpecialAbility specialAbility : place.getCard().getSpecial()) {
            if (specialAbility instanceof Equip) {
                specialAbility.run(gamePlayController, place);
                break;
            }
        }
    }

    public void dynamicEquipHandler(Place place, int code){
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

    public void equipByControlledMonstersHandler(){ //TODO add to where the cards are being place and are facing upward
        for (int i = 1; i < 6; i++) {
            MonsterZone monsterZone =
                    (MonsterZone) gamePlayController.getGamePlay().getMyGameBoard().getPlace(i, PLACE_NAME.MONSTER);
            if (monsterZone.getTemporaryFeatures().contains(TEMPORARY_FEATURES.EQUIPPED))
                checkForMultipliedByMonsterEquip(monsterZone);
        }
    }

    private void checkForMultipliedByMonsterEquip(MonsterZone monsterZone){
        for (int i = 1; i < 6; i++) {
            Place spell = gamePlayController.getGamePlay().getMyGameBoard().getPlace(i, PLACE_NAME.SPELL_AND_TRAP);
            if (spell.getAffect() == monsterZone){
                for (SpecialAbility specialAbility : spell.getCard().getSpecial()) {
                    if (specialAbility.getMethodName().equals("boostByControlledMonsters")) {
                        specialAbility.run(gamePlayController, spell);
                        break;
                    }
                }
            }
        }
    }

    public boolean checkTrapForSummonAndFlipSummon(Place place){
        Place spell;
        for (int i = 1; i < 6; i++) {
            spell = gamePlayController.getGamePlay().getOpponentGamePlayController().
                    getGamePlay().getMyGameBoard().getPlace(i, PLACE_NAME.SPELL_AND_TRAP);
            if (spell.getCard() != null){
                for (SpecialAbility specialAbility : spell.getCard().getSpecial()) {
                    if (specialAbility.getMethodName().equals("checkBeforeKillingAMonsterInFlipSummonOrSummon")){
                        spell.setAffect(place);
                        specialAbility.run(gamePlayController.getGamePlay().getOpponentGamePlayController(), place);
                        return (((Conditions) specialAbility).getMet());
                    }
                }
            }
        }
        return false;
    }

    public boolean runUponActivation(Place place){
        for (SpecialAbility specialAbility : place.getCard().getSpecial()) {
            if (specialAbility instanceof UponActivation) {
                specialAbility.run(gamePlayController, place);
                return ((UponActivation) specialAbility).getMet();
            }
        }
        return true;
    }
}
