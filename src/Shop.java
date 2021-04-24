package Model.shop;

import java.util.HashMap;
import java.util.TreeMap;

public class Shop {
    private static Shop shop;
    private PrintBuilder printBuilder;
    private static HashMap<String, Integer> items;

    static {
        items = new HashMap<>();
    }

    private Shop() {
    }

    public static void addCard(String name, int price){
        items.put(name ,price);
    }
    public static Shop getInstance() {
        if(shop == null){
            Shop shop = new Shop();
        }
        return shop;
    }

    //todo : should be tested
    public TreeMap<String, Integer> getAllShopItems() {
        return
    }

    public int getItemPrize(String name) {

    }

    public String toString() {
        TreeMap treeMap = new TreeMap<String, Integer>(items);
    }

}
