package model;

import controller.PrintBuilderController;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Shop {
    private static Shop shop = null;
    private PrintBuilderController printBuilderController;
    private static HashMap<String, Integer> items;

    static {
        items = new HashMap<>();
    }

    private Shop() {
    }

    public static void addCard(String name, int price) {
        items.put(name, price);
    }

    public static Shop getInstance() {
        if (shop == null)
            shop = new Shop();
        return shop;
    }

    //todo : should be tested
    public TreeMap<String, Integer> getAllShopItems() {
        return new TreeMap<String, Integer>(items);
    }

    public int getItemPrize(String name) {
        if (items.containsKey(name))
            return items.get(name);
        return -1;
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

    public String getAllCardsWithPrice(){
        return toString();

    }
}
