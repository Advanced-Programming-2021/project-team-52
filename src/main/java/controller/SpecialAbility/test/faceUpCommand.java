package controller.SpecialAbility.test;

import controller.SpecialAbility.SpecialCommand;
import controller.SpecialAbility.Storage;
import model.game.Place;
import model.game.STATUS;

public class faceUpCommand extends Storage implements SpecialCommand {
    public void run(){
        place.setStatus(status);
    }
}
