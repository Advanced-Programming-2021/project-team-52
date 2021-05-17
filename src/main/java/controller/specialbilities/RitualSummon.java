package controller.specialbilities;

import controller.GamePlayController;
import model.cards.monster.MonsterCards;
import model.game.PLACE_NAME;
import model.game.Place;
import model.game.STATUS;
import model.tools.StringMessages;

import java.lang.reflect.Method;

public class RitualSummon implements SpecialAbility, StringMessages {

    private Method method;
    private String methodName;
    private GamePlayController gamePlayController;
    private Place place;

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

    //TODO in dock it says that this can be cancelled but doest say how
    public void ritualSummon(){//TODO ++
        Place toSummon = getRitualCard();
        String[] tributes;
        while(true){
            printerAndScanner.printNextLine(ritualSummonTribute);
            tributes = printerAndScanner.scanNextLine().split("\\s+");
            if (checkCredibility(tributes, ((MonsterCards)toSummon.getCard()).getLevel()))
                break;
        }
        sacrificeTributes(tributes);
        gamePlayController.placeCard(toSummon, true, STATUS.ATTACK);
    }

    private Place getRitualCard() {
        Place toSummon = null;
        while (true){
            printerAndScanner.printNextLine(pleaseEnterTheCardThatYouWantToRitualSummon);
            String input = printerAndScanner.scanNextLine();
            if (input.matches("\\D")){
                printerAndScanner.printNextLine(youShouldRitualSummonRightNow);
                continue;
            }
            toSummon = gamePlayController.
                    getGamePlay().getMyGameBoard().getPlace(Integer.parseInt(input), PLACE_NAME.MONSTER);
            if (toSummon.getCard().getType().equals("Ritual"))
                break;
            printerAndScanner.printNextLine(youShouldRitualSummonRightNow);
        }
        return toSummon;
    }

    private boolean checkCredibility(String[] tributes, int toEqual){
        for (String tribute : tributes) {
            if (tribute.matches("\\D")) {
                printerAndScanner.printNextLine(youShouldRitualSummonRightNow);
                return false;
            }
            else if (gamePlayController.getGamePlay().getMyGameBoard().
                    getPlace(Integer.parseInt(tribute), PLACE_NAME.MONSTER).getCard() == null)
                return false;
        }
        for (int i = 0; i < tributes.length; i++) {
            for (int j = i+1; j < tributes.length; j++) {
                if (tributes[i].equals(tributes[j]))
                    return false;
            }
        }
        int sum = 0;
        for (String tribute : tributes) {
            sum += ((MonsterCards) gamePlayController.getGamePlay().
                    getMyGameBoard().getPlace(Integer.parseInt(tribute), PLACE_NAME.MONSTER).getCard()).getLevel();
        }
        if (sum != toEqual){
            printerAndScanner.printNextLine(wrongCardCombination);
            return false;
        }
        return true;
    }

    private void sacrificeTributes(String[] tributes){
        for (String tribute : tributes) {
            gamePlayController.getGamePlay().getMyGameBoard().killCards(
                    gamePlayController,
                    gamePlayController.getGamePlay().getMyGameBoard().getPlace(
                            Integer.parseInt(tribute), PLACE_NAME.MONSTER
                    )
            );
        }
    }
}
