package sample.controller.specialbilities;

import sample.controller.GamePlayController;
import sample.model.cards.monster.MonsterCards;
import sample.model.game.*;
import sample.model.tools.StringMessages;

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
    public void run(GamePlayController gamePlayController, Place place) {
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

    public ArrayList<String> getTypes() {
        return types;
    }

    public void normalEquip() {
        Place placeToEquip = place.getAffect();
        if (placeToEquip == null)
            placeToEquip = getEquip();
        else placeToEquip = place.getAffect();
        if (placeToEquip instanceof MonsterZone) {
            place.setAffect(placeToEquip);
            if (canModifyThisCard((MonsterCards) placeToEquip.getCard())) {
                int attackBoost = this.attackChange;
                int defenseBoost = this.defenseChange;
                GeneralSpecialAbility.attackBoost(placeToEquip, attackBoost, onDeath);
                GeneralSpecialAbility.defenseBoost(placeToEquip, defenseBoost, onDeath);
                Integer[] modifiers = ((MonsterZone) placeToEquip).getModifiers(place);
                if (!onDeath) {
                    modifiers[0] = attackBoost;
                    modifiers[1] = defenseBoost;
                } else modifiers[0] = modifiers[1] = 0;
                if (!placeToEquip.getTemporaryFeatures().contains(TEMPORARY_FEATURES.EQUIPPED))
                    placeToEquip.addTemporaryFeatures(TEMPORARY_FEATURES.EQUIPPED);
            }
        }
    }

    public void dynamicEquip() {
        Place placeToEquip = place.getAffect();
        if (placeToEquip == null)
            placeToEquip = getEquip();
        if (placeToEquip instanceof MonsterZone) {
            place.setAffect(placeToEquip);
            if (canModifyThisCard((MonsterCards) placeToEquip.getCard())) {
                Integer[] modifiers = ((MonsterZone) placeToEquip).getModifiers(place);
                removeThisBoost(modifiers);
                if (!onDeath)
                    if (placeToEquip.getStatus() == STATUS.ATTACK) {
                        modifiers[0] = ((MonsterCards) placeToEquip.getCard()).getDefense();
                        modifiers[1] = 0;
                        GeneralSpecialAbility.attackBoost(placeToEquip, modifiers[0], false);
                    } else {
                        modifiers[0] = 0;
                        modifiers[1] = ((MonsterCards) placeToEquip.getCard()).getAttack();
                        GeneralSpecialAbility.defenseBoost(placeToEquip, modifiers[1], false);
                    }
                else modifiers[0] = modifiers[1] = 0;
                if (!placeToEquip.getTemporaryFeatures().contains(TEMPORARY_FEATURES.EQUIPPED))
                    placeToEquip.addTemporaryFeatures(TEMPORARY_FEATURES.EQUIPPED);
            }
        }
    }

    private void boostByControlledMonsters() {
        Place placeToEquip = place.getAffect();
        if (placeToEquip == null)
            placeToEquip = getEquip();
        if (placeToEquip instanceof MonsterZone) {
            place.setAffect(placeToEquip);
            if (canModifyThisCard((MonsterCards) placeToEquip.getCard())) {
                Integer[] modifiers = ((MonsterZone) placeToEquip).getModifiers(place);
                int amount = boostByControlledCards();
                if (!onDeath) {
                    modifiers[0] -= amount;
                    modifiers[1] -= amount;
                }
                GeneralSpecialAbility.attackBoost(placeToEquip, modifiers[0] * quantifier, onDeath);
                GeneralSpecialAbility.defenseBoost(placeToEquip, modifiers[1] * quantifier, onDeath);
                modifiers[0] = modifiers[1] = onDeath ? 0 : amount;
                if (!placeToEquip.getTemporaryFeatures().contains(TEMPORARY_FEATURES.EQUIPPED))
                    placeToEquip.addTemporaryFeatures(TEMPORARY_FEATURES.EQUIPPED);
            }
        }
    }

    private Place getEquip() {
        int i;
        Place place;
        GameBoard myGameBoard = gamePlayController.getGamePlay().getMyGameBoard();
        GameBoard opponentGameBoard = gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getMyGameBoard();
        for (i = 1; i < 6; i++) {
            if (myGameBoard.getPlace(i, PLACE_NAME.MONSTER).getCard() != null ||
                    opponentGameBoard.getPlace(i, PLACE_NAME.MONSTER).getCard() != null)
                break;
        }
        place = null;
        if (i != 6) {
            gamePlayController.getMyCommunicator().selectCard("monster", true, true, false);
            int placeNumber = -1;
            while (true) {
                String toChek = gamePlayController.takeCommand();
                if (toChek.matches("^[-+]?[12345]$")) {
                    GamePlayController gamePlayController = toChek.matches("^-\\d$") ?
                            this.gamePlayController.getGamePlay().getOpponentGamePlayController() : this.gamePlayController;
                    placeNumber = Math.abs(Integer.parseInt(toChek));
                    if (placeNumber < 6 && placeNumber > 0) {
                        place = gamePlayController.getGamePlay().getMyGameBoard().getPlace(placeNumber, PLACE_NAME.MONSTER);
                        if (place.getCard() != null)
                            break;
                    }
                }
                if (toChek.equals("cancel")) {
                    place = null;
                    break;
                }
                printerAndScanner.printNextLine(wrongCard);
            }
        }
        return place;
    }

    private int boostByControlledCards() {
        int amount = 0;
        for (int i = 1; i < 6; i++) {
            if (gamePlayController.getGamePlay().getMyGameBoard().getPlace(i, PLACE_NAME.MONSTER).getStatus() != STATUS.SET)
                amount++;
        }
        return amount;
    }

    private boolean canModifyThisCard(MonsterCards monsterCards) {
        return types.contains(monsterCards.getMonsterType()) || types.size() == 0;
    }

    private void removeThisBoost(Integer[] modifiers) {
        Place place = this.place.getAffect();
        if (place != null) {
            GeneralSpecialAbility.attackBoost(place, modifiers[0], true);
            GeneralSpecialAbility.defenseBoost(place, modifiers[1], true);
        }
    }
}
