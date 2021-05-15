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
    private boolean specialSummon;
    private int key;

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

    public void canSummonNormally(){ //TODO ++ ++
        switch (key) {
            case 1 : summonWithReducingAttack();
            case 2 : summonWithSacrificingCardFromHand();
        }
    }

    private void summonWithReducingAttack(){
        GeneralSpecialAbility.attackBoost(place, amount, true);
        gamePlayController.summon(place, specialSummon);
    }

    private void summonWithSacrificingCardFromHand(){
        printerAndScanner.printNextLine(cardNumber);
        int handNumberToSacrifice = printerAndScanner.scanNextInt();
        Place place;
        while (true){
            place = gamePlayController.getGamePlay().getMyGameBoard().getPlace(handNumberToSacrifice, PLACE_NAME.HAND);
            if (place.getCard() == null){
                printerAndScanner.printNextLine(wrongTribute);
                handNumberToSacrifice = printerAndScanner.scanNextInt();
            } else break;
        }
        gamePlayController.getGamePlay().getMyGameBoard().killCards(gamePlayController, place);
        gamePlayController.summon(this.place, specialSummon);
    }

    public void summonWithTribute(){ //TODO ++ ++ ++
        int amount = this.amount;
        int numberOfMonsterToTribute = 0;
        for (int i = 1; i < 6; i++) {
            if (gamePlayController.getGamePlay().getMyGameBoard().getPlace(i, PLACE_NAME.MONSTER).getCard() != null)
                numberOfMonsterToTribute++;
        }
        if (amount <= numberOfMonsterToTribute){
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
                gamePlayController.summon(place, specialSummon);
                if (place.getCard() != null){
                    SpecialAbilityActivationController specialAbilityActivationController =
                            SpecialAbilityActivationController.getInstance();
                    specialAbilityActivationController.setGamePlayController(gamePlayController);
                    specialAbilityActivationController.runSuccessSpecialAbility(place);
                }
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
