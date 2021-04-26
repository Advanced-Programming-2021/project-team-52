package Controller;

import model.*;


public class ShopController {
    private static ShopController shop = null;
    private PrintBulider printBulider;
    private printerAndScanner printerAndScanner;

    private ShopController() {
    }

    public static ShopController getInstance() {
        if (shop == null)
            shop = new ShopController();
        return shop;
    }

    public void run(model.User user) {
    }

    public void buy(model.User user, String cardName) {
        Card card = Cards.getCard(cardName);
        if (card == null) {
            System.out.println("there is no card with this name");
            return;
        }
        if(!checkBeforeTransaction(cardName, user.getBalance())){
            System.out.println("not enough money");
            return;
        }
        user.changeBalance(-model.Shop.getInstance().getItemPrize(cardName));
        user.addCards(cardName);
    }

    private boolean checkBeforeTransaction(String cardName, int balance) {
        Card card = Cards.getCard(cardName);
        int cardPrice = model.Shop.getInstance().getItemPrize(cardName);
        if(cardPrice == -1)
            return false;  //card doesn't exists
        return cardPrice <= balance;
    }
}
