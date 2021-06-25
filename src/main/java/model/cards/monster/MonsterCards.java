package model.cards.monster;

import controller.specialbilities.SpecialAbility;
import model.cards.Cards;

import java.util.ArrayList;

public class MonsterCards extends Cards {

    private int level, attack, defense;
    private String attribute, monsterType;

    public MonsterCards(String name, int level, String attribute, String monsterType,
                        String type, int attack, int defense, String description, String status, int specialSpeed,
                        ArrayList<SpecialAbility> special, String specialsInString) {
        super(name, type, description, status, specialSpeed, special, specialsInString);
        Cards.addCard(this, name);
        this.level = level;
        this.attack = attack;
        this.defense = defense;
        this.attribute = attribute;
        this.monsterType = monsterType;
        addCard(this, name);
    }

    public int getLevel() {
        return level;
    }

    public int getDefense() {
        return defense;
    }

    public int getAttack() {
        return attack;
    }

    public String getAttribute() {
        return attribute;
    }

    public String getMonsterType() {
        return monsterType;
    }
}
