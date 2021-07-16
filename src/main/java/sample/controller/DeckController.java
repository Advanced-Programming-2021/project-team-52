package sample.controller;

import sample.view.sender.Sender;

public class DeckController {
    private static DeckController deckController = null;
    private Sender sender = Sender.getInstance();
    private final String PREFIX = "-DC-";

    private DeckController() {
    }


    public static DeckController getInstance() {
        if (deckController == null)
            deckController = new DeckController();
        return deckController;
    }


    public String createDeck(String deckName) {
        return sender.getResponseWithToken(PREFIX, "createDeck", deckName);
    }

    public String deleteDeck(String deckName) {
        return sender.getResponseWithToken(PREFIX, "deleteDeck", deckName);
    }

    public String activateDeck(String deckName) {
        return sender.getResponseWithToken(PREFIX, "activateDeck", deckName);
    }

    public String addCardToDeck(String cardName, String deckName, boolean isSide) {
        return sender.getResponseWithToken(PREFIX, "addCardToDeck", cardName, deckName,
                sender.convertBooleanToString(isSide));
    }


    public String removeCardFromDeck(String cardName, String deckName, boolean isSide) {
        return sender.getResponseWithToken(PREFIX, "removeCardFromDeck", cardName, deckName,
                sender.convertBooleanToString(isSide));
    }

    public String showAllDecks() {
        return sender.getResponseWithToken(PREFIX, "showAllDecks");
    }

    public String getMainDeckByString(String deckName) {
        return sender.getResponseWithToken(PREFIX, "getMainDeckByString", deckName);
    }

    public String getSideDeckByString(String deckName) {
        return sender.getResponseWithToken(PREFIX, "getSideDeckByString", deckName);
    }
}
