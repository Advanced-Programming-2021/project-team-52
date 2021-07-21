package sample.controller.specialbilities;

import sample.controller.GamePlayController;
import sample.model.game.PLACE_NAME;
import sample.model.game.Place;
import sample.model.tools.StringMessages;

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
        gamePlayController.getMyCommunicator().askOptions(
                printBuilderController.askForLpForActivation(amount).toString(), "yes","no");
        if (printerAndScanner.scanNextLine().equals("yes")) {
            gamePlayController.getGamePlay().getMyGameBoard().changeHealth(amount * -1);
            met = true;
        } else
            met = false;
    }

    private void sacrificeCardFromHand() {
        met = false;
        gamePlayController.getMyCommunicator().askOptions(askForPlace, "ok");
        gamePlayController.takeCommand();
        String command;
        Place place;
        while (true) {
            gamePlayController.getMyCommunicator().selectCard("hand", true, false, false);
            command = gamePlayController.takeCommand();
            if (command.equals("cancel"))
                break;
            if (!command.matches("^[012345]$"))
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
