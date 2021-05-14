package controller.specialbilities;

import controller.GamePlayController;
import model.game.MonsterZone;
import model.game.Place;

import java.lang.reflect.Method;

public class AttackSpecial implements SpecialAbility {

    private Method method;
    private String methodName;
    private GamePlayController gamePlayController;
    private Place place;
    private String monsterType;

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
    public String getMethodName() {
        return methodName;
    }

    public void setMethod(Method method) {
        this.methodName = method.getName();
        this.method = method;
    }

    public void reduceAttackToZero(){
        if (!gamePlayController.getGamePlay().getHistory().get(place).contains("noSpecial")) {
            gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay()
                    .getHistory().get(place.getAffect()).add(
                    "temporaryAttackBoost" + ((MonsterZone) place.getAffect()).getAttack());
            GeneralSpecialAbility.attackBoost(place.getAffect(), ((MonsterZone) place.getAffect()).getAttack(), true);
            gamePlayController.getGamePlay().getHistory().get(place).add("noSpecial");
        }
    }

    public void neutralizeAttack(){
        if (!gamePlayController.getGamePlay().getHistory().get(place).contains("noSpecialThisRound")) {
            place.getAffect().setAffect(null);
            specialSummon();
            gamePlayController.getGamePlay().getHistory().get(place).add("noSpecialThisRound");
        }
    }

    public void specialSummon(){
        //TODO special summon using monster type
    }
}
