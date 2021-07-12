package sample.controller.specialbilities;

import sample.controller.GamePlayController;
import sample.model.cards.monster.MonsterCards;
import sample.model.game.PLACE_NAME;
import sample.model.game.Place;
import sample.model.tools.StringMessages;

import java.lang.reflect.Method;

public class RitualSummon implements SpecialAbility, StringMessages {

    private Method method;
    private String methodName;
    private GamePlayController gamePlayController;
    private Place place;

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

    public void ritualSummon() {
        Place toSummon = this.place.getAffect();
        String[] tributes;
        gamePlayController.getMyCommunicator().askOptions(ritualSummonTribute, "ok");
        gamePlayController.takeCommand();
        while (true) {
            gamePlayController.getMyCommunicator().ritualSummon();
            tributes = gamePlayController.takeCommand().split("\\s+");
            if (checkCredibility(tributes, ((MonsterCards) toSummon.getCard()).getLevel()))
                break;
        }
        sacrificeTributes(tributes);
        gamePlayController.placeCard(toSummon, true, gamePlayController.askStatus());
    }

    private boolean checkCredibility(String[] tributes, int toEqual) {
        for (String tribute : tributes) {
            if (tribute.matches("[^12345]")) {
                printerAndScanner.printNextLine(youShouldRitualSummonRightNow);
                return false;
            } else if (gamePlayController.getGamePlay().getMyGameBoard().
                    getPlace(Integer.parseInt(tribute), PLACE_NAME.MONSTER).getCard() == null) {
                printerAndScanner.printNextLine(wrongCardCombination);
                return false;
            }
        }
        for (int i = 0; i < tributes.length; i++) {
            for (int j = i + 1; j < tributes.length; j++) {
                if (tributes[i].equals(tributes[j]))
                    return false;
            }
        }
        int sum = 0;
        for (String tribute : tributes) {
            sum += ((MonsterCards) gamePlayController.getGamePlay().
                    getMyGameBoard().getPlace(Integer.parseInt(tribute), PLACE_NAME.MONSTER).getCard()).getLevel();
        }
        if (sum != toEqual) {
            printerAndScanner.printNextLine(wrongCardCombination);
            return false;
        }
        return true;
    }

    private void sacrificeTributes(String[] tributes) {
        for (String tribute : tributes) {
            gamePlayController.killCard(
                    gamePlayController.getGamePlay().getMyGameBoard().getPlace(
                            Integer.parseInt(tribute), PLACE_NAME.MONSTER
                    )
            );
        }
    }
}
