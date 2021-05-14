package controller.specialbilities;

import controller.GamePlayController;
import model.cards.Cards;
import model.cards.monster.MonsterCards;
import model.game.*;
import model.tools.StringMessages;

import java.lang.reflect.Method;

public class Continuous implements SpecialAbility, StringMessages {

    private Method method;
    private String methodName;
    private GamePlayController gamePlayController;
    private Place place;
    private int amount;
    private int type;

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

    public void setMethod(Method method) {
        this.methodName = method.getName();
        this.method = method;
    }

    @Override
    public String getMethodName() {
        return methodName;
    }

//    public void scanner(){
//        if (place.getCard() != Cards.getCard("Scanner"))
//            ((MonsterZone) place).changeToThisCard(Cards.getCard("Scanner"));
//    }

    public void cannotBeNormallyDestroyed(){
        gamePlayController.getGamePlay().getHistory().get(place).add("cannotBeNormallyDestroyed");
    }

    public void attackAmountByQuantifier(){
        int sumOfLevels = 0;
        for (int i = 1; i < 6; i++) {
            Place monsterPlace = gamePlayController.getGamePlay().getMyGameBoard().getPlace(i, PLACE_NAME.MONSTER);
            if (monsterPlace.getCard() != null)
                if (monsterPlace.getStatus() != STATUS.SET)
                    sumOfLevels += ((MonsterCards) monsterPlace.getCard()).getLevel();
        }
        ((MonsterZone) place).setAttackModifier(amount * sumOfLevels);
    }

    public void drawCardIfAMonsterIsDestroyed(){
        gamePlayController.getGamePlay().getUniversalHistory().add("drawCardIfAMonsterIsDestroyed");
    }

    public void getLPIfEnemySpellIsActivated(){
        gamePlayController.getGamePlay().getUniversalHistory().add("getLPIfEnemySpellIsActivated" + amount);
    }

    public void payHealthEveryRound(){
        gamePlayController.getGamePlay().getHistory().get(place).add("payHealthEveryRound" + amount);
    }

    public void monstersCannotAttack(){
        gamePlayController.getGamePlay().getUniversalHistory().add("monstersCannotAttack" + amount);
        gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay()
                .getUniversalHistory().add("monstersCannotAttack" + amount);
    }

//    public void killAMonsterThatIsNormalOrFlipSummoned(){
//        gamePlayController.getGamePlay().getUniversalHistory().add("killAMonsterThatIsNormalOrFlipSummoned" + amount);
//        gamePlayController.getGamePlay().getHistory().get(place).add("killAMonsterThatIsNormalOrFlipSummoned" + amount);
//    }
//
//    public void killAllMonsterWhenAMonsterIsSummoned(){
//        gamePlayController.getGamePlay().getUniversalHistory().add("killAllMonsterWhenAMonsterIsSummoned");
//        gamePlayController.getGamePlay().getHistory().get(place).add("killAllMonsterWhenAMonsterIsSummoned");
//    }
}
