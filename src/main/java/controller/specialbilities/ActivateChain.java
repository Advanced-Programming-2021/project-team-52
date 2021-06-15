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

    public void setStatus(STATUS status) {
        this.status = status;
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
        gamePlayController.getGamePlay().getOpponentGamePlayController().killCard(place);
    }

    public void neutralizeTrap(){ //TODO ++
        gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getUniversalHistory().add("neutralizeTrap");
    }

    public void redirectAttack(){ //TODO ++
        gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getMyGameBoard()
                .changeHealth(((MonsterZone) place.getAffect()).getAttack() * -1);
    }

    public void destroyAllEnemyMonstersInThisStatus(){ //TODO ++
        GeneralSpecialAbility.killAllMonsters(true, gamePlayController, false, status);
    }

    public void stopSpecialSummon(){
        //TODO
        gamePlayController.getGamePlay().getMyGameBoard().changeHealth(amount * -1);
        gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getUniversalHistory().add("cannotSpecialSummon");
        gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getUniversalHistory().add("cannotSummon");
    }

    public void destroySpellWhileActivating(){ //TODO ++
            gamePlayController.getGamePlay().getOpponentGamePlayController().killCard(place.getAffect());
    }

    public void endBattlePhase(){ //TODO ++
        gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getUniversalHistory().add("endBattlePhase");
    }

    public void killAffect(){ //TODO ++
        gamePlayController.getGamePlay().getOpponentGamePlayController().killCard(place.getAffect());
    }

    public void preventAttack(){ //TODO ++
        gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getUniversalHistory().add("preventAttack");
    }

    public void preventNextChain(){
        gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getUniversalHistory().add("preventChain");
    }
}
