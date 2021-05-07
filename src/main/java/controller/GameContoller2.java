package controller;

import controller.SpecialAbility.SpecialController;
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
    protected SpecialController mySpecialController, opponentSpecialController;
    private boolean isHost;
    private String opponentName;
    DuelController duelController;

    public void run(){}

    public void drawCard(){}


}
