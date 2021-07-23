package sample.controller;

import sample.model.Shop;
import sample.model.User;
import sample.model.cards.Cards;
import sample.model.tools.RegexPatterns;
import sample.model.tools.StringMessages;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import static sample.controller.LoginController.users;


public class ShopController implements StringMessages, RegexPatterns {
    private static ShopController shopController = null;
    private PrintBuilderController printBuilderController;
    private Shop shop = Shop.getInstance();
    private final String ADMIN_USERNAME = "Maraam";
    private final String ADMIN_PASSWORD = "IHateYoGiOh";

    {
        printBuilderController = PrintBuilderController.getInstance();
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
        int cardPrice = Shop.getInstance().getItemPrize(cardName);
        return cardPrice <= balance;
    }


    public int getCardPriceByName(String cardName) {
        Cards card = Cards.getCard(cardName);
        if (card == null) {
            return -1;
        }
        return Shop.getInstance().getItemPrize(cardName);
    }

    public String buy(User user, String cardName) {
        Cards card = Cards.getCard(cardName);
        if (card == null) {
            return noCardWithThisName;
        }
        if (Shop.getBannedCards().contains(cardName))
            return "The card is banned";
        if (Shop.getNumberOfAvailableOfThisCard(cardName) == 0)
            return "There is not enough card in shop";
        if (!checkBeforeTransaction(cardName, user.getBalance())) {
            return notEnoughMoney;
        }
        int price = sample.model.Shop.getInstance().getItemPrize(cardName);
        user.changeBalance(-price);
        user.addCards(cardName);
        user.addCardToJustShowCards(cardName);
        Shop.decreaseNumberOfCard(cardName);
        shop.changeBalanceOfShop(+price);
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

    public static String getCardImagePathByName(String cardName) {
        try {
            if (CardCreatorController.newCardNames.contains(cardName))
                return "./src/main/resources/cardsInLowerCase/minimalfuck.jpg";
            String address = "./src/main/resources/cardsInLowerCase/" +
                    cardName.toLowerCase().trim().replace(" ", "") + ".jpg";
            File file = new File(address);
            return address;
        } catch (Exception e) {
            return "./src/main/resources/cardsInLowerCase/weddingcard.jpg";
        }
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

    public static boolean isCardWithThisNameValid(String cardName) {
        return Shop.getItems().containsKey(cardName) && Shop.getNumberOfItems().containsKey(cardName);
    }

    public String addCardToBannedCards(String name) {
        Cards card = Cards.getCard(name);
        if (card == null) {
            return noCardWithThisName;
        }
        if (Shop.getBannedCards().contains(name))
            return "Card has been already banned";
        Shop.addCardToForbiddenCards(name);
        return "Card banned successfully";
    }

    public String removeCardFromBannedCards(String name) {
        Cards card = Cards.getCard(name);
        if (card == null) {
            return noCardWithThisName;
        }
        if (!Shop.getBannedCards().contains(name))
            return "Card was not banned";
        Shop.removeCardFromForbiddenCards(name);
        return "Card is not banned any more";
    }

    public String addNumberOfCardToShop(String name, String amount) {
        System.out.println(amount);
        Cards card = Cards.getCard(name);
        if (card == null) {
            return noCardWithThisName;
        }
        if (!AuctionController.getInstance().isInputNumber(amount)) {
            return "Amount most be number";
        }
        if (Integer.parseInt(amount) <= 0)
            return "Amount most be more than 0";
        Shop.increaseNumberOfCard(name, Integer.parseInt(amount));
        return "Number of card increased successfully";
    }

    public String removeNumberOfCardToShop(String name, String amount) {
        System.out.println(amount);
        Cards card = Cards.getCard(name);
        if (card == null) {
            return noCardWithThisName;
        }
        if (!AuctionController.getInstance().isInputNumber(amount)) {
            return "Amount most be number";
        }
        if (Integer.parseInt(amount) <= 0)
            return "Amount most be more than 0";
        if (Integer.parseInt(amount) > Shop.getNumberOfItems().get(name))
            return "There are not enough of this card in shop";
        Shop.decreaseNumberOfCardWithInput(name, Integer.parseInt(amount));
        return "Number of card decreased successfully";
    }

    public String sellCard(User user, String cardName) {
//        System.out.println("111111111" + cardName);
        Cards card = Cards.getCard(cardName);
        if (card == null) {
            return noCardWithThisName;
        }
        if (Shop.getBannedCards().contains(cardName))
            return "The card is banned";
        int numberOfThisCardOutOfDeck = user.getNumberOfThisCardInCardsOutOfDeck(cardName);
        if (numberOfThisCardOutOfDeck < 1)
            return "You do not have enough of this card to sell";

        int price = shop.getItemPrize(cardName);
        Shop.increaseNumberOfCard(cardName, 1);
        shop.changeBalanceOfShop(-price);
        user.removeCardFromCardsWithoutDeck(cardName);
        user.changeBalance(+price);
        return "Card sold successfully";
    }

    public String adminLogin(String username, String password) {
        if (username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD))
            return "login successful";
        return "Invalid information";
    }

    public String getSuggestedCards(User user) {
        return getBestCardByUserBalance(user.getBalance());
    }

    public String getBestCardByUserBalance(int balance) {
        HashMap<String, Integer> items = Shop.getItems();
        String bestCard = "You can't buy shit";
        int bestCardPrice = 0;
        int cardPrice = 0;
        for (String s : items.keySet()) {
            cardPrice = items.get(s);
            if (cardPrice <= balance && cardPrice > bestCardPrice){
                bestCardPrice = cardPrice;
                bestCard = s;}
        }
        return bestCard;
    }


}
