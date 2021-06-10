package controller;

import model.Deck;
import model.User;
import view.PrinterAndScanner;

import static model.tools.RegexPatterns.*;
import static model.tools.StringMessages.*;
import static view.PrinterAndScanner.*;

import java.util.regex.Matcher;

public class MainController {
    private static MainController mainController = null;
    private static PrinterAndScanner printerAndScanner;
    private static PrintBuilderController printBuilderController;

    {
        printBuilderController = PrintBuilderController.getInstance();
        printerAndScanner = PrinterAndScanner.getInstance();
    }

    private MainController() {
    }

    public static MainController getInstance() {
        if (mainController == null)
            mainController = new MainController();
        return mainController;
    }

    public void run(User user) {
        String command = printerAndScanner.scanNextLine().toLowerCase();
        Matcher matcher;
        while (!command.equals("user logout")) {
            if ((matcher = RegexController.getMatcher(command, menuPattern)) != null) {
                if (RegexController.hasField(matcher, "enter")) {
                    if (matcher.group("enter").equals("duel")) {
//                        DuelController dualController = DuelController.getInstance();
//                        dualController.run(user);
                        new NewDuelController(user);
                    } else if (matcher.group("enter").equals("deck")) {
                        DeckController deckController = DeckController.getInstance();
                        deckController.run(user);
                    } else if (matcher.group("enter").equals("scoreboard")) {
                        ScoreBoardController scoreBoardController = ScoreBoardController.getInstance();
                        scoreBoardController.run();
                    } else if (matcher.group("enter").equals("profile")) {
                        ProfileController profileController = ProfileController.getInstance();
                        profileController.run(user);
                    } else if (matcher.group("enter").equals("shop")) {
                        ShopController shopController = ShopController.getInstance();
                        shopController.run(user);
                    } else if (matcher.group("enter").equals("login")) {
                        printerAndScanner.printNextLine(impossibilityOfMenuNavigation);
                    } else {
                        printerAndScanner.printNextLine(invalidMenu);
                    }
                } else if (RegexController.hasField(matcher, "showCurrent")) {
                    printerAndScanner.printNextLine(showMainMenu);
                } else if (RegexController.hasField(matcher, "exit")) {
                    printerAndScanner.printNextLine(shouldLogoutToExit);
                } else
                    printerAndScanner.printNextLine(invalidCommand);
            }else
                printerAndScanner.printNextLine(invalidCommand);
            command = printerAndScanner.scanNextLine().toLowerCase();
        }
        printerAndScanner.printNextLine(userLoggedOutSuccessfully);
        // testing
    }
}

