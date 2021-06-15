package controller.specialbilities;

import controller.GamePlayController;
import model.game.Field;
import model.game.PLACE_NAME;
import model.game.Place;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class FieldSpecial implements SpecialAbility {

    private Method method;
    private String methodName;
    private GamePlayController gamePlayController;
    private Place place;
    private int amount, quantifier;
    private ArrayList<String> type;
    private boolean enemyAsWell;
    public ArrayList<Place> affected;
    public boolean onDeath = false;

    @Override
    public void run(GamePlayController gamePlayController, Place place){
        this.gamePlayController = gamePlayController;
        this.place = place;
        this.affected = ((Field) place).getAffected();
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

    public void setQuantifier(int quantifier) {
        this.quantifier = quantifier;
    }

    public void setEnemyAsWell(boolean enemyAsWell) {
        this.enemyAsWell = enemyAsWell;
    }

    public void setOnDeath(boolean onDeath) {
        this.onDeath = onDeath;
    }

    private void attackChange(){//TODO ++
        changeAttackModifier(amount);
    }

    private void defenseChange(){//TODO ++
        changeDefenseModifier(amount);
    }

    private void attackBoostForGraveYard(){//TODO ++
        int amount = calculateAmountOfChange();
        changeAttackModifier(amount);
    }

    private void defenseBoostForGraveYard(){
        int amount = calculateAmountOfChange();
        changeDefenseModifier(amount);
    }

    private int calculateAmountOfChange() {
        Field field = (Field) place;
        int amount = 0;
        amount += gamePlayController.getGamePlay().getMyGameBoard().getGraveyard().size();
        amount += gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getMyGameBoard()
                .getGraveyard().size();
        if (amount != this.amount)
            amount = amount - field.getNumberOfCardsAffected();
        field.setNumberOfCardsAffected(amount);
        amount *= quantifier;
        return amount;
    }

    private void changeAttackModifier(int amount) {
        Place place;
        for (int i = 1; i < 6; i++) {
            place = gamePlayController.getGamePlay().getMyGameBoard().getPlace(i, PLACE_NAME.MONSTER);
            if (!affected.contains(place) && type.contains(place.getCard().getType())) {
                GeneralSpecialAbility.attackBoost(place, amount, onDeath);
                affected.add(place);
            }
            if (enemyAsWell){
                place = gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getMyGameBoard()
                        .getPlace(i, PLACE_NAME.MONSTER);
                if (!affected.contains(place) && type.contains(place.getCard().getType())){
                    GeneralSpecialAbility.attackBoost(place, amount, onDeath);
                    affected.add(place);
                }
            }
        }
    }

    private void changeDefenseModifier(int amount) {
        Place place;
        for (int i = 1; i < 6; i++) {
            place = gamePlayController.getGamePlay().getMyGameBoard().getPlace(i, PLACE_NAME.MONSTER);
            if (!affected.contains(place) && type.contains(place.getCard().getType())) {
                GeneralSpecialAbility.defenseBoost(place, amount, onDeath);
                affected.add(place);
            }
            if (enemyAsWell){
                place = gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getMyGameBoard()
                        .getPlace(i, PLACE_NAME.MONSTER);
                if (!affected.contains(place) && type.contains(place.getCard().getType())){
                    GeneralSpecialAbility.defenseBoost(place, amount, onDeath);
                    affected.add(place);
                }
            }
        }
    }
}
