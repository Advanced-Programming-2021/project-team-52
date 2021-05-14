package model.game;


import controller.SpecialAbility.SpecialAbilityController;
import model.cards.Cards;

import java.util.ArrayList;

public class Place {

    protected Cards card;
    private STATUS status;
    private PLACE_NAME type;
    private ArrayList<TEMPORARY_FEATURES> temporaryFeatures;
    private int attackModifier;
    private Place affect;
    // َAttention developers!! temporaryFeatures are only valid for monster cards

    protected Place(PLACE_NAME type, SpecialAbilityController specialAbilityController) {
        this.type = type;
        temporaryFeatures = new ArrayList<>();
    }

    public Place(PLACE_NAME type) {

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

    public void setAttackModifier(int attackModifier) {
        this.attackModifier = attackModifier;
    }

    public int getAttackModifier() {
        return attackModifier;
    }

    public Place getAffect() {
        return affect;
    }

    public void setAffect(Place affect) {
        this.affect = affect;
    }
}
