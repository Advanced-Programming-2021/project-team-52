import com.opencsv.exceptions.CsvException;
import controller.LoginController;
import controller.PrintBuilderController;
import controller.ProfileController;
import model.User;
import model.tools.StringMessages;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.PrinterAndScanner;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ProfileControllerTest extends PrintBuilderController implements StringMessages {
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private static PrintBuilderController printBuilderController = PrintBuilderController.getInstance();
    private static PrinterAndScanner printerAndScanner = PrinterAndScanner.getInstance();
    private LoginController loginController = LoginController.getInstance();
    private User user;
    private ProfileController profileController = ProfileController.getInstance();

    @BeforeEach
    public void init() {
        loginController = LoginController.getInstance();
        loginController.createUser("AliRahim", "1234AaZz", "Ali");
        user = LoginController.getUserByUsername("AliRahim");
        loginController.createUser("mamadM", "1234AaZz", "mamad");
    }

    @Test
    public void changeNickname() {
        System.setOut(new PrintStream(outContent));
        loginController.createUser("AliRahim", "1234AaZz", "Ali");
        user = LoginController.getUserByUsername("AliRahim");

        outContent.reset();
        profileController.changeNickname("mamad", user);
        Assertions.assertEquals(printBuilderController.thisNicknameAlreadyExists("mamad")
                , outContent.toString().trim().replace("\r", ""));

        outContent.reset();
        profileController.changeNickname("Ali", user);
        Assertions.assertEquals(printBuilderController.thisNicknameAlreadyExists("Ali")
                , outContent.toString().trim().replace("\r", ""));

        outContent.reset();
        profileController.changeNickname("@li", user);
        Assertions.assertEquals(nonStandardNickname, outContent.toString().trim().replace("\r", ""));

        outContent.reset();
        profileController.changeNickname("Ali_R", user);
        Assertions.assertEquals(nicknameChangedSuccessfully,
                outContent.toString().trim().replace("\r", ""));

        Assertions.assertTrue(LoginController.getNickNames().contains("Ali_R"));
        Assertions.assertFalse(LoginController.getNickNames().contains("Ali"));
        Assertions.assertEquals(user.getNickname(), "Ali_R");

    }

    @Test
    public void changeUsername() {
        System.setOut(new PrintStream(outContent));

        outContent.reset();
        profileController.changeUsername("mamadM", user);
        Assertions.assertEquals(printBuilderController.thisUsernameAlreadyExists("mamadM")
                , outContent.toString().trim().replace("\r", ""));

        outContent.reset();
        profileController.changeUsername("mamad M", user);
        Assertions.assertEquals(nonStandardUsername
                , outContent.toString().trim().replace("\r", ""));

        loginController.createUser("AliRahimiii", "1234AaZz", "Ali");
        User AliUser = LoginController.getUserByUsername("AliRahim");

        outContent.reset();
        profileController.changeUsername("nanaz", AliUser);
        Assertions.assertEquals(usernameChangedSuccessfully
                , outContent.toString().trim().replace("\r", ""));
    }

    @Test
    public void changePassword() {
        System.setOut(new PrintStream(outContent));

        outContent.reset();
        profileController.changePassword("aa33A", "ad", user);
        Assertions.assertEquals(nonStandardPassword, outContent.toString().trim().replace("\r", ""));

        outContent.reset();
        profileController.changePassword("aa33AaaAa", "ad", user);
        Assertions.assertEquals(currentPasswordIsInvalid,
                outContent.toString().trim().replace("\r", ""));

        outContent.reset();
        profileController.changePassword("1234AaZz", "1234AaZz", user);
        Assertions.assertEquals(enterNewPassword, outContent.toString().trim().replace("\r", ""));

        outContent.reset();
        profileController.changePassword("12345AaZzQ", "1234AaZz", user);
        Assertions.assertEquals(passwordChangedSuccessfully,
                outContent.toString().trim().replace("\r", ""));
        Assertions.assertEquals(user.getPassword(), "12345AaZzQ");

        Assertions.assertEquals("12345AaZzQ", user.getPassword());

        outContent.reset();
        loginController.loginUser("AliRahim", "12345AaZzQ");
        Assertions.assertEquals("user logged in successfully!",
                outContent.toString().trim().replace("\r", ""));

        outContent.reset();
        loginController.loginUser("AliRahim", "1234AaZz");
        Assertions.assertEquals("Username and password didn't match!",
                outContent.toString().trim().replace("\r", ""));

    }

    @Test
    public void showCurrent() {
        System.setOut(new PrintStream(outContent));
        ProfileController.showCurrent();
        Assertions.assertEquals(showCurrentInProfileController, outContent.toString().trim().replace("\r", ""));
    }

    @Test
    public void testRegex(){
        System.setOut(new PrintStream(outContent));
        loginController.createUser("AliRahim", "1234AaZz", "Ali");
        user = LoginController.getUserByUsername("AliRahim");

        outContent.reset();
        profileController.run(user, "profile change --nickname aliii");
        Assertions.assertEquals(nicknameChangedSuccessfully
                , outContent.toString().trim().replace("\r", ""));

        outContent.reset();
        profileController.run(user, "profile change --password --current 1234AaZz --new opo1234AS");
        Assertions.assertEquals(passwordChangedSuccessfully
                , outContent.toString().trim().replace("\r", ""));

        Assertions.assertTrue(profileController.run(user,"menu exit"));

        outContent.reset();
        profileController.run(user,"menu enter profile");
        Assertions.assertEquals(menuNavigationIsNotPossible,
                outContent.toString().trim().replace("\r", ""));

        outContent.reset();
        profileController.run(user,"menu show-current");
        Assertions.assertEquals(showCurrentInProfileController,
                outContent.toString().trim().replace("\r", ""));

        outContent.reset();
        profileController.run(user,"menu sdfsdf");
        Assertions.assertEquals(invalidCommand,
                outContent.toString().trim().replace("\r", ""));
    }
}
