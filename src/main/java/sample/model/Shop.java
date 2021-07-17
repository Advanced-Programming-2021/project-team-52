package sample.model;

import sample.view.sender.Sender;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Shop {
    private static Shop shop = null;
    private static HashMap<String, Integer> items;

    static {
        items = new HashMap<>();
    }

    private Shop() {
    }

    public static HashMap<String, Integer> getItems() {
        return items;
    }

    public static void addCard(String name, int price) {
        items.put(name, price);
    }

    public static Shop getInstance() {
        if (shop == null)
            shop = new Shop();
        return shop;
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
        return Sender.getInstance().getResponseWithToken("-SC-", "getAllCardsWithPrice");
    }
}
