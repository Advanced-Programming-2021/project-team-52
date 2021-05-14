package model.game;

//import controller.PrintBuliderController;
import model.*;
import view.PrinterAndScanner;

import java.util.ArrayList;

public class NewGame {
//    private PrintBuliderController printBuliderController;
    private PrinterAndScanner printerAndScanner;
    private GameBoard hostBoard;
    private GameBoard guestBoard;
    private GamePlay hostGamePlay;
    private GamePlay guestGamePlay;
    private int roundsNumber;
    ArrayList<Integer> healths;
    {
        healths = new ArrayList<>();
    }
    private NewGame(User host, Player guest,boolean guestIsAi){}

    public void setHostBoard(GameBoard hostBoard) {
        this.hostBoard = hostBoard;
    }

    public void setGuestBoard(GameBoard guestBoard) {
        this.guestBoard = guestBoard;
    }

    public void setHostGamePlay(GamePlay hostGamePlay) {
        this.hostGamePlay = hostGamePlay;
    }

    public void setGuestGamePlay(GamePlay guestGamePlay) {
        this.guestGamePlay = guestGamePlay;
    }


    public GameBoard getHostBoard() {
        return hostBoard;
    }

    public GameBoard getGuestBoard() {
        return guestBoard;
    }

    public GamePlay getHostGamePlay() {
        return hostGamePlay;
    }

    public GamePlay getGuestGamePlay() {
        return guestGamePlay;
    }

    public int getHealths() {
        return 0;
    }
}
