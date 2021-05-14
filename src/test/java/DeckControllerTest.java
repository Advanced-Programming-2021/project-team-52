import com.opencsv.exceptions.CsvException;
import controller.DeckController;
import controller.LoginController;
import controller.ShopController;
import model.Shop;
import model.User;
import model.cards.Cards;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class DeckControllerTest {

    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    User user;
    DeckController deckController = DeckController.getInstance();
    @BeforeEach
    public void init() throws IOException, CsvException {
        LoginController.instantiateCards();
        LoginController loginController = LoginController.getInstance();
        loginController.createUser("ali", "1234AaSs", "ali");
        user = LoginController.getUserByUsername("ali");
        ShopController shopController = ShopController.getInstance();
        shopController.buy(user, "Battle OX");
        shopController.buy(user, "Axe Raider");
        shopController.buy(user, "Yomi Ship");
        shopController.buy(user, "Curtain of the dark ones");
        shopController.buy(user, "Baby dragon");
        shopController.buy(user, "Man_Eater Bug");
        shopController.buy(user, "Alexandrite Dragon");
        shopController.buy(user, "Alexandrite Dragon");
        shopController.buy(user, "Alexandrite Dragon");
        shopController.buy(user, "Alexandrite Dragon");
        deckController.createDeck("perfectDeck", user);
        deckController.createDeck("goodDeck", user);
    }


//    @Test
//    public void CreateDeckTest(){
//        init();
//        System.setOut(new PrintStream(outContent));
//
//        User user = LoginController.getUserByUsername("ali");
//        DeckController.createDeck("perfectDeck", user);
//        outContent.reset();
//
//        DeckController.createDeck("perfectDeck", user);
//        Assertions.assertEquals("deck with name perfectDeck already exists\n", outContent.toString());
//    }

    @Test
    public void createDeckWithRepetitiveName() {
        System.setOut(new PrintStream(outContent));
        outContent.reset();

        deckController.createDeck("perfectDeck", user);
        Assertions.assertEquals("deck with name perfectDeck already exists\r\n", outContent.toString());
    }

    @Test
    public void showAllDeckOrderTest(){
        System.setOut(new PrintStream(outContent));
        outContent.reset();

//        DeckController.deleteDeck("perfectDeck", user);
//        DeckController.ge
//        DeckController.sho
        deckController.createDeck("ZerfectDeck", user);
        deckController.createDeck("GoodDeck", user);
        deckController.createDeck("1oodDeck", user);
        deckController.createDeck("PerfectDeck", user);
        deckController.showAllDecks(user);
        Assertions.assertEquals("deck with name perfectDeck already exists\r\n", outContent.toString());
    }

    @Test
    public void showOneDeckTest(){
        deckController.addCardToDeck("Battle OX", "perfectDeck",false, user);
        deckController.addCardToDeck("Battle OX", "perfectDeck", false, user);
        deckController.addCardToDeck("Baby dragon", "perfectDeck", true, user);
        deckController.addCardToDeck("Alexandrite Dragon", "perfectDeck", true, user);
        deckController.addCardToDeck("Unreal card", "perfectDeck",false, user);
        deckController.addCardToDeck("Alexandrite Dragon", "perfectDeck", false, user);
        deckController.addCardToDeck("Alexandrite Dragon", "perfectDeck",false, user);
        deckController.showDeck(user,"perfectDeck", true);
        Assertions.assertEquals("deck with name perfectDeck already exists\r\n", outContent.toString());
    }

    @Test
    public void addCardToDeck(){

    }
}
