import controller.LoginController;
import controller.MainController;
import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.io.ByteArrayOutputStream;

public class MainControllerTest {
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    MainController mainController = MainController.getInstance();
    User user;

    @Test
    public void testRegex(){
    }
}
