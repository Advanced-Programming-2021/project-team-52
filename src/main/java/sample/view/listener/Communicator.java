package sample.view.listener;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Communicator {

    private final DataInputStream DATA_INPUT_STREAM;
    private final DataOutputStream DATA_OUTPUT_STREAM;
    private final Socket SOCKET;
    private final ActionFinder ACTION_FINDER;

    public Communicator(Socket socket, DataInputStream dataInputStream, DataOutputStream dataOutputStream){
        this.SOCKET = socket;
        this.DATA_INPUT_STREAM = dataInputStream;
        this. DATA_OUTPUT_STREAM = dataOutputStream;
        this.ACTION_FINDER = new ActionFinder(this);
    }

    public static String askOption(String message, String... options){
        StringBuilder result = new StringBuilder("askOption " + message);
        for (String option : options) {
            result.append(" ").append(option);
        }
        return result.toString();
    }

    public void startReceiving(){
        Thread thread = new Thread(() -> {
            String input;
            while (true) {
                try {
                    input = DATA_INPUT_STREAM.readUTF();
                    String response = ACTION_FINDER.chooseClass(input);
                    System.out.println("response : " + response);
                    if (response.equals("finish")) break;
                    sendMessage(response);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
            shutDown();
        });
        thread.setDaemon(true);
        thread.start();
    }

    public void sendMessage(String message){
        try {
            DATA_OUTPUT_STREAM.writeUTF(message);
            DATA_OUTPUT_STREAM.flush();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public String receive(){
        String message = "";
        try {
            message = DATA_INPUT_STREAM.readUTF();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return message;
    }

    public void shutDown(){
        try {
            DATA_INPUT_STREAM.close();
            SOCKET.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void changeGameState(String gameState){
        sendMessage("changeGameState" + "," + gameState);
    }

    public void changePhase(String phase, String enemyTurn){
        sendMessage("changePhase" + "," + phase + "," + enemyTurn);
    }

    public void addToHand(int emptyHandPlace, String enemy, String cardName, String cardDescription){
        sendMessage("addToHand," + enemy + "," + cardName + "," + cardDescription);
    }

    public void moveFromHandToBoard(int handNumber, int destination, String enemy, String status, String cardName,
                                    String description){
        sendMessage("moveFromHandToBoard," + handNumber + "," + destination + "," + enemy + "," + status + "," +
                cardName + "," + description);
    }
}
