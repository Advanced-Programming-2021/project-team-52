package controller.specialbilities;

import controller.Chain;
import controller.GamePlayController;
import controller.PrintBuilderController;
import controller.RegexController;
import model.cards.Cards;
import model.game.Field;
import model.game.PLACE_NAME;
import model.game.Place;
import model.game.STATUS;
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

    private SpecialAbilityActivationController(){
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

    public void runKillCardDeathWishes(Place place, boolean killAttacker){
        for (SpecialAbility specialAbility : place.getCard().getSpecial()) {
            if (specialAbility instanceof DeathWish)
                if ((specialAbility.getMethodName().equals("killAttacker") && killAttacker) ||
                        specialAbility.getMethodName().equals("killDestroyer"))
                    specialAbility.run(gamePlayController, place);
        }
    }

    public void deathWishWithoutKillCard(Place place){
        for (SpecialAbility specialAbility : place.getCard().getSpecial()) {
            if (specialAbility instanceof DeathWish)
                if (!specialAbility.getMethodName().equals("killAttacker") &&
                        !specialAbility.getMethodName().equals("killDestroyer"))
                    specialAbility.run(gamePlayController, place);
        }
    }

    public boolean summonWithTribute(Place place){
        ArrayList<String> specials = new ArrayList<>();
        for (SpecialAbility specialAbility : place.getCard().getSpecial()) {
            if (specialAbility instanceof Tribute)
                specials.add(specialAbility.getMethodName());
        }
        if (specials.contains("canSummonNormally")){
            printerAndScanner.printNextLine(summonWithoutTribute);
            if (printerAndScanner.scanNextLine().equals("yes")){
                for (SpecialAbility specialAbility : place.getCard().getSpecial()) {
                    if (specialAbility.getMethodName().equals("canSummonNormally")){
                        specialAbility.run(gamePlayController, gamePlayController.getGamePlay().getSelectedCard());
                    }
                }
            } else runTributeSummon(place);
        } else runTributeSummon(place);
        return specials.size() == 0;
    }

    private void runTributeSummon(Place place){
        ArrayList<SpecialAbility> specials = place.getCard().getSpecial();
        for (int i = 0; i < specials.size(); i++) {
            if (specials.get(i).getMethodName().equals("summonWithTribute")){
                specials.get(i).run(gamePlayController, place);
                for (int j = i+1; j < specials.size(); j++) {
                    if (specials.get(j) instanceof Success) {
                        specials.get(j).run(gamePlayController, place);
                        break;
                    }
                }
                break;
            }
        }
    }

    public boolean hasTributeMethod(Cards card){
        for (SpecialAbility specialAbility : card.getSpecial()) {
            if (specialAbility instanceof Tribute)
                return true;
        }
        return false;
    }

    public void runFlipSpecial(Place place){
        for (SpecialAbility specialAbility : place.getCard().getSpecial()) {
            if (specialAbility instanceof Flip)
                specialAbility.run(gamePlayController, place);
        }
    }

    public boolean checkForConditions(Place place){
        ArrayList<Conditions> conditions = new ArrayList<>();
        for (SpecialAbility specialAbility : place.getCard().getSpecial()) {
            if (specialAbility instanceof Conditions)
                conditions.add((Conditions) specialAbility);
        }
        boolean met = conditions.isEmpty();
        for (Conditions condition : conditions) {
            condition.setMet(met);
            condition.run(gamePlayController, place);
            met = condition.getMet();
        }
        return met;
    }

    public void activateField(){
        Place place = gamePlayController.getGamePlay().getMyGameBoard().getPlace(0, PLACE_NAME.FIELD);
        if (place instanceof Field){
            Field field = (Field) place;
            for (SpecialAbility specialAbility : field.getCard().getSpecial()) {
                if (specialAbility instanceof FieldSpecial){
                    ((FieldSpecial) specialAbility).setAffected(field.getAffected());
                    specialAbility.run(gamePlayController, place);
                }
            }
        }
    }

    public void runFacUpSpecial(Place place){
        for (SpecialAbility specialAbility : place.getCard().getSpecial()) {
            if (specialAbility instanceof FaceUp)
                specialAbility.run(gamePlayController, place);
        }
    }

    public void activateEffectWithoutChain(Place place){
        for (SpecialAbility specialAbility : place.getCard().getSpecial()) {
            if ((specialAbility instanceof Continuous) || (specialAbility instanceof ActivateNoChain))
                specialAbility.run(gamePlayController, place);
        }
    }

    public void removeMonsterFromFieldAndEffect(Place place){
        Place field = gamePlayController.getGamePlay().getMyGameBoard().getPlace(0, PLACE_NAME.FIELD);
        if (field != null){
            ((Field) field).removeFromAffect(place);
        }
        //TODO remove from equip
    }

    public void stopControl(String command){
        GamePlayController opponentGamePlayController = gamePlayController.getGamePlay().getOpponentGamePlayController();
        Matcher matcher = RegexController.getMatcher(command, RegexPatterns.extractEndingNumber);
        Place opponent = opponentGamePlayController.getGamePlay().
                getMyGameBoard().getPlace(Integer.parseInt(matcher.group(1)), PLACE_NAME.MONSTER);
        if (opponentGamePlayController.getGamePlay().getHistory().get(opponent).contains("forEnemy")){
            int myEmptyPlace = gamePlayController.getGamePlay().getMyGameBoard().getFirstEmptyPlace(PLACE_NAME.MONSTER);
            if (myEmptyPlace != -1){
                Place toPlace = gamePlayController.getGamePlay().getMyGameBoard().getPlace(myEmptyPlace, PLACE_NAME.MONSTER);
                toPlace.setCard(opponent.getCard());
                toPlace.setStatus(opponent.getStatus());
            }
           opponentGamePlayController.getGamePlay().getMyGameBoard().killCards(opponentGamePlayController, opponent);
        }
    }

    public void handleScanner(Place place){
        gamePlayController.getGamePlay().getMyGameBoard().killCards(gamePlayController, place);
        place.setCard(Cards.getCard("Scanner"));
        place.setStatus(STATUS.ATTACK);
        printerAndScanner.printNextLine(askActivateScanner);
        if (printerAndScanner.scanNextLine().equals("yes")){
            place.getCard().getSpecial().get(0).run(gamePlayController, place);
            runFlipSpecial(place);
            runFacUpSpecial(place);
        }
    }

    public boolean getHealthOrDestroyCard(Place place, String command){
        Matcher matcher = RegexController.getMatcher(command, RegexPatterns.extractEndingNumber);
        printerAndScanner.printString(printBuilderController.askForPayingLp(matcher.group(1), place.getCard().getName()));
        if (printerAndScanner.scanNextLine().equals("yes")){
            gamePlayController.getGamePlay().getMyGameBoard().changeHealth(Integer.parseInt(matcher.group(1)) * -1);
            return false;
        } else {
            gamePlayController.getGamePlay().getMyGameBoard().killCards(gamePlayController, place);
            return true;
        }
    }

    public void runSuccessSpecialAbility(Place place){
        for (SpecialAbility specialAbility : place.getCard().getSpecial()) {
            if (specialAbility instanceof Success)
                specialAbility.run(gamePlayController, place);
        }
    }

    public void checkSummonDeactivation(Place placeToAfffect){
        ArrayList<Place> doAble = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            Place candidate = gamePlayController.getGamePlay().getOpponentGamePlayController().
                    getGamePlay().getMyGameBoard().getPlace(i, PLACE_NAME.SPELL_AND_TRAP);
            if (placeToAfffect.getCard() != null){
                if (candidate.getCard().getSpecialSpeed() == 3) {
                    for (SpecialAbility specialAbility : candidate.getCard().getSpecial()) {
                        if (specialAbility.getMethodName().equals("killThisCardUponSummon"))
                            doAble.add(candidate);
                    }
                }
            }
        }
        if (!doAble.isEmpty()){
            printerAndScanner.printNextLine(askActivateSpecial);
            if (printerAndScanner.scanNextLine().equals("yes")){
                Place place;
                while (true){
                    printerAndScanner.printNextLine(cardNumber);
                    int placeNumber = printerAndScanner.scanNextInt();
                    if (placeNumber > 5 || placeNumber < 1)
                        continue;
                    place = gamePlayController.getGamePlay().getOpponentGamePlayController().
                            getGamePlay().getMyGameBoard().getPlace(placeNumber, PLACE_NAME.SPELL_AND_TRAP);
                    if (!doAble.contains(place))
                        continue;
                    else break;
                }
                place.setAffect(placeToAfffect);
                new Chain(gamePlayController.getGamePlay().getOpponentGamePlayController(), place,
                        place.getCard().getSpecialSpeed(), false);
            }
        }
    }
}
