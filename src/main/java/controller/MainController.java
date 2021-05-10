package Controller;

import model.Deck;
import model.User;

import static model.tools.RegexPatterns.*;
import static view.PrinterAndScanner.*;
import java.util.regex.Matcher;

public class MainController {
    private static MainController mainController = null;

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
        if ((matcher = RegexController.getMatcher(command, menuPattern)) != null) {
            if (RegexController.hasField(matcher, "enter")) {
                if (RegexController.hasField(matcher, "duel")) {
                    DuelController dualController = DuelController.getInstance();
                    dualController.run(user);
                } else if (RegexController.hasField(matcher, "deck")) {
                    DeckController deckController = DeckController.getInstance();
                    deckController.run(user);
                } else if (RegexController.hasField(matcher, "scoreboard")) {
                    ScoreBoardController scoreBoardController = ScoreBoardController.getInstance();
                    scoreBoardController.run();
                } else if (RegexController.hasField(matcher, "profile")) {
                    ProfileController profileController = ProfileController.getInstance();
                    profileController.run(user);
                } else if (RegexController.hasField(matcher, "shop")) {
                    ShopController shopController = ShopController.getInstance();
                    shopController.run(user);
                } else {
                    System.out.println("menu navigation is not possible");
                }
            }
        }
    }
}

