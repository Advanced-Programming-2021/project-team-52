package sample.controller;

import com.opencsv.exceptions.CsvException;
import org.junit.Test;
import org.junit.jupiter.api.*;
import sample.model.Shop;
import sample.model.User;
import sample.model.tools.StringMessages;

import java.io.IOException;
import java.time.LocalDate;

public class ShopTest implements StringMessages {
    ShopController shopController;
    LoginController loginController;
    User user1;
    User user2;
    User user3;

    @Test
    public void test1() {
        try {
            LoginController.instantiateCards();
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
        shopController = ShopController.getInstance();
        loginController = LoginController.getInstance();
        loginController.createUser("ali", "1234AaSs", "ali", LocalDate.now());
        loginController.createUser("mamad", "1234AaSs", "mamad", LocalDate.now());
        loginController.createUser("reza", "1234AaSs", "reza", LocalDate.now());
        user1 = LoginController.getUserByUsername("ali");
        user2 = LoginController.getUserByUsername("mamad");
        user3 = LoginController.getUserByUsername("reza");

        shopController.buy(user1, "Battle OX");
        shopController.buy(user1, "Battle OX");
        shopController.buy(user2, "Battle OX");
        shopController.buy(user3, "Battle OX");
        Assertions.assertEquals(46, Shop.getNumberOfItems().get("Battle OX"));

        shopController.addCardToBannedCards("Battle OX");
        shopController.addCardToBannedCards("Yami");
        Assertions.assertEquals("The card is banned", shopController.buy(user1, "Battle OX"));
        Assertions.assertEquals("The card is banned", shopController.buy(user1, "Yami"));
        Assertions.assertEquals("The card is banned", shopController.buy(user2, "Yami"));
        Assertions.assertEquals("The card is banned", shopController.buy(user3, "Battle OX"));
        Assertions.assertEquals(46, Shop.getNumberOfItems().get("Battle OX"));
        Assertions.assertEquals(50, Shop.getNumberOfItems().get("Yami"));
        Assertions.assertEquals(2, user1.getCards().size());
        Assertions.assertEquals(1, user2.getCards().size());
        Assertions.assertEquals(1, user3.getCards().size());

        Assertions.assertEquals("Card has been already banned", shopController.addCardToBannedCards("Battle OX"));
        Assertions.assertEquals("Card banned successfully", shopController.addCardToBannedCards("Gate Guardian"));
        Assertions.assertEquals(noCardWithThisName, shopController.addCardToBannedCards("sdsfd"));

        Assertions.assertEquals("Card is not banned any more", shopController.removeCardFromBannedCards("Battle OX"));
        Assertions.assertEquals("Card was not banned", shopController.removeCardFromBannedCards("Battle OX"));
        shopController.buy(user1, "Battle OX");
        shopController.buy(user1, "Battle OX");
        shopController.buy(user2, "Battle OX");
        shopController.buy(user3, "Battle OX");
        Assertions.assertEquals(42, Shop.getNumberOfItems().get("Battle OX"));
        Assertions.assertEquals("The card is banned", shopController.buy(user1, "Yami"));


        user1.setBalance(1000000000);
        shopController.buy(user1, "Battle OX");
        shopController.buy(user1, "Battle OX");
        shopController.buy(user1, "Battle OX");
        shopController.buy(user1, "Battle OX");
        shopController.buy(user1, "Battle OX");
        shopController.buy(user1, "Battle OX");
        shopController.buy(user1, "Battle OX");
        shopController.buy(user1, "Battle OX");
        shopController.buy(user1, "Battle OX");
        shopController.buy(user1, "Battle OX");
        shopController.buy(user1, "Battle OX");
        shopController.buy(user1, "Battle OX");
        shopController.buy(user1, "Battle OX");
        shopController.buy(user1, "Battle OX");
        shopController.buy(user1, "Battle OX");
        shopController.buy(user1, "Battle OX");
        shopController.buy(user1, "Battle OX");
        shopController.buy(user1, "Battle OX");
        shopController.buy(user1, "Battle OX");
        shopController.buy(user1, "Battle OX");
        shopController.buy(user1, "Battle OX");
        shopController.buy(user1, "Battle OX");
        shopController.buy(user1, "Battle OX");
        shopController.buy(user1, "Battle OX");
        shopController.buy(user1, "Battle OX");
        shopController.buy(user1, "Battle OX");
        shopController.buy(user1, "Battle OX");
        shopController.buy(user1, "Battle OX");
        shopController.buy(user1, "Battle OX");
        shopController.buy(user1, "Battle OX");
        shopController.buy(user1, "Battle OX");
        shopController.buy(user1, "Battle OX");
        shopController.buy(user1, "Battle OX");
        shopController.buy(user1, "Battle OX");
        shopController.buy(user1, "Battle OX");
        shopController.buy(user1, "Battle OX");
        shopController.buy(user1, "Battle OX");
        shopController.buy(user1, "Battle OX");
        shopController.buy(user1, "Battle OX");
        shopController.buy(user1, "Battle OX");
        shopController.buy(user1, "Battle OX");
        Assertions.assertEquals(1, Shop.getNumberOfItems().get("Battle OX"));

        Assertions.assertEquals(cardBoughtSuccessfully, shopController.buy(user1, "Battle OX"));
        Assertions.assertEquals("There is not enough card in shop", shopController.buy(user1, "Battle OX"));
        Assertions.assertEquals("There is not enough card in shop", shopController.buy(user1, "Battle OX"));
        Assertions.assertEquals(0, Shop.getNumberOfItems().get("Battle OX"));

        Assertions.assertEquals(noCardWithThisName, shopController.addNumberOfCardToShop("asdasd", "12df3"));
        Assertions.assertEquals("Amount most be number", shopController.addNumberOfCardToShop("Battle OX", "12df3"));
        Assertions.assertEquals(0, Shop.getNumberOfItems().get("Battle OX"));

        Assertions.assertEquals("Number of card increased successfully", shopController.addNumberOfCardToShop("Battle OX", "20"));
        Assertions.assertEquals(20, Shop.getNumberOfItems().get("Battle OX"));
        Assertions.assertEquals(cardBoughtSuccessfully, shopController.buy(user1, "Battle OX"));
        Assertions.assertEquals(cardBoughtSuccessfully, shopController.buy(user1, "Battle OX"));
        Assertions.assertEquals(18, Shop.getNumberOfItems().get("Battle OX"));
    }

    @Test
    public void test2(){
        try {
            LoginController.instantiateCards();
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
        shopController = ShopController.getInstance();
        loginController = LoginController.getInstance();
        Shop shop = Shop.getInstance();
        loginController.createUser("ali", "1234AaSs", "ali", LocalDate.now());
        loginController.createUser("mamad", "1234AaSs", "mamad", LocalDate.now());
        loginController.createUser("reza", "1234AaSs", "reza", LocalDate.now());
        user1 = LoginController.getUserByUsername("ali");
        user2 = LoginController.getUserByUsername("mamad");
        user3 = LoginController.getUserByUsername("reza");
        Assertions.assertEquals(cardBoughtSuccessfully, shopController.buy(user1, "Battle OX"));
        Assertions.assertEquals(100002900, shop.getShopBalance());
        Assertions.assertEquals(cardBoughtSuccessfully, shopController.buy(user1, "Battle OX"));
        Assertions.assertEquals(100005800, shop.getShopBalance());

        Assertions.assertEquals(cardBoughtSuccessfully, shopController.buy(user1, "Yami"));
        Assertions.assertEquals(100010100, shop.getShopBalance());

        shopController.addCardToBannedCards("Gate Guardian");
        Assertions.assertEquals(noCardWithThisName, shopController.sellCard(user2, "ssdsdw"));
        Assertions.assertEquals("The card is banned", shopController.sellCard(user2, "Gate Guardian"));
        Assertions.assertEquals("You do not have enough of this card to sell", shopController.sellCard(user2, "Battle OX"));
        Assertions.assertEquals("Card sold successfully", shopController.sellCard(user1, "Battle OX"));
        Assertions.assertEquals(100010100 - 2900, shop.getShopBalance());
        Assertions.assertEquals(100000- 10100 + 2900, user1.getBalance());
    }
}
