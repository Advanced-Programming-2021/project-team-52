package model.game;


import model.cards.Cards;

import java.util.ArrayList;
import java.util.HashMap;

public class Place {

    protected Cards card;
    protected HashMap<Place, ArrayList<String>> history;
    private STATUS status;
    private PLACE_NAME type;
    private ArrayList<TEMPORARY_FEATURES> temporaryFeatures;
    private Place affect;
    // َAttention developers!! temporaryFeatures are only valid for monster cards

    protected Place(PLACE_NAME type) {
        this.type = type;
        temporaryFeatures = new ArrayList<>();
    }

    public void setHistory(HashMap<Place, ArrayList<String>> history) {
        this.history = history;
        this.history.put(this, new ArrayList<String>());
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

    public boolean isTemporaryFeaturesContainsThisFeature(TEMPORARY_FEATURES temporaryFeature){
        return temporaryFeatures.contains(temporaryFeature);
    }

    public void clearTemporaryFeatures(){
        temporaryFeatures.clear();
    }

    public Place getAffect() {
        return affect;
    }

    public void setAffect(Place affect) {
        this.affect = affect;
    }

    public void killCard(){
        if (this instanceof MonsterZone){
            ((MonsterZone) this).setAttackModifier(0);
            ((MonsterZone) this).setDefenseModifier(0);
            //TODO remove this from affected in field
            //TODO remove equipped cards
        }
        this.card = null;
        this.status = null;
        this.type = null;
        this.temporaryFeatures.clear();
        this.affect = null;
        history.get(this).clear();
        temporaryFeatures.clear();
        //TODO add to graveYard
    }

    public ArrayList<TEMPORARY_FEATURES> getTemporaryFeatures() {
        return temporaryFeatures;
    }
}
