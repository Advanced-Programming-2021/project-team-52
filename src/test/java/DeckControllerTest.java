import com.opencsv.exceptions.CsvException;
import controller.DeckController;
import controller.LoginController;
import controller.ShopController;
import model.Deck;
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

    @Test
    public void createDeckWithRepetitiveName() {
        System.setOut(new PrintStream(outContent));
        outContent.reset();

        deckController.createDeck("perfectDeck", user);
        Assertions.assertEquals("deck with name perfectDeck already exists\r\n", outContent.toString());
    }

    @Test
    public void createDeckTest(){
        System.setOut(new PrintStream(outContent));
        outContent.reset();
        deckController.createDeck("testDeck", user);
        Assertions.assertEquals("deck created successfully!\r\n", outContent.toString());

    }

    @Test
    public void showAllDeckOrderTest(){
        System.setOut(new PrintStream(outContent));
        deckController.createDeck("ZerfectDeck", user);
        deckController.createDeck("GoodDeck", user);
        deckController.createDeck("1oodDeck", user);
        deckController.createDeck("PerfectDeck", user);
        outContent.reset();
        deckController.showAllDecks(user);
        Assertions.assertEquals("Decks:\n" +
                "Active deck:\n" +
                "Other decks:\n" +
                "1oodDeck: main deck 0, side deck 0, invalid\n" +
                "GoodDeck: main deck 0, side deck 0, invalid\n" +
                "PerfectDeck: main deck 0, side deck 0, invalid\n" +
                "ZerfectDeck: main deck 0, side deck 0, invalid\n" +
                "goodDeck: main deck 0, side deck 0, invalid\n" +
                "perfectDeck: main deck 0, side deck 0, invalid\n\r\n", outContent.toString());
    }

    @Test
    public void showOneDeckTest(){
        System.setOut(new PrintStream(outContent));
        deckController.addCardToDeck("Battle OX", "perfectDeck",false, user);
        deckController.addCardToDeck("Battle OX", "perfectDeck", false, user);
        deckController.addCardToDeck("Baby dragon", "perfectDeck", true, user);
        deckController.addCardToDeck("Alexandrite Dragon", "perfectDeck", true, user);
        deckController.addCardToDeck("Unreal card", "perfectDeck",false, user);
        deckController.addCardToDeck("Alexandrite Dragon", "perfectDeck", false, user);
        deckController.addCardToDeck("Alexandrite Dragon", "perfectDeck",false, user);
        outContent.reset();
        deckController.showDeck(user,"perfectDeck", true);
        Assertions.assertEquals("Deck: perfectDeck\n" +
                "Monsters: \n" +
                "Alexandrite Dragon: Many of the czars' lost jewels can be found in the scales of this priceless " +
                "dragon." +
                " Its creator remains a mystery, along with how they acquired the imperial treasures. But whosoever " +
                "finds this dragon has hit the jackpot... whether they know it or not.\n" +
                "Baby dragon: Much more than just a child, this dragon is gifted with untapped power.\n" +
                "Spell: \n" +
                "Trap: \n\r\n", outContent.toString());
    }

    @Test
    public void addCardToDeckUnrealCardTest(){
        System.setOut(new PrintStream(outContent));
        outContent.reset();
        deckController.addCardToDeck("unreal card", "perfectDeck",false, user);
        Assertions.assertEquals("card with name " + "unreal card" + " does not exist\r\n", outContent.toString());
    }

    @Test
    public void addCardToUnrealDeck(){
        System.setOut(new PrintStream(outContent));
        outContent.reset();
        deckController.addCardToDeck("Battle OX", "unreal deck",false, user);
        Assertions.assertEquals("deck with name " + "unreal deck" + " does not exist\r\n",
                outContent.toString());
    }

    @Test
    public void addCardToFullDeck(){
        // at first change 2 numbers of isDeckFull() from 60 and 15 to 3 and 2
        System.setOut(new PrintStream(outContent));

        deckController.addCardToDeck("Battle OX", "perfectDeck",true, user);
        deckController.addCardToDeck("Axe Raider", "perfectDeck",true, user);
        outContent.reset();

        deckController.addCardToDeck("Baby dragon", "perfectDeck",true, user);
        Assertions.assertEquals( "side deck is full\r\n", outContent.toString());
    }

    @Test
    public void addCardMoreThanItStatusToDeck(){
        System.setOut(new PrintStream(outContent));

        Cards card = Cards.getCard("Alexandrite Dragon");
        card.setStatus("Limited");
        deckController.addCardToDeck("Alexandrite Dragon", "perfectDeck",true, user);
        outContent.reset();

        deckController.addCardToDeck("Alexandrite Dragon", "perfectDeck",false, user);
        Assertions.assertEquals( "there are already "+"1" +" cards with name " + "Alexandrite Dragon" +
                " in deck " + "perfectDeck\r\n", outContent.toString());
    }

    @Test
    public void deleteDeckTest(){
        System.setOut(new PrintStream(outContent));
        deckController.deleteDeck("perfectDeck", user);
        outContent.reset();
        deckController.createDeck("perfectDeck", user);
        Assertions.assertEquals("deck created successfully!\r\n", outContent.toString());
    }

    @Test
    public void cardsDestinationAfterDeletingDeckTest(){
        System.setOut(new PrintStream(outContent));
        deckController.addCardToDeck("Yomi Ship", "perfectDeck",false, user);
        deckController.deleteDeck("perfectDeck", user);
        outContent.reset();
        deckController.addCardToDeck("Yomi Ship", "goodDeck",false, user);
        Assertions.assertEquals("card added to deck successfully\r\n", outContent.toString());
    }

    @Test
    public void deckWithThisNameAlreadyExists(){
        System.setOut(new PrintStream(outContent));
        outContent.reset();
        deckController.addCardToDeck("Yomi Ship", "tt",false, user);
        Assertions.assertEquals("deck with name " + "tt" + " does not exist\r\n", outContent.toString());
    }

    @Test
    public void validDeckTest(){
        // first isDeckValid() numbers from 40 to 4
        Deck deck = user.getDeckByName("perfectDeck");
        deckController.addCardToDeck("Alexandrite Dragon", "perfectDeck", false, user);
        deckController.addCardToDeck("Yomi Ship", "perfectDeck", false, user);
        deckController.addCardToDeck("Alexandrite Dragon", "perfectDeck", false, user);
        deckController.addCardToDeck("Alexandrite Dragon", "perfectDeck", true, user);
        Assertions.assertFalse(deck.isDeckValid());
        deckController.addCardToDeck("Axe Raider", "perfectDeck", false, user);
        Assertions.assertTrue(deck.isDeckValid());
    }

    @Test
    public void activeDeckTest(){
        System.setOut(new PrintStream(outContent));
        Deck deck = user.getDeckByName("perfectDeck");
        deckController.addCardToDeck("Alexandrite Dragon", "perfectDeck", false, user);
        outContent.reset();
        deckController.activateDeck("perfectDeck", user);
        deckController.showAllDecks(user);
        Assertions.assertEquals("deck activated successfully\n" +
                "Decks:\n" +
                "Active deck:\n" +
                "perfectDeck: main deck 1, side deck 0, invalid\n" +
                "Other decks:\n" +
                "goodDeck: main deck 0, side deck 0, invalid\n\r", outContent.toString());
    }

    @Test
    public void showAllCardsOfUserTest(){
        System.setOut(new PrintStream(outContent));
        deckController.addCardToDeck("Yomi Ship", "perfectDeck", false, user);
        outContent.reset();
        deckController.showAllUserCards(user);
        Assertions.assertEquals("Alexandrite Dragon: Many of the czars' lost jewels can be found in the scales " +
                "of this priceless dragon. Its creator remains a mystery, along with how they acquired the imperial " +
                "treasures. But whosoever finds this dragon has hit the jackpot... whether they know it or not.\n" +
                "Axe Raider: An axe-wielding monster of tremendous strength and agility.\n" +
                "Baby dragon: Much more than just a child, this dragon is gifted with untapped power.\n" +
                "Battle OX: A monster with tremendous power, it destroys enemies with a swing of its axe.\n" +
                "Curtain of the dark ones: A curtain that a spellcaster made, it is said to raise a dark power.\n" +
                "Man_Eater Bug: FLIP: Target 1 monster on the field; destroy that target.\n" +
                "Yomi Ship: If this card is destroyed by battle and sent to the GY: Destroy the monster that destroyed " +
                "this card.\n\r\n", outContent.toString());

    }
}
