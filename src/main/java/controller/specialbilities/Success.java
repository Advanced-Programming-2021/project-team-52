package controller.specialbilities;

import controller.GamePlayController;
import model.cards.Cards;
import model.game.PLACE_NAME;
import model.game.Place;
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
    public String getMethodName(){
        return methodName;
    }

    public void killCard(){
        gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getMyGameBoard()
                .killCards(
                        gamePlayController.getGamePlay().getOpponentGamePlayController(), place.getAffect());
    }

    public void destroyAllEnemyCards(){ //TODO ++
        for (int i = 1; i < 6; i++) {
            gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getMyGameBoard()
                    .getPlace(i, PLACE_NAME.MONSTER).killCard();
            gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getMyGameBoard()
                    .getPlace(i, PLACE_NAME.SPELL_AND_TRAP).killCard();
        }
    }

    public void specialSummonMonster() { //TODO ++
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
                    } else if (command.equals("hand") && existsInHand){
                        summonFromHand();
                        break;
                    } else if (command.equals("deck") && existsInDeck){
                        summonFromDeck();
                        break;
                    } else printerAndScanner.printNextLine(invalidCommand);
                }
            }
        }
    }

    private void summonFromGraveYard(){
        ArrayList<Cards> graveYard = gamePlayController.getGamePlay().getMyGameBoard().getGraveyard();
        printerAndScanner.printString(printBuilderController.buildGraveyard(graveYard));
        String cardName = printerAndScanner.scanNextLine();
        Cards toSummon;
        while (true){
            toSummon = Cards.getCard(cardName);
            if (toSummon != null)
                if (toSummon.getType().equals(monsterType))
                    if (graveYard.contains(toSummon))
                        break;
            printerAndScanner.printNextLine(wrongCard);
            cardName = printerAndScanner.scanNextLine();
        }
        graveYard.remove(toSummon);
        Place toPlace = new Place(PLACE_NAME.HAND);
        toPlace.setCard(toSummon);
        gamePlayController.summon(toPlace, false);
    }

    private void summonFromHand(){
        printerAndScanner.printNextLine(cardNumber);
        int cardNumber = printerAndScanner.scanNextInt();
        while (true){
            if (cardNumber <6 && cardNumber >= 0){
                Place place = gamePlayController.getGamePlay().getMyGameBoard().getPlace(cardNumber, PLACE_NAME.HAND);
                if (place.getCard().getType().equals(monsterType)){
                    gamePlayController.summon(place, false);
                    break;
                }
            }
            printerAndScanner.printNextLine(wrongCard);
            cardNumber = printerAndScanner.scanNextInt();
        }
    }

    private void summonFromDeck(){
        ArrayList<Cards> mainCards = gamePlayController.getGamePlay().getMyGameBoard().getMainCards();
        ArrayList<Integer> cardsPicked = gamePlayController.getGamePlay().getMyGameBoard().getCardsPicked();
        printerAndScanner.printNextLine(nameOfTheCardYouWantToSummon);
        String cardName = printerAndScanner.scanNextLine();
        outerLoop :
        while (true){
            Cards card = Cards.getCard(cardName);
            if (card.getType().equals(monsterType))
                for (int i = 0; i < mainCards.size(); i++) {
                    if (mainCards.get(i).equals(card) && !cardsPicked.contains(i)){
                        Place toSummon = new Place(PLACE_NAME.HAND);
                        toSummon.setCard(card);
                        gamePlayController.summon(place, false);
                        cardsPicked.add(i);
                        gamePlayController.shuffleDeck();
                        break outerLoop;
                    }
            }
            printerAndScanner.printNextLine(wrongCard);
            cardName = printerAndScanner.scanNextLine();
        }
    }

    private boolean monsterOfThisTypeExistsInGraveYard(){
        ArrayList<Cards> graveYard = gamePlayController.getGamePlay().getMyGameBoard().getGraveyard();
        for (Cards cards : graveYard) {
            if (cards.getType().equals(monsterType))
                return true;
        }
        return false;
    }

    private boolean monsterOfThisTypeExistsInHand(){
        for (int i = 0; i < 6; i++) {
            if (gamePlayController.getGamePlay().getMyGameBoard().getPlace(i, PLACE_NAME.HAND).getCard()
                    .getType().equals(monsterType))
                return true;
        }
        return false;
    }

    private boolean monsterOfThisTypeExistsInDeck(){
        ArrayList<Cards> mainCards = gamePlayController.getGamePlay().getMyGameBoard().getMainCards();
        ArrayList<Integer> cardsPicked = gamePlayController.getGamePlay().getMyGameBoard().getCardsPicked();
        for (int i = 0; i < mainCards.size(); i++) {
            if (mainCards.get(i).getType().equals(monsterType) && !cardsPicked.contains(i))
                return true;
        }
        return false;
    }

}
