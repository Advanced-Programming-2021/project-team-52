import controller.LoginController;
import controller.ScoreBoardController;
import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;


public class ScoreBoardControllerTest {
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Test
    public void sortUserByScoreTest(){
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
                "8- akbar: 9", ScoreBoardController.getInstance().toString().trim().replace("\r",""));
    }
}
