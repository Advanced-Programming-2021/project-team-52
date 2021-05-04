package model.game;


import model.cards.Cards;

public class Place {

    private Cards card;
    private STATUS status;
    private PLACE_NAME type;

    protected Place(PLACE_NAME type){
        this.type = type;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }

    public void setCard(Cards card) {
        this.card = card;
    }

    public STATUS getStatus() {
        return status;
    }

    public Cards getCard() {
        return card;
    }
}
