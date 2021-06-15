package controller.specialbilities;

import controller.GamePlayController;
import model.game.PLACE_NAME;
import model.game.Place;
import model.tools.StringMessages;

import java.lang.reflect.Method;

public class UponActivation implements SpecialAbility, StringMessages {

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
            e.printStackTrace();
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

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public boolean getMet(){
        return met;
    }

    private void payLPForActivation(){ //TODO ++
        printerAndScanner.printNextLine(printBuilderController.askForLpForActivation(amount).toString());
        if (printerAndScanner.scanNextLine().equals("yes")){
            gamePlayController.getGamePlay().getMyGameBoard().changeHealth(amount * -1);
            met = true;
        } else
            met = false;
    }

    private void sacrificeCardFromHand(){
        printerAndScanner.printNextLine(askForPlace);
        int toRemove = printerAndScanner.scanNextInt();
        while (gamePlayController.getGamePlay().getMyGameBoard().getPlace(toRemove, PLACE_NAME.HAND) == null){
            printerAndScanner.printNextLine(wrongCard);
            toRemove = printerAndScanner.scanNextInt();
        }
        gamePlayController.killCard(gamePlayController.getGamePlay().getMyGameBoard().getPlace(toRemove, PLACE_NAME.HAND));
    }
}
