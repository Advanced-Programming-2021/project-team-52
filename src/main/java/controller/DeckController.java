package controller;

import java.util.ArrayList;
import java.util.regex.Matcher;

import model.Deck;
import model.User;
import model.cards.Cards;
import model.tools.RegexPatterns;
import model.tools.StringMessages;
import view.PrinterAndScanner;

// todo : ask for lexicography order
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

    public void setAddCardCheat(boolean addCardCheat) {
        DeckController.addCardCheat = addCardCheat;
    }

    public boolean isAddCardCheat() {
        return addCardCheat;
    }

    public void run(User user) {
        String command = printerAndScanner.scanNextLine();
        Matcher matcher;
        while (true) {
            if ((matcher = RegexController.getMatcher(command, deckCreatePattern)) != null)
                createDeck(matcher.group("deck"), user);
            else if ((matcher = RegexController.getMatcher(command, deckDeletePattern)) != null)
                deleteDeck(matcher.group("deck"), user);
            else if ((matcher = RegexController.getMatcher(command, deckSetActivePattern)) != null)
                activateDeck(matcher.group("deck"), user);
            else if ((matcher = RegexController.getMatcher(command, deckAddCardPattern)) != null) {
                if (matcher.group("addOrRemove").equals("add"))
                    addCardToDeck(matcher.group("card"), matcher.group("deck")
                            , matcher.group("side") == null, user);
                else if (matcher.group("addOrRemove").equals("rm"))
                    removeCardFromDeck(matcher.group("card"), matcher.group("deck")
                            , matcher.group("side") == null, user);
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
            } else
                printerAndScanner.printNextLine(invalidCommand);
            command = printerAndScanner.scanNextLine();
        }
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
            // todo : ask how to get status
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

    private boolean checkNumberOfCardsWithDifferentStatus(String cardName, String deckName, Deck deck, int numberOfStatus) {
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
        if (deck.isCardWithThisNameExists(cardName, isSide)) {
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

//    public void checkBeforeAddOrRemoveCard(String deckName, String cardName, boolean isSide, boolean isAdding) {
//        if (isAdding) {
//            if (!(cards.allCards.contian(cardName))) {
//                System.out.println("card with name" + cardName + "does not exist");
//            } else if (!(deckNames.contains(deckName))) {
//                System.out.println("deck with name" + deckName + "does not exist");
//            } else if (!isSide && deck.getMainDeckCardCount() > 60) {
//                System.out.println("main deck is full");
//            } else if (isSide && deck.getSideDeckCardCount() > 15) {
//                System.out.println("side deck is full");
//            } else if (deck.getEachCardCount(cardName) > 3) {
//                System.out.println("there are already three cards with name" + cardName + " in deck" + deckName);
//            } else {
//                if (!isSide) {
//                    int cardCount = deck.getMainDeckCardCount() + 1;
//                    deck.addCard(deck, cardName);
//                    deck.setMainDeckCardCount(cardCount);
//                    System.out.println("card added to deck successfully");
//                } else {
//                    int cardCount = deck.getSideDeckCardCount() + 1;
//                    deck.addCard(deck, cardName);
//                    deck.setSideDeckCardCount(cardCount);
//                    System.out.println("card added to deck successfully");
//                }
//            }
//        } else {
//            if (!(cards.allCards.contian(cardName))) {
//                System.out.println("card with name" + cardName + "does not exist");
//            } else if (deck.getAllMainCards().contains(cardName)) {
//                System.out.println("card with name" + cardName + "does not exist in main deck");
//            } else if (deck.getAllSideCards().contains(cardName)) {
//                System.out.println("card with name" + cardName + "does not exist in side deck");
//            } else {
//                if (isSide) {
//                    int cardCount = deck.getSideDeckCardCount() - 1;
//                    deck.removeCard(deck, cardName);
//                    deck.setSideDeckCardCount(cardCount);
//                } else {
//                    int cardCount = deck.getMainDeckCardCount() - 1;
//                    deck.removeCard(deck, cardName);
//                    deck.setMainDeckCardCount(cardCount);
//                }
//            }
//        }
//    }
}
