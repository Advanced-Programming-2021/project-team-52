package controller;

import model.User;

public class CheatController {

//    private static DuelController duelController;
//    private static GamePlayController gamePlayController;

    private static void increaseMoney(User user, int amount){
        int currentMoney = user.getBalance();
        user.setBalance(currentMoney + amount);
    }
    private static void addCard(User user, String cardName){
        user.addCards(cardName);
    }
    
    //we have them in their own appropriate class
//    public static void increaseLp(int amount){
//        gamePlayController.increaseLpWithCheat(amount);
//    }
//    public static void setDualWinner(String name){
//        duelController.setDuelWinnerByCheat(name);
//    }
    
}
