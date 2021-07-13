package sample.controller;

import sample.view.sender.Sender;


public class ImportAndExportController {
    private static ImportAndExportController importAndExportController = null;
    private Sender sender = Sender.getInstance();
    private final String PREFIX = "-IEC-";

    private ImportAndExportController() {
    }

    public static ImportAndExportController getInstance() {
        if (importAndExportController == null)
            importAndExportController = new ImportAndExportController();
        return importAndExportController;
    }

    public String exportCardInCSV(String cardName) {
        return sender.getResponseWithToken(PREFIX, "exportCardInCSV", cardName);
    }

    public String importCardFromCSV(String cardName) {
        return sender.getResponseWithToken(PREFIX, "importCardFromCSV", cardName);
    }

    public String exportCardInJson(String cardName) {
        return sender.getResponseWithToken(PREFIX, "exportCardInJson", cardName);
    }

    public String importCardFromJson(String cardName) {
        return sender.getResponseWithToken(PREFIX, "importCardFromJson", cardName);
    }
}
