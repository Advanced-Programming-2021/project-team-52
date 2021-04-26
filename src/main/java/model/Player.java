package model;

import model.Deck;

public class Player {
    private String nickname;
    private Deck activeDeck;

    public void setActiveDeck(Deck activeDeck) {
        this.activeDeck = activeDeck;
    }

    public Deck getActiveDeck() {
        return activeDeck;
    }
}
