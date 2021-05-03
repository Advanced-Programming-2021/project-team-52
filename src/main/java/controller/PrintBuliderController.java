package controller;

import model.Deck;
import model.User;
import model.game.GameBoard;

import java.util.ArrayList;

public class PrintBuilderController {
    private static PrintBuilderController printPrintBuilderController = null;

    private void PrintBuilderController(){}
    public static PrintBuilderController getInstance() {
        if (printPrintBuilderController == null)
            printPrintBuilderController = new PrintBuilderController();
        return printPrintBuilderController;
    }
    public void buildAllCardsInDeck(Deck deck) {}
    public void buildAllDecks(User user) {}
    public void buildGraveyard(ArrayList<String> graveyard){}
    public void buildGameWinner(String username, int score1 , int score2) {}
    public void buildMatchWinner(String username, int score1 , int score2) {}
    public void buildScoreBoard(){}
    public void buildChainTurnPromot(String playerName, GameBoard board){}
}
