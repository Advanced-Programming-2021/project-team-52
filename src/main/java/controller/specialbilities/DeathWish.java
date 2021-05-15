package controller.specialbilities;

import controller.GamePlayController;
import model.cards.monster.MonsterCards;
import model.game.Place;

import java.lang.reflect.Method;

public class DeathWish implements SpecialAbility {

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
            System.out.println("error : " + e);
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

    public Method getMethod() {
        return method;
    }

    public void killAttacker(){ //TODO ++
        if (place.getAffect().getCard() instanceof MonsterCards)
            gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getMyGameBoard().killCards(
                    gamePlayController.getGamePlay().getOpponentGamePlayController(), place.getAffect());
    }

    public void killDestroyer(){//TODO ++
        if (place.getAffect().getCard() instanceof MonsterCards)
            gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getMyGameBoard().killCards(
                    gamePlayController.getGamePlay().getOpponentGamePlayController(), place.getAffect());
    }

    public void removeAllAttackBoost(){ //TODO ++
        GeneralSpecialAbility.boostAllAttack(gamePlayController, amount, true);
        gamePlayController.getGamePlay().getHistory().get(place).remove("attack boost all " + amount);
    }

    public void removeAttackBoost(){
        GeneralSpecialAbility.attackBoost(place, amount, true);
    }

    public void removeAllDefenseBoost(){
        GeneralSpecialAbility.boostAllDefense(gamePlayController, amount, true);
        gamePlayController.getGamePlay().getHistory().get(place).remove("defense boost all " + amount);
    }

    public void removeDefenseBoost(){
        GeneralSpecialAbility.defenseBoost(place, amount, true);
    }

    public void canActivateTrap(){ //TODO ++
        gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay()
                .getUniversalHistory().add("cannotActivateTrap");
    }

    public void noHealthReduction(){ //TODO ++
        gamePlayController.getGamePlay().getUniversalHistory().add("noHealthReduction");
    }

    public void canAttack(){ //TODO ++
        gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay()
                .getUniversalHistory().remove("cannotAttack");
    }

    public void monstersCanAttack(){
        gamePlayController.getGamePlay().getUniversalHistory().remove("monstersCannotAttack" + amount);
        gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay()
                .getUniversalHistory().remove("monstersCannotAttack" + amount);
    }

    public void noLongerNeutralizeTrap(){
        gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay()
                .getUniversalHistory().remove("neutralizeTrap");
    }

    public void canNoLongerKillCardUponSummon(){
        gamePlayController.getGamePlay().getUniversalHistory().remove("killThisCardUponSummon");
    }

//    public void killAMonsterThatIsNormalOrFlipSummoned(){
//        gamePlayController.getGamePlay().getUniversalHistory().remove("killAMonsterThatIsNormalOrFlipSummoned" + amount);
//        gamePlayController.getGamePlay().getHistory().get(place).remove("killAMonsterThatIsNormalOrFlipSummoned" + amount);
//    }
//
//    public void killAllMonsterWhenAMonsterIsSummoned(){
//        gamePlayController.getGamePlay().getUniversalHistory().remove("killAllMonsterWhenAMonsterIsSummoned");
//        gamePlayController.getGamePlay().getHistory().get(place).remove("killAllMonsterWhenAMonsterIsSummoned");
//    }
}
