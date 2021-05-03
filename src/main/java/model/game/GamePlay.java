package model.game;

import model.cards.Cards;
import model.game.GameBoard;

public class GamePlay {

    private GameBoard myGameBoard,opponentGameBoard;
    private Cards selectedCard;
    private boolean alreadySummonedOrSet, isHost, isAi, flipSummonedInThisRound;

    GamePlay(boolean isHost, GameBoard myGameBoard, GameBoard opponentGameBoard, boolean isAi){
        this.isHost = isHost;
        this.isAi = isAi;
        this.myGameBoard = myGameBoard;
        this.opponentGameBoard = opponentGameBoard;
    }

    void setOpponentBoard(GameBoard board){
        this.opponentGameBoard = board;
    }

    public GameBoard getMyGameBoard() {
        return myGameBoard;
    }

    public GameBoard getOpponentGameBoard() {
        return opponentGameBoard;
    }

    public Cards getSelectedCard() {
        return selectedCard;
    }

    public boolean getAlreadySummonedOrSet(){
        return alreadySummonedOrSet;
    }

    public boolean getIsHost(){
        return isHost;
    }

    public boolean getIsAI(){
        return isAi;
    }

    public boolean getFlipSummonedInThisRound(){
        return flipSummonedInThisRound;
    }

    public void setSelectedCard(Cards selectedCard) {
        this.selectedCard = selectedCard;
    }

    public void setAlreadySummonedOrSet(boolean alreadySummonedOrSet) {
        this.alreadySummonedOrSet = alreadySummonedOrSet;
    }

    public void setFlipSummonedInThisRound(boolean flipSummonedInThisRound) {
        this.flipSummonedInThisRound = flipSummonedInThisRound;
    }
}
