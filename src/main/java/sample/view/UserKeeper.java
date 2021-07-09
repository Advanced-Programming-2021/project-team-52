package sample.view;

import sample.model.User;

public class UserKeeper {
    static User currentUser;

    public static void setCurrentUser(User currentUser) {
        UserKeeper.currentUser = currentUser;
    }

    public static User getCurrentUser() {
        return currentUser;
    }
}
