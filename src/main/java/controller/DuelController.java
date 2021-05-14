package controller;

import model.Deck;
import model.User;
import model.tools.RegexPatterns;
import model.tools.StringMessages;
import view.PrinterAndScanner;

import java.util.regex.Matcher;

public class DuelController implements RegexPatterns, StringMessages {
    private static DuelController duelController;
    private PrintBuilderController printBuilderController;
    private PrinterAndScanner printerAndScanner;

    {
        printBuilderController = PrintBuilderController.getInstance();
        printerAndScanner = PrinterAndScanner.getInstance();
    }

    public DuelController() {
    }

    public static DuelController getInstance() {
        if (duelController == null) {
            duelController = new DuelController();
        }
        return duelController;
    }

    public void run(User user) {
        String command = printerAndScanner.scanNextLine();
        Matcher matcher;
        while (true) {
            if ((matcher = RegexController.getMatcher(command, newDuelPattern)) != null)
                startDuel(user, matcher.group("secondPlayer"), matcher.group("rounds"));
//            else if ()
            command = printerAndScanner.scanNextLine();
        }
    }

    //    private boolean checkBeforeStartingDuel(String opponent, String roundCard){}
    private void calculateScores(int gameRounds, User user) {
        //Loser in a 1-round game
        if (user.getCards().size() == 0 && gameRounds == 1) {
            user.setScore(user.getScore() + 100);
        } //winner in a 1-round game
        else if (user.getCards().size() != 0 && gameRounds == 1) {
            user.setScore(user.getScore() + 1000);
        } //loser in a 3-round game
        else if (user.getCards().size() == 0 && gameRounds == 3) {
            user.setScore(user.getScore() + 300);
        } // winner in a 3-round game
        else if (user.getCards().size() != 0 && gameRounds == 3) {
            user.setScore(user.getScore() + 3000);
        }
    }

    private void transferCardsBetweenMainAndSideDeck(String cardName, Deck deck) {
        String deckSituation;
        if (deck == null) {
            if (deck.getAllMainCards().contains(cardName)) {
                deckSituation = "isMainDeck";
            } else if (deck.getAllSideCards().contains(cardName)) {
                deckSituation = "isSideDeck";
            } else {
                System.out.println("invalid command");
                deckSituation = "invalid";
            }
            if (deckSituation.equals("isMainDeck") && deck.getMainDeckCardCount() > 40 && deck.getSideDeckCardCount() < 15) {
                deck.getAllSideCards().remove(cardName);
                deck.getAllMainCards().add(cardName);
            }
            if (deckSituation.equals("isSideDeck") && deck.getSideDeckCardCount() > 0 && deck.getMainDeckCardCount() < 60) {
                deck.getAllMainCards().remove(cardName);
                deck.getAllSideCards().add(cardName);
            }
        }
    }

    public void setDuelWinnerByCheat() {}

    // todo : player number 2 ??????
    public void startDuel(User user, String opponentName, String numberOfRounds) {
        User opponentUser = LoginController.getUserByUsername(opponentName);
        if (opponentUser == null) {
            printerAndScanner.printNextLine(thereIsNoPlayerWithThisUsername);
            return;
        }
        Deck userActiveDeck = user.getActiveDeck();
        Deck opponentUserActiveDeck = opponentUser.getActiveDeck();
        if (userActiveDeck == null) {
            printerAndScanner.printNextLine(printBuilderController.userHasNoActiveDeck(user.getUsername()));
            return;
        }
        if (opponentUserActiveDeck == null) {
            printerAndScanner.printNextLine(printBuilderController.userHasNoActiveDeck(opponentName));
            return;
        }
        if (!userActiveDeck.isDeckValid()) {
            printerAndScanner.printNextLine(printBuilderController.userDeckIsInvalid(user.getUsername()));
            return;
        }
        if(!opponentUserActiveDeck.isDeckValid()){
            printerAndScanner.printNextLine(printBuilderController.userDeckIsInvalid(opponentName));
            return;
        }
        int numberOfRoundsInInteger = Integer.parseInt(numberOfRounds);
        if(numberOfRoundsInInteger != 1 && numberOfRoundsInInteger != 3){
            printerAndScanner.printNextLine(numberOfRoundsIsNotSupported);
            return;
        }
        // start game
    }

}
