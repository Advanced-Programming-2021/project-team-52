package sample.view;

import sample.model.User;

public class UserKeeper {
    private static UserKeeper userKeeper = null;
     User currentUser;

    private UserKeeper(){}

  public static UserKeeper getInstance(){
       if(userKeeper == null)
           userKeeper = new UserKeeper();
       return userKeeper;
  }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
