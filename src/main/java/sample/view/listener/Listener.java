package sample.view.listener;

import com.opencsv.exceptions.CsvException;
import sample.controller.*;
import sample.model.User;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
                threadProcess(serverSocket, socket);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void threadProcess(ServerSocket serverSocket, Socket socket) {
        new Thread(() -> {
            try {
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                while (true) {
                    String input = dataInputStream.readUTF();
                    String response = new ActionFinder().chooseClass(input);
                    System.out.println("response : " + response);
                    if (response.equals("finish")) break;
                    dataOutputStream.writeUTF(response);
                    dataOutputStream.flush();
                }
                dataInputStream.close();
                socket.close();
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("a client disconnected");
            }
        }).start();
    }
}
