package sample.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;

import sample.model.Deck;
import sample.model.User;
import sample.model.cards.Cards;
import sample.model.tools.RegexPatterns;
import sample.model.tools.StringMessages;
import sample.view.PrinterAndScanner;

public class DeckController implements RegexPatterns, StringMessages {
    private static DeckController deckController = null;
    private static ArrayList<String> deckNames;
    private static PrintBuilderController printBuilderController = PrintBuilderController.getInstance();
    private static PrinterAndScanner printerAndScanner = PrinterAndScanner.getInstance();
    private static boolean addCardCheat = false;

    private DeckController() {
    }


    public static DeckController getInstance() {
        if (deckController == null)
            deckController = new DeckController();
        return deckController;
    }


//    public void start(User user) {
//        String command = printerAndScanner.scanNextLine();
//        while (!run(user, command)) {
//            command = printerAndScanner.scanNextLine();
//        }
//    }
//
//    public boolean run(User user, String command) {
//        Matcher matcher;
//        if ((matcher = RegexController.getMatcher(command, deckCreatePattern)) != null)
//            createDeck(matcher.group("deck"), user);
//        else if ((matcher = RegexController.getMatcher(command, deckDeletePattern)) != null)
//            deleteDeck(matcher.group("deck"), user);
//        else if ((matcher = RegexController.getMatcher(command, deckSetActivePattern)) != null)
//            activateDeck(matcher.group("deck"), user);
//        else if ((matcher = RegexController.getMatcher(command, cardShowPattern)) != null)
//            printerAndScanner.printNextLine(printBuilderController.
//                    showOneCard(Cards.getCard(matcher.group("card"))));
//        else if ((matcher = RegexController.getMatcher(command, deckAddCardPattern)) != null) {
//            if (matcher.group("addOrRemove").equals("add"))
//                addCardToDeck(matcher.group("card"), matcher.group("deck")
//                        , matcher.group("side") != null, user);
//            else if (matcher.group("addOrRemove").equals("rm"))
//                removeCardFromDeck(matcher.group("card"), matcher.group("deck")
//                        , matcher.group("side") != null, user);
//            else
//                printerAndScanner.printNextLine(invalidCommand);
//        } else if ((matcher = RegexController.getMatcher(command, deckShowPattern)) != null) {
//            if (RegexController.hasField(matcher, "all"))
//                showAllDecks(user);
//            else if (RegexController.hasField(matcher, "deck")) {
//                showDeck(user, matcher.group("deck"), RegexController.hasField(matcher, "side"));
//            } else if (RegexController.hasField(matcher, "card")) {
//                showAllUserCards(user);
//            } else
//                printerAndScanner.printNextLine(invalidCommand);
//        } else if ((matcher = RegexController.getMatcher(command, menuPattern)) != null) {
//            if (RegexController.hasField(matcher, "exit"))
//                return true;
//            else if (RegexController.hasField(matcher, "enter"))
//                printerAndScanner.printNextLine(menuNavigationIsNotPossible);
//            else if (RegexController.hasField(matcher, "showCurrent"))
//                showCurrent();
//            else
//                printerAndScanner.printNextLine(invalidCommand);
//        } else
//            printerAndScanner.printNextLine(invalidCommand);
//        return false;
//    }

    public String createDeck(String deckName, User user) {
        Deck deck = user.getDeckByName(deckName);
        if (deck != null) {
            return printBuilderController.DeckWithThisNameAlreadyExists(deckName);
        }
        Deck newDeck = new Deck(deckName);
        user.addDeck(newDeck);
        return deckCreatedSuccessfully;
    }

    public String deleteDeck(String deckName, User user) {
        Deck deck = user.getDeckByName(deckName);
        if (deckDoesNotExists(deckName, deck))
            return printBuilderController.deckWithThisNameDoesNotExist(deckName);
        // transfer deck cards to all cards
        ArrayList<String> userCards = user.getCards();
        userCards.addAll(deck.getAllMainCards());
        userCards.addAll(deck.getAllSideCards());
        user.deleteDeck(deckName);
//        printerAndScanner.printNextLine(deckDeletedSuccessfully);
        return deckDeletedSuccessfully;
    }

    public String activateDeck(String deckName, User user) {
        Deck deck = user.getDeckByName(deckName);
        if (deckDoesNotExists(deckName, deck))
            return printBuilderController.deckWithThisNameDoesNotExist(deckName);
        user.setActiveDeck(deck);
//        printerAndScanner.printNextLine(deckActivatedSuccessfully);
        return deckActivatedSuccessfully;
    }

    private boolean deckDoesNotExists(String deckName, Deck deck) {
        if (deck == null) {
//            printerAndScanner.printNextLine(printBuilderController.deckWithThisNameDoesNotExist(deckName));
            return true;
        }
        return false;
    }

