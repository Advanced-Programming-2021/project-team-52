package controller;

import model.User;
import model.tools.RegexPatterns;
import view.PrinterAndScanner;

import java.util.regex.Matcher;

import java.util.ArrayList;
import java.util.HashMap;

public class LoginController implements RegexPatterns {
    static LoginController loginController = null;
    static ArrayList<String> userNames;
    static ArrayList<String> nickNames;
    static HashMap<String, User> users;
    User user;
    private static PrintBuilderController printBuilderController;
    private static PrinterAndScanner printerAndScanner;
    private boolean loginChecker = false;

    {
        printBuilderController = PrintBuilderController.getInstance();
        printerAndScanner = PrinterAndScanner.getInstance();
        userNames = new ArrayList<>();
        nickNames = new ArrayList<>();
        users = new HashMap<>();
    }

    private LoginController() {
    }

    public static void main(String[] args) {
        LoginController.getInstance().run();
    }

    public static LoginController getInstance() {
        if (loginController == null) loginController = new LoginController();
        return loginController;
    }


    private void run() {
        String command = printerAndScanner.scanNextLine().toLowerCase();
        Matcher matcher;
        while (true) {
            if ((matcher = RegexController.getMatcher(command, loginPattern)) != null) {
                loginUser(matcher.group("username"), matcher.group("password"));
            } else if ((matcher = RegexController.getMatcher(command, userCreatPattern)) != null) {
                createUser(matcher.group("username"), matcher.group("password"), matcher.group("nickname"));
            } else if ((matcher = RegexController.getMatcher(command, menuPattern)) != null) {
                if (RegexController.hasField(matcher, "showcurrent")) {
                    showCurrentMenu();
                } else if (RegexController.hasField(matcher, "exit")) {
                    break;
                } else if (RegexController.hasField(matcher, "enter")) {
                    if (loginChecker) {
                        MainController mainController = MainController.getInstance();
                        mainController.run(user);
                    } else {
                        System.out.println("please login first");
                    }
                }
            } else System.out.println("invalid command");
            command = printerAndScanner.scanNextLine().toLowerCase();
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

    public void createUser(String username, String password, String nickname) {
        if (userNames.contains(username)) {
            System.out.println("user with username" + username + "already exist");
        }
        if (nickNames.contains(nickname)) {
            System.out.println("user with nickname" + nickname + "already exist");
        } else {
            User.createUser(username, password, nickname);
            userNames.add(username);
            nickNames.add(nickname);
            users.put(username, user);
            System.out.println("user created successfully!");
        }
    }

    private void loginUser(String username, String password) {
        if (userNames.contains(username)) {
            System.out.println("Username and password didn't match!");
        } else if (!(password.equals(user.getPassword()))) {
            System.out.println("Username and password didn't match!");
        } else {
            user = user.getUserByUsername(username);
            loginChecker = true;
            System.out.println("user logged in successfully!");
        }
    }

}

