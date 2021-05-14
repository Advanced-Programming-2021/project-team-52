import com.opencsv.exceptions.CsvException;
import controller.LoginController;
import controller.ProfileController;
import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class ProfileControllerTest {
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Test
    public void changeNicknameWithExistingNickname() throws IOException, CsvException {
        System.setOut(new PrintStream(outContent));
        LoginController.instantiateCards();
        LoginController.getInstance().createUser("john", "123456John", "jojo");
        LoginController.getInstance().createUser("john", "123456John", "jojo");
        LoginController.getInstance().createUser("leonard", "123456Leonard", "leo");
        User user = LoginController.getUserByUsername("john");
        outContent.reset();
        ProfileController.changeNickname("leo", user);
        Assertions.assertEquals("user with nickname " + "leo" + " already exists\r\n", outContent.toString());
    }
}
