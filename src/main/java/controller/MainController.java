package controller;

import model.Deck;
import model.User;
import view.PrinterAndScanner;

import static model.tools.RegexPatterns.*;
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
                        DuelController dualController = DuelController.getInstance();
                        dualController.run(user);
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
                        System.out.println("menu navigation is not possible");
                    } else {
                        System.out.println("invalid menu");
                    }
                } else if (RegexController.hasField(matcher, "showCurrent")) {
                    System.out.println("Main menu :\n" +
                            "menu show-current\n" +
                            "menu enter <menu name>\n" +
                            "menu exit");
                } else if (RegexController.hasField(matcher, "exit")) {
                    System.out.println("You have to logout to exit");
                } else
                    System.out.println("invalid command");
            }else
                System.out.println("invalid command");
            command = printerAndScanner.scanNextLine().toLowerCase();
        }
        System.out.println("user logged out successfully!");
        // testing
    }
}

