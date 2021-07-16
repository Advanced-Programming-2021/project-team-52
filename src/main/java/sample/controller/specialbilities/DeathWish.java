package sample.controller.specialbilities;

import sample.controller.GamePlayController;
import sample.model.cards.monster.MonsterCards;
import sample.model.game.PLACE_NAME;
import sample.model.game.Place;

import java.lang.reflect.Method;

public class DeathWish implements SpecialAbility {

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

    @Override
    public void setMethod(Method method) {
        this.methodName = method.getName();
        this.method = method;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void killAttacker() {
        if (place.getAffect().getCard() instanceof MonsterCards)
            gamePlayController.getGamePlay().getOpponentGamePlayController().killCard(place.getAffect());
    }

    public void killDestroyer() {
        if (place.getAffect().getCard() instanceof MonsterCards)
            gamePlayController.getGamePlay().getOpponentGamePlayController().killCard(place.getAffect());
    }

    public void removeAllAttackBoost() {
        if (place.getType() != PLACE_NAME.HAND) {
            GeneralSpecialAbility.boostAllAttack(gamePlayController, amount, true);
            gamePlayController.getGamePlay().getHistory().get(place).remove("attack boost all " + amount);
        }
    }

    public void canActivateTrap() {
        gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay()
                .getUniversalHistory().add("cannotActivateTrap");
    }

    public void noHealthReduction() {
        gamePlayController.getGamePlay().getUniversalHistory().add("noHealthReduction");
        gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay()
                .getUniversalHistory().add("noHealthReduction");
    }

    public void canAttack() {
        gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay()
                .getUniversalHistory().remove("cannotAttack");
    }

    public void monsterCanAttack() {
        gamePlayController.getGamePlay().getUniversalHistory().remove("monstersCannotAttack" + amount);
        gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay()
                .getUniversalHistory().remove("monstersCannotAttack" + amount);
    }
}
