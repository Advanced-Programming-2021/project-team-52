package sample.controller;


import com.opencsv.exceptions.CsvException;

import org.junit.Test;
import sample.model.Auction;
import sample.model.Deck;
import sample.model.User;
import org.junit.jupiter.api.*;
import sample.model.tools.StringMessages;

import java.io.IOException;
import java.time.LocalDate;

public class AuctionControllerTest implements StringMessages {
    AuctionController auctionController;
    DeckController deckController;
    User auctioneer;
    User buyer1;
    User buyer2;

    @Test
    public void makeAuctionTest() {
        try {
            LoginController.instantiateCards();
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
        auctionController = AuctionController.getInstance();
        deckController = DeckController.getInstance();
        LoginController.getInstance().createUser("ali", "1234AaSs", "ali", LocalDate.now());
        LoginController.getInstance().createUser("mamad", "1234AaSs", "mamad", LocalDate.now());
        LoginController.getInstance().createUser("reza", "1234AaSs", "reza", LocalDate.now());
        auctioneer = LoginController.getUserByUsername("ali");
        buyer1 = LoginController.getUserByUsername("mamad");
        buyer2 = LoginController.getUserByUsername("reza");
        deckController.createDeck("poop", auctioneer);

        Assertions.assertEquals(FIRST_PRICE_MOST_BE_BETWEEN_100_AND_10000, auctionController.makeAnAuction(auctioneer, "10", "Battle OX"));
        Assertions.assertEquals(PLEASE_ONLY_ENTER_NUMBER_FOR_PRICE_OR_ID, auctionController.makeAnAuction(auctioneer, "1d0", "Battle OX"));
        Assertions.assertEquals(CARD_NAME_IS_INVALID, auctionController.makeAnAuction(auctioneer, "1000", "Battle OXx"));
        auctioneer.addCards("Battle OX");
        Assertions.assertEquals(AUCTION_HAS_BEEN_CREATED_SUCCESSFULLY, auctionController.makeAnAuction(auctioneer, "1000", "Battle OX"));
        Assertions.assertEquals(YOU_DO_NOT_HAVE_ANY_OF_THIS_CARD_OUT_OF_DECK, auctionController.makeAnAuction(auctioneer, "1000", "Battle OX"));
        Assertions.assertEquals(0, auctioneer.getCards().size());

        auctioneer.addCards("Yami");
        deckController.addCardToDeck("Yami", "poop", false, auctioneer);
        Assertions.assertEquals(YOU_DO_NOT_HAVE_ANY_OF_THIS_CARD_OUT_OF_DECK, auctionController.makeAnAuction(auctioneer, "1000", "Yami"));
    }

    @Test
    public void participateToAuctionTest() {
        try {
            LoginController.instantiateCards();
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
        auctionController = AuctionController.getInstance();
        deckController = DeckController.getInstance();
        LoginController.getInstance().createUser("ali", "1234AaSs", "ali", LocalDate.now());
        LoginController.getInstance().createUser("mamad", "1234AaSs", "mamad", LocalDate.now());
        LoginController.getInstance().createUser("reza", "1234AaSs", "reza", LocalDate.now());
        auctioneer = LoginController.getUserByUsername("ali");
        buyer1 = LoginController.getUserByUsername("mamad");
        buyer2 = LoginController.getUserByUsername("reza");
        deckController.createDeck("poop", auctioneer);

        auctioneer.addCards("Battle OX");
        Assertions.assertEquals(AUCTION_HAS_BEEN_CREATED_SUCCESSFULLY, auctionController.makeAnAuction(auctioneer, "1000", "Battle OX"));
        Assertions.assertEquals(YOU_CAN_NOT_ATTEND_TO_YOUR_OWN_AUCTION, auctionController.participateToAuction(auctioneer, "999", "10000"));
        Assertions.assertEquals(PLEASE_ONLY_ENTER_NUMBER_FOR_PRICE_OR_ID, auctionController.participateToAuction(auctioneer, "999", "10d000"));

        Assertions.assertEquals(YOU_ATTENDED_TO_AUCTION_SUCCESSFULLY, auctionController.participateToAuction(buyer1, "999", "10000"));
        Assertions.assertEquals(90000, buyer1.getBalance());
        Assertions.assertEquals(YOUR_OFFER_IS_LESS_OR_EQUAL_THAN_BEST_OFFER, auctionController.participateToAuction(buyer2, "999", "10000"));
        Assertions.assertEquals(YOU_ATTENDED_TO_AUCTION_SUCCESSFULLY, auctionController.participateToAuction(buyer2, "999", "20000"));
        Assertions.assertEquals(100000, buyer1.getBalance());
        Assertions.assertEquals(80000, buyer2.getBalance());

        Assertions.assertEquals(YOU_DO_NOT_HAVE_ENOUGH_MONEY, auctionController.participateToAuction(buyer1, "999", "200000"));

    }

    @Test
    public void finishAuctionTest1(){
        // change FIRST_TOTAL_TIME to 20000
        // change TOTAL_TIME_AFTER_OFFER to 10000
        try {
            LoginController.instantiateCards();
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
        auctionController = AuctionController.getInstance();
        deckController = DeckController.getInstance();
        LoginController.getInstance().createUser("ali", "1234AaSs", "ali", LocalDate.now());
        LoginController.getInstance().createUser("mamad", "1234AaSs", "mamad", LocalDate.now());
        LoginController.getInstance().createUser("reza", "1234AaSs", "reza", LocalDate.now());
        auctioneer = LoginController.getUserByUsername("ali");
        buyer1 = LoginController.getUserByUsername("mamad");
        buyer2 = LoginController.getUserByUsername("reza");
        deckController.createDeck("poop", auctioneer);

        auctioneer.addCards("Battle OX");
        Assertions.assertEquals(1, auctioneer.getCards().size());
        Assertions.assertEquals(0, auctioneer.getCardsInAuction().size());
        Assertions.assertEquals(AUCTION_HAS_BEEN_CREATED_SUCCESSFULLY, auctionController.makeAnAuction(auctioneer, "1000", "Battle OX"));
        Assertions.assertEquals(0, auctioneer.getCards().size());
        Assertions.assertEquals(1, auctioneer.getCardsInAuction().size());
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(1, auctioneer.getCards().size());
        Assertions.assertEquals(0, auctioneer.getCardsInAuction().size());
        Assertions.assertEquals(THERE_IS_NO_AUCTION_WITH_THIS_ID, auctionController.participateToAuction(buyer1, "999", "10000"));
        Assertions.assertEquals(100000, auctioneer.getBalance());
    }

    @Test
    public void finishAuctionTest2(){
        // change FIRST_TOTAL_TIME to 20000
        // change TOTAL_TIME_AFTER_OFFER to 10000
        try {
            LoginController.instantiateCards();
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
        auctionController = AuctionController.getInstance();
        deckController = DeckController.getInstance();
        LoginController.getInstance().createUser("ali", "1234AaSs", "ali", LocalDate.now());
        LoginController.getInstance().createUser("mamad", "1234AaSs", "mamad", LocalDate.now());
        LoginController.getInstance().createUser("reza", "1234AaSs", "reza", LocalDate.now());
        auctioneer = LoginController.getUserByUsername("ali");
        buyer1 = LoginController.getUserByUsername("mamad");
        buyer2 = LoginController.getUserByUsername("reza");
        deckController.createDeck("poop", auctioneer);

        auctioneer.addCards("Battle OX");
        Assertions.assertEquals(AUCTION_HAS_BEEN_CREATED_SUCCESSFULLY, auctionController.makeAnAuction(auctioneer, "1000", "Battle OX"));
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(YOU_ATTENDED_TO_AUCTION_SUCCESSFULLY, auctionController.participateToAuction(buyer1, "999", "10000"));
        Assertions.assertEquals(10000, AuctionController.getActiveActionById(999).getTotalTime());
        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(YOU_ATTENDED_TO_AUCTION_SUCCESSFULLY, auctionController.participateToAuction(buyer2, "999", "20000"));
        try {
            Thread.sleep(11000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(THERE_IS_NO_AUCTION_WITH_THIS_ID, auctionController.participateToAuction(buyer1, "999", "20000"));
        Assertions.assertEquals(120000,auctioneer.getBalance());
        Assertions.assertEquals(80000, buyer2.getBalance());
        Assertions.assertEquals(100000, buyer1.getBalance());
        Assertions.assertTrue(buyer2.getCards().contains("Battle OX"));
        Assertions.assertFalse(auctioneer.getCards().contains("Battle OX"));
        Assertions.assertFalse(auctioneer.getCardsInAuction().contains("Battle OX"));
        Assertions.assertFalse(buyer1.getCards().contains("Battle OX"));
        Assertions.assertFalse(buyer1.getCardsInAuction().contains("Battle OX"));
    }

    @Test
    public void  getAllAuctions(){
        try {
            LoginController.instantiateCards();
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
        auctionController = AuctionController.getInstance();
        deckController = DeckController.getInstance();
        LoginController.getInstance().createUser("ali", "1234AaSs", "ali", LocalDate.now());
        LoginController.getInstance().createUser("mamad", "1234AaSs", "mamad", LocalDate.now());
        LoginController.getInstance().createUser("reza", "1234AaSs", "reza", LocalDate.now());
        auctioneer = LoginController.getUserByUsername("ali");
        buyer1 = LoginController.getUserByUsername("mamad");
        buyer2 = LoginController.getUserByUsername("reza");
        deckController.createDeck("poop", auctioneer);

        auctioneer.addCards("Battle OX");
        Assertions.assertEquals(AUCTION_HAS_BEEN_CREATED_SUCCESSFULLY, auctionController.makeAnAuction(auctioneer, "1000", "Battle OX"));
        Assertions.assertEquals(YOU_ATTENDED_TO_AUCTION_SUCCESSFULLY, auctionController.participateToAuction(buyer1, "999", "10000"));
        Assertions.assertEquals(YOU_ATTENDED_TO_AUCTION_SUCCESSFULLY, auctionController.participateToAuction(buyer2, "999", "20000"));
        auctioneer.addCards("Yami");
        Assertions.assertEquals(YOU_DO_NOT_HAVE_ANY_OF_THIS_CARD_OUT_OF_DECK, auctionController.makeAnAuction(auctioneer, "1000", "Battle OX"));
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(AUCTION_HAS_BEEN_CREATED_SUCCESSFULLY, auctionController.makeAnAuction(auctioneer, "1000", "Yami"));
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        Assertions.assertEquals(YOU_ATTENDED_TO_AUCTION_SUCCESSFULLY, AuctionController.getAllActiveActionsInString());
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        Assertions.assertEquals(YOU_ATTENDED_TO_AUCTION_SUCCESSFULLY, AuctionController.getAllActiveActionsInString());
    }

    @Test
    public void test3(){
        try {
            LoginController.instantiateCards();
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
        auctionController = AuctionController.getInstance();
        deckController = DeckController.getInstance();
        LoginController.getInstance().createUser("ali", "1234AaSs", "ali", LocalDate.now());
        LoginController.getInstance().createUser("mamad", "1234AaSs", "mamad", LocalDate.now());
        LoginController.getInstance().createUser("reza", "1234AaSs", "reza", LocalDate.now());
        auctioneer = LoginController.getUserByUsername("ali");
        buyer1 = LoginController.getUserByUsername("mamad");
        buyer2 = LoginController.getUserByUsername("reza");
        deckController.createDeck("poop", auctioneer);

        auctioneer.addCards("Battle OX");
        Assertions.assertEquals(AUCTION_HAS_BEEN_CREATED_SUCCESSFULLY, auctionController.makeAnAuction(auctioneer, "1000", "Battle OX"));
        Assertions.assertEquals(YOU_ATTENDED_TO_AUCTION_SUCCESSFULLY, auctionController.participateToAuction(buyer1, "999", "10000"));
        try {
            Thread.sleep(18000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(1, AuctionController.getAllActiveAuctions().size());
        Assertions.assertEquals(YOU_ATTENDED_TO_AUCTION_SUCCESSFULLY, auctionController.participateToAuction(buyer2, "999", "20000"));
        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(1, AuctionController.getAllActiveAuctions().size());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(0, AuctionController.getAllActiveAuctions().size());
    }
}
