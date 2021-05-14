package controller.specialbilities;

import controller.GamePlayController;
import model.cards.Cards;
import model.game.PLACE_NAME;
import model.game.Place;
import model.tools.StringMessages;

import java.lang.reflect.Method;

public class Tribute implements SpecialAbility, StringMessages {

    private Method method;
    private String methodName;
    private GamePlayController gamePlayController;
    private Place place;
    private int amount;

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

    public void canSummonNormally(){
        GeneralSpecialAbility.attackBoost(place, amount, true);
        gamePlayController.summon(place);
    }

    public void summonWithTribute(){
        int amount = this.amount;
        int numberOfMonsterToTribute = 0;
        for (int i = 1; i < 6; i++) {
            if (gamePlayController.getGamePlay().getMyGameBoard().getPlace(i, PLACE_NAME.MONSTER).getCard() != null)
                numberOfMonsterToTribute++;
        }
        if (amount >= numberOfMonsterToTribute){
            printerAndScanner.printNextLine(doYouWantToTribute);
            if (printerAndScanner.scanNextLine().equals("yes")){
                int[] cardsToTribute = new int[amount];
                do
                    scanTributes(amount, cardsToTribute);
                while (!validForTribute(cardsToTribute));
                for (int i : cardsToTribute) {
                    Place toKill = gamePlayController.getGamePlay().getMyGameBoard().getPlace(cardsToTribute[i], PLACE_NAME.MONSTER);
                    gamePlayController.getGamePlay().getMyGameBoard().killCards(
                            gamePlayController, place);
                }
                gamePlayController.summon(place);
            }
        } else printerAndScanner.printNextLine(thereAreNotEnoughCardsForTribute);
    }

    private void scanTributes (int amount, int[] cardsToTribute){
        while (amount > 0){
            printerAndScanner.printNextLine(pleaseEnterNextCard);
            cardsToTribute[amount - 1] = printerAndScanner.scanNextInt() - 1;
            amount--;
        }
    }

    private boolean validForTribute(int[] cardsToTribute){
        for (int card : cardsToTribute) {
            if (card < 5 && card >= 0) {
                if (gamePlayController.getGamePlay().getMyGameBoard().getPlace(card, PLACE_NAME.MONSTER).getCard() == null){
                    if (cardsToTribute.length == 1)
                        printerAndScanner.printNextLine(thereNoMonstersOneThisAddress);
                    else printerAndScanner.printNextLine(noMonsterOnOneOfTheseAddresses);
                    return false;
                }
            } else {
                printerAndScanner.printNextLine(wrongTribute);
                return false;
            }
        }
        return true;
    }
}
