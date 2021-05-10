package controller;

import model.User;
import view.PrinterAndScanner;

import java.util.ArrayList;
import java.util.HashMap;

public class LoginController {
    static LoginController login;
    static ArrayList<String> userName;
    static ArrayList<String> nickNames;
    static HashMap<String, User> users;
    User user;
    private static PrintBuilderController printBuilderController;
    private static PrinterAndScanner printerAndScanner;
    private boolean loginChecker = false;

    private LoginController() {
    }

    public static LoginController getInstance() {
        if (login == null) login = new LoginController();
        return login;
    }

private void run(){
        String command = printerAndScanner.scanNextLine().toLowerCase();
        Matcher matcher;
        while (true){
            if((matcher = RegexController.getMatcher(command, loginPattern)) != null){
                loginUser(matcher.group("username"), matcher.group("password"));
            } else if((matcher = RegexController.getMatcher(command, userCreatPattern)) != null){
                createUser(matcher.group("username"), matcher.group("password"), matcher.group("nickname"));
            } else if ((matcher = RegexController.getMatcher(command, menuPattern)) != null){
                if(RegexController.hasField(matcher, "showcurrent")){
                    showCurrentMenu();
                } else if(RegexController.hasField(matcher, "exit")){
                    break;
                } else if (RegexController.hasField(matcher, "enter")){
                    if(loginChecker) {
                        MainController mainController = MainController.getInstance();
                        mainController.run(user);
                    } else {
                        System.out.println("please login first");
                    }
                }
            }
        }
    }

    private static void instantiateCards() {
        //todo: should be written after handling the cards
    }

    public static User getUser(String username, String password) {
        if (users.containsKey(username)) {
            if (users.get(username).getPassword().equals(password)) {
                return users.get(username).getUserByUsername(username);
            } else {
                System.out.println("user with name " + username + " doesn't exist or username and password doesnt match");
                return null;
            }
        } else {
            System.out.println("user with name " + username + " doesn't exist or username and password doesnt match");
            return null;
        }
    }

    private String showCurrentMenu() {
        return "login Menu";
    }

    private User createUser(String username, String password, String nickname) {
        if (userName.contains(username)) {
            System.out.println("user with username" + username + "already exist");
        }
        if (nickNames.contains(nickname)) {
            System.out.println("user with nickname" + nickname + "already exist");
        } else {
            User.createUser(username, password, nickname);
        }
        return user;
    }

    private void loginUser(String username, String password){
        if(userName.contains(username)){
            System.out.println("Username and password didn't match!");
        }
        else if(!(password.equals(user.getPassword()))){
            System.out.println("Username and password didn't match!");
        } else {
            user = user.getUserByUsername(username);
            loginChecker = true;
            System.out.println("user logged in successfully!");
        }
    }

    }
}
