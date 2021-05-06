package controller;

import model.game.GameBoard;
import model.game.GamePlay;
import model.game.NewGame;
import view.PrinterAndScanner;

public class GameContoller2 {
    protected static PrinterAndScanner printerAndScanner;
    protected static PrintBuilderController printBuilderController;
    protected static boolean firstTurn;

    protected GamePlay gamePlay;
    protected GameBoard gameBoard;
    protected GameBoard opponentGameBoard;
    protected NewGame newGame;
    private boolean isHost;
    private String opponentName;
    DuelController duelController;

    public void run(){}


}
