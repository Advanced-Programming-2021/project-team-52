package sample.model.game;


import sample.model.cards.Cards;

import java.util.ArrayList;

public class Place {

    private final int NUM;
    protected Cards card;
    protected ArrayList<String> history;
    private STATUS status;
    private PLACE_NAME type;
    private ArrayList<TEMPORARY_FEATURES> temporaryFeatures;
    private Place affect;

    public Place(PLACE_NAME type, int num) {
        this.type = type;
        this.temporaryFeatures = new ArrayList<>();
        this.NUM = num;
    }

    public void setHistory(ArrayList<String> history) {
        this.history = history;
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

    public PLACE_NAME getType() {
        return type;
    }

    public void addTemporaryFeatures(TEMPORARY_FEATURES temporaryFeature) {
        temporaryFeatures.add(temporaryFeature);
    }

    public void removeTemporaryFeatures(TEMPORARY_FEATURES temporaryFeature) {
        temporaryFeatures.remove(temporaryFeature);
    }

    public Place getAffect() {
        return affect;
    }

    public void setAffect(Place affect) {
        this.affect = affect;
    }

    public int getNUM() {
        return NUM;
    }

    public void killCard() {
        if (this instanceof MonsterZone) {
            MonsterZone monsterZone = (MonsterZone) this;
            monsterZone.setAttackModifier(0);
            monsterZone.setDefenseModifier(0);
            monsterZone.clearEquip();
        }
        this.card = null;
        this.status = null;
        this.temporaryFeatures.clear();
        this.affect = null;
        if (!type.equals(PLACE_NAME.HAND))
            history.clear();
    }

    public ArrayList<TEMPORARY_FEATURES> getTemporaryFeatures() {
        return temporaryFeatures;
    }
}
