package sample.controller;

import sample.view.sender.Sender;

public class ProfileController {
    private static ProfileController profileController = null;
    private Sender sender = Sender.getInstance();
    private final String PREFIX = "-PC-";

    private ProfileController() {
    }

    public static ProfileController getInstance() {
        if (profileController == null)
            profileController = new ProfileController();
        return profileController;
    }

    public String changeNickname(String newNickname) {
        return sender.getResponse(sender.setMessageWithToken(PREFIX, "changeNickname", newNickname));
    }

    public String changePassword(String newPassword, String newPasswordAgain, String oldPassword) {
        return sender.getResponse(sender.setMessageWithToken(PREFIX, "changePassword", newPassword,
                newPasswordAgain, oldPassword));
    }

    public String changeProfileImage(String path) {
        return sender.getResponse(sender.setMessageWithToken(PREFIX, "changeProfileImage", path));
    }
}
