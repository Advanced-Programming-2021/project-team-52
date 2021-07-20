package sample.controller;

import sample.view.sender.Sender;

public class ScoreBoardController {
    private static ScoreBoardController scoreBoardController = null;
    private Sender sender = Sender.getInstance();
    private final String PREFIX = "-SBC-";

    private ScoreBoardController() {
    }

    public static ScoreBoardController getInstance() {
        if (scoreBoardController == null)
            scoreBoardController = new ScoreBoardController();
        return scoreBoardController;
    }

    @Override
    public String toString() {
        return sender.getResponse(sender.setMessageWithToken(PREFIX, "toString"));
    }


}
