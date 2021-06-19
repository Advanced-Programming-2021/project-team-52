package model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class User extends Player {
    private String username;
    private String nickname;
    private String password;
    private int balance = 100000;
    private int score = 0;
    private Deck activeDeck;
    private HashMap<String, Deck> decks;
    private ArrayList<String> cards;
    private ArrayList<String> cardsToJustShow; // don't use this

    {
        decks = new HashMap<>();
        cards = new ArrayList<>();
        cardsToJustShow = new ArrayList<>();
    }

    private User(String username, String password, String nickname) {
        setUsername(username);
        setPassword(password);
        setNickname(nickname);
    }

    public static User createUser(String username, String password, String nickname) {
        return new User(username, password, nickname);
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setActiveDeck(Deck activeDeck) {
        this.activeDeck = activeDeck;
    }

    public String getUsername() {
        return username;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return password;
    }

    public int getBalance() {
        return balance;
    }

    public int getScore() {
        return score;
    }

    public ArrayList<String> getCards() {
        return cards;
    }

    public HashMap<String, Deck> getDecks() {
        return decks;
    }

    public ArrayList<String> getCardsToJustShow() {
        return cardsToJustShow;
    }

    public Deck getActiveDeck() {
        return activeDeck;
    }

    public Deck getDeckByName(String name) {
        if (decks.containsKey(name))
            return decks.get(name);
        return null;
    }

    public Deck getDeck(String name) {
        return decks.get(name);
    }


    public void addDeck(Deck deck) {
        decks.put(deck.getName(), deck);
    }

    public void deleteDeck(String name) {
        decks.remove(name);
    }

    public void changeBalance(int amount) {
        this.balance += amount;
    }

    public void changeScore(int amount) {
        this.score += amount;
    }

    public void addCards(String name) {
        cards.add(name);
    }

    public void removeCardFromCardsWithoutDeck(String cardName) {
        cards.remove(cardName);
    }

    public void addCardToCardsWithoutDeck(String cardName) {
        cards.add(cardName);
    }

    public boolean isCardWithThisNameExists(String cardName) {
        return cards.contains(cardName);
    }

    public void addCardToJustShowCards(String cardName) {
        if (!cardsToJustShow.contains(cardName))
            cardsToJustShow.add(cardName);
    }
}
