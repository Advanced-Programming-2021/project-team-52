package model.cards.spell;

import model.cards.Cards;
//TODO add speed
public class SpellCards extends Cards{
    private String icon, status;
    public SpellCards(String name, String type, String icon, String description, String status) {
        super( name, type, description);
        this.icon = icon;
        this.status = status;
    }

    public String getIcon() {
        return icon;
    }

    public String getStatus() {
        return status;
    }
}
