package sample.view.gameboardview;

import javafx.application.Platform;
import javafx.stage.Stage;
import sample.view.sender.Sender;

public class GameBoardHandler implements Runnable{

    private boolean run = true;
    private Sender sender = Sender.getInstance();
    private Stage stage;
    private Communicator communicator;
    private GameBoardView gameBoardView;

    public GameBoardHandler(Stage stage){
        this.stage = stage;
    }

    public void setRun(boolean run) {
        this.run = run;
    }

    @Override
    public void run() {
        String message;
        while (run){
            message = sender.receive();
            handle(message);
        }
    }

    private void handle(String message){
        String[] strings;
        if (message.startsWith("duel"))
            startANewDuel(message.split(","));
        else if (message.startsWith("reset"))
            gameBoardView.shutdown(stage, null, message.matches("changeSene"));
        else if (message.startsWith("askOption")) {
            strings = message.replaceAll("askOption,", "").split(",");
            String[] options = new String[strings.length - 1];
            System.arraycopy(strings, 1, options, 0, strings.length - 1);
            communicator.askOptions(strings[0], options);
        } else if (message.startsWith("flipCoin"))
            communicator.flipCoin(Integer.parseInt(message.replaceAll("flipCoin,", "")));
        else if (message.equals("removeCoin"))
            communicator.removeCoin();
        else if (message.startsWith("changeGameState"))
            communicator.changeGameState(message.replaceAll("changeGameState,", ""));
        else if (message.startsWith("changePhase")){
            strings = message.replaceAll("chagnePhase,", "").split(",");
            communicator.changePhase(strings[0], strings[1].equals("true"));
        } else if (message.startsWith("addToHand")){
            strings = message.replaceAll("addToHand,", " ").split(",");
            communicator.addToHand(Integer.parseInt(strings[0]), strings[1].equals("true"), strings[2], strings[3]);
        } else if (message.startsWith("moveFromHandToBoard")){
            strings = message.replaceAll("moveFromHandToBoard,", "").split("',");
            communicator.moveFromHandToBoard(Integer.parseInt(strings[0]), Integer.parseInt(strings[1]),
                    strings[2].equals("trues"), strings[3], strings[4], strings[5]);
        } else if (message.startsWith("changePosition")){
            strings = message.replaceAll("changePosition,", "").split(",");
            communicator.changePosition(Integer.parseInt(strings[0]), strings[1].equalsIgnoreCase("true"), strings[2]);
        } else if (message.startsWith("flipSummon")){
            strings = message.replaceAll("flipSummon,", "").split(",");
            communicator.flipSummon(Integer.parseInt(strings[0]), strings[1].equalsIgnoreCase("true"), strings[2]);
        } else if (message.startsWith("showCard")){
            strings = message.replaceAll("showCard,", "").split(",");
            communicator.showCard(Integer.parseInt(strings[0]), strings[1], strings[2], strings[3].equalsIgnoreCase("true"));
        } else if (message.startsWith("moveToGraveyard")){
            strings = message.replaceAll("moveToGraveyard,", "").split(",");
            communicator.moveToGraveyard(Integer.parseInt(strings[0]), strings[1].equalsIgnoreCase("true"), strings[2], strings[3]);
        } else if (message.startsWith("selectCard")){
            strings = message.replaceAll("selectCard,", "").split(",");
            communicator.selectCard(strings[0], strings[1].equalsIgnoreCase("true"),
                    strings[2].equalsIgnoreCase("true"), strings[3].equalsIgnoreCase("true"));
        } else if (message.startsWith("specialSummonFromGraveYard"))
            communicator.specialSummonFromGraveYard(message.
                    replaceAll("specialSummonFromGraveYard,", "").equalsIgnoreCase("true"));
        else if (message.startsWith("scanner"))
            communicator.scanner();
        else if (message.startsWith("mindCrush"))
            communicator.mindCrush();
        else if (message.startsWith("ritualSummon"))
            communicator.ritualSummon();
        else if (message.startsWith("specialSummonFromAnywhere"))
            communicator.specialSummonFromAnywhere(message.replaceAll("specialSummonFromAnywhere,", ""));
        else if (message.startsWith("reduceHeath")){
            strings = message.replaceAll("reduceHealth,", "").split(",");
            communicator.reduceHealth(Integer.parseInt(strings[0]), strings[1].equalsIgnoreCase("true"));
        }
    }

    private void startANewDuel(String[] args){
        GameBoardView gameBoardView = new GameBoardView(stage, args[0], args[1], args[2], args[3]);
        gameBoardView.initialize();
        this.communicator = gameBoardView.getCommunicator();
        this.gameBoardView = gameBoardView;
    }
}
