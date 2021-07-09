package sample.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.model.*;
import sample.model.tools.RegexPatterns;
import sample.model.tools.StringMessages;
import sample.view.PrinterAndScanner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;

public class ScoreBoardController implements RegexPatterns, StringMessages {
    private static ScoreBoardController scoreBoardController = null;
    private static PrintBuilderController printBuilderController = PrintBuilderController.getInstance();
    private static PrinterAndScanner printerAndScanner = PrinterAndScanner.getInstance();
    Collection<User> collection;
    ArrayList<User> usersInScoreOrder;


    private ScoreBoardController() {
    }

    public static ScoreBoardController getInstance() {
        if (scoreBoardController == null)
            scoreBoardController = new ScoreBoardController();
        return scoreBoardController;
    }

    public void start() {
        String command = printerAndScanner.scanNextLine();
        while (!run(command)) {
            command = printerAndScanner.scanNextLine();
        }
    }

    public boolean run(String command) {
        Matcher matcher;
        if (command.equals("scoreboard show")) {
            sortUserByScore();
            printerAndScanner.printNextLine(toString());
        } else if ((matcher = RegexController.getMatcher(command, menuPattern)) != null) {
            if (RegexController.hasField(matcher, "exit"))
                return true;
            else if (RegexController.hasField(matcher, "enter"))
                printerAndScanner.printNextLine(menuNavigationIsNotPossible);
            else if (RegexController.hasField(matcher, "showCurrent"))
                showCurrent();
            else
                printerAndScanner.printNextLine(invalidCommand);
        } else
            printerAndScanner.printNextLine(invalidCommand);
        return false;
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

    public ObservableList<User> getTopUsers(){
        sortUserByScore();
        ObservableList<User> topUsers = FXCollections.observableArrayList();
        for (int i = 0; i < usersInScoreOrder.size(); i++) {
            if(i < 20)
            topUsers.add(usersInScoreOrder.get(i));
        }
        return topUsers;
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
