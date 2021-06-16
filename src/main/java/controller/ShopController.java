package controller;

import model.Shop;
import model.User;
import model.cards.Cards;
import model.cards.monster.MonsterCards;
import model.tools.RegexPatterns;
import model.tools.StringMessages;
import view.PrinterAndScanner;
import java.util.regex.Matcher;


public class ShopController implements StringMessages, RegexPatterns {
    private static ShopController shop = null;
    private static PrintBuilderController printBuilderController;
    private static PrinterAndScanner printerAndScanner;

    {
        printBuilderController = PrintBuilderController.getInstance();
        printerAndScanner = PrinterAndScanner.getInstance();
    }
//
    private ShopController() {
    }

    public static ShopController getInstance() {
        if (shop == null)
            shop = new ShopController();
        return shop;
    }

    public void run(User user) {
        String command = printerAndScanner.scanNextLine();
        Matcher matcher;
        while (true) {
            if ((matcher = RegexController.getMatcher(command, shopBuyPattern)) != null) {
                if (RegexController.hasField(matcher, "card"))
                    buy(user, matcher.group("card"));
                else if (RegexController.hasField(matcher, "all"))
                    printerAndScanner.printNextLine(Shop.getInstance().getAllCardsWithPrice());
                else
                    printerAndScanner.printNextLine(invalidCommand);
            } else if ((matcher = RegexController.getMatcher(command, menuPattern)) != null) {
                if (RegexController.hasField(matcher, "exit"))
                    break;
                else if (RegexController.hasField(matcher, "enter"))
                    printerAndScanner.printNextLine(menuNavigationIsNotPossible);
                else if (RegexController.hasField(matcher, "showCurrent"))
                    showCurrent();
                else
                    printerAndScanner.printNextLine(invalidCommand);
            }
            command = printerAndScanner.scanNextLine();
        }
    }

    public void buy(User user, String cardName) {
        Cards card = Cards.getCard(cardName);
        if (card == null) {
            printerAndScanner.printNextLine(noCardWithThisName);
            return;
        }
        if (!checkBeforeTransaction(cardName, user.getBalance())) {
            printerAndScanner.printNextLine(notEnoughMoney);
            return;
        }
        user.changeBalance(-model.Shop.getInstance().getItemPrize(cardName));
        user.addCards(cardName);
        user.addCardToJustShowCards(cardName);
        printerAndScanner.printNextLine(cardBoughtSuccessfully); // This message does not exists
    }

    private boolean checkBeforeTransaction(String cardName, int balance) {
        int cardPrice = Shop.getInstance().getItemPrize(cardName);
        return cardPrice <= balance;
    }

    public static void showCurrent() {
        printerAndScanner.printNextLine(showCurrentInShopController);
    }
}
