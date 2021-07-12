package sample.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.opencsv.exceptions.CsvException;
import javafx.scene.image.Image;
import sample.model.Deck;
import sample.model.User;
import sample.model.tools.RegexPatterns;
import sample.view.PrinterAndScanner;
import sample.view.UserKeeper;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.regex.Matcher;

import static sample.model.tools.StringMessages.*;

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
        User user = User.createUser("a", "a", "a", "", LocalDate.now());
        Deck deck = new Deck("a");
        for (int i = 0; i < 10; i++) deck.addCard("Battle OX", false);
        for (int i = 0; i < 10; i++) deck.addCard("Closed Forest", false);
        user.setActiveDeck(deck);
        User user1 = User.createUser("b", "b", "b", "", LocalDate.now());
        Deck deck1 = new Deck("b");
        for (int i = 0; i < 20; i++) deck1.addCard("Battle OX", false);
        user1.setActiveDeck(deck1);
        users.put("b", user1);
        users.put("a", user);
    }

    private LoginController() {
    }

    public static void main(String[] args) throws IOException, CsvException {
        instantiateCards();
//        LoginController.getInstance().start();
    }

    public static LoginController getInstance() {
        if (loginController == null) loginController = new LoginController();
        return loginController;
    }

    public static ArrayList<String> getNickNames() {
        return nickNames;
    }

//    public void start() {
//        readUser();
//        String command = printerAndScanner.scanNextLine();
//        while (true) {
//            if (run(command)) break;
//            command = printerAndScanner.scanNextLine();
//        }
//        saveUsers();
//    }

//    public boolean run(String command) {
//        Matcher matcher;
//        if ((matcher = RegexController.getMatcher(command, loginPattern)) != null) {
////            if (loginUser(matcher.group("username"), matcher.group("password")))
////                MainController.getInstance().start(users.get(matcher.group("username")));
//        } else if ((matcher = RegexController.getMatcher(command, userCreatPattern)) != null) {
//            createUser(matcher.group("username"), matcher.group("password"),
//                    matcher.group("nickname"));
//        } else if ((matcher = RegexController.getMatcher(command, menuPattern)) != null) {
//            if (RegexController.hasField(matcher, "showCurrent")) {
//                showCurrentMenu();
//            } else if (RegexController.hasField(matcher, "exit")) {
//                return true;
//            } else if (RegexController.hasField(matcher, "enter")) {
//                printerAndScanner.printNextLine(loginFirst);
//            } else printerAndScanner.printNextLine(invalidCommand);
//        } else printerAndScanner.printNextLine(invalidCommand);
//        return false;
//    }

//    private void createUser(String username, String password, String nickname) {
//    }

    public static void instantiateCards() throws IOException, CsvException {
        InstantiateCards.start();

    }

    private void showCurrentMenu() {
        printerAndScanner.printNextLine(showLoginMenu);
    }

    public String createUser(String username, String password, String nickname, LocalDate dateOfBirth) {
        if (RegexController.getMatcher(username, standardUsernameAndNickname) == null)
            return createUserFailedBecauseOfUsername;
        else if (RegexController.getMatcher(nickname, standardUsernameAndNickname) == null)
            return createUserFailedBecauseOfNickname;
        else if (RegexController.getMatcher(password, standardPassword) == null)
            return createUserFailedBecauseOfPasswordWeakness;
        else if (userNames.contains(username)) {
            return printBuilderController.thisUsernameAlreadyExists(username);
        } else if (nickNames.contains(nickname)) {
            return printBuilderController.thisNicknameAlreadyExists(nickname);
        } else {
            User user = User.createUser(username, password, nickname, getRandomImage(), dateOfBirth);
            userNames.add(username);
            nickNames.add(nickname);
            users.put(username, user);
//            UserKeeper.getInstance().setCurrentUser(user);
            return createUserSuccessfully;
        }
    }

    private String getRandomImage() {
        //            return new Image(new FileInputStream("./src/main/resources/media/images/profile/1.jpg"));
//            return new Image(new FileInputStream("C:\\Users\\paitakht\\IdeaProjects\\project-ppp\\src\\main\\resources\\media\\images\\profile\\1.jpg"));
//        File f = new File("C:\\Users\\paitakht\\IdeaProjects\\project-ppp\\src\\main\\resources\\media\\images\\profile\\1.jpg");
//        Image img = new Image(f.toURI().toString());
//        return img;
//        return new Image("src\\main\\resources\\media\\images\\profile\\1.jpg");
//        Random random = new Random();
//        int randomNumber = random.nextInt(5);
//        try {
////            System.out.println("./src/main/resources/media/images/profile/" /*+(new Random()).nextInt(5)+ */"1.jpg");
//            return new Image(new FileInputStream ("./src/main/resources/media/images/profile/" +/*(new Random()).nextInt(4)/ + */"1.jpg" ));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        return null;
        Random random = new Random();
        int randomInt = random.nextInt(4);
        randomInt++;
        return "./src/main/resources/media/images/profile/" + randomInt + ".jpg";

    }


    public String loginUser(String username, String password) {
        User user = users.get(username);
        if (user == null || !user.getPassword().equals(password)) {
            return usernameAndPasswordDoNotMatch;
        }
//        System.out.println(user.getUsername());
        UserKeeper.getInstance().setCurrentUser(user);
//        System.out.println(UserKeeper.getInstance().getCurrentUser());
        return userLoggedInSuccessfully;
    }

    public boolean isUsernameMatchesDateOfBirth(String username, LocalDate dateOfBirth) {
        User user = getUserByUsername(username);
        if (user == null)
            return false;
        return user.getDateOfBirth().equals(dateOfBirth);
    }

    public boolean isUserWithThisUsernameExists(String username) {
        return getUserByUsername(username) != null;
    }

    public static User getUserByUsername(String username) {
        if (users.containsKey(username))
            return users.get(username);
        return null;
    }

    public void saveUsers() {
        File file = new File("./src/main/resources/users");
        try {
            file.mkdir();
        } catch (Exception ignored) {
        }
        Gson gson = new Gson();
        for (User user : users.values()) {
            String nextUser = gson.toJson(user);
            try {
                FileWriter nextFile = new FileWriter("./src/main/resources/users/"
                        + user.getUsername() + ".json");
                nextFile.write(nextUser);
                nextFile.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void readUser() {
        File directory = new File("./src/main/resources/users");
        File[] allUsers = directory.listFiles();
        if (allUsers != null)
            for (File user : allUsers) {
                try {
                    String json = new String(Files.readAllBytes(Paths.get(user.getPath())));
                    User user1 = new Gson().fromJson(json, new TypeToken<User>() {
                    }.getType());
                    userNames.add(user1.getUsername());
                    nickNames.add(user1.getNickname());
                    users.put(user1.getUsername(), user1);
                } catch (Exception ignored) {
                }
            }
    }
}

