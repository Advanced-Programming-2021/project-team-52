package controller.specialbilities;

import controller.GamePlayController;
import model.cards.Cards;
import model.game.PLACE_NAME;
import model.game.Place;

import java.lang.reflect.Method;

public class Success implements SpecialAbility {

    private Method method;
    private String methodName;
    private GamePlayController gamePlayController;
    private Place place;

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
    public void setMethod(Method method) {
        this.methodName = method.getName();
        this.method = method;
    }

    @Override
    public String getMethodName(){
        return methodName;
    }

    public void killCard(){
        gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getMyGameBoard()
                .killCards(
                        gamePlayController.getGamePlay().getOpponentGamePlayController(), place.getAffect());
    }

    public void destroyAllEnemyCards(){
        for (int i = 1; i < 6; i++) {
            gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getMyGameBoard()
                    .getPlace(i, PLACE_NAME.MONSTER).killCard();
            gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getMyGameBoard()
                    .getPlace(i, PLACE_NAME.SPELL_AND_TRAP).killCard();
        }
    }
}
