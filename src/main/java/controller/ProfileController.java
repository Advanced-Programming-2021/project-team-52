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

    public void run(User user) {
        String command = printerAndScanner.scanNextLine().toLowerCase();
        Matcher matcher;
        while (true) {
            if ((matcher = RegexController.getMatcher(command, profileChangeNickNamePattern)) != null) {
                changeNickname(matcher.group("nickname"), user);
            } else if ((matcher = RegexController.getMatcher(command, profileChangePasswordPattern)) != null) {
                changePassword(matcher.group("new"), matcher.group("current"), user);
            } else if ((matcher = RegexController.getMatcher(command, menuPattern)) != null) {
                if (RegexController.hasField(matcher, "exit"))
                    break;
                else if (RegexController.hasField(matcher, "enter"))
                    printerAndScanner.printNextLine(menuNavigationIsNotPossible);
                else if (RegexController.hasField(matcher, "showCurrent"))
                    showCurrent();
                else
                    printerAndScanner.printNextLine(invalidCommand);
            } else
                printerAndScanner.printNextLine(invalidCommand);
            command = printerAndScanner.scanNextLine().toLowerCase();
        }
    }

    public static void changeNickname(String newNickname, User user) {
        Matcher matcher;
        if (LoginController.nickNames.contains(newNickname)) {
            printerAndScanner.printNextLine(printBuilderController.thisNicknameAlreadyExists(newNickname));
            return;
        }
        if ((matcher = RegexController.getMatcher(newNickname, standardUsernameAndNickname)) == null) {
            printerAndScanner.printNextLine(nonStandardNickname);
            return;
        }
        LoginController.nickNames.remove(user.getNickname());
        user.setNickname(newNickname);
        LoginController.nickNames.add(newNickname);
        printerAndScanner.printNextLine(nicknameChangedSuccessfully);
    }


    public static void changePassword(String newPassword, String oldPassword, User user) {
        Matcher matcher;
        if ((matcher = RegexController.getMatcher(newPassword, RegexPatterns.standardPassword)) == null) {
            printerAndScanner.printNextLine(nonStandardPassword);
        } else if (user.getPassword().equals(oldPassword))
            printerAndScanner.printNextLine(currentPasswordIsInvalid);
        else if (oldPassword.equals(newPassword))
            printerAndScanner.printNextLine(enterNewPassword);
        else {
            user.setPassword(newPassword);
            printerAndScanner.printNextLine(passwordChangedSuccessfully);
        }
    }

    private static void showCurrent() {
        printerAndScanner.printNextLine(showCurrentInProfileController);
    }
}
