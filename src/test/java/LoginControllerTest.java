import controller.LoginController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class LoginControllerTest {
    @Test
    public void createUser(){
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        LoginController loginController = LoginController.getInstance();
        loginController.createUser("AliRahim", "12345", "ali");
//        loginController.loginUser("AliRahim", "543");
        Assertions.assertEquals("Username and password didn't match!", outContent.toString());
    }
}
