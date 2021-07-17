package sample.controller;

import sample.view.listener.Communicator;

public class GamePlayControllerCommandReceiver implements Runnable {

    private GamePlayController gamePlayController;
    private Communicator communicator;

    public GamePlayControllerCommandReceiver(GamePlayController gamePlayController, Communicator communicator) {
        this.gamePlayController = gamePlayController;
        this.communicator = communicator;
    }

    @Override
    public void run() {
        String message;
        while (gamePlayController.getRun()) {
            message = communicator.receive();
            if (message.startsWith("getPossibleAction"))
                getPossibleAction(message);
            else if (message.startsWith("getSelectedCardStatus"))
                getSelectedCardStatus();
            else if (message.startsWith("shutdown"))
                break;
            else gamePlayController.putCommand(message);
        }
    }

    private void getPossibleAction(String string) {
        String[] strings = string.replaceAll("getPossibleAction,", "").split(",");
        Action[] actions = gamePlayController.getPossibleAction(Integer.parseInt(strings[0]),
                strings[1].equalsIgnoreCase("true"), GameState.getGameStateByString(strings[2]));
        communicator.sendMessage(actions[0].name() + "," + actions[1].name());
    }

    private void getSelectedCardStatus() {
        communicator.sendMessage(gamePlayController.getGamePlay().getSelectedCard().getStatus().name());
    }
}
