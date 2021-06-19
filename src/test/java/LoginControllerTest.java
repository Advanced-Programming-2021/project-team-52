import controller.LoginController;
import controller.MainController;
import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;


import static controller.specialbilities.SpecialAbility.printBuilderController;
import static model.tools.StringMessages.*;


public class LoginControllerTest {

    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    LoginController loginController = LoginController.getInstance();
    User user;
    

    @Test
    @DisplayName("create user test")
    public void createUser(){

        loginController.createUser("mamad","12345MFm52", "gholi");

        System.setOut(new PrintStream(outContent));

        outContent.reset();
        LoginController loginController = LoginController.getInstance();
        loginController.createUser("AliRahim@", "12345Nf2", "ali");
      //  loginController.loginUser("AliRahim@", "12345Nf2");
        Assertions.assertEquals(createUserFailedBecauseOfUsername,
                outContent.toString().trim().replace("\r",""));

        outContent.reset();
        loginController.createUser("AliRahim","12345", "ali#");
        Assertions.assertEquals(createUserFailedBecauseOfNickname,
                outContent.toString().trim().replace("\r",""));

        outContent.reset();
        loginController.createUser("AliRahim","1234575", "ali");
        Assertions.assertEquals(createUserFailedBecauseOfPasswordWeakness,
                outContent.toString().trim().replace("\r",""));

        outContent.reset();
        loginController.createUser("AliRahim","12345ereR4", "ali@#");
        Assertions.assertEquals(createUserFailedBecauseOfNickname,
                outContent.toString().trim().replace("\r",""));

       outContent.reset();
       loginController.createUser("mamad","123452MGr", "ali");
       Assertions.assertEquals(printBuilderController.thisUsernameAlreadyExists("mamad"),
               outContent.toString().trim().replace("\r",""));

       outContent.reset();
       loginController.createUser("AliRahim","1234523GTr", "gholi");
       Assertions.assertEquals(printBuilderController.thisNicknameAlreadyExists("gholi"),
               outContent.toString().trim().replace("\r",""));

        outContent.reset();
        loginController.createUser("AliRahim","12345ereR4", "ali");
        Assertions.assertEquals(createUserSuccessfully,
                outContent.toString().trim().replace("\r",""));

    }

    @Test
    @DisplayName("login User checker")
    public void loginUser(){
        loginController.createUser("AliRahim", "123456TGg", "AliBala");

        System.setOut(new PrintStream(outContent));

        outContent.reset();
        loginController.loginUser("AliRahim", "123456YHo");
        Assertions.assertEquals(usernameAndPasswordDoNotMatch,
                outContent.toString().trim().replace("\r", ""));

        outContent.reset();
        loginController.loginUser("AliRahimi", "123456TGg");
        Assertions.assertEquals(usernameAndPasswordDoNotMatch,
                outContent.toString().trim().replace("\r", ""));

        outContent.reset();
        loginController.loginUser("AliRahim", "123456TGg");
        Assertions.assertEquals(userLoggedInSuccessfully,
                outContent.toString().trim().replace("\r", ""));
    }

    @Test
    @DisplayName("menu checker")
    public void showCurrentMenu(){
        System.setOut(new PrintStream(outContent));
        outContent.reset();
        loginController.showCurrentMenu();
        Assertions.assertEquals(showLoginMenu, outContent.toString().trim().replace("\r", ""));
        outContent.reset();
        Assertions.assertEquals(showLoginMenu, showLoginMenu.trim().replace("\r", ""));
    }

    @Test
    public void runChecker(){
        System.setOut(new PrintStream(outContent));

        outContent.reset();
        loginController.run("user create --username ali_r --nickname ali --password 1234asAs");
        Assertions.assertEquals(createUserSuccessfully, outContent.toString().trim().replace("\r", ""));

//        outContent.reset();
//        loginController.run("user login --username ali_r --password 1234asAs");
//        lo
//        Assertions.assertEquals(userLoggedInSuccessfully, outContent.toString().trim().replace("\r", ""));

        outContent.reset();
        loginController.run("menu show-current");
        Assertions.assertEquals(showLoginMenu, outContent.toString().trim().replace("\r", ""));

        Assertions.assertTrue(loginController.run("menu exit"));

        outContent.reset();
        loginController.run("enter profile");
        Assertions.assertEquals(invalidCommand, outContent.toString().trim().replace("\r", ""));

        outContent.reset();
        loginController.run("menu enter profile");
        Assertions.assertEquals(loginFirst, outContent.toString().trim().replace("\r", ""));

    }

    @Test
    public void getUserByUsernameTest(){
        loginController.createUser("AliRahim", "123wwWWas123", "ali");
        loginController.createUser("mamad", "123wwWWas123", "ali");

        Assertions.assertNotNull(LoginController.getUserByUsername("AliRahim"));
        Assertions.assertNull(LoginController.getUserByUsername("aaa"));

    }


}

// import controller.LoginController;
// import org.junit.jupiter.api.Assertions;
// import org.junit.jupiter.api.Test;

// import java.io.ByteArrayOutputStream;
// import java.io.PrintStream;

// public class LoginControllerTest {
//     @Test
//     public void createUser(){
//         ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//         System.setOut(new PrintStream(outContent));
//         LoginController loginController = LoginController.getInstance();
//         loginController.createUser("AliRahim", "12345", "ali");
// //        loginController.loginUser("AliRahim", "543");
//         Assertions.assertEquals("Username and password didn't match!", outContent.toString());
//     }
// }
