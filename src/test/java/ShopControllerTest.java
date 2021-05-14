import com.opencsv.exceptions.CsvException;
import controller.LoginController;
import controller.ShopController;
import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class ShopControllerTest {
    @BeforeEach
    public void defineUser() throws IOException, CsvException {
        LoginController.instantiateCards();
        LoginController.getInstance().createUser("ali_r", "1234aAsS", "Ali");
    }
    @Test
    public void buyTest(){
        User user = LoginController.getUserByUsername("ali_r");
        ShopController.getInstance().buy(user, "Battle OX");
        Assertions.assertEquals(user.getBalance(), 97100);
    }

}
