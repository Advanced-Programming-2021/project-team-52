package sample.model;

import javafx.scene.image.Image;
import sample.view.sender.Sender;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class User extends Player {
    private Sender sender = Sender.getInstance();
    private String PREFIX = "-UM-";

    private String username;
    private String nickname;
    private String password;
    private int balance;
    private int score;
    private String imageAddress;
    private LocalDate dateOfBirth;
    private Deck activeDeck;
    private HashMap<String, Deck> decks;
    private ArrayList<String> cards;
    private ArrayList<String> cardsToJustShow; // don't use this
    private int numberOfWins;
    private int numberOfLosses;
    private int numberOfRoundsWon;
    private int numberOfRoundsLost;

    {
        decks = new HashMap<>();
        cards = new ArrayList<>();
        cardsToJustShow = new ArrayList<>();
    }

    public User() {
    }

    //
    public void setScore(int score) {
        this.score = score;
    }

    public String getUsername() {
        return sender.getResponseWithToken(PREFIX, "getUsername");
    }

    public String getNickname() {
        return sender.getResponseWithToken(PREFIX, "getNickname");
    }

    public int getBalance() {
        return Integer.parseInt(sender.getResponseWithToken(PREFIX, "getBalance"));
    }

    //
    public int getScore() {
        return score;
    }

    public String getImageAddress() {
        return sender.getResponseWithToken(PREFIX, "getImageAddress");
    }

    //
    public Deck getActiveDeck() {
        return activeDeck;
    }


    //
    public Deck getDeck(String name) {
        return decks.get(name);
    }

    //
    public void addToNumberOfGamesWon(int amount) {
        this.numberOfWins += amount;
    }

    //
    public void addToNumberOfGamesLost(int amount) {
        this.numberOfLosses += amount;
    }

    //
    public void addToNumberOfRoundsWon(int amount) {
        this.numberOfRoundsWon += amount;
    }

    //
    public void addToNumberOfRoundsLost(int amount) {
        this.numberOfRoundsLost += amount;
    }


    //
    public void changeBalance(int amount) {
        this.balance += amount;
    }

}
