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
    public void run(GamePlayController gamePlayController, Place place) {
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

    public boolean getMet() {
        return met;
    }

    private void payLPForActivation() {
        printerAndScanner.printNextLine(printBuilderController.askForLpForActivation(amount).toString());
        if (printerAndScanner.scanNextLine().equals("yes")) {
            gamePlayController.getGamePlay().getMyGameBoard().changeHealth(amount * -1);
            met = true;
        } else
            met = false;
    }

    private void sacrificeCardFromHand() {
        met = false;
        printerAndScanner.printNextLine(askForPlace);
        String command;
        Place place;
        while (true) {
            command = printerAndScanner.scanNextLine();
            if (command.equals("cancel"))
                break;
            if (command.matches("[^012345]"))
                printerAndScanner.printNextLine(invalidInput);
            else {
                place = gamePlayController.getGamePlay().getMyGameBoard().getPlace(Integer.parseInt(command), PLACE_NAME.HAND);
                if (place.getCard() == null)
                    printerAndScanner.printNextLine(wrongCard);
                else {
                    gamePlayController.killCard(place);
                    met = true;
                    break;
                }
            }
        }
    }
}
