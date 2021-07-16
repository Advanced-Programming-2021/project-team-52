package sample.controller.specialbilities;

import sample.controller.GamePlayController;
import sample.controller.PrintBuilderController;
import sample.model.game.Place;
import sample.view.PrinterAndScanner;

import java.lang.reflect.Method;

public interface SpecialAbility {

    PrinterAndScanner printerAndScanner = PrinterAndScanner.getInstance();
    PrintBuilderController printBuilderController = PrintBuilderController.getInstance();

    public void run(GamePlayController gamePlayController, Place place);

    public void setMethod(Method method);

    public String getMethodName();
}
