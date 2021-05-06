package controller;


import model.Deck;
import model.User;

public class DuelController {
    private static Duel duel;
    private PrintBulider printBulider;
    private printerAndScanner printerAndScanner;

    public DuelController(){}
    public static Duel getInstance(){}
    public void run(User user){}
    private boolean checkBeforeStartingDuel(String opponent, String roundCard){}
    private void calculateScores(int gameRounds, User user){
        //Loser in a 1-round game
        if(user.getCards().size() == 0 && gameRounds == 1){
            user.setScore(user.getScore() + 100);
        } //winner in a 1-round game
        else if (user.getCards().size() != 0 && gameRounds == 1){
            user.setScore(user.getScore() + 1000);
        } //loser in a 3-round game
        else if(user.getCards().size() == 0 && gameRounds == 3){
            user.setScore(user.getScore()+300);
        } // winner in a 3-round game
        else if(user.getCards().size() != 0 && gameRounds == 3){
            user.setScore(user.getScore()+3000);
        }
    }
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
    public void setDuelWinnerByCheat(){}
    public void changeHealthAmount(int amount, boolean changeHost){}

}
