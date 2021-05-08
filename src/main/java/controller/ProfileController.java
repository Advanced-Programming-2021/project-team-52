package controller;

import model.User;
import model.tools.RegexPatterns;
import org.graalvm.compiler.phases.graph.ScopedPostOrderNodeIterator;
import view.PrinterAndScanner;
import model.tools.StringMessages;

import java.util.regex.Matcher;

// todo : ask about static
public class ProfileController implements RegexPatterns, StringMessages {
    private static ProfileController profile = null;
    private static PrintBuilderController printBuilderController;
    private static PrinterAndScanner printerAndScanner;

    private ProfileController() {
    }

    public static ProfileController getInstance() {
        if (profile == null)
            profile = new ProfileController();
        return profile;
    }

    public void run(User user) {
        String command = printerAndScanner.scanNextLine();
        Matcher matcher;
        while (!command.equals("menu exit")) {
            if ((matcher = RegexController.getMatcher(command, profileChangeNickNamePattern)) != null) {
                changeNickname(matcher.group("nickname"), user);
            } else if ((matcher = RegexController.getMatcher(command, profileChangePasswordPattern)) != null) {
                changePassword(matcher.group("new"), matcher.group("current"), user);
            }else if((matcher = RegexController.getMatcher(command, menuPattern)) != null){
                // todo : exit menu
                // todo : how does menu navigation works
                // todo : show current
                // todo : do they make null in different group? why it didn't work
            }
            command = printerAndScanner.scanNextLine();
        }
    }

    private static void changeNickname(String newNickname, User user) {
        if (LoginController.nicknames.contains(newNickname))
            printerAndScanner.printNextLine(printBuilderController.thisNicknameAlreadyExists(newNickname));
        else {
            user.setNickname(newNickname);
            printerAndScanner.printNextLine(nicknameChangedSuccessfully);
        }
    }


    private static void changePassword(String newPassword, String oldPassword, User user) {
        if (user.getPassword().equals(oldPassword))
            printerAndScanner.printNextLine(currentPasswordIsInvalid);
        else if (oldPassword.equals(newPassword))
            printerAndScanner.printNextLine(enterNewPassword);
        else {
            user.setPassword(newPassword);
            printerAndScanner.printNextLine(passwordChangedSuccessfully);
        }
    }
}
