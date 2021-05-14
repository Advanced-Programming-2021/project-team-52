package controller.specialbilities;

import controller.GamePlayController;
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
    public ArrayList<Place> affected;
    private boolean enemyAsWell;
    public boolean onDeath = false;

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
    public void setMethod(Method method) {
        this.methodName = method.getName();
        this.method = method;
    }

    @Override
    public String getMethodName() {
        return methodName;
    }

    public void setAffected(ArrayList<Place> affected) {
        this.affected = affected;
    }

    private void attackChange(){
        changeAttackModifier(amount);
    }

    private void defenseChange(){
        changeDefenseModifier(amount);
    }

    private void attackBoostForGraveYard(){
        int amount = calculateAmountOfChange();
        changeAttackModifier(amount);
    }

    private void defenseBoostForGraveYard(){
        int amount = calculateAmountOfChange();
        changeDefenseModifier(amount);
    }

    private int calculateAmountOfChange() {
        int amount = 0;
        amount += gamePlayController.getGamePlay().getMyGameBoard().getGraveyard().size();
        amount += gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getMyGameBoard()
                .getGraveyard().size();
        if (amount != this.amount)
            amount = amount - this.amount;
        this.amount = amount;
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
