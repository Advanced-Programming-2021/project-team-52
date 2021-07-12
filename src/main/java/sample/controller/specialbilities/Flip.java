package sample.controller.specialbilities;

import sample.controller.GamePlayController;
import sample.model.game.PLACE_NAME;
import sample.model.game.Place;
import sample.model.tools.StringMessages;

import java.lang.reflect.Method;

public class Flip implements SpecialAbility, StringMessages {

    private Method method;
    private String methodName;
    private GamePlayController gamePlayController;
    private Place place;

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
    public void setMethod(Method method) {
        this.methodName = method.getName();
        this.method = method;
    }

    @Override
    public String getMethodName() {
        return methodName;
    }

    public void destroyAMonster() {
        int i;
        for (i = 1; i < 6; i++) {
            if (gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getMyGameBoard()
                    .getPlace(i, PLACE_NAME.MONSTER).getCard() != null)
                break;
        }
        if (i == 6)
            return;
        gamePlayController.getMyCommunicator().askOptions(askActivateSpecial, "yes", "no");
        if (gamePlayController.takeCommand().equals("yes")) {
            String number;
            while (true) {
                gamePlayController.getMyCommunicator().selectCard("monster", false, true, false);
                number = gamePlayController.takeCommand();
                if (number.matches("[^12345]"))
                    continue;
                if (gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getMyGameBoard()
                        .getPlace(i, PLACE_NAME.MONSTER) == null)
                    printerAndScanner.printNextLine(wrongCard);
                else break;
            }
            gamePlayController.getGamePlay().getOpponentGamePlayController().killCard(
                    gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getMyGameBoard().
                            getPlace(i, PLACE_NAME.MONSTER)
            );
        }
    }
}
