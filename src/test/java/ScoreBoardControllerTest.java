import controller.LoginController;
import org.junit.jupiter.api.Test;


public class ScoreBoardControllerTest {
    @Test
    public void sortUserByScoreTest(){
        LoginController loginController = LoginController.getInstance();
        loginController.createUser("AliRahim", "12345", "Ali");

    }
}
