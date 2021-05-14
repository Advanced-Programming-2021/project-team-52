import controller.DeckController;
import controller.LoginController;
import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class DeckControllerTest {
    @BeforeEach
    public void init(){
        LoginController.getInstance().createUser("ali", "1234AaSs", "ali");
    }

    @Test
    public void CreateDeckTest(){
        init();
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        User user = LoginController.getUserByUsername("ali");
        DeckController.createDeck("perfectDeck", user);
        outContent.reset();

        DeckController.createDeck("perfectDeck", user);
        Assertions.assertEquals("deck with name perfectDeck already exists\n", outContent.toString());
    }
}
