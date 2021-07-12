package sample.controller.specialbilities;

import sample.controller.GamePlayController;
import sample.model.game.PLACE_NAME;
import sample.model.game.Place;
import sample.model.game.STATUS;
import sample.model.tools.StringMessages;

import java.lang.reflect.Method;

public class Tribute implements SpecialAbility, StringMessages {

    private Method method;
    private String methodName;
    private GamePlayController gamePlayController;
    private Place place;
    private int amount;
    private boolean specialSummon;
    private boolean cannotSet;
    private STATUS status;
    private int key;

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

    public void setSpecialSummon(boolean specialSummon) {
        this.specialSummon = specialSummon;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }

    public void setCannotSet(boolean cannotSet) {
        this.cannotSet = cannotSet;
    }

    public void canSummonNormally() {
        switch (key) {
            case 1:
                summonWithReducingAttack();
                break;
            case 2:
                summonWithSacrificingCardFromHand();
        }
    }

    private void summonWithReducingAttack() {
        Place summonedPlace = null;
        if (status == STATUS.SET)
            if (cannotSet)
                printerAndScanner.printNextLine(StringMessages.cannotSet);
            else summonedPlace =
                    gamePlayController.setCard(place, gamePlayController.getGamePlay().getMyGameBoard().getFirstEmptyPlace(PLACE_NAME.MONSTER));
        else {
            summonedPlace = gamePlayController.placeCard(place, specialSummon, STATUS.ATTACK);
        }
        if (summonedPlace != null)
            if (summonedPlace.getCard() != null)
                GeneralSpecialAbility.attackBoost(summonedPlace, amount, true);
    }

    private void summonWithSacrificingCardFromHand() {
        gamePlayController.getMyCommunicator().selectCard("monster", true, false, false);
        int handNumberToSacrifice = Integer.parseInt(gamePlayController.takeCommand());
        Place place;
        while (true) {
            place = gamePlayController.getGamePlay().getMyGameBoard().getPlace(handNumberToSacrifice, PLACE_NAME.HAND);
            if (place != null)
                if (place != this.place && place.getCard() != null)
                    break;
            printerAndScanner.printNextLine(wrongTribute);
            gamePlayController.getMyCommunicator().selectCard("monster", true, false, false);
            handNumberToSacrifice = Integer.parseInt(gamePlayController.takeCommand());
        }
        gamePlayController.killCard(place);
        if (status == STATUS.SET)
            if (cannotSet)
                printerAndScanner.printNextLine(StringMessages.cannotSet);
            else gamePlayController.setCard(this.place,
                    gamePlayController.getGamePlay().getMyGameBoard().getFirstEmptyPlace(PLACE_NAME.MONSTER));
        else gamePlayController.placeCard(this.place, specialSummon, STATUS.ATTACK);
    }

    public void summonWithTribute() {
        int amount = this.amount;
        int numberOfMonsterToTribute = 0;
        for (int i = 1; i < 6; i++) {
            if (gamePlayController.getGamePlay().getMyGameBoard().getPlace(i, PLACE_NAME.MONSTER).getCard() != null)
                numberOfMonsterToTribute++;
        }
        if (amount <= numberOfMonsterToTribute) {
            gamePlayController.getMyCommunicator().askOptions(doYouWantToTribute, "yes", "no");
            if (gamePlayController.takeCommand().equals("yes")) {
                int[] cardsToTribute = new int[amount];
                do scanTributes(amount, cardsToTribute);
                while (!validForTribute(cardsToTribute));
                for (int i = 0; i < cardsToTribute.length; i++) {
                    Place toKill = gamePlayController.getGamePlay().getMyGameBoard().getPlace(cardsToTribute[i], PLACE_NAME.MONSTER);
                    gamePlayController.killCard(toKill);
                }
                Place summonedPlace = null;
                if (status == STATUS.SET)
                    if (cannotSet)
                        printerAndScanner.printNextLine(StringMessages.cannotSet);
                    else gamePlayController.setCard(place,
                            gamePlayController.getGamePlay().getMyGameBoard().getFirstEmptyPlace(PLACE_NAME.MONSTER));
                else summonedPlace = gamePlayController.placeCard(place, specialSummon, STATUS.ATTACK);
                if (summonedPlace != null)
                    if (summonedPlace.getCard() != null)
                        gamePlayController.getSpecialAbilityActivationController().runSuccessSpecialAbility(summonedPlace);
            }
        } else printerAndScanner.printNextLine(thereAreNotEnoughCardsForTribute);
    }

    private void scanTributes(int amount, int[] cardsToTribute) {
        outerLoop:
        while (amount > 0) {
            gamePlayController.getMyCommunicator().askOptions(pleaseEnterNextCard, "ok");
            String command = gamePlayController.takeCommand();
            while (!command.matches("^\\d+$")){
                gamePlayController.getMyCommunicator().selectCard("monster", true, false, false);
                command = gamePlayController.takeCommand();
            }
            int tribute = Integer.parseInt(command);
            for (int i = cardsToTribute.length - 1; i >= amount; i--) {
                if (cardsToTribute[i] == tribute){
                    gamePlayController.getMyCommunicator().askOptions(wrongTribute, "ok");
                    continue outerLoop;
                }
            }
            cardsToTribute[amount - 1] = tribute;
            amount--;
        }
    }

    private boolean validForTribute(int[] cardsToTribute) {
        for (int card : cardsToTribute) {
            if (card < 6 && card > 0) {
                if (gamePlayController.getGamePlay().getMyGameBoard().getPlace(card, PLACE_NAME.MONSTER).getCard() == null) {
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
