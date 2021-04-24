package model.cards.trap;

import model.cards.Cards;
//TODO add speed
public class TrapCards extends Cards{
    private String icon, status;

    public TrapCards(String name, String type, String icon, String description, String status) {
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
