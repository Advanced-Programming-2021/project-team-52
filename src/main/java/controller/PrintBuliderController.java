package controller;

import model.Deck;
import model.User;
import model.game.GameBoard;
import model.cards.Cards;

import java.util.ArrayList;

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
}
