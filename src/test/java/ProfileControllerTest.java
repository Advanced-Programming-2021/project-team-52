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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
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

    //    @Test
//    public void changeNicknameWithExistingNickname() throws IOException, CsvException {
//        System.setOut(new PrintStream(outContent));
//        LoginController.instantiateCards();
//        LoginController.getInstance().createUser("john", "123456John", "jojo");
//        LoginController.getInstance().createUser("john", "123456John", "jojo");
//        LoginController.getInstance().createUser("leonard", "123456Leonard", "leo");
//        User user = LoginController.getUserByUsername("john");
//        outContent.reset();
//        ProfileController.getInstance().changeNickname("leo", user);
//        Assertions.assertEquals("user with nickname " + "leo" + " already exists\r\n", outContent.toString());
//    }
//    @Test
//    public void change() throws IOException, CsvException {
//        LoginController.instantiateCards();
//        LoginController loginController = LoginController.getInstance();
//        String input = "user create --username Ali --nickname Ali --password 1234AaZz";
//        ByteArrayInputStream inContent = new ByteArrayInputStream(input.getBytes());
//        System.setIn(inContent);
//        loginController.run();
//        Assertions.assertEquals("user created successfully!", outContent.toString());
//
//    }
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

//        outContent.reset();
//        profileController.changeUsername("aliNistam", user);
//        Assertions.assertEquals(usernameChangedSuccessfully
//                , outContent.toString().trim().replace("\r", ""));
//
//        outContent.reset();
//        loginController.loginUser("AliRahim", "1234AaZz");
//        Assertions.assertEquals("Username and password didn't match!",
//                outContent.toString().trim().replace("\r", ""));
//
//        outContent.reset();
//        loginController.loginUser("aliNistam", "1234AaZz");
//        Assertions.assertEquals("user logged in successfully!",
//                outContent.toString().trim().replace("\r", ""));

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
    public void testRegexes(){
        String s = "create user --username ali --password 1234 --nickname ali";
        ByteArrayInputStream bais = new ByteArrayInputStream(s.getBytes());
        System.setIn(bais);
        profileController.run(user);
    }
}
