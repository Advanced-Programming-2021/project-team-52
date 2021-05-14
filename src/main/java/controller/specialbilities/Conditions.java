package controller.specialbilities;

import controller.GamePlayController;
import model.game.PLACE_NAME;
import model.game.Place;

import java.lang.reflect.Method;

public class Conditions implements SpecialAbility{

    private Method method;
    private String methodName;
    private GamePlayController gamePlayController;
    private Place place;
    private boolean met = false;
    private int amount;

    @Override
    public void run(GamePlayController gamePlayController, Place place){
        this.gamePlayController = gamePlayController;
        this.place = place;
        try {
            method.invoke(this);
        } catch (Exception e) {
            System.out.println("error : " + e);
        }
    }

    @Override
    public String getMethodName() {
        return methodName;
    }

    @Override
    public void setMethod(Method method) {
        this.methodName = method.getName();
        this.method = method;
    }

    public void setMet(boolean met) {
        this.met = met;
    }

    public boolean getMet(){
        return met;
    }

    private void graveYardIsNotEmpty(){
        met = !gamePlayController.getGamePlay().getMyGameBoard().getGraveyard().isEmpty() ||
                ! gamePlayController.getGamePlay().
                        getOpponentGamePlayController().getGamePlay().getMyGameBoard().getGraveyard().isEmpty();
    }

    private void thereIsMonsterInGraveYard(){
        for (int i = 1; i < 6; i++) {
            if (gamePlayController.getGamePlay().getMyGameBoard().getPlace(i, PLACE_NAME.MONSTER).getCard() != null) {
                met = true;
            }
        }
    }

    private void opponentHasMonster(){
        for (int i = 1; i < 6; i++) {
            if (gamePlayController.getGamePlay().getOpponentGamePlayController().
                    getGamePlay().getMyGameBoard().getPlace(i, PLACE_NAME.MONSTER) != null) {
                met = true;
                return;
            }
        }
        met = false;
    }

    private void handIsNotEmpty(){
        for (int i = 0; i < 6; i++) {
            if (gamePlayController.getGamePlay().getMyGameBoard().getPlace(i, PLACE_NAME.HAND).getCard() != null) {
                met = true;
                return;
            }
        }
        met = false;
    }

    private void opponentHasSpell(){
        for (int i = 1; i < 6; i++) {
            if (gamePlayController.getGamePlay().getOpponentGamePlayController().
                    getGamePlay().getMyGameBoard().getPlace(i, PLACE_NAME.SPELL_AND_TRAP) != null) {
                met = true;
                return;
            }
        }
        met = false;
    }

    private void monsterPlaceIsEmpty(){
        met = gamePlayController.getGamePlay().getMyGameBoard().getFirstEmptyPlace(PLACE_NAME.MONSTER) != -1;
    }

    private void playLPForActivation(){
        printerAndScanner.printNextLine(printBuilderController.askForLpForActivation(amount).toString());
        if (printerAndScanner.scanNextLine().equals("yes")){
            gamePlayController.getGamePlay().getMyGameBoard().changeHealth(amount * -1);
            met = true;
        } else
            met = false;
    }
}
