package controller.specialbilities;

import controller.GamePlayController;
import model.cards.monster.MonsterCards;
import model.game.MonsterZone;
import model.game.PLACE_NAME;
import model.game.Place;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class FieldSpecial implements SpecialAbility {

    private Method method;
    private String methodName;
    private GamePlayController gamePlayController;
    private Place place;
    private int amount;
    private ArrayList<String> type;
    private boolean enemyAsWell;
    public boolean onDeath = false;

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
    public void setMethod(Method method) {
        this.methodName = method.getName();
        this.method = method;
    }

    @Override
    public String getMethodName() {
        return methodName;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setType(ArrayList<String> type) {
        this.type = type;
    }

    public void setEnemyAsWell(boolean enemyAsWell) {
        this.enemyAsWell = enemyAsWell;
    }

    public void setOnDeath(boolean onDeath) {
        this.onDeath = onDeath;
    }

    private void attackChange() {//TODO ++
        changeAttackModifier(amount);
    }

    private void defenseChange() {
        changeDefenseModifier(amount);
    }

    private void attackBoostForGraveYard() {
        int amount = calculateAmountOfChange();
        changeAttackModifier(amount);
    }

    private void defenseBoostForGraveYard() {
        int amount = calculateAmountOfChange();
        changeDefenseModifier(amount);
    }

    private int calculateAmountOfChange() {
        int amount = 0;
        amount += gamePlayController.getGamePlay().getMyGameBoard().getGraveyard().size();
        amount += gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getMyGameBoard()
                .getGraveyard().size();
        amount *= this.amount;
        return amount;
    }

    private void changeAttackModifier(int amount) {
        for (int i = 1; i < 6; i++) {
            changeAttackOrDefense(i, gamePlayController, true, amount);
            if (enemyAsWell)
                changeAttackOrDefense(i, gamePlayController.getGamePlay().getOpponentGamePlayController(), true, amount);
        }
    }

    private void changeDefenseModifier(int amount) {
        for (int i = 1; i < 6; i++) {
            changeAttackOrDefense(i, gamePlayController, false, amount);
            if (enemyAsWell)
                changeAttackOrDefense(i, gamePlayController.getGamePlay().getOpponentGamePlayController(), false, amount);
        }
    }

    private void changeAttackOrDefense(int i, GamePlayController gamePlayController, boolean changeAttack, int amount) {
        MonsterZone monsterZone = (MonsterZone) gamePlayController.getGamePlay().getMyGameBoard().getPlace(i, PLACE_NAME.MONSTER);
        if (monsterZone.getCard() == null)
            return;
        if (type.size() > 0 && !type.contains(((MonsterCards) monsterZone.getCard()).getMonsterType()))
            return;
        Integer[] equipped = monsterZone.getModifiers(place);
        if (changeAttack) {
            GeneralSpecialAbility.attackBoost(monsterZone, equipped[0], true);
            if (!onDeath)
                GeneralSpecialAbility.attackBoost(monsterZone, amount, false);
            equipped[0] = onDeath ? 0 : amount;
        } else {
            GeneralSpecialAbility.defenseBoost(monsterZone, equipped[1], true);
            if (!onDeath)
                GeneralSpecialAbility.defenseBoost(monsterZone, amount, false);
            equipped[1] = onDeath ? 0 : amount;
        }
    }
}
