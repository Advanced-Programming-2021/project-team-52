package sample.controller.specialbilities;

import sample.controller.GamePlayController;
import sample.model.cards.monster.MonsterCards;
import sample.model.game.PLACE_NAME;
import sample.model.game.Place;
import sample.model.game.STATUS;
import sample.model.tools.StringMessages;

import java.lang.reflect.Method;

public class FaceUp implements SpecialAbility, StringMessages {

    private Method method;
    private String methodName;
    private GamePlayController gamePlayController;
    private Place place;
    private int boostAmount;

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

    public void setMethod(Method method) {
        this.methodName = method.getName();
        this.method = method;
    }

    @Override
    public String getMethodName() {
        return methodName;
    }

    public void setBoostAmount(int boostAmount) {
        this.boostAmount = boostAmount;
    }

    public void boostAllAttack() {
        gamePlayController.getGamePlay().getUniversalHistory().add("attackBoost" + boostAmount);
        GeneralSpecialAbility.boostAllAttack(gamePlayController, boostAmount, false);
    }

    public void cannotBeAttackedWhileThereAreOtherMonsters() {
        gamePlayController.getGamePlay().getHistory().get(place).add("cannotBeAttackedWhileThereAreOtherMonsters");
    }

    public void cannotActivateTrap() {
        gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay()
                .getUniversalHistory().add("cannotActivateTrap");
    }

    public void summonAMonster() {
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
                            Place place = gamePlayController.getGamePlay().getMyGameBoard().getPlace(cardNumber, PLACE_NAME.HAND);
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
                    printerAndScanner.printNextLine(summonedSuccessfully);
                }
            }
        }
    }

    public void addScannerHistory() {
        gamePlayController.getGamePlay().getHistory().get(place).add("scanner");
    }

    public void canActivateMonster() {
        gamePlayController.getGamePlay().getHistory().get(place).add("canBeActivated");
    }
}
