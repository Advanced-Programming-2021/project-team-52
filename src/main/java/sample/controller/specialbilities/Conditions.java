package sample.controller.specialbilities;

import sample.controller.GamePlayController;
import sample.model.cards.Cards;
import sample.model.cards.monster.MonsterCards;
import sample.model.game.GameBoard;
import sample.model.game.PLACE_NAME;
import sample.model.game.Place;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class Conditions implements SpecialAbility {

    private Method method;
    private String methodName;
    private GamePlayController gamePlayController;
    private Place place;
    private boolean met = false;
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

    public boolean getMet() {
        return met;
    }

    public void graveYardIsNotEmpty() {
        met = !gamePlayController.getGamePlay().getMyGameBoard().getGraveyard().isEmpty() ||
                !gamePlayController.getGamePlay().
                        getOpponentGamePlayController().getGamePlay().getMyGameBoard().getGraveyard().isEmpty();
    }

    public void thereIsMonsterInGraveYard() {
        for (int i = 1; i < 6; i++) {
            if (gamePlayController.getGamePlay().getMyGameBoard().getPlace(i, PLACE_NAME.MONSTER).getCard() != null) {
                met = true;
            }
        }
    }

    public void opponentHasMonster() {
        for (int i = 1; i < 6; i++) {
            if (gamePlayController.getGamePlay().getOpponentGamePlayController().
                    getGamePlay().getMyGameBoard().getPlace(i, PLACE_NAME.MONSTER).getCard() != null) {
                met = true;
                return;
            }
        }
        met = false;
    }

    public void handIsNotEmpty() {
        for (int i = 0; i < 6; i++) {
            if (gamePlayController.getGamePlay().getMyGameBoard().getPlace(i, PLACE_NAME.HAND).getCard() != null) {
                met = true;
                return;
            }
        }
        met = false;
    }

    private void monsterCardWithAtLeastThisLevelExistsInGraveyard() {
        met = false;
        for (Cards card : gamePlayController.getGamePlay().getMyGameBoard().getGraveyard()) {
            if (card instanceof MonsterCards)
                if (((MonsterCards) card).getLevel() >= amount) {
                    met = true;
                    break;
                }
        }
    }

    public void opponentHasMonsterWithAtLeastThisDamage() {
        met = false;
        Place place;
        GameBoard opponentGameBoard =
                gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getMyGameBoard();
        for (int i = 1; i < 6; i++) {
            place = opponentGameBoard.getPlace(i, PLACE_NAME.MONSTER);
            if (place.getCard() != null)
                if (((MonsterCards) place.getCard()).getAttack() > amount) {
                    met = true;
                    break;
                }
        }
    }

    public void otherCardsInHand() {
        met = false;
        Place place;
        for (int i = 0; i < 6; i++) {
            place = gamePlayController.getGamePlay().getMyGameBoard().getPlace(i, PLACE_NAME.HAND);
            if (place.getCard() != null && place != this.place) {
                met = true;
                break;
            }
        }
    }

    public void monsterCardOfThisTypeExists() {
        met = false;
        try {
            ArrayList<String> types = null;
            for (SpecialAbility specialAbility : place.getCard().getSpecial()) {
                if (specialAbility.getMethodName().equals("dynamicEquip")) {
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
        } catch (ClassCastException | NullPointerException e) {
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
}
