package controller;

public class DuelController {
    private static Duel duel;
    private PrintBulider printBulider;
    private printerAndScanner printerAndScanner;

    public DuelController(){}
    public static Duel getInstance(){}
    public void run(User user){}
    private boolean checkBeforeStartingDuel(String opponent, String roundCard){}
    private void caculateScores(int turn){}
    private void transferCardsBetweenMainAndSideDeck(){}
    public void surrender(){}
    public void serDuelWinnerByCheat(){}
    public void changeHealthAmount(int amount, boolean changeHost){}

}
