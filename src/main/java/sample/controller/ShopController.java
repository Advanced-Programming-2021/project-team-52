package sample.controller;

import javafx.scene.paint.Color;
import sample.model.Shop;
import sample.model.User;
import sample.model.cards.Cards;
import sample.model.tools.RegexPatterns;
import sample.model.tools.StringMessages;
import sample.view.PrinterAndScanner;
import sample.view.sender.Sender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;

import static sample.controller.LoginController.users;


public class ShopController implements StringMessages, RegexPatterns {
    private static ShopController shopController = null;
    private static PrintBuilderController printBuilderController;
    private static PrinterAndScanner printerAndScanner;
    private Sender sender = Sender.getInstance();
    private final String PREFIX = "-SC-";

    {
        printBuilderController = PrintBuilderController.getInstance();
        printerAndScanner = PrinterAndScanner.getInstance();
    }

    //
    private ShopController() {
    }

    public static ShopController getInstance() {
        if (shopController == null)
            shopController = new ShopController();
        return shopController;
    }

    public boolean checkBeforeTransaction(String cardName, int balance) {
        return sender.convertStringToBoolean(sender.getResponseWithToken(PREFIX, "checkBeforeTransaction",
                cardName, String.valueOf(balance)));
    }

    public int getCardPriceByName(String cardName) {
        return Integer.parseInt(sender.getResponseWithToken(PREFIX, "getCardPriceByName", cardName));
    }

    public String buy(String cardName) {
        return sender.getResponseWithToken(PREFIX, "buy", cardName);
    }

    public int getNumberOfThisCardOutOfDeck(String cardName) {
        return Integer.parseInt(sender.getResponseWithToken(PREFIX,
                "getNumberOfThisCardOutOfDeck", cardName));
    }

    public String getCardImagePathByName(String cardName) {
        return sender.getResponseWithToken(PREFIX, "getCardImagePathByName", cardName);
    }

    public String getAllUnusedCardsByString() {
        return sender.getResponseWithToken(PREFIX, "getAllUnusedCardsByString");
    }

    public String sellCard(String cardName){
        return sender.getResponseWithToken(PREFIX, "sellCard", cardName);

    }
}
