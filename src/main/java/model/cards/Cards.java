package model.cards;

import controller.specialbilities.SpecialAbility;
import model.cards.monster.MonsterCards;
import model.cards.spell.SpellCards;

import java.util.ArrayList;
import java.util.HashMap;

public class Cards {

    private static final HashMap<String, Cards> ALL_CARDS;

    private String name, type, description, status;
    private ArrayList<SpecialAbility> special;
    private int specialSpeed;//TODO add special speed to csv

    static{
        ALL_CARDS = new HashMap<>();
    }

    protected Cards(String name, String type, String description, String status){
        this.name = name;
        this.type = type;
        this.description = description;
        this.status = status;
    }

    protected static void addCard(Cards card, String name){
        ALL_CARDS.put(name, card);
    }

    public static Cards getCard(String name){
        return ALL_CARDS.getOrDefault(name, null);
    }

    public static ArrayList<String> getAllNames(){
        return new ArrayList<>(ALL_CARDS.keySet());
    }

    //just for unit tests
    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<SpecialAbility> getSpecial() {
        return special;
    }

    public String getStatus() {
        return status;
    }

    public void setSpecialSpeed(int specialSpeed) {
        this.specialSpeed = specialSpeed;
    }

    public int getSpecialSpeed() {
        return specialSpeed;
    }

    @Override
    public String toString() {
        StringBuilder results = new StringBuilder("Name: ").append(this.name).append("\n");
        if (this instanceof MonsterCards){
            MonsterCards monsterCard = (MonsterCards) this;
            results.append("Level: ").append(monsterCard.getLevel()).append("\n");
            results.append("Type: ").append(monsterCard.getType()).append("\n");
            results.append("ATK: ").append(monsterCard.getAttack()).append("\n");
            results.append("DEF: ").append(monsterCard.getDefense()).append("\n");
        } else {
            if (this instanceof SpellCards) results.append("Spell").append("\n");
            else results.append("Trap").append("\n");
            results.append("Type: ").append(this.type).append("\n");
        }
        results.append("Description: ").append(this.getDescription()).append("\n");
        return results.toString();
    }
}
