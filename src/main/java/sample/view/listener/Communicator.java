package sample.view.listener;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;

public class Communicator {
    private static HashMap<Thread, String> onlineUsers = new HashMap<>();

    private final DataInputStream DATA_INPUT_STREAM;
    private final DataOutputStream DATA_OUTPUT_STREAM;
    private final Socket SOCKET;
    private final ActionFinder ACTION_FINDER;
    private Thread thread;

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
        thread = new Thread(() -> {
            String input;
            while (true) {
                try {
//                    System.out.println("I am here");
                    input = DATA_INPUT_STREAM.readUTF();
                    String response = ACTION_FINDER.chooseClass(input);
                    String[] elements = input.split(",");
//                    System.out.println(input);
                    if(elements[0].equals("-LC-loginUser") && response.length() == 36){
                        onlineUsers.put(thread, elements[1]);
//                        System.out.println("hereeeeee" + elements[1]);
                    }
                    System.out.println("response : " + response);
                    if (response.equals("finish")) break;
                    sendMessage(response);
                } catch (SocketException e) {
                    System.out.println("client disconnected");
                    break;
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

    public static String getOnlineUsernames(){
        StringBuilder response = new StringBuilder();
        for (Thread thread : onlineUsers.keySet()) {
            try {
                if(thread.isAlive())
                    response.append(onlineUsers.get(thread)).append("\n");
//                    System.out.println("user "+onlineUsers.get(thread)+ " is online");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return response.toString();
    }

    public void changeGameState(String gameState){
        sendMessage("changeGameState" + "," + gameState);
    }

    public void changePhase(String phase, String enemyTurn){
        sendMessage("changePhase" + "," + phase + "," + enemyTurn);
    }

    public void addToHand(int emptyHandPlace, String enemy, String cardName, String cardDescription){
        sendMessage("addToHand," + emptyHandPlace + "," + enemy + "," + cardName + "," + cardDescription);
    }

    public void moveFromHandToBoard(int handNumber, int destination, String enemy, String status, String cardName,
                                    String description){
        sendMessage("moveFromHandToBoard," + handNumber + "," + destination + "," + enemy + "," + status + "," +
                cardName + "," + description);
    }

    public void changePosition(int number, boolean enemy, String status){
        sendMessage("changePosition," + number + "," + enemy + "," + status);
    }

    public void flipSummon(int num, boolean enemy, String cardName){
        sendMessage("flipSummon," + num + "," + enemy + "," + cardName);
    }

    public void showCard(int placeNumber, String cardName, String description, boolean enemy){
        sendMessage("showCard," + placeNumber + "," + cardName + "," + description + "," + enemy);
    }

    public void moveToGraveyard(int number, boolean enemy, String cardName, String description){
        sendMessage("moveToGraveyard," + number + "," + enemy + "," + cardName + "," + description);
    }

    public void selectCard(String cardType, boolean me, boolean enemy, boolean addType){
        sendMessage("selectCard," + cardType + "," + me + "," + enemy + "," + addType);
    }

    public void specialSummonFromGraveYard(boolean justName){
        sendMessage("specialSummonFromGraveYard," + justName);
    }

    public void scanner(){
        sendMessage("scanner");
    }

    public void mindCrush(){
        sendMessage("mindCrush");
    }

    public void askOptions(String message, String... options){
        sendMessage(Communicator.askOption(message, options));
    }

    public void ritualSummon(){
        sendMessage("ritualSummon");
    }

    public void specialSummonFromAnywhere(String message){
        sendMessage("specialSummonFromAnywhere," + message);
    }

    public void reduceHealth(int amount, boolean enemy){
        sendMessage("reduceHealth," + amount + "," + enemy);
    }
}
