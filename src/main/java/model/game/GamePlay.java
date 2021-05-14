package model.game;

import controller.GamePlayController;
import controller.PrintBuilderController;
import view.PrinterAndScanner;

import java.util.ArrayList;
import java.util.HashMap;

public class GamePlay {

    private static PrinterAndScanner printerAndScanner;
    private static PrintBuilderController printBuilderController;
    private static boolean firstTurn;

    private GameBoard myGameBoard;
    private boolean alreadySummonedOrSet, isHost, isAi, flipSummonedInThisRound; //TODO move already summoned or set from gameplay to here
    private HashMap<Place, ArrayList<String>> history;
    private ArrayList<String> universalHistory;
    private Place selectedCard;
    private String name;
    private GamePlayController opponentGamePlayController;

    public GamePlay(boolean isHost, GameBoard myGameBoard, boolean isAi, String name){
        this.isHost = isHost;
        this.isAi = isAi;
        this.myGameBoard = myGameBoard;
        this.history = new HashMap<>();
        this.alreadySummonedOrSet = false;
        this.flipSummonedInThisRound = false;
        this.selectedCard = null;
        this.universalHistory = new ArrayList<>();
        this.name = name;
    }

    public void setOpponentGamePlayController(GamePlayController opponentGamePlayController) {
        this.opponentGamePlayController = opponentGamePlayController;
    }

    public GamePlayController getOpponentGamePlayController() {
        return opponentGamePlayController;
    }

    public GameBoard getMyGameBoard() {
        return myGameBoard;
    }

    public HashMap<Place, ArrayList<String>> getHistory() {
        return history;
    }

    public Place getSelectedCard() {
        return selectedCard;
    }

    public String getName() {
        return name;
    }

    public void setSelectedCard(Place selectedCard) {
        this.selectedCard = selectedCard;
    }

    public ArrayList<String> getUniversalHistory() {
        return universalHistory;
    }
}
