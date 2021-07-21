package sample.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Shop {
    private static Shop shop = null;
    private static HashMap<String, Integer> items;
    private static HashMap<String, Integer> numberOfItems;
    private static ArrayList<String> bannedCards;
    private int shopBalance = 100000000;

    static {
        items = new HashMap<>();
        numberOfItems = new HashMap<>();
        bannedCards = new ArrayList<>();
    }

    private Shop() {
    }

    public static Shop getInstance() {
        if (shop == null)
            shop = new Shop();
        return shop;
    }

    public static void setItems(HashMap<String, Integer> items) {
        Shop.items = items;
    }

    public static void setNumberOfItems(HashMap<String, Integer> numberOfItems) {
        Shop.numberOfItems = numberOfItems;
    }

    public static void setBannedCards(ArrayList<String> bannedCards) {
        Shop.bannedCards = bannedCards;
    }

    public int getShopBalance() {
        return shopBalance;
    }

    public void setShopBalance(int shopBalance) {
        this.shopBalance = shopBalance;
    }

    public static HashMap<String, Integer> getItems() {
        return items;
    }

    public static HashMap<String, Integer> getNumberOfItems() {
        return numberOfItems;
    }

    public static ArrayList<String> getBannedCards() {
        return bannedCards;
    }

    public static void addCard(String name, int price) {
        items.put(name, price);
        numberOfItems.put(name, 5);
    }

    public static void increaseNumberOfCard(String name, int number) {
        numberOfItems.put(name, numberOfItems.get(name) + number);

    }
    public static void decreaseNumberOfCardWithInput(String name, int number){
        numberOfItems.put(name, numberOfItems.get(name) - number);
    }

    public static void decreaseNumberOfCard(String name) {
        numberOfItems.put(name, numberOfItems.get(name) - 1);
    }

    public static int getNumberOfAvailableOfThisCard(String name) {
        return numberOfItems.get(name);
    }

    public static void addCardToForbiddenCards(String name){
        if(!bannedCards.contains(name))
        bannedCards.add(name);
    }

    public static void removeCardFromForbiddenCards(String name){
        bannedCards.remove(name);
    }

    public void changeBalanceOfShop(int amount) {
        shopBalance = shopBalance + amount;
    }


    public int getItemPrize(String name) {
        return items.get(name);
    }

    public String toString() {
        TreeMap<String, Integer> treeMap = new TreeMap<>(items);
        StringBuilder response = new StringBuilder();
        for (Map.Entry<String, Integer> entry : treeMap.entrySet()) {
            response.append(entry.getKey());
            response.append(":");
            response.append(entry.getValue());
            response.append("\n");
        }
        return response.toString();
    }

    public String getAllCardsWithPrice() {
        return toString();

    }
}
