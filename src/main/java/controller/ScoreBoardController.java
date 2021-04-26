package Controller;

import model.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

// say in group : delete run method input (User : user)

public class ScoreBoardController {
    private static ScoreBoardController scoreBoard = null;
    private PrintBulider printBulider;
    private printerAndScanner printerAndScanner;
    Collection<model.User> collection = LoginController.users.values();
    ArrayList<model.User> usersInScoreOrder = new ArrayList<>(collection);

    private ScoreBoardController() {
    }

    public static ScoreBoardController getInstance() {
        if (scoreBoard == null)
            scoreBoard = new ScoreBoardController();
        return scoreBoard;
    }

    public void run() {
    }

    private void sortUserByScore() {
        for (int i = 0; i < usersInScoreOrder.size(); i++) {
            for (int j = i + 1; j < usersInScoreOrder.size(); j++) {
                if (usersInScoreOrder.get(i).getScore() > usersInScoreOrder.get(j).getScore())
                    Collections.swap(usersInScoreOrder, i, j);
                else if (usersInScoreOrder.get(i).getScore() == usersInScoreOrder.get(j).getScore()) {
                    if (usersInScoreOrder.get(i).getUsername().toLowerCase()
                            .compareTo(usersInScoreOrder.get(j).getUsername().toLowerCase()) > 0)
                        Collections.swap(usersInScoreOrder, i, j);
                }
            }
        }
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
        }
        return String.valueOf(response);
    }
}
