package model;
import java.util.ArrayList;
import java.util.HashMap;

// need to tell in group : transfer getUserByUsername func from userModel to loginController
// todo : unit test for all functions

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
        // todo : add user to users arraylists in loginController
        // todo : handleCreateNewUserProblems such as repetitive username, weak password
        // todo : check user problems by assignments
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

    public void setCards(ArrayList<String> cards) {
        this.cards = cards;
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

    public User getUserByUsername(String userName){
        if(userName.equals(this.username)) {
            return this;
        }
        else {
            System.out.println("user with username" + userName + "doesn't exist");
            return null;
        }
    }

    public Deck getActiveDeck() {
        return activeDeck;
    }

    public Deck getDeckByName(String name) {
        if(decks.containsKey(name))
            return decks.get(name);
        return null;
        // can be null
    }

    public ArrayList<Deck> getDeckList() {
        // check for order
        ArrayList<Deck> allDecks;
        if (decks != null) {
            allDecks = new ArrayList<>(decks.values());
            return allDecks;
        }
        return null;
    }

    public Deck getDeck(String name) {
        return decks.get(name);
        // can be null
    }


    public void addDeck(Deck deck) {
        // is deck with this name already exists
        decks.put(deck.getName(), deck);
    }

    public void deleteDeck(String name) {
        // is deck with this name exists
        decks.remove(name);
    }

    public void changeBalance(int amount) {
        this.balance += amount;
        // amount can be negative
    }

    public void changeScore(int amount) {
        this.score += amount;
        // amount can be negative
    }

    public void addCards(String name) {
        // is card with this name already exists
        cards.add(name);
    }

    public void removeCardFromCardsWithoutDeck(String cardName){
        cards.remove(cardName);
    }

    public void addCardToCardsWithoutDeck(String cardName){
        cards.add(cardName);
    }

    public boolean isCardWithThisNameExists(String cardName){
        return cards.contains(cardName);
    }

    public void addCardToJustShowCards(String cardName){
        if(!cardsToJustShow.contains(cardName))
            cardsToJustShow.add(cardName);
    }



    @Override
    public String toString() {
        // todo : what do we need for show from User model
        return "User{" +
                "username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", password='" + password + '\'' +
                ", balance=" + balance +
                ", score=" + score +
                ", decks=" + decks +
                ", cards=" + cards +
                '}';
    }

    // can't be tested
//    public static void main(String[] args) {
//        User ali = new User("ali", "1234", "MrAli");
////        createUser("ali", "1234", "MrAli");
//        Deck deck1 = new Deck("alis deck");
//        Deck deck2 = new Deck("another alis deck");
//        ali.addDeck(deck1);
//        ali.addDeck(deck2);
//        System.out.println(ali.getDeckByName("alis deck").getName());
//        System.out.println(ali.getDeckByName("hamchin chizi vojood nadare").getName());
//
//        User mamad = new User("mamad", "1234", "MrMamad");
////        createUser("mamad", "1234", "MrMamad");
//        Deck deck3 = new Deck("alis deck");
//        Deck deck4 = new Deck("another mamad deck");
//        mamad.addDeck(deck3);
//        mamad.addDeck(deck4);
//    }

}
