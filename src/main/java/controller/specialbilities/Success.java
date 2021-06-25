package controller.specialbilities;

import controller.GamePlayController;
import model.cards.Cards;
import model.cards.monster.MonsterCards;
import model.game.PLACE_NAME;
import model.game.Place;
import model.game.STATUS;
import model.game.TEMPORARY_FEATURES;
import model.tools.StringMessages;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class Success implements SpecialAbility, StringMessages {

    private Method method;
    private String methodName;
    private GamePlayController gamePlayController;
    private Place place;
    private String monsterType;

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

    public void setMonsterType(String monsterType) {
        this.monsterType = monsterType;
    }

    public void killAffect() {
        gamePlayController.getGamePlay().getOpponentGamePlayController().killCard(place.getAffect());
    }

    public void destroyAllEnemyCards() {
        GamePlayController opponentGamePlayController = gamePlayController.getGamePlay().getOpponentGamePlayController();
        for (int i = 1; i < 6; i++) {
            opponentGamePlayController.killCard(
                    opponentGamePlayController.getGamePlay().getMyGameBoard().getPlace(i, PLACE_NAME.MONSTER));
            opponentGamePlayController.killCard(
                    opponentGamePlayController.getGamePlay().getMyGameBoard().getPlace(i, PLACE_NAME.SPELL_AND_TRAP));
        }
    }

    public void specialSummonMonster() {
        boolean existsInGraveYard = monsterOfThisTypeExistsInGraveYard();
        boolean existsInHand = monsterOfThisTypeExistsInHand();
        boolean existsInDeck = monsterOfThisTypeExistsInDeck();
        int emptyPlace = gamePlayController.getGamePlay().getMyGameBoard().getFirstEmptyPlace(PLACE_NAME.MONSTER);
        if (emptyPlace != -1 && (existsInDeck || existsInHand || existsInGraveYard)) {
            printerAndScanner.printNextLine(askActivateSpecial);
            if (printerAndScanner.scanNextLine().equals("yes")) {
                printerAndScanner.printNextLine(whereDoYouWantToSpecialSummonFrom);
                String command = printerAndScanner.scanNextLine();
                while (!command.equals("cancel")) {
                    if (command.equals("graveyard") && existsInGraveYard) {
                        summonFromGraveYard();
                        break;
                    } else if (command.equals("hand") && existsInHand) {
                        summonFromHand();
                        break;
                    } else if (command.equals("deck") && existsInDeck) {
                        summonFromDeck();
                        break;
                    } else {
                        printerAndScanner.printNextLine(invalidCommand);
                        command = printerAndScanner.scanNextLine();
                    }
                }
            }
        }
    }

    private void summonFromGraveYard() {
        ArrayList<Cards> graveYard = gamePlayController.getGamePlay().getMyGameBoard().getGraveyard();
        printerAndScanner.printString(printBuilderController.buildGraveyard(graveYard));
        String cardName = printerAndScanner.scanNextLine();
        Cards toSummon;
        while (true) {
            toSummon = Cards.getCard(cardName);
            if (toSummon != null)
                if (((MonsterCards) toSummon).getMonsterType().equals(monsterType) && toSummon.getSpecial().size() == 0)
                    if (graveYard.contains(toSummon))
                        break;
            printerAndScanner.printNextLine(wrongCard);
            cardName = printerAndScanner.scanNextLine();
        }
        graveYard.remove(toSummon);
        Place toPlace = new Place(PLACE_NAME.HAND);
        toPlace.setCard(toSummon);
        gamePlayController.placeCard(toPlace, false, STATUS.ATTACK);
    }

    private void summonFromHand() {
        printerAndScanner.printNextLine(cardNumber);
        int cardNumber = printerAndScanner.scanNextInt();
        while (true) {
            if (cardNumber < 6 && cardNumber >= 0) {
                Place place = gamePlayController.getGamePlay().getMyGameBoard().getPlace(cardNumber, PLACE_NAME.HAND);
                if (place.getCard() != null)
                    if (((MonsterCards) place.getCard()).getMonsterType().equals(monsterType) && place.getCard().getSpecial().size() == 0) {
                        gamePlayController.placeCard(place, false, STATUS.ATTACK);
                        break;
                    }
            }
            printerAndScanner.printNextLine(wrongCard);
            cardNumber = printerAndScanner.scanNextInt();
        }
    }

    private void summonFromDeck() {
        ArrayList<Cards> mainCards = gamePlayController.getGamePlay().getMyGameBoard().getMainCards();
        ArrayList<Integer> cardsPicked = gamePlayController.getGamePlay().getMyGameBoard().getCardsPicked();
        printerAndScanner.printNextLine(nameOfTheCardYouWantToSummon);
        String cardName = printerAndScanner.scanNextLine();
        outerLoop:
        while (true) {
            Cards card = Cards.getCard(cardName);
            if (card != null)
                if (((MonsterCards) card).getMonsterType().equals(monsterType) && card.getSpecial().size() == 0)
                    for (int i = 0; i < mainCards.size(); i++) {
                        if (mainCards.get(i).equals(card) && !cardsPicked.contains(i)) {
                            Place toSummon = new Place(PLACE_NAME.HAND);
                            toSummon.setCard(card);
                            gamePlayController.placeCard(place, false, STATUS.ATTACK);
                            cardsPicked.add(i);
                            gamePlayController.shuffleDeck();
                            break outerLoop;
                        }
                    }
            printerAndScanner.printNextLine(wrongCard);
            cardName = printerAndScanner.scanNextLine();
        }
        gamePlayController.shuffleDeck();
    }

    private boolean monsterOfThisTypeExistsInGraveYard() {
        ArrayList<Cards> graveYard = gamePlayController.getGamePlay().getMyGameBoard().getGraveyard();
        for (Cards cards : graveYard) {
            if (cards.getType().equals(monsterType))
                return true;
        }
        return false;
    }

    private boolean monsterOfThisTypeExistsInHand() {
        Cards card;
        for (int i = 0; i < 6; i++) {
            card = gamePlayController.getGamePlay().getMyGameBoard().getPlace(i, PLACE_NAME.HAND).getCard();
            if (card != null)
                if (((MonsterCards) card).getMonsterType().equals(monsterType))
                    return true;
        }
        return false;
    }

    private boolean monsterOfThisTypeExistsInDeck() {
        ArrayList<Cards> mainCards = gamePlayController.getGamePlay().getMyGameBoard().getMainCards();
        ArrayList<Integer> cardsPicked = gamePlayController.getGamePlay().getMyGameBoard().getCardsPicked();
        for (int i = 0; i < mainCards.size(); i++) {
            if (((MonsterCards) mainCards.get(i)).getMonsterType().equals(monsterType) && !cardsPicked.contains(i))
                return true;
        }
        return false;
    }

    public void preventAttack() {
        place.getAffect().getTemporaryFeatures().add(TEMPORARY_FEATURES.CARD_ATTACKED_IN_THIS_TURN);
    }
}
