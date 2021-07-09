package sample.controller.specialbilities;

import sample.controller.GamePlayController;
import sample.model.game.MonsterZone;
import sample.model.game.Place;
import sample.model.tools.StringMessages;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class AttackSpecial implements SpecialAbility, StringMessages {

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

    public void reduceAttackToZero() {
        if (!gamePlayController.getGamePlay().getHistory().get(place).contains("noSpecial")) {
            printerAndScanner.printNextLine(askActivateSpecial);
            if (printerAndScanner.scanNextLine().equals("yes")) {
                ArrayList<String> history = gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay()
                        .getHistory().get(place.getAffect());
                history.add("temporaryAttackBoost" + ((MonsterZone) place.getAffect()).getAttack());
                history.add("neutralizeAttack");
                GeneralSpecialAbility.attackBoost(place.getAffect(), ((MonsterZone) place.getAffect()).getAttack(), true);
                gamePlayController.getGamePlay().getHistory().get(place).add("noSpecial");
            }
        }
    }

    public void neutralizeAttack() {
        if (!gamePlayController.getGamePlay().getHistory().get(place).contains("noSpecialThisRound")) {
            gamePlayController.getGamePlay().getHistory().get(place).add("neutralizeAttack");
            gamePlayController.getGamePlay().getHistory().get(place).add("noSpecialThisRound");
        }
    }

    public void reduceAttackerLPIfItWasFacingDown() {
        gamePlayController.getGamePlay().getOpponentGamePlayController()
                .getGamePlay().getMyGameBoard().changeHealth(amount * -1);
    }
}
