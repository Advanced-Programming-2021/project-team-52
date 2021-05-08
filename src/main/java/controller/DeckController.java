package controller;

import java.util.ArrayList;
import java.util.regex.Matcher;

import model.Deck;
import model.User;
import model.tools.RegexPatterns;
import model.tools.StringMessages;
import view.PrinterAndScanner;

public class DeckController implements RegexPatterns, StringMessages {
    private static DeckController deckController = null;
    private static ArrayList<String> deckNames;
    private static PrintBuilderController printBuilderController;
    private static PrinterAndScanner printerAndScanner;

    private DeckController() {
    }



    public static DeckController getInstance() {
        if (deckController != null)
            deckController = new DeckController();
        return deckController;
    }

    public void run(User user) {
        String command = printerAndScanner.scanNextLine();
        Matcher matcher;
        while (true){
            if ((matcher = RegexController.getMatcher(command, deckCreatePattern)) != null)
                createDeck(matcher.group("deck"), user);
            else if((matcher = RegexController.getMatcher(command, deckDeletePattern)) != null)
                deleteDeck(matcher.group("deck"), user);
            else if((matcher = RegexController.getMatcher(command, deckSetActivePattern)) != null)
                activateDeck(matcher.group("deck"), user);
            if ((matcher = RegexController.getMatcher(command, deckAddCardPattern)) != null){

            }

        }
    }

    public static void createDeck(String deckName, User user){
        if(user.getDeckByName(deckName) != null){
            printerAndScanner.printNextLine(printBuilderController.DeckWithThisNameAlreadyExists(deckName));
            return;
        }
        Deck deck = new Deck(deckName);
        user.addDeck(deck);
        printerAndScanner.printNextLine(deckCreatedSuccessfully);
    }

    public static void deleteDeck(String deckName, User user){
        Deck deck = user.getDeckByName(deckName);
        if(deck == null){
            printerAndScanner.printNextLine(printBuilderController.deckWithThisNameDoesNotExist(deckName));
            return;
        }
        // transfer deck cards to all cards
        ArrayList<String>userCards = user.getCards();
        userCards.addAll(deck.getAllMainCards());
        userCards.addAll(deck.getAllSideCards());

        user.deleteDeck(deckName);
        printerAndScanner.printNextLine(deckDeletedSuccessfully);
    }

    public static void activateDeck(String deckName, User user){
        Deck deck = user.getDeckByName(deckName);
        if(deck == null){
            printerAndScanner.printNextLine(printBuilderController.deckWithThisNameDoesNotExist(deckName));
            return;
        }
        user.setActiveDeck(deck);
        printerAndScanner.printNextLine(deckActivatedSuccessfully);
    }



//    private static void addDeck(Deck deck, String name) {
//        if (deck == null) {
//            new Deck(name);
//            deckNames.add(name);
//        }
//    }
//
//    public static void DeleteDeck(String name) {
//        if (deck != null) {
//            deckNames.remove(name);
//        }
//    }

    public void checkBeforeAddOrRemoveCard(String deckName, String cardName, boolean isSide, boolean isAdding) {
        if (isAdding) {
            if (!(cards.allCards.contian(cardName))) {
                System.out.println("card with name" + cardName + "does not exist");
            } else if (!(deckNames.contains(deckName))) {
                System.out.println("deck with name" + deckName + "does not exist");
            } else if (!isSide && deck.getMainDeckCardCount() > 60) {
                System.out.println("main deck is full");
            } else if (isSide && deck.getSideDeckCardCount() > 15) {
                System.out.println("side deck is full");
            } else if (deck.getEachCardCount(cardName) > 3) {
                System.out.println("there are already three cards with name" + cardName + " in deck" + deckName);
            } else {
                if (!isSide) {
                    int cardCount = deck.getMainDeckCardCount() + 1;
                    deck.addCard(deck, cardName);
                    deck.setMainDeckCardCount(cardCount);
                    System.out.println("card added to deck successfully");
                } else {
                    int cardCount = deck.getSideDeckCardCount() + 1;
                    deck.addCard(deck, cardName);
                    deck.setSideDeckCardCount(cardCount);
                    System.out.println("card added to deck successfully");
                }
            }
        } else {
            if (!(cards.allCards.contian(cardName))) {
                System.out.println("card with name" + cardName + "does not exist");
            } else if (deck.getAllMainCards().contains(cardName)) {
                System.out.println("card with name" + cardName + "does not exist in main deck");
            } else if (deck.getAllSideCards().contains(cardName)) {
                System.out.println("card with name" + cardName + "does not exist in side deck");
            } else {
                if (isSide) {
                    int cardCount = deck.getSideDeckCardCount() - 1;
                    deck.removeCard(deck, cardName);
                    deck.setSideDeckCardCount(cardCount);
                } else {
                    int cardCount = deck.getMainDeckCardCount() - 1;
                    deck.removeCard(deck, cardName);
                    deck.setMainDeckCardCount(cardCount);
                }
            }
        }
    }
}
