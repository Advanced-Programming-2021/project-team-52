package sample.model;

import javafx.scene.image.Image;
import sample.view.listener.ActionFinder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;

public class User extends Player {

    private String username;
    private String nickname;
    private String password;
    private int balance;
    private int score;
    private String imageAddress;
    private String avatarAddress;
    private LocalDate dateOfBirth;
    private Deck activeDeck;
    private HashMap<String, Deck> decks;
    private ArrayList<String> cards;
    private ArrayList<String> cardsToJustShow; // don't use this
    private ArrayList<String> cardsInAuction = new ArrayList<>();
    private int numberOfWins;
    private int numberOfLosses;
    private int numberOfRoundsWon;
    private int numberOfRoundsLost;
    private ActionFinder actionFinder;

    {
        decks = new HashMap<>();
        cards = new ArrayList<>();
        cardsToJustShow = new ArrayList<>();
    }

    private User(String username, String password, String nickname, String imageAddress, String avatarAddress, LocalDate dateOfBirth) {
        setUsername(username);
        setPassword(password);
        setNickname(nickname);
        this.imageAddress = imageAddress;
//        System.out.println(image.getUrl());
        setDateOfBirth(dateOfBirth);
        this.avatarAddress = avatarAddress;
        this.score = 0;
        this.balance = 100000;
        this.numberOfWins = 0;
        this.numberOfLosses = 0;
        this.numberOfRoundsWon = 0;
        this.numberOfRoundsLost = 0;
    }

    public static User createUser(String username, String password,
                                  String nickname, String imageAddress, String avatarAddress, LocalDate dateOfBirth) {
        return new User(username, password, nickname, imageAddress, avatarAddress, dateOfBirth);
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

    public void setImageAddress(String imageAddress) {
        this.imageAddress = imageAddress;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setActiveDeck(Deck activeDeck) {
        this.activeDeck = activeDeck;
    }

    public void setCardsInAuction(ArrayList<String> cardsInAuction) {
        this.cardsInAuction = cardsInAuction;
    }

    public void setAvatarAddress(String avatarAddress) {
        this.avatarAddress = avatarAddress;
    }

    public void setActionFinder(ActionFinder actionFinder) {
        this.actionFinder = actionFinder;
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

    public String getImageAddress() {
        return imageAddress;
    }

    public String getAvatarAddress() {
        return avatarAddress;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public ArrayList<String> getCards() {
        return cards;
    }

    public ArrayList<String> getCardsInAuction() {
        return cardsInAuction;
    }

    public HashMap<String, Deck> getDecks() {
        return decks;
    }

    public ActionFinder getActionFinder() {
        return actionFinder;
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

    public int getNumberOfLosses() {
        return numberOfLosses;
    }

    public int getNumberOfWins() {
        return numberOfWins;
    }

    public int getNumberOfRoundsWon() {
        return numberOfRoundsWon;
    }

    public int getNumberOfRoundsLost() {
        return numberOfRoundsLost;
    }

    public void addToNumberOfGamesWon(int amount) {
        this.numberOfWins += amount;
    }

    public void addToNumberOfGamesLost(int amount) {
        this.numberOfLosses += amount;
    }

    public void addToNumberOfRoundsWon(int amount) {
        this.numberOfRoundsWon += amount;
    }

    public void addToNumberOfRoundsLost(int amount) {
        this.numberOfRoundsLost += amount;
    }

    public void addDeck(Deck deck) {
        decks.put(deck.getName(), deck);
    }

    public void addCardToAuctionCards(String cardName) {
        cardsInAuction.add(cardName);
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

    public void removeCardFromAuctionCards(String cardName) {
        cardsInAuction.remove(cardName);
    }

    public int getNumberOfThisCardInCardsOutOfDeck(String cardName) {
        int numberOfCards = 0;
        for (String card : cards) {
            if (card.equals(cardName))
                ++numberOfCards;
        }
        return numberOfCards;
    }

    public boolean isCardWithThisNameExists(String cardName) {
        return cards.contains(cardName);
    }

    public void addCardToJustShowCards(String cardName) {
        if (!cardsToJustShow.contains(cardName))
            cardsToJustShow.add(cardName);
    }

    public String getUserProfile() {
        StringBuilder response = new StringBuilder();
        response.append("Name : ").append(username).append("\n");
        response.append("Score : ").append(score).append("\n");
        response.append("Wins : ").append(numberOfWins).append("\n");
        response.append("Losses : ").append(numberOfLosses).append("\n");
//        response.append("imageAddress").append(imageAddress);
        return response.toString();

    }
}
