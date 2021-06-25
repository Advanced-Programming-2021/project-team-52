package model;

import model.cards.Cards;

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

    public Deck(String name) {
        this.name = name;
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

    public void addCard(String cardName, boolean isSide) {
        if (!isSide) {
            allMainCards.add(cardName);
            mainDeckCardCount++;
        } else {
            allSideCards.add(cardName);
            sideDeckCardCount++;
        }
        if (eachCardCount.get(cardName) == null)
            eachCardCount.put(cardName, 1);
        else
            eachCardCount.put(cardName, eachCardCount.get(cardName) + 1);
    }

    public void removeCard(String cardName, boolean isSide) {
        if (!isSide) {
            allMainCards.remove(cardName);
            mainDeckCardCount--;
        } else {
            allSideCards.remove(cardName);
            sideDeckCardCount--;
        }
        eachCardCount.put(cardName, eachCardCount.get(cardName) - 1);
    }

    public boolean isDeckFull(boolean isSide) {
        if (!isSide)
            return mainDeckCardCount >= 60;
        else
            return sideDeckCardCount >= 15;
    }

    public int numberOfThisCardInDeck(String cardName) {
        if (eachCardCount.containsKey(cardName))
            return eachCardCount.get(cardName);
        return 0;
    }

    public boolean isCardWithThisNameExists(String cardName, boolean isSide) {
        if (!isSide)
            return allMainCards.contains(cardName);
        else
            return allSideCards.contains(cardName);
    }

    public boolean isDeckValid() {
        return mainDeckCardCount >= 40;
    }

    //TODO remove next functions
    public void addAllCardsToDeck() {
        allMainCards = new ArrayList<>(Cards.getAllNames());
    }

}
