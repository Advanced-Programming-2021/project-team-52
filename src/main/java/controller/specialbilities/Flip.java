package controller.specialbilities;

import controller.GamePlayController;
import model.game.PLACE_NAME;
import model.game.Place;
import model.tools.StringMessages;

import java.lang.reflect.Method;

public class Flip implements SpecialAbility, StringMessages {

    private Method method;
    private String methodName;
    private GamePlayController gamePlayController;
    private Place place;
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
    public void setMethod(Method method) {
        this.methodName = method.getName();
        this.method = method;
    }

    @Override
    public String getMethodName() {
        return methodName;
    }

    public void destroyAMonster(){//TODO ++
        int i;
        for (i = 1; i < 6; i++) {
            if (gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getMyGameBoard()
                    .getPlace(i, PLACE_NAME.MONSTER).getCard() != null)
                break;
        }
        if (i == 6)
            return;
        printerAndScanner.printNextLine(askActivateSpecial);
        i = printerAndScanner.scanNextInt();
        while (gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getMyGameBoard()
                .getPlace(i, PLACE_NAME.MONSTER) == null){
            printerAndScanner.printNextLine(wrongCard);
            i = printerAndScanner.scanNextInt();
        }
        gamePlayController.getGamePlay().getOpponentGamePlayController().killCard(
                gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getMyGameBoard().
                        getPlace(i, PLACE_NAME.MONSTER)
        );
    }

//    public void reduceAttackerLP(){
//        if (place.getAffect() != null) {
//            gamePlayController.getGamePlay().getOpponentGamePlayController()
//                    .getGamePlay().getMyGameBoard().changeHealth(amount * -1);
//        }
//    }

//    public void cannotActivateTrap(){
//        gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay()
//                .getUniversalHistory().add("cannotActivateTrap");
//    }
}
