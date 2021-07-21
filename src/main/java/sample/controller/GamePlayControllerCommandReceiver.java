package sample.controller;

import sample.view.listener.Communicator;

public class GamePlayControllerCommandReceiver implements Runnable {

    private GamePlayController gamePlayController;
    private Communicator myCommunicator, opponentCommunicator;

    public GamePlayControllerCommandReceiver(GamePlayController gamePlayController, Communicator myCommunicator,
                                             Communicator opponentCommunicator) {
        this.gamePlayController = gamePlayController;
        this.myCommunicator = myCommunicator;
        this.opponentCommunicator = opponentCommunicator;
    }

    @Override
    public void run() {
        String message;
        while (gamePlayController.getRun()) {
            message = myCommunicator.receive();
            if (message.startsWith("getPossibleAction"))
                getPossibleAction(message);
            else if (message.startsWith("getSelectedCardStatus"))
                getSelectedCardStatus();
            else if (message.startsWith("shutdown"))
                break;
            else if (message.startsWith("message") || message.equals("pause") || message.equals("resume")) {
                myCommunicator.sendMessage(message);
                opponentCommunicator.sendMessage(message);
            } else gamePlayController.putCommand(message);
        }
    }

    private void getPossibleAction(String string) {
        String[] strings = string.replaceAll("getPossibleAction,", "").split(",");
        Action[] actions = gamePlayController.getPossibleAction(Integer.parseInt(strings[0]),
                strings[1].equalsIgnoreCase("true"), GameState.getGameStateByString(strings[2]));
        myCommunicator.sendMessage("actions*" + actions[0].name() + "*" + actions[1].name() + "*" + strings[0] + "*" + strings[1]);
    }

    private void getSelectedCardStatus() {
        myCommunicator.sendMessage(gamePlayController.getGamePlay().getSelectedCard().getStatus().name());
    }
}
