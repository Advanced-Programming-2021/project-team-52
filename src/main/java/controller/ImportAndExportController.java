package controller;

import java.util.ArrayList;
import model.User;

public class ImportAndExportController {
    private static ImportAndExportController importAndExportController;
    private PrintBuilder printBuilder;
    private PrinterAndScanner printerAndScanner;

    private ImportAndExportController() {}
    public static ImportAndExportController getinstance() {}
    public void run(User user) {}
    public ArrayList<String> Import(User user) {}
    public void export(ArrayList<String> cards) {}

}
