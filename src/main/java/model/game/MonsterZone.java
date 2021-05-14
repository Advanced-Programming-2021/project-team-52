package model.game;

import model.cards.Cards;
import model.cards.monster.MonsterCards;

public class MonsterZone extends Place{

    private int attackModifier;
    private int defenseModifier;


    protected MonsterZone(PLACE_NAME type) {
        super(type);
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
        for (int i = 0; i < super.history.get(this).size(); i++) {
            string = super.history.get(this).get(i);
            if (!string.equals("scanner"))
                super.history.get(this).remove(string);
        }
        //TODO reverse the card before scanner special abilities
    }
}
