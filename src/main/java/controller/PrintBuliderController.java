package controller;

import model.Deck;
import model.User;
import model.game.GameBoard;
import model.cards.Cards;

import java.util.ArrayList;
import java.util.Collections;

public class PrintBuilderController {
    private static PrintBuilderController printPrintBuilderController = null;

    private void PrintBuilderController() {
    }

    public static PrintBuilderController getInstance() {
        if (printPrintBuilderController == null)
            printPrintBuilderController = new PrintBuilderController();
        return printPrintBuilderController;
    }

    public void buildAllCardsInDeck(Deck deck) {
    }

    public void buildAllDecks(User user) {
    }

    public void buildGraveyard(ArrayList<Cards> graveyard) {
    }

    public void buildGameWinner(String username, int score1, int score2) {
    }

    public void buildMatchWinner(String username, int score1, int score2) {
    }

    public void buildScoreBoard() {
    }

    public void buildChainTurnPromot(String playerName, GameBoard board) {
    }

    public String winingAgainstOO(int damage) {
        return "your opponent’s monster is destroyed" +
                " and your opponent receives" +
                damage +
                "battle damage";
    }

    public String losingAgainstOO(int damage) {
        return "Your monster card is destroyed" +
                " and you received " + damage + " battle" +
                " damage";
    }

    public String losingAgainstDO(int damage) {
        return "no card is destroyed and" +
                " you received " + damage + "battle damage";
    }

    public String hiddenCardAfterAttacking(String cardName) {
        return "opponent’s monster card was " + cardName + " and ";
    }

    public String thisNicknameAlreadyExists(String newNickname) {
        return "user with nickname " + newNickname + " already exists";
    }

    public String DeckWithThisNameAlreadyExists(String deckName) {
        return "deck with name " + deckName + "already exists";
    }

    public String deckWithThisNameDoesNotExist(String deckName) {
        return "deck with name " + deckName + " does not exist";
    }

    public String cardWithThisNameDoesNotExist(String cardName) {
        return "card with name " + cardName + " does not exist";
    }

    public String thereAreAlreadyThreeCardsWithThisNameInThisDeck(String cardName, String deckName) {
        return "there are already three cards with name " + cardName + " in deck " + deckName;
    }

    public String cardWithThisNameDoesNotExistInThisDeck(String cardName, boolean isSide) {
        if (!isSide)
            return "card with name " + cardName + " does not exist in main deck";
        else
            return "card with name " + cardName + " does not exist in side deck";
    }

    public String showAllDecks(User user){
        Deck activeDeck = user.getActiveDeck();
        ArrayList<String> allDecks = new ArrayList<>(user.getDecks().keySet());
        Collections.sort(allDecks);

        StringBuilder response = new StringBuilder();
        response.append("Decks:\nActive deck:\n");
        showOneDeck(activeDeck, response);

        response.append("Other decks:\n");
        for (String deckName : allDecks) {
            if(!deckName.equals(activeDeck.getName())){
                Deck deck = user.getDeck(deckName);
                showOneDeck(deck, response);
            }
        }
        return response.toString();
    }

    private void showOneDeck(Deck deck, StringBuilder response) {
        response.append(deck.getName());
        response.append(": main deck");
        response.append(deck.getMainDeckCardCount());
        response.append(", side deck");
        response.append(deck.getAllSideCards());
        response.append(", ");
        if(deck.isDeckValid())
            response.append("valid\n");
        else
            response.append("invalid\n");
    }



}
