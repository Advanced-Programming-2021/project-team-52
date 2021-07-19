package sample.view.sender;

import sample.model.User;
import sample.view.UserKeeper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Sender {


    public static final String GAME_PLAY_CONTROLLER_PREFIX = "-GPC-";
    private static  Sender sender = null;
    private  StringBuilder messageBuilder = new StringBuilder();
    private String token;

    private Sender() {
    }

    public static Sender getInstance() {
        if (sender == null)
            sender = new Sender();
        return sender;
    }

    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    public void  setupConnection() {

        try {
            socket = new Socket("localhost", 7755);
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  String getResponse(String message) {
        System.out.println("Message : " + message);
        try {
            dataOutputStream.writeUTF(message);
            dataOutputStream.flush();
            return dataInputStream.readUTF();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Error";
    }

    public boolean convertStringToBoolean(String message) {
        return message.toLowerCase().trim().equals("true");
    }

    public String convertBooleanToString(boolean message) {
        if (message)
            return "true";
        return "false";
    }

    public synchronized String setMessageWithoutToken(String prefix, String methodName, String ...arg){
        messageBuilder.setLength(0);
        messageBuilder.append(prefix).append(methodName);
        for (String s : arg) {
            messageBuilder.append(",");
            messageBuilder.append(s);
        }
//        System.out.println(messageBuilder.toString());
        return messageBuilder.toString();
    }
    public synchronized String setMessageWithToken(String prefix, String methodName, String ...arg){
        messageBuilder.setLength(0);
        messageBuilder.append(prefix).append(methodName).append(",");
        for (String parameter : arg) {
            messageBuilder.append(parameter).append(",");
        }
        messageBuilder.append(token);
        return messageBuilder.toString();
    }

    public String getResponseWithToken(String prefix, String methodName, String ...arg){
        return getResponse(setMessageWithToken(prefix,methodName,arg));
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void send(String message){
        try {
            dataOutputStream.writeUTF(message);
            dataOutputStream.flush();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public String receive(){
        String message = "";
        try {
            message = dataInputStream.readUTF();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return message;
    }
}
