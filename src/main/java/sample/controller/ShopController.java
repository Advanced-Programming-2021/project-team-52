package sample.controller;

import javafx.scene.paint.Color;
import sample.model.Shop;
import sample.model.User;
import sample.model.cards.Cards;
import sample.model.tools.RegexPatterns;
import sample.model.tools.StringMessages;
import sample.view.PrinterAndScanner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;

import static sample.controller.LoginController.users;


public class ShopController implements StringMessages, RegexPatterns {
    private static ShopController shopController = null;
    private static PrintBuilderController printBuilderController;
    private static PrinterAndScanner printerAndScanner;
    private int mostExpensiveCard = getPriceOfMostExpensiveCard();

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

    public void start(User user) {
        String command = printerAndScanner.scanNextLine();
        while (!run(user, command)) {
            command = printerAndScanner.scanNextLine();
        }
    }

    public boolean run(User user, String command) {
        Matcher matcher;
        if ((matcher = RegexController.getMatcher(command, shopBuyPattern)) != null) {
            if (RegexController.hasField(matcher, "card"))
                buy(user, matcher.group("card"));
            else if (RegexController.hasField(matcher, "all"))
                printerAndScanner.printNextLine(Shop.getInstance().getAllCardsWithPrice());
            else
                printerAndScanner.printNextLine(invalidCommand);
        } else if ((matcher = RegexController.getMatcher(command, cardShowPattern)) != null) {
            printerAndScanner.printNextLine(printBuilderController.
                    showOneCard(Cards.getCard(matcher.group("card"))));
        } else if ((matcher = RegexController.getMatcher(command, menuPattern)) != null) {
            if (RegexController.hasField(matcher, "exit"))
                return true;
            else if (RegexController.hasField(matcher, "enter"))
                printerAndScanner.printNextLine(menuNavigationIsNotPossible);
            else if (RegexController.hasField(matcher, "showCurrent"))
                showCurrent();
            else
                printerAndScanner.printNextLine(invalidCommand);
        } else
            printerAndScanner.printNextLine(invalidCommand);
        return false;
    }

//    public void buy(User user, String cardName) {
//        Cards card = Cards.getCard(cardName);
//        if (card == null) {
//            printerAndScanner.printNextLine(noCardWithThisName);
//            return;
//        }
//        if (!checkBeforeTransaction(cardName, user.getBalance())) {
//            printerAndScanner.printNextLine(notEnoughMoney);
//            return;
//        }
//        user.changeBalance(-sample.model.Shop.getInstance().getItemPrize(cardName));
//        user.addCards(cardName);
//        user.addCardToJustShowCards(cardName);
//        printerAndScanner.printNextLine(cardBoughtSuccessfully);
//    }

    public boolean checkBeforeTransaction(String cardName, int balance) {
        int cardPrice = Shop.getInstance().getItemPrize(cardName);
        return cardPrice <= balance;
    }

    private static void showCurrent() {
        printerAndScanner.printNextLine(showCurrentInShopController);
    }

    // new for sample.view
    public int getCardPriceByName(String cardName) {
        Cards card = Cards.getCard(cardName);
        if (card == null) {
            return -1;
        }
        return Shop.getInstance().getItemPrize(cardName);
    }

    public int getUserBalance(User user) {
        if (!users.containsValue(user))
            return -1;
        return user.getBalance();
    }

    public String buy(User user, String cardName) {
        Cards card = Cards.getCard(cardName);
        if (card == null) {
            return noCardWithThisName;
        }
        if (!checkBeforeTransaction(cardName, user.getBalance())) {
            return notEnoughMoney;
        }
        user.changeBalance(-sample.model.Shop.getInstance().getItemPrize(cardName));
        user.addCards(cardName);
        user.addCardToJustShowCards(cardName);
//        System.out.println(user.getCards());
        return cardBoughtSuccessfully;
    }

    public int getNumberOfThisCardOutOfDeck(User user, String cardName) {
        Cards card = Cards.getCard(cardName);
        if (card == null) {
            return -1;
        }
        if (!users.containsValue(user))
            return -1;
        return user.getNumberOfThisCardInCardsOutOfDeck(cardName);
    }

    public Color getColorOfCardBasedOnPrice(String cardName) {
        Cards card = Cards.getCard(cardName);
        if (card == null) {
            return Color.RED;
        }
        int cardPrice = Shop.getInstance().getItemPrize(cardName);
        if (cardPrice > mostExpensiveCard * 0.75)
            return Color.AQUA;
        if (cardPrice > mostExpensiveCard * 0.5)
            return Color.GOLD;
        if (cardPrice > mostExpensiveCard * 0.25)
            return Color.SILVER;
        else
            return Color.BROWN;

    }

    public boolean DoesUserHaveEnoughMoney(User user, String cardName) {
        return user.getBalance() >= Shop.getInstance().getItemPrize(cardName);
    }

    private int getPriceOfMostExpensiveCard() {
        HashMap<String, Integer> items = Shop.getItems();
        int mostExpensiveCard = 0;
        for (Integer price : items.values()) {
            if (price > mostExpensiveCard)
                mostExpensiveCard = price;
        }
        return mostExpensiveCard;
    }

    // TODO : handle wrong input
    public static String getCardImagePathByName(String cardName) {
        return "./src/main/resources/cards in lower case/" +
                cardName.toLowerCase().trim().replace("\\s", "") + ".jpg";
    }

    public String getAllUnusedCardsByString(User user) {
        StringBuilder response = new StringBuilder();
        ArrayList<String> unusedCards = user.getCards();
        ArrayList<String> oneOfEachCard = new ArrayList<>();
        for (String card : unusedCards) {
            if (!oneOfEachCard.contains(card))
                oneOfEachCard.add(card);
        }
        for (String card : oneOfEachCard) {
            response.append(card).append("\n");
        }
        return response.toString();
    }

//    public int getPriceOfCheapestCard() {
//        HashMap<String, Integer> items = Shop.getItems();
//        int cheapestCard = 0;
//        for (Integer price : items.values()) {
//            if (price < cheapestCard)
//                cheapestCard = price;
//        }
//        return cheapestCard;
//    }
}
