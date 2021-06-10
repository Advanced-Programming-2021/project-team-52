package model.game;

import model.cards.Cards;
import model.cards.monster.MonsterCards;

import java.util.HashMap;

public class MonsterZone extends Place{

    private int attackModifier;
    private int defenseModifier;
    private HashMap<Place, Integer[]> equippedCards;


    protected MonsterZone() {
        super(PLACE_NAME.MONSTER);
        equippedCards = new HashMap<>();
    }

    public int getAttack(){
        return ((MonsterCards) card).getAttack() + attackModifier;
    }

    public int getDefense(){
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

    public void changeToThisCard(Cards card){
        super.setAffect(null);
        super.setCard(card);
        String string = "";
        for (int i = 0; i < super.history.size(); i++) {
            string = super.history.get(i);
            if (!string.equals("scanner"))
                super.history.remove(string);
        }
        //TODO reverse the card before scanner special abilities
    }

    public void putInEquipped(Place place, int attack, int defense){
        equippedCards.remove(place);
        equippedCards.put(place, new Integer[2]);
        equippedCards.get(place)[0] = attack;
        equippedCards.get(place)[1] = defense;
    }

    public Integer[] getModifiers(Place place){
        if (!hasThisModifier(place)) {
            equippedCards.put(place, new Integer[2]);
            equippedCards.get(place)[0] = 0;
            equippedCards.get(place)[1] = 0;
        }
        return equippedCards.get(place);
    }

    public boolean hasThisModifier(Place place){
        return equippedCards.containsKey(place);
    }
}
