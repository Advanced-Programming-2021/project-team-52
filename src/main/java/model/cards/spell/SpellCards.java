package model.cards.spell;

import model.cards.Cards;
//TODO add speed
public class SpellCards extends Cards {
    private String icon;
    public SpellCards(String name, String type, String icon, String description, String status) {
        super( name, type, description, status);
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }
}
