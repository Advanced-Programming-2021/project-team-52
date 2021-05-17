package controller.ai;

import model.game.PHASE;
import model.game.Place;

public class CommunicatorBetweenAIAndGamePlay {
    private static CommunicatorBetweenAIAndGamePlay communicatorBetweenAIAndGamePlay = null;

    private CommunicatorBetweenAIAndGamePlay() {
    }

    public static CommunicatorBetweenAIAndGamePlay getInstance() {
        if (communicatorBetweenAIAndGamePlay == null)
            communicatorBetweenAIAndGamePlay = new CommunicatorBetweenAIAndGamePlay();
        return communicatorBetweenAIAndGamePlay;
    }

    public void summonMonster(){}

    public void setMonster(){}

    public void activeSpell(Place place) {
    }

    public void setSpell(Place place){}

    public boolean isGamePlayLookingForCommand() {
        return true;
    }

    public void setTrap(Place place){

    }

    public void endPhase(){}
}
