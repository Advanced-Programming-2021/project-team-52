package controller.specialbilities;

import controller.GamePlayController;
import model.cards.Cards;
import model.cards.monster.MonsterCards;
import model.cards.spell.SpellCards;
import model.cards.trap.TrapCards;
import model.game.GameBoard;
import model.game.MonsterZone;
import model.game.PLACE_NAME;
import model.game.Place;

import java.lang.reflect.Method;
import java.util.ArrayList;

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
                    getGamePlay().getMyGameBoard().getPlace(i, PLACE_NAME.MONSTER).getCard() != null) {
                met = true;
                return;
            }
        }
        met = false;
    }

    public void handIsNotEmpty(){//TODO ++ ++
        for (int i = 0; i < 6; i++) {
            if (gamePlayController.getGamePlay().getMyGameBoard().getPlace(i, PLACE_NAME.HAND).getCard() != null) {
                met = true;
                return;
            }
        }
        met = false;
    }

    private void monsterCardWithAtLeastThisLevelExistsInGraveyard(){
        met = false;
        for (Cards card : gamePlayController.getGamePlay().getMyGameBoard().getGraveyard()) {
            if (card instanceof MonsterCards)
                if (((MonsterCards) card).getLevel() >= amount){
                    met = true;
                    break;
                }
        }
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

    public void opponentHasMonsterWithAtLeastThisDamage(){ //TODO ++
//        met = false;
//        if (place.getAffect() != null)
//        if (place.getAffect().getCard() instanceof MonsterCards)
//            if (((MonsterZone) place.getAffect()).getAttack() >= amount)
//                met = true;
        met = false;
        Place place;
        GameBoard opponentGameBoard =
                gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getMyGameBoard();
        for (int i = 1; i < 6; i++) {
            place = opponentGameBoard.getPlace(i, PLACE_NAME.MONSTER);
            if (place.getCard() != null)
                if (((MonsterCards) place.getCard()).getAttack() > amount){
                    met = true;
                    break;
                }
        }
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
    
    public void monsterCardOfThisTypeExists(){
        met = false;
        try {
            ArrayList<String> types = null;
            for (SpecialAbility specialAbility : place.getCard().getSpecial()) {
                if (specialAbility.getMethodName().equals("dynamicEquip")){
                    types = ((Equip) specialAbility).getTypes();
                    break;
                }
            }
            Place place;
            GameBoard myGameBoard = gamePlayController.getGamePlay().getMyGameBoard();
            GameBoard opponentGameBoard =
                    gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getMyGameBoard();
            for (int i = 1; i < 6; i++) {
                place = myGameBoard.getPlace(i, PLACE_NAME.MONSTER);
                if (check(types, place)) break;
                place = opponentGameBoard.getPlace(i, PLACE_NAME.MONSTER);
                if (check(types, place)) break;
            }
        } catch (ClassCastException | NullPointerException e){
            met = false;
        }
    }

    private boolean check(ArrayList<String> types, Place place) {
        if (place.getCard() != null)
            if (types.contains(((MonsterCards) place.getCard()).getMonsterType())) {
                met = true;
                return true;
            }
        return false;
    }

//    private void canOnlyBeSpecialSummoned(){
//        met = false;
//    }
}
