package controller.specialbilities;

import controller.GamePlayController;
import model.cards.Cards;
import model.cards.monster.MonsterCards;
import model.game.PLACE_NAME;
import model.game.Place;
import model.game.STATUS;
import model.tools.StringMessages;

import java.lang.reflect.Method;

public class FaceUp implements SpecialAbility, StringMessages {

    private Method method;
    private String methodName;
    private GamePlayController gamePlayController;
    private Place place;
    private int boostAmount;

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

    public void setMethod(Method method) {
        this.methodName = method.getName();
        this.method = method;
    }

    @Override
    public String getMethodName() {
        return methodName;
    }

    public void boostAllAttack(){ //TODO ++
        gamePlayController.getGamePlay().getUniversalHistory().add("attackBoost" + boostAmount);
        GeneralSpecialAbility.boostAllAttack(gamePlayController, boostAmount, false);
    }

    public void attackBoost(){
        GeneralSpecialAbility.attackBoost(place, boostAmount, false);
    }

    public void boostAllDefense(){
        gamePlayController.getGamePlay().getUniversalHistory().add("defenseBoost" + boostAmount);
        GeneralSpecialAbility.boostAllDefense(gamePlayController, boostAmount, false);
    }

    public void defenseBoost(){
        GeneralSpecialAbility.defenseBoost(place, boostAmount, false);
    }

    public void cannotBeAttackedWhileThereAreOtherMonsters(){ //TODO ++
        gamePlayController.getGamePlay().getHistory().get(place).add("cannotBeAttackedWhileThereAreOtherMonsters");
    }

    public void cannotActivateTrap(){ //TODO ++
        gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay()
                .getUniversalHistory().add("cannotActivateTrap");
    }

    public void summonAMonster(){ //TODO ++
        int emptyMonsterPlace = gamePlayController.getGamePlay().getMyGameBoard().getFirstEmptyPlace(PLACE_NAME.MONSTER);
        if (emptyMonsterPlace != -1) {
            int i;
            for (i = 0; i < 6; i++) {
                Place hand = gamePlayController.getGamePlay().getMyGameBoard().getPlace(i, PLACE_NAME.HAND);
                if (hand.getCard() != null)
                    if (hand.getCard() instanceof MonsterCards)
                        if (((MonsterCards) hand.getCard()).getLevel() <= boostAmount)
                            break;
            }
            if (i < 6) {
                printerAndScanner.printNextLine(askActivateSpecial);
                if (printerAndScanner.scanNextLine().equals("yes")) {
                    printerAndScanner.printNextLine(cardNumber);
                    int cardNumber = printerAndScanner.scanNextInt();
                    while (true) {
                        if (cardNumber < 6 && cardNumber > -1) {
                            Place place = gamePlayController.getGamePlay().getMyGameBoard().getPlace(cardNumber, PLACE_NAME.MONSTER);
                            if (place.getCard() instanceof MonsterCards)
                                if (((MonsterCards) place.getCard()).getLevel() <= boostAmount)
                                    break;
                        }
                        printerAndScanner.printNextLine(wrongCard);
                        cardNumber = printerAndScanner.scanNextInt();
                    }
                    Place hand = gamePlayController.getGamePlay().getMyGameBoard().getPlace(cardNumber, PLACE_NAME.HAND);
                    Place toPlaceTo = gamePlayController.getGamePlay().getMyGameBoard().getPlace(emptyMonsterPlace, PLACE_NAME.MONSTER);
                    toPlaceTo.setCard(hand.getCard());
                    toPlaceTo.setStatus(STATUS.DEFENCE);
                    hand.setCard(null);
                }
            }
        }
    }
}
