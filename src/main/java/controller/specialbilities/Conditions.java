package controller.specialbilities;

import controller.GamePlayController;
import model.cards.monster.MonsterCards;
import model.cards.spell.SpellCards;
import model.cards.trap.TrapCards;
import model.game.MonsterZone;
import model.game.PLACE_NAME;
import model.game.Place;

import java.lang.reflect.Method;

public class Conditions implements SpecialAbility{

    private Method method;
    private String methodName;
    private GamePlayController gamePlayController;
    private Place place;
    private boolean met = false;
    private int amount;

    @Override
    public void run(GamePlayController gamePlayController, Place place){
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

    public void setMet(boolean met) {
        this.met = met;
    }

    public boolean getMet(){
        return met;
    }

    public void graveYardIsNotEmpty(){ //TODO ++
        met = !gamePlayController.getGamePlay().getMyGameBoard().getGraveyard().isEmpty() ||
                ! gamePlayController.getGamePlay().
                        getOpponentGamePlayController().getGamePlay().getMyGameBoard().getGraveyard().isEmpty();
    }

    public void thereIsMonsterInGraveYard(){ //TODO ++
        for (int i = 1; i < 6; i++) {
            if (gamePlayController.getGamePlay().getMyGameBoard().getPlace(i, PLACE_NAME.MONSTER).getCard() != null) {
                met = true;
            }
        }
    }

    public void opponentHasMonster(){ //TODO ++
        for (int i = 1; i < 6; i++) {
            if (gamePlayController.getGamePlay().getOpponentGamePlayController().
                    getGamePlay().getMyGameBoard().getPlace(i, PLACE_NAME.MONSTER) != null) {
                met = true;
                return;
            }
        }
        met = false;
    }

    public void handIsNotEmpty(){//TODO ++
        for (int i = 0; i < 6; i++) {
            if (gamePlayController.getGamePlay().getMyGameBoard().getPlace(i, PLACE_NAME.HAND).getCard() != null) {
                met = true;
                return;
            }
        }
        met = false;
    }

    public void opponentHasSpell(){
        for (int i = 1; i < 6; i++) {
            if (gamePlayController.getGamePlay().getOpponentGamePlayController().
                    getGamePlay().getMyGameBoard().getPlace(i, PLACE_NAME.SPELL_AND_TRAP) != null) {
                met = true;
                return;
            }
        }
        met = false;
    }

    public void monsterPlaceIsEmpty(){
        met = gamePlayController.getGamePlay().getMyGameBoard().getFirstEmptyPlace(PLACE_NAME.MONSTER) != -1;
    }

    public void affectIsMonster(){
        met = place.getAffect().getCard() instanceof MonsterCards;
    }

    public void affectIsSpell(){
        met = place.getAffect().getCard() instanceof SpellCards;
    }

    public void affectIsTrap(){
        met = place.getAffect().getCard() instanceof TrapCards;
    }

    public void checkBeforeKillingAMonsterInFlipSummonOrSummon(Place place){
        met = false;
        if (place.getCard() != null)
            if (place.getCard() instanceof MonsterCards)
                if (((MonsterCards) place.getCard()).getAttack() >= amount)
                    met = true;
    }

    public void affectHasAtLeastThisDamage(){ //TODO ++
        met = false;
        if (place.getAffect().getCard() instanceof MonsterCards)
            if (((MonsterZone) place.getAffect()).getAttack() >= amount)
                met = true;
    }

    public void otherCardsInHand(){ //TODO ++
        met = false;
        Place place;
        for (int i = 0; i < 6; i++) {
            place = gamePlayController.getGamePlay().getMyGameBoard().getPlace(i, PLACE_NAME.HAND);
            if (place.getCard() != null && place != this.place){
                met = true;
                break;
            }
        }
    }
}
