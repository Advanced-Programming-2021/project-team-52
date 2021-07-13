package sample.controller.specialbilities;

import sample.controller.GamePlayController;
import sample.model.cards.monster.MonsterCards;
import sample.model.game.MonsterZone;
import sample.model.game.PLACE_NAME;
import sample.model.game.Place;
import sample.model.game.STATUS;
import sample.model.tools.StringMessages;

import java.lang.reflect.Method;

public class Continuous implements SpecialAbility, StringMessages {

    private Method method;
    private String methodName;
    private GamePlayController gamePlayController;
    private Place place;
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

    public void setMethod(Method method) {
        this.methodName = method.getName();
        this.method = method;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void cannotBeNormallyDestroyed() {
        gamePlayController.getGamePlay().getHistory().get(place).add("cannotBeNormallyDestroyed");
    }

    public void attackAmountByQuantifier() {
        if (place.getStatus() == STATUS.SET)
            return;
        int sumOfLevels = 0;
        for (int i = 1; i < 6; i++) {
            Place monsterPlace = gamePlayController.getGamePlay().getMyGameBoard().getPlace(i, PLACE_NAME.MONSTER);
            if (monsterPlace.getCard() != null)
                if (monsterPlace.getStatus() != STATUS.SET)
                    if (monsterPlace != place)
                        sumOfLevels += ((MonsterCards) monsterPlace.getCard()).getLevel();
        }
        sumOfLevels -= 2;
        ((MonsterZone) place).setAttackModifier(amount * sumOfLevels);
    }

    public void drawCardIfAMonsterIsDestroyed() {
        gamePlayController.getGamePlay().getHistory().get(place).add("drawCardIfAMonsterIsDestroyed");
    }

    public void getLPIfSpellIsActivated() {
        gamePlayController.getGamePlay().getUniversalHistory().add("getLPIfSpellIsActivated" + amount);
    }

    public void payHealthEveryRound() {
        gamePlayController.getGamePlay().getHistory().get(place).add("payHealthEveryRound" + amount);
    }

    public void monstersCannotAttack() {
        gamePlayController.getGamePlay().getUniversalHistory().add("monstersCannotAttack" + amount);
        gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay()
                .getUniversalHistory().add("monstersCannotAttack" + amount);
    }
}
