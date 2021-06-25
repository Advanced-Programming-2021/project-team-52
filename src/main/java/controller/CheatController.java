package controller;

import model.User;
import view.PrinterAndScanner;

public class CheatController {

    private static GamePlayController gamePlayController;
    private static PrintBuilderController printBuilderController;
    private static PrinterAndScanner printerAndScanner;

    {
        printBuilderController = PrintBuilderController.getInstance();
        printerAndScanner = PrinterAndScanner.getInstance();
    }

    private static void increaseMoney(User user, int amount) {
        int currentMoney = user.getBalance();
        user.setBalance(currentMoney + amount);
    }

    private static void addCard() {
        DeckController.getInstance().setAddCardCheat(!DeckController.getInstance().isAddCardCheat());
    }
}
