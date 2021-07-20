package sample.controller;

import sample.view.sender.Sender;


public class PrintBuilderController {
    private static PrintBuilderController printPrintBuilderController = null;
    private  final String PREFIX = "-PBC-";
    private Sender sender =Sender.getInstance();

    public static PrintBuilderController getInstance() {
        if (printPrintBuilderController == null)
            printPrintBuilderController = new PrintBuilderController();
        return printPrintBuilderController;
    }

    public String deckWithThisNameDoesNotExist(String deckName) {
        return "deck with name " + deckName + " does not exist";
    }

    public String showOneCard2(String card) {
        return sender.getResponseWithToken(PREFIX, "showOneCard2",card );
    }

}
