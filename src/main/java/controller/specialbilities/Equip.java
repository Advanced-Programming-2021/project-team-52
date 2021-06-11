package controller.specialbilities;

import controller.GamePlayController;
import model.cards.monster.MonsterCards;
import model.game.*;
import model.tools.StringMessages;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class Equip implements SpecialAbility, StringMessages {

    private Method method;
    private String methodName;
    private GamePlayController gamePlayController;
    private Place place;
    private int attackChange;
    private int defenseChange;
    private int quantifier;
    private ArrayList<String> types;
    private boolean onDeath = false;

    @Override
    public void run(GamePlayController gamePlayController, Place place){
        this.gamePlayController = gamePlayController;
        this.place = place;
        try {
            method.invoke(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getMethodName() {
        return methodName;
    }

    @Override
    public void setMethod(Method method) {
        this.methodName = method.getName();
        this.method = method;
    }

    public void setAttackChange(int attackChange) {
        this.attackChange = attackChange;
    }

    public void setDefenseChange(int defenseChange) {
        this.defenseChange = defenseChange;
    }

    public void setTypes(ArrayList<String> types) {
        this.types = types;
    }

    public void setOnDeath(boolean onDeath) {
        this.onDeath = onDeath;
    }

    public void setQuantifier(int quantifier) {
        this.quantifier = quantifier;
    }

    public void normalEquip(){ //TODO ++
        Place placeToEquip = null;
        if (!onDeath)
            placeToEquip = getEquip();
        else placeToEquip = place.getAffect();
        if (placeToEquip instanceof MonsterZone){
            placeToEquip.setAffect(placeToEquip);
            int attackBoost = this.attackChange;
            int defenseBoost = this.defenseChange;
            if (attackBoost > 0)
                GeneralSpecialAbility.attackBoost(placeToEquip, attackBoost, onDeath);
            if (defenseBoost > 0)
                GeneralSpecialAbility.attackBoost(placeToEquip, defenseBoost, onDeath);
            Integer[] modifiers = ((MonsterZone) placeToEquip).getModifiers(place);
            modifiers[0] = attackBoost;
            modifiers[1] = defenseBoost;
            if (!placeToEquip.getTemporaryFeatures().contains(TEMPORARY_FEATURES.EQUIPPED))
                placeToEquip.addTemporaryFeatures(TEMPORARY_FEATURES.EQUIPPED);
        }
    }

    public void dynamicEquip(){ //TODO ++
        Place placeToEquip = null;
        if (!onDeath)
            placeToEquip = getEquip();
        if (placeToEquip instanceof MonsterZone) {
            placeToEquip.setAffect(placeToEquip);
            Integer[] modifiers = ((MonsterZone) placeToEquip).getModifiers(place);
            GeneralSpecialAbility.attackBoost(placeToEquip, modifiers[0], true);
            GeneralSpecialAbility.defenseBoost(placeToEquip, modifiers[1], true);
            if (quantifier == 0){ // boost attack by defense
                modifiers[0] = ((MonsterCards) placeToEquip.getCard()).getDefense();
                GeneralSpecialAbility.attackBoost(placeToEquip, modifiers[0], true);
                modifiers[1] = 0;
            } else { //boost defense by attack
                modifiers[1] = ((MonsterCards) placeToEquip.getCard()).getAttack();
                GeneralSpecialAbility.defenseBoost(placeToEquip, modifiers[1], true);
                modifiers[0] = 0;
            }
            if (!placeToEquip.getTemporaryFeatures().contains(TEMPORARY_FEATURES.EQUIPPED))
                placeToEquip.addTemporaryFeatures(TEMPORARY_FEATURES.EQUIPPED);
            quantifier = 0;
        }
    }

    private void boostByControlledMonsters() { //TODO ++
        Place placeToEquip = null;
        if (!onDeath)
            placeToEquip = getEquip();
        if (placeToEquip instanceof MonsterZone) {
            placeToEquip.setAffect(placeToEquip);
            Integer[] modifiers = ((MonsterZone) placeToEquip).getModifiers(place);
            int amount = boostByControlledCards();
            GeneralSpecialAbility.attackBoost(placeToEquip, (amount - modifiers[0]) * quantifier, onDeath);
            GeneralSpecialAbility.defenseBoost(placeToEquip, (amount - modifiers[1]) * quantifier, onDeath);
            modifiers[0] = amount;
            modifiers[1] = amount;
            if (!placeToEquip.getTemporaryFeatures().contains(TEMPORARY_FEATURES.EQUIPPED))
                placeToEquip.addTemporaryFeatures(TEMPORARY_FEATURES.EQUIPPED);
        }
    }

    private Place getEquip(){
        int i;
        for (i = 1; i < 6; i++) {
            if (types.contains(gamePlayController.getGamePlay().getMyGameBoard().getPlace(i, PLACE_NAME.MONSTER).getCard().getType())
            || types.size() == 0)
                break;
        }
        if (i != 6){
            printerAndScanner.printNextLine(cardNumber);
            int placeNumber = -1;
            while (true){
                String toChek = printerAndScanner.scanNextLine();
                if (toChek.matches("^\\d+$")){
                    placeNumber = Integer.parseInt(toChek);
                    if (placeNumber < 6 && placeNumber > 0){
                        if (types.contains(gamePlayController.getGamePlay().getMyGameBoard().
                                getPlace(placeNumber, PLACE_NAME.MONSTER).getCard().getType()) ||
                                types.size() == 0)
                            break;
                    }
                }
                if (toChek.equals("cancel")){
                    placeNumber = -1;
                    break;
                }
                printerAndScanner.printNextLine(wrongCard);
            }
            if (placeNumber != -1)
                return gamePlayController.getGamePlay().getMyGameBoard().getPlace(placeNumber, PLACE_NAME.MONSTER);
        }
        return null;
    }

    private int boostByControlledCards(){
        int amount = 0;
        for (int i = 1; i < 6; i++) {
            if (gamePlayController.getGamePlay().getMyGameBoard().getPlace(i, PLACE_NAME.MONSTER).getStatus() != STATUS.SET)
                amount++;
        }
        return amount;
    }
}
