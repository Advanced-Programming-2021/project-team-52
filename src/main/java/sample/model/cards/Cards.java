package sample.model.cards;

import sample.view.sender.Sender;

import java.util.ArrayList;
import java.util.HashMap;

public class Cards {
    private static Sender sender = Sender.getInstance();
    private static String PREFIX = "-CM-";

    public static String isCardWithThisNameExist(String cardName) {
        if (sender.convertStringToBoolean(sender.getResponseWithToken(PREFIX, "isCardWithThisNameExist", cardName)))
            return cardName;
        return null;
    }
}
