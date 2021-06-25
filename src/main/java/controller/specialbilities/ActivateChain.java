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

    public void setStatus(STATUS status) {
        this.status = status;
    }

    public void destroySpellAndTraps() {
        int numberOfDestroyableCards = 0;
        GamePlayController opponentGamePlayController = gamePlayController.getGamePlay().getOpponentGamePlayController();
        for (int i = 1; i < 6; i++) {
            if (gamePlayController.getGamePlay().getMyGameBoard().getPlace(i, PLACE_NAME.SPELL_AND_TRAP).getCard() != null)
                numberOfDestroyableCards++;
            if (opponentGamePlayController.getGamePlay().getMyGameBoard().getPlace(i, PLACE_NAME.SPELL_AND_TRAP).getCard() != null)
                numberOfDestroyableCards++;
        }
        numberOfDestroyableCards--;
        int amount = Math.min(this.amount, numberOfDestroyableCards);
        String command;
        Place toKill;
        while (amount > 0) {
            printerAndScanner.printNextLine(askForSpellInGameToDestroy);
            command = printerAndScanner.scanNextLine();
            if (command.equalsIgnoreCase("cancel"))
                break;
            if (!command.matches("^[+-]?[12345]$"))
                printerAndScanner.printNextLine(invalidInput);
            else {
                GamePlayController gamePlayController = command.matches("^-\\d$") ?
                        opponentGamePlayController : this.gamePlayController;
                toKill = gamePlayController.getGamePlay().getMyGameBoard().getPlace(Math.abs(Integer.parseInt(command)), PLACE_NAME.SPELL_AND_TRAP);
                if (toKill.getCard() == null || toKill == place)
                    printerAndScanner.printNextLine(wrongCard);
                else {
                    toKill.getTemporaryFeatures().add(TEMPORARY_FEATURES.FORCE_KILL);
                    gamePlayController.killCard(toKill);
                    amount--;
                }
            }
        }
    }

    public void neutralizeTrap() {
        gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getUniversalHistory().add("neutralizeTrap");
    }

    public void redirectAttack() {
        GamePlayController opponentGamePlayController = gamePlayController.getGamePlay().getOpponentGamePlayController();
        int amountToDamage = opponentGamePlayController.getGamePlay().getUniversalHistory().contains("preventDamageFromTrap") ?
                0 : ((MonsterZone) place.getAffect()).getAttack() * -1;
        opponentGamePlayController.getGamePlay().getMyGameBoard().changeHealth(amountToDamage);
    }

    public void destroyAllEnemyMonstersInThisStatus() {
        GeneralSpecialAbility.killAllMonsters(true, gamePlayController, false, status);
    }

    public void destroySpellWhileActivating() {
        gamePlayController.getGamePlay().getOpponentGamePlayController().killCard(place.getAffect());
    }

    public void endBattlePhase() {
        gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getUniversalHistory().add("endBattlePhase");
    }

    public void killAffect() {
        gamePlayController.getGamePlay().getOpponentGamePlayController().killCard(place.getAffect());
    }

    public void preventAttack() {
        gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getUniversalHistory().add("preventAttack");
    }

    public void preventSummonOrSpecialSummon() {
        gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getUniversalHistory().add("preventSummon");
    }

    public void preventDamageFromTrap() {
        gamePlayController.getGamePlay().getUniversalHistory().add("preventDamageFromTrap");
    }
}