    public String addCardToDeck(String cardName, String deckName, boolean isSide, User user) {
        if (cardDoesNotExists(cardName, user))
            return printBuilderController.cardWithThisNameDoesNotExist(cardName);
        Deck deck = user.getDeckByName(deckName);
        if (deckDoesNotExists(deckName, deck))
            return printBuilderController.deckWithThisNameDoesNotExist(deckName);
        if (deck.isDeckFull(isSide)) {
            if (!isSide)
                return mainDeckIsFull;
//                printerAndScanner.printNextLine(mainDeckIsFull);
            else
                return sideDeckIsFull;
//                printerAndScanner.printNextLine(sideDeckIsFull);
//            return;
        }
        Cards card = Cards.getCard(cardName);

        if (!addCardCheat) {
            if (card.getStatus().equals("Unlimited")) {
                if (checkNumberOfCardsWithDifferentStatus(cardName, deckName, deck, 3))
                    return printBuilderController.thereAreAlreadyThreeCardsWithThisNameInThisDeck
                            (cardName, deckName, 3);
            } else if (card.getStatus().equals("Half limited")) {
                if (checkNumberOfCardsWithDifferentStatus(cardName, deckName, deck, 2))
                    return printBuilderController.thereAreAlreadyThreeCardsWithThisNameInThisDeck
                            (cardName, deckName, 2);
            } else if (card.getStatus().equals("Limited")) {
                if (checkNumberOfCardsWithDifferentStatus(cardName, deckName, deck, 1))
                    return printBuilderController.thereAreAlreadyThreeCardsWithThisNameInThisDeck
                            (cardName, deckName, 1);
            }
        }

        user.removeCardFromCardsWithoutDeck(cardName);
        deck.addCard(cardName, isSide);
//        printerAndScanner.printNextLine(cardAddedToDeckSuccessfully);
        return cardAddedToDeckSuccessfully;
    }

    private boolean cardDoesNotExists(String cardName, User user) {
        if (!user.isCardWithThisNameExists(cardName)) {
//            printerAndScanner.printNextLine(printBuilderController.cardWithThisNameDoesNotExist(cardName));
            return true;
        }
        return false;
    }

    private boolean checkNumberOfCardsWithDifferentStatus(String cardName, String deckName, Deck deck,
                                                          int numberOfStatus) {
        if (deck.numberOfThisCardInDeck(cardName) >= numberOfStatus) {
//            printerAndScanner.printNextLine(printBuilderController.
//                    thereAreAlreadyThreeCardsWithThisNameInThisDeck(cardName, deckName, numberOfStatus));
            return true;
        }
        return false;
    }

    public String removeCardFromDeck(String cardName, String deckName, boolean isSide, User user) {
        Deck deck = user.getDeckByName(deckName);
        if (deckDoesNotExists(deckName, deck))
            return printBuilderController.deckWithThisNameDoesNotExist(deckName);
        if (!deck.isCardWithThisNameExists(cardName, isSide)) {
//            printerAndScanner.printNextLine(printBuilderController
//                    .cardWithThisNameDoesNotExistInThisDeck(cardName, isSide));
            return printBuilderController.cardWithThisNameDoesNotExistInThisDeck(cardName, isSide);
        }
        deck.removeCard(cardName, isSide);
        user.addCardToCardsWithoutDeck(cardName);
//        printerAndScanner.printNextLine(cardRemovedFormDeckSuccessfully);
        return cardRemovedFormDeckSuccessfully;
    }

    public void showAllDecks(User user) {
        printerAndScanner.printNextLine(printBuilderController.showAllDecks(user));
    }

    public void showDeck(User user, String deckName, boolean isSide) {
        Deck deck = user.getDeckByName(deckName);
        if (deckDoesNotExists(deckName, deck)) return;
        printerAndScanner.printNextLine(printBuilderController.showOneDeck(deck, isSide));
    }

    public String getMainDeckByString(String deckName, User user){
        Deck deck = user.getDeckByName(deckName);
        if (deckDoesNotExists(deckName, deck))
            return printBuilderController.deckWithThisNameDoesNotExist(deckName);
        ArrayList<String> cards = deck.getAllMainCards();
        Collections.sort(cards);
        StringBuilder response = new StringBuilder();
        for (String card : cards) {
            response.append(card).append("\n");
        }
        return response.toString();
    }
    public String getSideDeckByString(String deckName, User user){
        Deck deck = user.getDeckByName(deckName);
        if (deckDoesNotExists(deckName, deck))
            return printBuilderController.deckWithThisNameDoesNotExist(deckName);
        ArrayList<String> cards = deck.getAllSideCards();
        Collections.sort(cards);
        StringBuilder response = new StringBuilder();
        for (String card : cards) {
            response.append(card).append("\n");
        }
        return response.toString();
    }


    public void showAllUserCards(User user) {
        printerAndScanner.printNextLine(printBuilderController.showAllCardsOfUser(user.getCardsToJustShow()));
    }

    public void showCurrent() {
        printerAndScanner.printNextLine(showCurrentInDeckController);
    }

    public void setAddCardCheat(boolean addCardCheat) {
        DeckController.addCardCheat = addCardCheat;
    }

    public boolean isAddCardCheat() {
        return addCardCheat;
    }

}
