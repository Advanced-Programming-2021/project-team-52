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
    private void transferCardsBetweenMainAndSideDeck(String cardName, Deck deck){
        String deckSituation;
        if (deck == null) {
            if(deck.getAllMainCards().contains(cardName)){
                deckSituation = "isMainDeck";
            } else if(deck.getAllSideCards().contains(cardName)){
                deckSituation = "isSideDeck";
            } else {
                System.out.println("invalid command");
                deckSituation = "invalid";
            }
            if (deckSituation.equals("isMainDeck") && deck.getMainDeckCardCount()>40 && deck.getSideDeckCardCount()<15){
                deck.getAllSideCards().remove(cardName);
                deck.getAllMainCards().add(cardName);
            }
            if(deckSituation.equals("isSideDeck") && deck.getSideDeckCardCount() > 0 && deck.getMainDeckCardCount() < 60){
                deck.getAllMainCards().remove(cardName);
                deck.getAllSideCards().add(cardName);
            }
        }
    }
    public void surrender(){}
    public void serDuelWinnerByCheat(){}
    public void changeHealthAmount(int amount, boolean changeHost){}

}
