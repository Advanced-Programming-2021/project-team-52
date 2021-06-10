package controller;

import com.opencsv.exceptions.CsvException;
import model.Deck;
import model.User;
import model.tools.RegexPatterns;
import view.PrinterAndScanner;

import java.io.IOException;
import java.util.regex.Matcher;

import java.util.ArrayList;
import java.util.HashMap;

import static model.tools.StringMessages.*;

// todo : fix problems of pattern for username, password and nickname in Pattern regexes
public class LoginController implements RegexPatterns {

    static LoginController loginController = null;
    static ArrayList<String> userNames;
    static ArrayList<String> nickNames;
    static HashMap<String, User> users;
    private static PrintBuilderController printBuilderController;
    private static PrinterAndScanner printerAndScanner;
//    private boolean loginChecker = false;

    //TODO remove next block
    static {
        try {
            instantiateCards();
        } catch (Exception e) {
            e.printStackTrace();
        }
        users = new HashMap<>();
        User test = User.createUser("a", "a", "a");
        Deck deck = new Deck("a");
        deck.addAllCardsToDeck();
        test.setActiveDeck(deck);
        users.put("a", test);
        test = User.createUser("b", "b", "b");
        deck = new Deck("b");
        deck.addAllCardsToDeck();
        test.setActiveDeck(deck);
        users.put("b", test);
    }

    {
        printBuilderController = PrintBuilderController.getInstance();
        printerAndScanner = PrinterAndScanner.getInstance();
        userNames = new ArrayList<>();
        nickNames = new ArrayList<>();
//        users = new HashMap<>();
    }

    private LoginController() {
    }

    public static void main(String[] args) throws IOException, CsvException {
//        instantiateCards();
        LoginController.getInstance().run();
    }

    public static LoginController getInstance() {
        if (loginController == null) loginController = new LoginController();
        return loginController;
    }

    public static ArrayList<String> getNickNames() {
        return nickNames;
    }

    public void run() {
        String command = printerAndScanner.scanNextLine();
        Matcher matcher;
        while (true) {
            if ((matcher = RegexController.getMatcher(command, loginPattern)) != null) {
                if (loginUser(matcher.group("username"), matcher.group("password")))
                    MainController.getInstance().run(users.get(matcher.group("username")));
            } else if ((matcher = RegexController.getMatcher(command, userCreatPattern)) != null) {
                createUser(matcher.group("username"), matcher.group("password"), matcher.group("nickname"));
            } else if ((matcher = RegexController.getMatcher(command, menuPattern)) != null) {
                if (RegexController.hasField(matcher, "showCurrent")) {
                    showCurrentMenu();
                } else if (RegexController.hasField(matcher, "exit")) {
                    break;
                } else if (RegexController.hasField(matcher, "enter")) {
                    printerAndScanner.printNextLine(loginFirst);
                } else printerAndScanner.printNextLine(invalidCommand);
            } else printerAndScanner.printNextLine(invalidCommand);
            command = printerAndScanner.scanNextLine();
        }
    }

    public static void instantiateCards() throws IOException, CsvException {
        InstantiateCards.start();

    }

    private void showCurrentMenu() {
        printerAndScanner.printNextLine(showLoginMenu);
    }

    public void createUser(String username, String password, String nickname) {
        if (RegexController.getMatcher(username, standardUsernameAndNickname) == null)
            printerAndScanner.printNextLine(createUserFailedBecauseOfUsername);
        else if ( RegexController.getMatcher(nickname, standardUsernameAndNickname) == null)
            printerAndScanner.printNextLine(createUserFailedBecauseOfNickname);
        else if ( RegexController.getMatcher(password, standardPassword) == null)
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

    public static User getUserByUsername(String username){
        if(users.containsKey(username))
            return users.get(username);
        return null;
    }

}

