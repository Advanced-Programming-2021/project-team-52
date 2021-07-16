package sample.controller;

import javafx.scene.image.Image;
import sample.model.User;
import sample.model.tools.RegexPatterns;
import sample.view.PrinterAndScanner;
import sample.model.tools.StringMessages;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.regex.Matcher;

public class ProfileController implements RegexPatterns, StringMessages {
    private static ProfileController profileController = null;
    private static PrintBuilderController printBuilderController = PrintBuilderController.getInstance();
    private static PrinterAndScanner printerAndScanner = PrinterAndScanner.getInstance();

    private ProfileController() {
    }

    public static ProfileController getInstance() {
        if (profileController == null)
            profileController = new ProfileController();
        return profileController;
    }


//    public void start(User user) {
//        String command = printerAndScanner.scanNextLine();
//        while (!run(user, command)) {
//            command = printerAndScanner.scanNextLine();
//        }
//    }
//
//    public boolean run(User user, String command) {
//        Matcher matcher;
//        if ((matcher = RegexController.getMatcher(command, profileChangeNickNamePattern)) != null) {
//            changeNickname(matcher.group("nickname"), user);
//        } else if ((matcher = RegexController.getMatcher(command, profileChangePasswordPattern)) != null) {
//            changePassword(matcher.group("new"), matcher.group("current"), user);
//        } else if ((matcher = RegexController.getMatcher(command, menuPattern)) != null) {
//            if (RegexController.hasField(matcher, "exit"))
//                return true;
//            else if (RegexController.hasField(matcher, "enter"))
//                printerAndScanner.printNextLine(menuNavigationIsNotPossible);
//            else if (RegexController.hasField(matcher, "showCurrent"))
//                showCurrent();
//            else
//                printerAndScanner.printNextLine(invalidCommand);
//        } else
//            printerAndScanner.printNextLine(invalidCommand);
//        return false;
//    }


    public String changeNickname(String newNickname, User user) {
        if (LoginController.nickNames.contains(newNickname)) {
            return printBuilderController.thisNicknameAlreadyExists(newNickname);
        }
        if (RegexController.getMatcher(newNickname, standardUsernameAndNickname) == null) {
            return nonStandardNickname;
        }
        LoginController.nickNames.remove(user.getNickname());
        user.setNickname(newNickname);
        LoginController.nickNames.add(newNickname);
        return nicknameChangedSuccessfully;
    }

//    public void changeUsername(String newUsername, User user) {
//        if (LoginController.userNames.contains(newUsername)) {
//            printerAndScanner.printNextLine(printBuilderController.thisUsernameAlreadyExists(newUsername));
//            return;
//        }
//        if (RegexController.getMatcher(newUsername, standardUsernameAndNickname) == null) {
//            printerAndScanner.printNextLine(nonStandardUsername);
//            return;
//        }
//        LoginController.userNames.remove(user.getUsername());
//        LoginController.users.remove(user.getUsername());
//        user.setUsername(newUsername);
//        LoginController.userNames.add(newUsername);
//        LoginController.users.put(newUsername, user);
//        printerAndScanner.printNextLine(usernameChangedSuccessfully);
//    }

    public String changePassword(String newPassword,String newPasswordAgain, String oldPassword, User user) {
        if(!newPassword.equals(newPasswordAgain))
            return inputPasswordsDoesNotMatch;
        if (RegexController.getMatcher(newPassword, RegexPatterns.standardPassword) == null)
            return nonStandardPassword;
        else if (!user.getPassword().equals(oldPassword))
            return currentPasswordIsInvalid;
        else if (oldPassword.equals(newPassword))
            return enterNewPassword;

        user.setPassword(newPassword);
        return passwordChangedSuccessfully;
    }

    public String changeProfileImage(String path, User user){
        File file = new File(path);
        if(!file.exists()) {
            return THERE_IS_NO_IMAGE_WITH_THIS_PATH;
        }
       user.setImageAddress(path);
        return imageChangedSuccessfully;
    }

//    public String changeProfileImage(User user, String newImageName){
//        try {
//            Image image = new Image("src\\main\\resources\\media\\images\\profile\\" + newImageName + ".jpg");
//            user.setProfileImage(image);
//            return imageChangedSuccessfully;
//        }catch (IllegalArgumentException e){
//            return noImageWithThisName;
//        }
//    }
//
//    public Image getUserImage(User user){
//        return user.getProfileImage();
//    }
//
//    public int getNumberOfLossesOfUser(User user){
//        return user.getNumberOfLosses();
//    }
//
//    public int getNumberOfWinsOfUser(User user){
//        return user.getNumberOfWins();
//    }

    private static void showCurrent() {
        printerAndScanner.printNextLine(showCurrentInProfileController);
    }
}
