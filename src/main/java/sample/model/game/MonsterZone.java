package sample.model.game;

import sample.model.cards.monster.MonsterCards;

import java.util.HashMap;

public class MonsterZone extends Place {

    private int attackModifier;
    private int defenseModifier;
    private HashMap<Place, Integer[]> equippedCards;


    protected MonsterZone(int number) {
        super(PLACE_NAME.MONSTER, number);
        equippedCards = new HashMap<>();
    }

    public int getAttack() {
        return ((MonsterCards) card).getAttack() + attackModifier;
    }

    public int getDefense() {
        return ((MonsterCards) card).getDefense() + defenseModifier;
    }

    public void setAttackModifier(int attackModifier) {
        this.attackModifier = attackModifier;
    }

    public void setDefenseModifier(int defenseModifier) {
        this.defenseModifier = defenseModifier;
    }

    public int getAttackModifier() {
        return attackModifier;
    }

    public int getDefenseModifier() {
        return defenseModifier;
    }

    public Integer[] getModifiers(Place place) {
        if (!hasThisModifier(place)) {
            equippedCards.put(place, new Integer[2]);
            equippedCards.get(place)[0] = 0;
            equippedCards.get(place)[1] = 0;
        }
        return equippedCards.get(place);
    }

    public boolean hasThisModifier(Place place) {
        return equippedCards.containsKey(place);
    }

    public void clearEquip() {
        equippedCards.clear();
    }
}
