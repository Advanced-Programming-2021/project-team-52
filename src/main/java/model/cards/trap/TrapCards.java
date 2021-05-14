package model.cards.trap;

import model.cards.Cards;
//TODO add speed
public class TrapCards extends Cards{
    private String icon;

    public TrapCards(String name, String type, String icon, String description, String status) {
        super( name, type, description, status);
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }
}
