package sample.view.listener;

import com.opencsv.exceptions.CsvException;
import sample.controller.LoginController;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Listener {

    private LoginController loginController = LoginController.getInstance();

    public Listener() {
        try {
            LoginController.instantiateCards();
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
        loginController.readUser();
    }

    public void connectToClient() {
        try {
            ServerSocket serverSocket = new ServerSocket(7755);
            while (true) {
                Socket socket = serverSocket.accept();
                threadProcess(socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void threadProcess(Socket socket) {
//        new Thread(() -> {
//            try {
//                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
//                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
//                ActionFinder actionFinder = new ActionFinder();
//                while (true) {
//                    String input = dataInputStream.readUTF();
//                    String response = actionFinder.chooseClass(input);
//                    System.out.println("response : " + response);
//                    if (response.equals("finish")) break;
//                    dataOutputStream.writeUTF(response);
//                    dataOutputStream.flush();
//                }
//                dataInputStream.close();
//                socket.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//                System.out.println("a client disconnected");
//            }
//        }).start();
        try {
            Communicator communicator = new Communicator(socket,
                    new DataInputStream(socket.getInputStream()), new DataOutputStream(socket.getOutputStream()));
            communicator.startReceiving();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
