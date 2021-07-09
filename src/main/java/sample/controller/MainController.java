//package sample.controller;
//
//import sample.model.User;
//import sample.view.PrinterAndScanner;
//
//import static sample.model.tools.RegexPatterns.*;
//import static sample.model.tools.StringMessages.*;
//
//import java.util.regex.Matcher;
//
//public class MainController {
//    private static MainController mainController = null;
//    private static PrinterAndScanner printerAndScanner;
//    private static PrintBuilderController printBuilderController;
//
//    {
//        printBuilderController = PrintBuilderController.getInstance();
//        printerAndScanner = PrinterAndScanner.getInstance();
//    }
//
//    private MainController() {
//    }
//
//    public static MainController getInstance() {
//        if (mainController == null)
//            mainController = new MainController();
//        return mainController;
//    }
//
//    public void start(User user) {
//        String command = printerAndScanner.scanNextLine();
//        while (!run(user, command)) {
//            command = printerAndScanner.scanNextLine();
//        }
//        printerAndScanner.printNextLine(userLoggedOutSuccessfully);
//    }
//
//    public boolean run(User user, String command) {
//        Matcher matcher;
//        if (command.equals("user logout"))
//            return true;
//        else if ((matcher = RegexController.getMatcher(command, menuPattern)) != null) {
//            if (RegexController.hasField(matcher, "enter")) {
//                if (matcher.group("enter").equals("duel")) {
//                    new NewDuelController(user);
//                } else if (matcher.group("enter").equals("deck")) {
//                    DeckController deckController = DeckController.getInstance();
//                    deckController.start(user);
//                } else if (matcher.group("enter").equals("scoreboard")) {
//                    ScoreBoardController scoreBoardController = ScoreBoardController.getInstance();
//                    scoreBoardController.start();
//                } else if (matcher.group("enter").equals("profile")) {
//                    ProfileController profileController = ProfileController.getInstance();
//                    profileController.start(user);
//                } else if (matcher.group("enter").equals("shop")) {
//                    ShopController shopController = ShopController.getInstance();
//                    shopController.start(user);
////                } else if (matcher.group("enter").equals("IE")) {
////                    ImportAndExportController.getInstance().run();
//                } else if (matcher.group("enter").equals("login")) {
//                    printerAndScanner.printNextLine(impossibilityOfMenuNavigation);
//                } else {
//                    printerAndScanner.printNextLine(invalidMenu);
//                }
//            } else if (RegexController.hasField(matcher, "showCurrent")) {
//                printerAndScanner.printNextLine(showMainMenu);
//            } else if (RegexController.hasField(matcher, "exit")) {
//                printerAndScanner.printNextLine(shouldLogoutToExit);
//            } else
//                printerAndScanner.printNextLine(invalidCommand);
//        } else
//            printerAndScanner.printNextLine(invalidCommand);
//        return false;
//    }
//}
//