package controller;

import model.User;

import java.util.ArrayList;
import java.util.HashMap;

public class LoginController {
    static LoginController loginController;
    static ArrayList<String> userName;
    static ArrayList<String> nicknames;
    static HashMap<String, User> users;
    User user;
    private PrintBulider printBulider;
    private printerAndScanner printerAndScanner;

    private LoginController(){}
    public static LoginController getinstance(){
        if (loginController == null) loginController = new LoginController();
        return loginController;
    }
    private static void intantiateCArds(){}
    public static User getUser(String username, String password) {
        return user;
    }
    private void run(){}
    private String showCurrentMenu(){}
    private User createUser(String username, String password, String nickname){}
    private void loginUser(String username, String password){}

}
