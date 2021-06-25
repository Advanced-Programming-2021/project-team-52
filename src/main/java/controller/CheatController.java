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

    private static void increaseMoney(User user, int amount){
        int currentMoney = user.getBalance();
        user.setBalance(currentMoney + amount);
    }
    private static void addCard(){
    DeckController.getInstance().setAddCardCheat(!DeckController.getInstance().isAddCardCheat());
    }
    
    //we have them in their own appropriate class
//    public static void increaseLp(int amount){
//        gamePlayController.increaseLpWithCheat(amount);
//    }
//    public static void setDualWinner(String name){
//        duelController.setDuelWinnerByCheat(name);
//    }
    
}
