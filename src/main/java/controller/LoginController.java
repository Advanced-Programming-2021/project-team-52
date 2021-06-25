package controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.opencsv.exceptions.CsvException;
import model.Deck;
import model.User;
import model.tools.RegexPatterns;
import view.PrinterAndScanner;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;

import static model.tools.StringMessages.*;

public class LoginController implements RegexPatterns {

    static LoginController loginController = null;
    static ArrayList<String> userNames;
    static ArrayList<String> nickNames;
    static HashMap<String, User> users;
    private static PrintBuilderController printBuilderController;
    private static PrinterAndScanner printerAndScanner;

    {
        printBuilderController = PrintBuilderController.getInstance();
        printerAndScanner = PrinterAndScanner.getInstance();
        userNames = new ArrayList<>();
        nickNames = new ArrayList<>();
        users = new HashMap<>();
    }

    private LoginController() {
    }

    public static void main(String[] args) throws IOException, CsvException {
        instantiateCards();
        LoginController.getInstance().start();
    }

    public static LoginController getInstance() {
        if (loginController == null) loginController = new LoginController();
        return loginController;
    }

    public static ArrayList<String> getNickNames() {
        return nickNames;
    }

    public void start() {
        readUser();
        String command = printerAndScanner.scanNextLine();
        while (true) {
            if (run(command)) break;
            command = printerAndScanner.scanNextLine();
        }
        saveUsers();
    }

    public boolean run(String command) {
        Matcher matcher;
        if ((matcher = RegexController.getMatcher(command, loginPattern)) != null) {
            if (loginUser(matcher.group("username"), matcher.group("password")))
                MainController.getInstance().start(users.get(matcher.group("username")));
        } else if ((matcher = RegexController.getMatcher(command, userCreatPattern)) != null) {
            createUser(matcher.group("username"), matcher.group("password"),
                    matcher.group("nickname"));
        } else if ((matcher = RegexController.getMatcher(command, menuPattern)) != null) {
            if (RegexController.hasField(matcher, "showCurrent")) {
                showCurrentMenu();
            } else if (RegexController.hasField(matcher, "exit")) {
                return true;
            } else if (RegexController.hasField(matcher, "enter")) {
                printerAndScanner.printNextLine(loginFirst);
            } else printerAndScanner.printNextLine(invalidCommand);
        } else printerAndScanner.printNextLine(invalidCommand);
        return false;
    }

    public static void instantiateCards() throws IOException, CsvException {
        InstantiateCards.start();

    }

    public void showCurrentMenu() {
        printerAndScanner.printNextLine(showLoginMenu);
    }

    public void createUser(String username, String password, String nickname) {
        if (RegexController.getMatcher(username, standardUsernameAndNickname) == null)
            printerAndScanner.printNextLine(createUserFailedBecauseOfUsername);
        else if (RegexController.getMatcher(nickname, standardUsernameAndNickname) == null)
            printerAndScanner.printNextLine(createUserFailedBecauseOfNickname);
        else if (RegexController.getMatcher(password, standardPassword) == null)
            printerAndScanner.printNextLine(createUserFailedBecauseOfPasswordWeakness);
        else if (userNames.contains(username)) {
            //bug fixed
            printerAndScanner.printNextLine(printBuilderController.thisUsernameAlreadyExists(username));
        } else if (nickNames.contains(nickname)) {
            //bug fixed
            printerAndScanner.printNextLine(printBuilderController.thisNicknameAlreadyExists(nickname));
        } else {
            User user = User.createUser(username, password, nickname);
            userNames.add(username);
            nickNames.add(nickname);
            users.put(username, user);
            printerAndScanner.printNextLine(createUserSuccessfully);
        }
    }

    public boolean loginUser(String username, String password) {
        User user = users.get(username);
        if (user == null || !user.getPassword().equals(password)) {
            printerAndScanner.printNextLine(usernameAndPasswordDoNotMatch);
            return false;
        }
        printerAndScanner.printNextLine(userLoggedInSuccessfully);
        return true;
    }

    public static User getUserByUsername(String username) {
        if (users.containsKey(username))
            return users.get(username);
        return null;
    }

    private void saveUsers(){
        File file = new File("./src/main/resources/users");
        try {
            file.mkdir();
        } catch (Exception ignored){
        }
        Gson gson = new Gson();
        for (User user : users.values()) {
            String nextUser = gson.toJson(user);
            try {
                FileWriter nextFile = new FileWriter("./src/main/resources/users/" + user.getUsername() + ".json");
                nextFile.write(nextUser);
                nextFile.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void readUser(){
        File directory = new File("./src/main/resources/users");
        File[] allUsers = directory.listFiles();
        if (allUsers != null)
            for (File user : allUsers) {
                try {
                    String json = new String(Files.readAllBytes(Paths.get(user.getPath())));
                    User user1 = new Gson().fromJson(json, new TypeToken<User>(){}.getType());
                    userNames.add(user1.getUsername());
                    nickNames.add(user1.getNickname());
                    users.put(user1.getUsername(), user1);
                } catch (Exception ignored){
                }
            }
    }

}

