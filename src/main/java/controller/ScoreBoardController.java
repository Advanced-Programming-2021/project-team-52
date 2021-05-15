package controller;

import model.*;
import model.tools.RegexPatterns;
import model.tools.StringMessages;
import view.PrinterAndScanner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;

// say in group : delete run method input (User : user)

public class ScoreBoardController implements RegexPatterns, StringMessages {
    private static ScoreBoardController scoreBoard = null;
    private static PrintBuilderController printBuilderController;
    private static PrinterAndScanner printerAndScanner;
    Collection<User> collection;
    ArrayList<User> usersInScoreOrder;

    {
        printBuilderController = PrintBuilderController.getInstance();
        printerAndScanner = PrinterAndScanner.getInstance();
    }

    private ScoreBoardController(){
    }

    public static ScoreBoardController getInstance() {
        if (scoreBoard == null)
            scoreBoard = new ScoreBoardController();
        return scoreBoard;
    }

    public void run() {
        String command = printerAndScanner.scanNextLine();
        Matcher matcher;
        while (true){
            if(command.equals("scoreboard show")){
                sortUserByScore();
                printerAndScanner.printNextLine(toString());
            }else if ((matcher = RegexController.getMatcher(command, menuPattern)) != null) {
                if (RegexController.hasField(matcher, "exit"))
                    break;
                else if (RegexController.hasField(matcher, "enter"))
                    printerAndScanner.printNextLine(menuNavigationIsNotPossible);
                else if (RegexController.hasField(matcher, "showCurrent"))
                    showCurrent();
                else
                    printerAndScanner.printNextLine(invalidCommand);
            }
            command = printerAndScanner.scanNextLine();
        }
    }

    public void sortUserByScore() {
        collection = LoginController.users.values();
        usersInScoreOrder = new ArrayList<>(collection);
        for (int i = 0; i < usersInScoreOrder.size(); i++) {
            for (int j = i + 1; j < usersInScoreOrder.size(); j++) {
                if (usersInScoreOrder.get(i).getScore() < usersInScoreOrder.get(j).getScore())
                    Collections.swap(usersInScoreOrder, i, j);
                else if (usersInScoreOrder.get(i).getScore() == usersInScoreOrder.get(j).getScore()) {
                    if (usersInScoreOrder.get(i).getUsername()
                            .compareTo(usersInScoreOrder.get(j).getUsername()) > 0)
                        Collections.swap(usersInScoreOrder, i, j);
                }
            }
        }
    }

    private static void showCurrent() {
        printerAndScanner.printNextLine(getShowCurrentInScoreboardController);
    }


    @Override
    public String toString() {
        sortUserByScore();
        StringBuilder response = new StringBuilder();
        int temporaryRank = 1;
        for (int i = 0; i < usersInScoreOrder.size(); i++) {
            if (i != 0) {
                if (usersInScoreOrder.get(i).getScore() != usersInScoreOrder.get(i - 1).getScore())
                    temporaryRank = i + 1;
            }
            response.append(temporaryRank);
            response.append("- ");
            response.append(usersInScoreOrder.get(i).getNickname());
            response.append(": ");
            response.append(usersInScoreOrder.get(i).getScore());
            response.append("\n");
        }
        return response.toString();
    }
}
