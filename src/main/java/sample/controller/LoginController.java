package sample.controller;

import sample.model.User;
import sample.view.sender.Sender;

import java.time.LocalDate;
import java.util.HashMap;

public class LoginController {
    private Sender sender = Sender.getInstance();
    private final String PREFIX = "-LC-";

    static LoginController loginController = null;
    static HashMap<String, User> users = new HashMap<>();

    private LoginController() {
    }

    public static LoginController getInstance() {
        if (loginController == null) loginController = new LoginController();
        return loginController;
    }

    public String createUser(String username, String password, String nickname, LocalDate dateOfBirth) {
        return sender.getResponse(sender.setMessageWithoutToken
                (PREFIX, "createUser", username, password, nickname));
    }


    public String loginUser(String username, String password) {
//        return sender.setMessageWithToken(PREFIX, "loginUser", username, password);
//        return sender.getResponse(PREFIX + "loginUser " + username + "," + password);
        return sender.getResponse(sender.setMessageWithoutToken(PREFIX, "loginUser", username,password));
    }

    public boolean isUserWithThisUsernameExists(String username) {
//        return sender.convertStringToBoolean(sender.getResponse(PREFIX + "isUserWithThisUsernameExists," + username));
        return sender.convertStringToBoolean(sender.getResponse(sender.setMessageWithoutToken(PREFIX, "isUserWithThisUsernameExists", username)));
    }

    public static User getUserByUsername(String username) {
        if (users.containsKey(username))
            return users.get(username);
        return null;
    }

    public String logout(){
        return sender.getResponse(sender.setMessageWithToken(PREFIX, "logout"));
    }
}

