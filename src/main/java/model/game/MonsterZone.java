package model.game;

import model.cards.Cards;
import model.cards.monster.MonsterCards;

public class MonsterZone extends Place{

    private int attackModifier;
    private int defendModifier;


    protected MonsterZone(PLACE_NAME type) {
        super(type);
    }

    public int getAttack(){
        return ((MonsterCards) card).getAttack() + attackModifier;
    }

    public int getDefend(){
        return ((MonsterCards) card).getDefense() + defendModifier;
    }

    public void setAttackModifier(int attackModifier) {
        this.attackModifier = attackModifier;
    }

    public void setDefendModifier(int defendModifier) {
        this.defendModifier = defendModifier;
    }

    // todo : complete method
    public void changeToThisCard(Cards card){

    }
}
