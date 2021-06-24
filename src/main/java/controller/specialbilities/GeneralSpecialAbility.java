package controller.specialbilities;

import controller.GamePlayController;
import model.cards.monster.MonsterCards;
import model.game.*;

public abstract class GeneralSpecialAbility {

    public static void boostAllAttack(GamePlayController gamePlayController, int boostAmount, boolean reduce){
        for (int i = 1; i < 6; i++) {
            attackBoost(gamePlayController.getGamePlay().getMyGameBoard().getPlace(i, PLACE_NAME.MONSTER), boostAmount, reduce);
        }
    }

    public static void attackBoost(Place place, int boostAmount, boolean reduce){
        if (boostAmount == -1)
            boostAmount = ((MonsterCards) place.getCard()).getAttack();
        boostAmount = reduce ? boostAmount * -1 : boostAmount;
        ((MonsterZone) place).setAttackModifier(((MonsterZone) place).getAttackModifier() + boostAmount);
    }

    public static void boostAllDefense(GamePlayController gamePlayController, int boostAmount, boolean reduce){
        for (int i = 1; i < 6; i++) {
            defenseBoost(gamePlayController.getGamePlay().getMyGameBoard().getPlace(i, PLACE_NAME.MONSTER), boostAmount, reduce);
        }
    }

    public static void defenseBoost(Place place, int boostAmount, boolean reduce){
        if (boostAmount == -1)
            boostAmount = ((MonsterCards) place.getCard()).getDefense();
        boostAmount = reduce ? boostAmount * -1 : boostAmount;
        ((MonsterZone) place).setDefenseModifier(((MonsterZone) place).getDefenseModifier() + boostAmount);
    }

    public static void killAllMonsters(boolean enemyOnly, GamePlayController gamePlayController
            , boolean dontUseStatus, STATUS status){
//        Place place;
        killAllMonsterCards(gamePlayController.getGamePlay().getOpponentGamePlayController(), dontUseStatus, status);
        if (!enemyOnly) {
            killAllMonsterCards(gamePlayController, dontUseStatus, status);
        }
    }

    private static void killAllMonsterCards(GamePlayController gamePlayController, boolean dontUseStatus, STATUS status) {
        Place place;
        GameBoard gameBoard = gamePlayController.getGamePlay().getMyGameBoard();
        for (int i = 1; i < 6; i++) {
            place = gameBoard.getPlace(i, PLACE_NAME.MONSTER);
            if (place.getCard() != null)
                if (place.getStatus() == status || dontUseStatus)
                    gamePlayController.killCard(place);
        }
    }
}
