import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User extends Player {
    private String username;
    private String nickname;
    private String password;
    private int balance;
    private int score;
    private HashMap<String, Deck> decks;
    private ArrayList<String> cards;

    {
        decks = new HashMap<>();
        cards = new ArrayList<>();
    }

    private User(String username, String password, String nickname) {

    }

    public static void createUser(String username, String password, String nickname) {
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


    public Deck getDeckByName(String name) {
    }

    public ArrayList<Deck> getDeckList() {
    }

    public Deck getDeck(String name) {
    }


    public void addDeck(Deck deck) {
    }

    public void deleteDeck(String name) {
    }

    public void changeBalance(int amount) {
    }

    public void changeScore(int amount) {
    }

    public void addCards(String name) {
    }

    public String toString() {
    }

}
