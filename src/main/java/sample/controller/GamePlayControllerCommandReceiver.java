package sample.controller;

import sample.view.listener.Communicator;

public class GamePlayControllerCommandReceiver implements Runnable{

    private GamePlayController gamePlayController;
    private Communicator communicator;

    public GamePlayControllerCommandReceiver(GamePlayController gamePlayController, Communicator communicator){
        this.gamePlayController = gamePlayController;
        this.communicator = communicator;
    }

    @Override
    public void run() {
        while (gamePlayController.getRun()){
            gamePlayController.putCommand(communicator.receive());
        }
    }
}
