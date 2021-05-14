package controller.specialbilities;

import controller.GamePlayController;
import model.game.Place;

import java.lang.reflect.Method;

public class FaceUp implements SpecialAbility {

    private Method method;
    private String methodName;
    private GamePlayController gamePlayController;
    private Place place;
    private int boostAmount;

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

    public void boostAllAttack(){
        gamePlayController.getGamePlay().getUniversalHistory().add("attackBoost" + boostAmount);
        GeneralSpecialAbility.boostAllAttack(gamePlayController, boostAmount, false);
    }

    public void attackBoost(){
        GeneralSpecialAbility.attackBoost(place, boostAmount, false);
    }

    public void boostAllDefense(){
        gamePlayController.getGamePlay().getUniversalHistory().add("defenseBoost" + boostAmount);
        GeneralSpecialAbility.boostAllDefense(gamePlayController, boostAmount, false);
    }

    public void defenseBoost(){
        GeneralSpecialAbility.defenseBoost(place, boostAmount, false);
    }

    public void cannotBeAttacked(){
        gamePlayController.getGamePlay().getHistory().get(place).add("cannotBeAttacked");
    }
}
