package controller;

import java.util.ArrayList;
import java.util.regex.Matcher;

import model.Deck;
import model.User;
import model.cards.Cards;
import model.cards.spell.SpellCards;
import model.tools.RegexPatterns;
import model.tools.StringMessages;
import view.PrinterAndScanner;

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


    public void start(User user) {
        String command = printerAndScanner.scanNextLine();
        while (!run(user, command)) {
            command = printerAndScanner.scanNextLine();
        }
    }

    public boolean run(User user, String command) {
        Matcher matcher;
        if ((matcher = RegexController.getMatcher(command, deckCreatePattern)) != null)
            createDeck(matcher.group("deck"), user);
        else if ((matcher = RegexController.getMatcher(command, deckDeletePattern)) != null)
            deleteDeck(matcher.group("deck"), user);
        else if ((matcher = RegexController.getMatcher(command, deckSetActivePattern)) != null)
            activateDeck(matcher.group("deck"), user);
        else if((matcher = RegexController.getMatcher(command, cardShowPattern)) != null)
            printerAndScanner.printNextLine(printBuilderController.
                    showOneCard(Cards.getCard(matcher.group("card"))));
        else if ((matcher = RegexController.getMatcher(command, deckAddCardPattern)) != null) {
            if (matcher.group("addOrRemove").equals("add"))
                addCardToDeck(matcher.group("card"), matcher.group("deck")
                        , matcher.group("side") != null, user);
            else if (matcher.group("addOrRemove").equals("rm"))
                removeCardFromDeck(matcher.group("card"), matcher.group("deck")
                        , matcher.group("side") != null, user);
            else
                printerAndScanner.printNextLine(invalidCommand);
        } else if ((matcher = RegexController.getMatcher(command, deckShowPattern)) != null) {
            if (RegexController.hasField(matcher, "all"))
                showAllDecks(user);
            else if (RegexController.hasField(matcher, "deck")) {
                showDeck(user, matcher.group("deck"), RegexController.hasField(matcher, "side"));
            } else if (RegexController.hasField(matcher, "card")) {
                showAllUserCards(user);
            } else
                printerAndScanner.printNextLine(invalidCommand);
        } else if((matcher = RegexController.getMatcher(command, menuPattern)) != null) {
            if (RegexController.hasField(matcher, "exit"))
                return true;
            else if (RegexController.hasField(matcher, "enter"))
                printerAndScanner.printNextLine(menuNavigationIsNotPossible);
            else if (RegexController.hasField(matcher, "showCurrent"))
                showCurrent();
            else
                printerAndScanner.printNextLine(invalidCommand);
        } else
            printerAndScanner.printNextLine(invalidCommand);
        return false;
    }

    public void createDeck(String deckName, User user) {
        Deck deck = user.getDeckByName(deckName);
        if (deck != null) {
            printerAndScanner.printNextLine(printBuilderController.DeckWithThisNameAlreadyExists(deckName));
            return;
        }
        Deck newDeck = new Deck(deckName);
        user.addDeck(newDeck);
        printerAndScanner.printNextLine(deckCreatedSuccessfully);
    }

    public void deleteDeck(String deckName, User user) {
        Deck deck = user.getDeckByName(deckName);
        if (deckDoesNotExists(deckName, deck)) return;
        // transfer deck cards to all cards
        ArrayList<String> userCards = user.getCards();
        userCards.addAll(deck.getAllMainCards());
        userCards.addAll(deck.getAllSideCards());
        user.deleteDeck(deckName);
        printerAndScanner.printNextLine(deckDeletedSuccessfully);
    }

    public void activateDeck(String deckName, User user) {
        Deck deck = user.getDeckByName(deckName);
        if (deckDoesNotExists(deckName, deck)) return;
        user.setActiveDeck(deck);
        printerAndScanner.printNextLine(deckActivatedSuccessfully);
    }

    private boolean deckDoesNotExists(String deckName, Deck deck) {
        if (deck == null) {
            printerAndScanner.printNextLine(printBuilderController.deckWithThisNameDoesNotExist(deckName));
            return true;
        }
        return false;
    }

    public void addCardToDeck(String cardName, String deckName, boolean isSide, User user) {
        if (cardDoesNotExists(cardName, user)) return;
        Deck deck = user.getDeckByName(deckName);
        if (deckDoesNotExists(deckName, deck)) return;
        if (deck.isDeckFull(isSide)) {
            if (!isSide)
                printerAndScanner.printNextLine(mainDeckIsFull);
            else
                printerAndScanner.printNextLine(sideDeckIsFull);
            return;
        }
        Cards card = Cards.getCard(cardName);

        if (!addCardCheat) {
            if (card.getStatus().equals("Unlimited")) {
                if (checkNumberOfCardsWithDifferentStatus(cardName, deckName, deck, 3)) return;
            }else if (card.getStatus().equals("Half limited")) {
                if (checkNumberOfCardsWithDifferentStatus(cardName, deckName, deck, 2)) return;
            }else if (card.getStatus().equals("Limited")) {
                if (checkNumberOfCardsWithDifferentStatus(cardName, deckName, deck, 1)) return;
            }
        }

        user.removeCardFromCardsWithoutDeck(cardName);
        deck.addCard(cardName, isSide);
        printerAndScanner.printNextLine(cardAddedToDeckSuccessfully);
    }

    private boolean cardDoesNotExists(String cardName, User user) {
        if (!user.isCardWithThisNameExists(cardName)) {
            printerAndScanner.printNextLine(printBuilderController.cardWithThisNameDoesNotExist(cardName));
            return true;
        }
        return false;
    }

    private boolean checkNumberOfCardsWithDifferentStatus(String cardName, String deckName, Deck deck,
                                                          int numberOfStatus) {
        if (deck.numberOfThisCardInDeck(cardName) >= numberOfStatus) {
            printerAndScanner.printNextLine(printBuilderController.
                    thereAreAlreadyThreeCardsWithThisNameInThisDeck(cardName, deckName, numberOfStatus));
            return true;
        }
        return false;
    }

    public void removeCardFromDeck(String cardName, String deckName, boolean isSide, User user) {
        Deck deck = user.getDeckByName(deckName);
        if (deckDoesNotExists(deckName, deck)) return;
        if (!deck.isCardWithThisNameExists(cardName, isSide)) {
            printerAndScanner.printNextLine(printBuilderController
                    .cardWithThisNameDoesNotExistInThisDeck(cardName, isSide));
            return;
        }
        deck.removeCard(cardName, isSide);
        user.addCardToCardsWithoutDeck(cardName);
        printerAndScanner.printNextLine(cardRemovedFormDeckSuccessfully);
    }

    public void showAllDecks(User user) {
        printerAndScanner.printNextLine(printBuilderController.showAllDecks(user));
    }

    public void showDeck(User user, String deckName, boolean isSide) {
        Deck deck = user.getDeckByName(deckName);
        if (deckDoesNotExists(deckName, deck)) return;
        printerAndScanner.printNextLine(printBuilderController.showOneDeck(deck, isSide));
    }

    public void showAllUserCards(User user) {
        printerAndScanner.printNextLine(printBuilderController.showAllCardsOfUser(user.getCardsToJustShow()));
    }

    public void showCurrent(){
        printerAndScanner.printNextLine(showCurrentInDeckController);
    }

    public void setAddCardCheat(boolean addCardCheat) {
        DeckController.addCardCheat = addCardCheat;
    }

    public boolean isAddCardCheat() {
        return addCardCheat;
    }

}
