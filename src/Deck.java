import java.util.ArrayList;
import java.util.HashMap;

public class Deck {
    private String name;
    private int mainDeckCardCount;
    private int sideDeckCardCount;
    private HashMap<String, Integer> eachCardCount;
    private ArrayList<String> allMainCards;
    private ArrayList<String> allSideCards;

    {
        eachCardCount = new HashMap<>();
        allMainCards = new ArrayList<>();
        allSideCards = new ArrayList<>();
    }

    Deck(String name) {
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setMainDeckCardCount(int mainDeckCardCount) {
        this.mainDeckCardCount = mainDeckCardCount;
    }

    public void setSideDeckCardCount(int sideDeckCardCount) {
        this.sideDeckCardCount = sideDeckCardCount;
    }

    public void setEachCardCount(String name, int amount) {

    }


    public String getName() {
        return name;
    }

    public int getMainDeckCardCount() {
        return mainDeckCardCount;
    }

    public int getSideDeckCardCount() {
        return sideDeckCardCount;
    }

    public ArrayList<String> getAllMainCards() {
        return allMainCards;
    }

    public ArrayList<String> getAllSideCards() {
        return allSideCards;
    }

    public int getEachCardCount(String name) {
    }


    public void addCard(Deck deck, String cardName) {
    }

    public void removeCard(Deck deck, String cardName) {
    }

    public void addCardByCheat(String name) {
    }

    public String toString() {
    }
}
