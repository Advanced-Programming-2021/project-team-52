package controller.specialbilities;

import controller.GamePlayController;
import model.game.*;
import model.tools.StringMessages;

import java.lang.reflect.Method;

public class ActivateChain implements SpecialAbility, StringMessages {

    private Method method;
    private String methodName;
    private GamePlayController gamePlayController;
    private Place place;
    private int amount;
    private STATUS status;

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

    public void destroySpellAndTraps(){ //TODO ++
        int amount = this.amount;
        int spellAndTrapCards = 0;
        for (int i = 1; i < 6; i++) {
            if (gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getMyGameBoard().getPlace
                    (i, PLACE_NAME.SPELL_AND_TRAP).getCard() != null)
                spellAndTrapCards++;
        }
        while (spellAndTrapCards > 0 && amount > 0){
            printerAndScanner.printNextLine(doOnceMore);
            if (printerAndScanner.scanNextLine().equalsIgnoreCase("no"))
                break;
            printerAndScanner.printNextLine(askForPlace);
            int spellOrTrapToDestroy = printerAndScanner.scanNextInt();
            while (gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getMyGameBoard().getPlace
                    (spellOrTrapToDestroy, PLACE_NAME.SPELL_AND_TRAP).getCard() == null){
                printerAndScanner.printNextLine(wrongCard);
                spellOrTrapToDestroy = printerAndScanner.scanNextInt();
            }
            destroyASpellOrTrap(spellOrTrapToDestroy);
            spellAndTrapCards--;
            amount--;
        }
    }

    private void destroyASpellOrTrap(int number){
        Place toKill = gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getMyGameBoard().getPlace
                (number, PLACE_NAME.SPELL_AND_TRAP);
        gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getMyGameBoard().killCards(
                gamePlayController.getGamePlay().getOpponentGamePlayController(), place);
    }

    public void neutralizeTrap(){ //TODO ++
        gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getUniversalHistory().add("neutralizeTrap");
    }

    public void redirectAttack(){
        gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getMyGameBoard()
                .changeHealth(((MonsterZone) place).getAttack() * -1);
    }

    public void destroyAllEnemyMonstersInThisStatus(){
        GeneralSpecialAbility.killAllMonsters(true, gamePlayController, false, status);
    }

    public void stopSpecialSummon(){
        //TODO
        gamePlayController.getGamePlay().getMyGameBoard().changeHealth(amount * -1);
        gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getUniversalHistory().add("cannotSpecialSummon");
        gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getUniversalHistory().add("cannotSummon");
    }

    public void destroySpellWhileActivating(){
        boolean handIsNotEmpty = false;
        for (int i = 0; i < 6; i++) {
            if (gamePlayController.getGamePlay().getMyGameBoard().getPlace(i, PLACE_NAME.HAND).getCard() != null)
                handIsNotEmpty = true;
        }
        if (handIsNotEmpty) {
            printerAndScanner.printNextLine(askForPlace);
            int toRemove = printerAndScanner.scanNextInt();
            while (gamePlayController.getGamePlay().getMyGameBoard().getPlace(toRemove, PLACE_NAME.HAND) == null){
                printerAndScanner.printNextLine(wrongCard);
                toRemove = printerAndScanner.scanNextInt();
            }
            gamePlayController.getGamePlay().getMyGameBoard().getGraveyard().add(
                    gamePlayController.getGamePlay().getMyGameBoard().getPlace(toRemove, PLACE_NAME.HAND).getCard());
            gamePlayController.getGamePlay().getMyGameBoard().getPlace(toRemove, PLACE_NAME.HAND).setCard(null);
            place.killCard();
        }
    }

    public void endBattlePhase(){
        gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getUniversalHistory().add("endBattlePhase");
    }
}
