import controller.LoginController;
import controller.PrintBuilderController;
import controller.ProfileController;
import controller.ScoreBoardController;
import model.User;
import model.tools.StringMessages;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import view.PrinterAndScanner;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;


public class ScoreBoardControllerTest extends PrintBuilderController implements StringMessages {
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private static PrinterAndScanner printerAndScanner = PrinterAndScanner.getInstance();


    @Test
    public void sortUserByScoreTest() {
        System.setOut(new PrintStream(outContent));

        LoginController loginController = LoginController.getInstance();
        loginController.createUser("AliRahim", "12345AaZz", "AliRahim");
        User user = LoginController.getUserByUsername("AliRahim");
        user.setScore(20);

        loginController.createUser("BliRahim", "12345AaZz", "BliRahim");
        user = LoginController.getUserByUsername("BliRahim");
        user.setScore(20);

        loginController.createUser("aliRahim", "12345AaZz", "aliRahim");
        user = LoginController.getUserByUsername("aliRahim");
        user.setScore(20);

        loginController.createUser("aliRahjm", "12345AaZz", "aliRahjm");
        user = LoginController.getUserByUsername("aliRahjm");
        user.setScore(20);

        loginController.createUser("aziRahim", "12345AaZz", "aziRahim");
        user = LoginController.getUserByUsername("aziRahim");
        user.setScore(20);

        loginController.createUser("aziRahim", "12345AaZz", "aziRahim");
        user = LoginController.getUserByUsername("aziRahim");
        user.setScore(20);

        loginController.createUser("mamad", "12345AaZz", "mamad");
        user = LoginController.getUserByUsername("mamad");
        user.setScore(10);

        loginController.createUser("asghar", "12345AaZz", "asghar");
        user = LoginController.getUserByUsername("asghar");
        user.setScore(90);

        loginController.createUser("akbar", "12345AaZz", "akbar");
        user = LoginController.getUserByUsername("akbar");
        user.setScore(9);

        ScoreBoardController.getInstance().sortUserByScore();


        outContent.reset();
        Assertions.assertEquals("1- asghar: 90\n" +
                        "2- AliRahim: 20\n" +
                        "2- BliRahim: 20\n" +
                        "2- aliRahim: 20\n" +
                        "2- aliRahjm: 20\n" +
                        "2- aziRahim: 20\n" +
                        "7- mamad: 10\n" +
                        "8- akbar: 9"
                , ScoreBoardController.getInstance().toString().trim().replace("\r", ""));

        ProfileController.getInstance().changeNickname("parsa",
                LoginController.getUserByUsername("asghar"));

        outContent.reset();
        Assertions.assertEquals("1- parsa: 90\n" +
                        "2- AliRahim: 20\n" +
                        "2- BliRahim: 20\n" +
                        "2- aliRahim: 20\n" +
                        "2- aliRahjm: 20\n" +
                        "2- aziRahim: 20\n" +
                        "7- mamad: 10\n" +
                        "8- akbar: 9"
                , ScoreBoardController.getInstance().toString().trim().replace("\r", ""));

    }

    @Test
    public void showCurrent() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        ScoreBoardController.showCurrent();
        Assertions.assertEquals(getShowCurrentInScoreboardController,
                outContent.toString().trim().replace("\r", ""));
    }

    @Test
    public void testRegexes() {
        System.setOut(new PrintStream(outContent));
        LoginController loginController = LoginController.getInstance();
        ScoreBoardController scoreBoardController = ScoreBoardController.getInstance();

        loginController.createUser("AliRahim", "12345AaZz", "AliRahim");
        User user = LoginController.getUserByUsername("AliRahim");

        outContent.reset();
        scoreBoardController.run("scoreboard show");
        Assertions.assertEquals("1- AliRahim: 0",
                outContent.toString().trim().replace("\r", ""));

        Assertions.assertTrue(scoreBoardController.run("menu exit"));

        outContent.reset();
        scoreBoardController.run("menu enter profile");
        Assertions.assertEquals(menuNavigationIsNotPossible,
                outContent.toString().trim().replace("\r", ""));

        outContent.reset();
        scoreBoardController.run("menu show-current");
        Assertions.assertEquals(getShowCurrentInScoreboardController,
                outContent.toString().trim().replace("\r", ""));

        outContent.reset();
        scoreBoardController.run("menu sdfsdf");
        Assertions.assertEquals(invalidCommand,
                outContent.toString().trim().replace("\r", ""));

    }
}
