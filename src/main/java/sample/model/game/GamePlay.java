package sample.model.game;

import sample.controller.GamePlayController;

import java.util.ArrayList;
import java.util.HashMap;

public class GamePlay {

    private GameBoard myGameBoard;
    private boolean isHost, isAi; //TODO move already summoned or set from gameplay to here
    private HashMap<Place, ArrayList<String>> history;
    private ArrayList<String> universalHistory;
    private Place selectedCard;
    private String name;
    private GamePlayController opponentGamePlayController;
//    private boolean gameEnded;

    public GamePlay(boolean isHost, GameBoard myGameBoard, boolean isAi, String name) {
        this.isHost = isHost;
        this.isAi = isAi;
        this.myGameBoard = myGameBoard;
        this.history = new HashMap<>();
        this.selectedCard = new Place(PLACE_NAME.HAND);
        this.universalHistory = new ArrayList<>();
        this.name = name;
        instantiateHistories();
    }

    private void instantiateHistories() {
        for (Place value : myGameBoard.getPlace().values()) {
            if (value.getType() != PLACE_NAME.HAND) {
                history.put(value, new ArrayList<>());
                value.setHistory(history.get(value));
            }
        }

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
