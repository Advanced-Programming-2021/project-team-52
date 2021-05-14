package controller.specialbilities;

import controller.GamePlayController;
import controller.PrintBuilderController;
import model.game.GamePlay;
import model.game.Place;
import view.PrinterAndScanner;

import java.lang.reflect.Method;

public interface SpecialAbility {

    PrinterAndScanner printerAndScanner = PrinterAndScanner.getInstance();
    PrintBuilderController printBuilderController = PrintBuilderController.getInstance();

    public void run(GamePlayController gamePlayController, Place place);

    public void setMethod(Method method);

    public String getMethodName();
}
