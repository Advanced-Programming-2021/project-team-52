package controller.SpecialAbility.test;

import controller.SpecialAbility.SpecialController;
import model.game.GameBoard;
import model.game.PLACE_NAME;
import model.game.Place;
import view.PrinterAndScanner;

public class Chain {
    private static PrinterAndScanner printerAndScanner;
    private SpecialController mySpecialController, opponentSpecialController;
    private Place place;
    private GameBoard myGameBoard, opponentGameBoard;

    static {
        printerAndScanner = PrinterAndScanner.getInstance();
    }

    public Chain(SpecialController mySpecialController, SpecialController opponentSpecialController, Place place,
                 GameBoard myGameBoard, GameBoard opponentGameBoard){
        this.mySpecialController = mySpecialController;
        this.opponentSpecialController = opponentSpecialController;
        this.place = place;
        this.myGameBoard = myGameBoard;
        this.opponentGameBoard = opponentGameBoard;
        this.run();
    }

    public void run(){
        if (mySpecialController.canChain(place.getCard())){
            Place placeToChain = myGameBoard.getPlace(printerAndScanner.scanNextInt(),
                    printerAndScanner.scanNextLine().equals("monster") ? PLACE_NAME.MONSTER : PLACE_NAME.SPELL_AND_TRAP);
            placeToChain.setAffect(place);
            new Chain(opponentSpecialController, mySpecialController, place, opponentGameBoard, myGameBoard);
        }
        startChaining();
    }

    public void startChaining(){

    }
}
