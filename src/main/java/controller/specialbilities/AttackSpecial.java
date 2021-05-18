package controller.specialbilities;

import controller.GamePlayController;
import model.game.MonsterZone;
import model.game.Place;
import model.tools.StringMessages;

import java.lang.reflect.Method;

public class AttackSpecial implements SpecialAbility, StringMessages {

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

    public void setMethod(Method method) {
        this.methodName = method.getName();
        this.method = method;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void reduceAttackToZero(){ //TODO ++
        if (!gamePlayController.getGamePlay().getHistory().get(place).contains("noSpecial")) {
            printerAndScanner.printNextLine(askActivateSpecial);
            if (printerAndScanner.scanNextLine().equals("yes")) {
                gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay()
                        .getHistory().get(place.getAffect()).add(
                        "temporaryAttackBoost" + ((MonsterZone) place.getAffect()).getAttack());
                GeneralSpecialAbility.attackBoost(place.getAffect(), ((MonsterZone) place.getAffect()).getAttack(), true);
                gamePlayController.getGamePlay().getHistory().get(place).add("noSpecial");
            }
        }
    }

    public void neutralizeAttack(){ //TODO ++
        if (!gamePlayController.getGamePlay().getHistory().get(place).contains("noSpecialThisRound")) {
            place.getAffect().setAffect(null);
            gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getHistory().
                    get(place.getAffect()).add("neutralizeAttack");
            SpecialAbilityActivationController specialAbilityActivationController =
                    SpecialAbilityActivationController.getInstance();
            specialAbilityActivationController.setGamePlayController(gamePlayController);
            specialAbilityActivationController.runSuccessSpecialAbility(place);
            gamePlayController.getGamePlay().getHistory().get(place).add("noSpecialThisRound");
        }
    }

    public void reduceAttackerLPIfItWasFacingDown(){ //TODO ++
        if (place.getAffect() != null) {
            gamePlayController.getGamePlay().getOpponentGamePlayController()
                    .getGamePlay().getMyGameBoard().changeHealth(amount * -1);
        }
    }
}
