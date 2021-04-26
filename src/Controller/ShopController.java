package Controller;

import model.*;

public class ShopController {
    private static ShopController shop = null;
    private PrintBulider printBulider;
    private printerAndScanner printerAndScanner;

    private ShopController() {
        LoginController.
    }

    public static ShopController getInstance() {
        if(shop == null)
            shop = new ShopController();
        return shop;
    }

    public void run(User user) {
    }

    public void buy(User user, String cardName) {

    }

    private boolean checkBeforeTransaction(String cardName, int balance) {

    }

}
