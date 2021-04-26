package Controller;

import model.*;

public class ProfileController {
    private static ProfileController profile = null;
    private PrintBulider printBulider;
    private printerAndScanner printerAndScanner;

    private ProfileController() {
    }

    public static ProfileController getInstance() {
        if (profile == null)
            profile = new ProfileController();
        return profile;
    }

    public void run(User user) {
    }

    private static void changeNickname(String newNickname, User user) {
        if (LoginController.nicknames.contains(newNickname))
            System.out.println("user with nickname " + newNickname + "already exists");
        else {
            user.setNickname(newNickname);
            System.out.println("nickname changed successfully!");
        }
    }


    private static void changePassword(String newPassword, String oldPassword, User user) {
        if (user.getPassword().equals(oldPassword))
            System.out.println("current password is invalid");
        else if (oldPassword.equals(newPassword))
            System.out.println("please enter a new password");
        else {
            user.setPassword(newPassword);
            System.out.println("password changed successfully!");
        }
    }
}
