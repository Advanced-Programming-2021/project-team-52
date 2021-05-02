package controller;

import java.util.ArrayList;

public class DeckController {
    private static Deck deck;
    private static ArrayList<String> deckNames;
    private PrintBulider printBulider;
    private printerAndScanner printerAndScanner;

    private DeckController(){};
    public static Deck getInstance(String name){
        if(deck != null)
            deck = new Deck(name);
        return deck;
    }
    public void run(User user){}
    private static void addDeck(Deck deck , String name){
        if(deck == null){
            new Deck(name);
            deckNames.add(name);
        }
    }
    public static void DeleteDeck(String name){
        if(deck != null){
            deckNames.remove(name);
        }
    }
    public void checkBeforeAddOrRemoveCard(String deckName, String cardName, boolean isSide, boolean isAdding) {
        if (isAdding) {
            if (!(cards.allCards.contian(cardName))) {
                System.out.println("card with name" + cardName + "does not exist");
            } else if (!(deckNames.contains(deckName))) {
                System.out.println("deck with name" + deckName + "does not exist");
            } else if (!isSide && deck.getMainDeckCardCount() > 60) {
                System.out.println("main deck is full");
            } else if (isSide && deck.getSideDeckCardCount() > 15) {
                System.out.println("side deck is full");
            } else if (deck.getEachCardCount(cardName) > 3) {
                System.out.println("there are already three cards with name" + cardName + " in deck" + deckName);
            } else {
                if (!isSide) {
                    int cardCount = deck.getMainDeckCardCount() + 1;
                    deck.addCard(deck, cardName);
                    deck.setMainDeckCardCount(cardCount);
                    System.out.println("card added to deck successfully");
                } else {
                    int cardCount = deck.getSideDeckCardCount() + 1;
                    deck.addCard(deck, cardName);
                    deck.setSideDeckCardCount(cardCount);
                    System.out.println("card added to deck successfully");
                }
            }
        } else {
            if (!(cards.allCards.contian(cardName))) {
                System.out.println("card with name" + cardName + "does not exist");
            } else if (deck.getAllMainCards().contains(cardName)){
                System.out.println("card with name" + cardName + "does not exist in main deck");
            } else if (deck.getAllSideCards().contains(cardName)){
                System.out.println("card with name" + cardName + "does not exist in side deck");
            } else {
                if(isSide){
                    int cardCount = deck.getSideDeckCardCount() - 1;
                    deck.removeCard(deck, cardName);
                    deck.setSideDeckCardCount(cardCount);
                } else {
                    int cardCount = deck.getMainDeckCardCount() - 1;
                    deck.removeCard(deck, cardName);
                    deck.setMainDeckCardCount(cardCount);
                }
            }
        }
    }
}
