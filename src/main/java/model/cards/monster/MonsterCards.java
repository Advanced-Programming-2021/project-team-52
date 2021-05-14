package model.cards.monster;

import model.cards.Cards;

public class MonsterCards extends Cards{
//TODO add speed
    private int level, attack, defense, hasSpecialAbility, specialAbilityId;
    private String attribute, monsterType;

    public MonsterCards(String name, int level, String attribute, String monsterType,
                        String type, int attack, int defense, String description){
        super(name,type,description);
        Cards.addCard(this, name);
        this.level = level;
        this.attack = attack;
        this.defense = defense;
        this.attribute = attribute;
        this.monsterType = monsterType;
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

    public int getHasSpecialAbility() {
        return hasSpecialAbility;
    }

    public int getSpecialAbilityId() {
        return specialAbilityId;
    }

    public String getAttribute() {
        return attribute;
    }

    public String getMonsterType() {
        return monsterType;
    }
}
