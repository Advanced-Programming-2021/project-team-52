package model.game;


import model.cards.monster.MonsterCards;
import model.cards.spell.SpellCards;
import model.cards.trap.TrapCards;
import model.cards.Cards;

public class Place {

    private Cards card;
    private String status;
    private PlaceName type;

    protected Place(PlaceName type){
        this.type = type;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCard(Cards card) {
        this.card = card;
    }

    public String getStatus() {
        return status;
    }

    public Cards getCard() {
        return card;
    }
}
