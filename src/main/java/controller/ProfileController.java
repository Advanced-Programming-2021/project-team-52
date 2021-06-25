package controller;

import model.User;
import model.tools.RegexPatterns;
import view.PrinterAndScanner;
import model.tools.StringMessages;

import java.util.regex.Matcher;

public class ProfileController implements RegexPatterns, StringMessages {
    private static ProfileController profile = null;
    private static PrintBuilderController printBuilderController = PrintBuilderController.getInstance();
    private static PrinterAndScanner printerAndScanner = PrinterAndScanner.getInstance();

    private ProfileController() {
    }

    public static ProfileController getInstance() {
        if (profile == null)
            profile = new ProfileController();
        return profile;
    }


    public void start(User user) {
        String command = printerAndScanner.scanNextLine();
        while (!run(user, command)) {
            command = printerAndScanner.scanNextLine();
        }
    }

    public boolean run(User user, String command) {
        Matcher matcher;
        if ((matcher = RegexController.getMatcher(command, profileChangeNickNamePattern)) != null) {
            changeNickname(matcher.group("nickname"), user);
        } else if ((matcher = RegexController.getMatcher(command, profileChangePasswordPattern)) != null) {
            changePassword(matcher.group("new"), matcher.group("current"), user);
        } else if ((matcher = RegexController.getMatcher(command, menuPattern)) != null) {
            if (RegexController.hasField(matcher, "exit"))
                return true;
            else if (RegexController.hasField(matcher, "enter"))
                printerAndScanner.printNextLine(menuNavigationIsNotPossible);
            else if (RegexController.hasField(matcher, "showCurrent"))
                showCurrent();
            else
                printerAndScanner.printNextLine(invalidCommand);
        } else
            printerAndScanner.printNextLine(invalidCommand);
        return false;
    }


    public void changeNickname(String newNickname, User user) {
        if (LoginController.nickNames.contains(newNickname)) {
            printerAndScanner.printNextLine(printBuilderController.thisNicknameAlreadyExists(newNickname));
            return;
        }
        if (RegexController.getMatcher(newNickname, standardUsernameAndNickname) == null) {
            printerAndScanner.printNextLine(nonStandardNickname);
            return;
        }
        LoginController.nickNames.remove(user.getNickname());
        user.setNickname(newNickname);
        LoginController.nickNames.add(newNickname);
        printerAndScanner.printNextLine(nicknameChangedSuccessfully);
    }

    public void changeUsername(String newUsername, User user) {
        if (LoginController.userNames.contains(newUsername)) {
            printerAndScanner.printNextLine(printBuilderController.thisUsernameAlreadyExists(newUsername));
            return;
        }
        if (RegexController.getMatcher(newUsername, standardUsernameAndNickname) == null) {
            printerAndScanner.printNextLine(nonStandardUsername);
            return;
        }
        LoginController.userNames.remove(user.getUsername());
        LoginController.users.remove(user.getUsername());
        user.setUsername(newUsername);
        LoginController.userNames.add(newUsername);
        LoginController.users.put(newUsername, user);
        printerAndScanner.printNextLine(usernameChangedSuccessfully);
    }

    public void changePassword(String newPassword, String oldPassword, User user) {
        if (RegexController.getMatcher(newPassword, RegexPatterns.standardPassword) == null) {
            printerAndScanner.printNextLine(nonStandardPassword);
        } else if (!user.getPassword().equals(oldPassword))
            printerAndScanner.printNextLine(currentPasswordIsInvalid);
        else if (oldPassword.equals(newPassword))
            printerAndScanner.printNextLine(enterNewPassword);
        else {
            user.setPassword(newPassword);
            printerAndScanner.printNextLine(passwordChangedSuccessfully);
        }
    }

    public static void showCurrent() {
        printerAndScanner.printNextLine(showCurrentInProfileController);
    }
}
