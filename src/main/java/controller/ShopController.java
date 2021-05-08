package controller;

import model.Shop;
import model.User;
import model.cards.Cards;
import model.tools.RegexPatterns;
import model.tools.StringMessages;
import view.PrinterAndScanner;

import java.util.regex.Matcher;


public class ShopController implements StringMessages, RegexPatterns {
    private static ShopController shop = null;
    private PrintBuilderController printBuilderController;
    private PrinterAndScanner printerAndScanner;

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
        while (!command.equals("menu exit")) {
            if ((matcher = RegexController.getMatcher(command, shopBuyPattern)) != null) {
                buy(user, matcher.group("card"));
                // todo : how to use show all?? throws null??
                //if
                Shop shop = Shop.getInstance();
                printerAndScanner.printNextLine(shop.getAllCardsWithPrice());
            } else if ((matcher = RegexController.getMatcher(command, menuPattern)) != null) {
                // todo : same as profileController
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
        printerAndScanner.printNextLine(cardBoughtSuccessfully); // This message does not exists
    }

    private boolean checkBeforeTransaction(String cardName, int balance) {
        Cards card = Cards.getCard(cardName);
        int cardPrice = Shop.getInstance().getItemPrize(cardName);
        if (cardPrice == -1)
            return false;  //card doesn't exists
        return cardPrice <= balance;
    }
}
