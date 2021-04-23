import java.util.HashMap;
import java.util.TreeMap;

public class Shop {
    private static Shop shop;
    private PrintBuilder printBuilder;
    private HashMap<Shop, Integer> items;

    {
        items = new HashMap<>();
    }

    private Shop() {
    }


    public static Shop getInstance() {
    }

    public TreeMap<String, String> getAllShopItems() {
    }

    public int getItemPrize(String name) {
    }

    private void initializeItem() {
    }

    public String toString() {
    }
}
