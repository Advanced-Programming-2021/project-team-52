package controller.SpecialAbility;

import controller.GameContoller2;
import controller.RegexController;
import model.cards.Cards;
import model.cards.monster.MonsterCards;
import model.cards.spell.SpellCards;
import model.cards.trap.TrapCards;
import model.game.*;
import model.tools.RegexPatterns;
import model.tools.StringMessages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;

public class SpecialController extends GameContoller2 implements RegexPatterns, StringMessages {
    private HashMap<Place, ArrayList<String>> history;

    public SpecialController(){history = new HashMap<>();}

    public void faceUp(Place place, STATUS status) {
        place.setStatus(status);
        faceUpHandler(place);
    }

    public void faceUpHandler(Place place) {
        String[] specials = place.getCard().getSpecial();
        for (String special : specials) {
            if (special.startsWith("attack boost")) {
                Matcher matcher = RegexController.getMatcher(special, attackBoostPattern);
                    boostAttack(Integer.parseInt(matcher.group("amount")), RegexController.hasField(matcher, "all"), place.getAffect());
            } else if (special.equals("destroy all") && !history.get(place).contains("no more special")){
                for (int i = 1; i < 6; i++) {
                    place.setAffect(opponentGameBoard.getPlace(i, PLACE_NAME.MONSTER));
                    destroy(place, false);
                    place.setAffect(opponentGameBoard.getPlace(i, PLACE_NAME.SPELL_AND_TRAP));
                    destroy(place, false);
                }
            } else if (special.equals("destroy a monster")){
                int monsterToDestroy = printerAndScanner.scanNextInt();
                place.setAffect(opponentGameBoard.getPlace(monsterToDestroy, PLACE_NAME.MONSTER));
                destroy(place, true);
            }
        }
    }

    public void summon(Cards card) {
        for (String special : card.getSpecial()) {
            if (special.startsWith("tribute"))
                summonTribute(card);
        }
    }

    public void summonTribute(Cards card){
        for (String special : card.getSpecial()) {
            if (special.startsWith("tribute")){
                Matcher matcher = RegexController.getMatcher(special, tributePattern);
                    boolean doAlternative = false;
                    if (RegexController.hasField(matcher, "alternative")) {
                        printerAndScanner.printNextLine(doAlternativeSpecial);
                        doAlternative = printerAndScanner.scanNextLine().equals("yes");
                    }
                        if (doAlternative || numberOfAvailableTributes() < Integer.parseInt(matcher.group("amount"))){
                            Matcher matcher2 = RegexController.getMatcher(matcher.group("alternative"), attackBoostPattern);
                                Place place = putInPlace(card);
                                history.get(place).add("no more special");
                                boostAttack(Integer.parseInt(matcher2.group("amount")), false, place);
                                doSetOrFaceUp(place);

                        } else {
                            getTribute(Integer.parseInt(matcher.group("amount")));
                            STATUS status;
                            do{
                                printerAndScanner.printNextLine(askStatus);
                                do {
                                    status = STATUS.getStatusByString(printerAndScanner.scanNextLine());
                                } while (status == null);
                            } while (status == STATUS.SET && RegexController.hasField(matcher, "faceUpOnly"));
                            faceUp(putInPlace(card), status);
                        }
                }
            }

    }

    public Place putInPlace(Cards card){
        // put in place logic
    }

    public void doSetOrFaceUp(Place place){
        printerAndScanner.printNextLine(askStatus);
        STATUS status;
        do {
            status = STATUS.getStatusByString(printerAndScanner.scanNextLine());
        } while (status == null);
        if (status != STATUS.SET)
            faceUp(place, status);
    }

    public void getTribute(int amount){
        while (amount > 0){
            printerAndScanner.printNextLine(getTribute);
            int tribute = printerAndScanner.scanNextInt();
            Place place = gameBoard.getPlace(tribute, PLACE_NAME.MONSTER);
            if (place.getCard() == null) {
                printerAndScanner.printNextLine(wrongTribute);
                continue;
            }
            place.setAffect(place);
            destroy(place, false);
            amount--;
        }
    }

    public int numberOfAvailableTributes(){
        int count = 0;
        for (int i = 1; i < 6; i++)
            if (gameBoard.getPlace (i, PLACE_NAME.MONSTER).getCard() != null)
                count++;
            return count;
    }

    public void boostAttack(int amount, boolean doForAll, Place affect){
        if (doForAll){
            for (int i = 1; i < 6; i++) {
                Place place =  gameBoard.getPlace(i, PLACE_NAME.MONSTER);
                if (place != null)
                   place.setAttackModifier(place.getAttackModifier() + amount);
            }
        } else
            affect.setAttackModifier(affect.getAttackModifier() + amount);
    }

    public void convertToAnotherCard(Place place) {
        ArrayList<Cards> graveyardCards = opponentGameBoard.getGraveyard();
        String cardName;
        do {
            cardName = printerAndScanner.scanNextLine();
        } while (!graveyardCards.contains(Cards.getCard(cardName)));
        ((MonsterZone) place).changeToThisCard(Cards.getCard(cardName));
        history.get(place).add("scanner");
    }

    public void destroy(Place place, boolean killAttackerDeathWish){
        Place affected = place.getAffect();
        for (String special : affected.getCard().getSpecial()) {
            if (special.startsWith("DeathWish"))
                deathWish(place, killAttackerDeathWish);
        }
        killCard(affected);
        history.remove(affected);
    }

    public void checkHistory(){
        for (Place place : history.keySet()){
            for (String special : history.get(place)) {
                if (special.equals("scanner")){
                    ((MonsterZone) place).changeToThisCard(Cards.getCard("scanner"));
                } else {
                    Matcher matcher = RegexController.getMatcher(special, attackBoostPattern);
                        boostAttack(Integer.parseInt(matcher.group("amount")), false, place);
                        history.get(place).remove(special);

                }
            }
        }
    }

    public void deathWish(Place placeAffecting, boolean killAttackerDeathWish){
        Place place = placeAffecting.getAffect();
        for (String special : place.getCard().getSpecial()) {
            if (special.startsWith("attack boost")){
                Matcher matcher = RegexController.getMatcher(special, attackBoostPattern);
                    for (int i = 1; i < 6; i++) {
                        boostAttack(Integer.parseInt(matcher.group("number")) * -1, RegexController.hasField(matcher, "all"), place.getAffect());
                    }

            } else if (special.equals("deathWish : kill attacker") && killAttackerDeathWish) {
                place.setAffect(placeAffecting);
                destroy(placeAffecting, false);
            } else if (special.equals("no LP lost"))
                // add losing LP to the player

        }
    }

    public void killCard(Place place){
        // remove place
    }

    public void attack(Place place){
        if (!opponentHasDisabledAttacking())
        if (!history.get(place.getAffect()).contains("no more special")) {
            for (String special : place.getAffect().getCard().getSpecial()) {
                if (special.startsWith("reduce attacker attack")) {
                    Matcher matcher = RegexController.getMatcher(special, reduceAttackerAttackPattern);
                        if (!RegexController.hasField(matcher, "faceUp") || place.getAffect().getStatus() != STATUS.SET) {
                            int amountToChange;
                            if (matcher.group("amount").equals("all"))
                                amountToChange = ((MonsterZone) place).getAttack();
                            else amountToChange = Integer.parseInt(matcher.group("amount"));
                            boostAttack(amountToChange * -1, false, place);
                            if (RegexController.hasField(matcher, "oneRound"))
                                history.get(place).add("attack boost " + amountToChange);
                            if (RegexController.hasField(matcher, "oneUse"))
                                history.get(place.getAffect()).add("no more special");
                        }

                }
            }
        }
    }

    public void activateSpell(Place place){
        for (String special : place.getCard().getSpecial()) {
            if (special.equals("reveal all enemy monsters"))
                revealAllEnemyMonsterCards();
            else if (special.equals("opponent cannot attack"))
                history.get(place).add("opponent cannot attack");
            else if (special.startsWith("draw card")){
                drawCard(special);
            }
        }
    }

    public void drawCard(String special){
        Matcher matcher = RegexController.getMatcher(special, RegexPatterns.drawCardPattern);
        int numberOfDraws = Integer.parseInt(matcher.group("amount"));
        while (numberOfDraws > 0){
            super.drawCard();
            numberOfDraws--;
        }
    }

    public void revealAllEnemyMonsterCards(){
        for (int i = 1; i < 6; i++) {
            Place place = opponentGameBoard.getPlace(i, PLACE_NAME.MONSTER);
            if (place.getCard() != null) {
                if (place.getStatus() == STATUS.SET)
                    faceUp(place, STATUS.ATTACK);
            }
        }
    }

    public boolean opponentHasDisabledAttacking(){
        for (Place place : opponentSpecialController.history.keySet()) {
            if (history.get(place).contains("opponent cannot attack"))
                return true;
        }
        return false;
    }

    public boolean canChain(Cards card){
        if (card instanceof MonsterCards){
            for (int i = 1; i < 6; i++) {
                if (checkIfMonsterCanChain(card, opponentGameBoard.getPlace(i, PLACE_NAME.MONSTER)))
                    return true;
                else if (checkIfSpellOrTrapCanChain(card, opponentGameBoard.getPlace(i, PLACE_NAME.MONSTER)))
                    return true;
            }
        } else if (card instanceof SpellCards || card instanceof TrapCards){
            for (int i = 1; i < 6; i++) {
                if (checkIfSpellOrTrapCanChain(card, opponentGameBoard.getPlace(i, PLACE_NAME.MONSTER)))
                    return true;
            }
        }
        return false;
    }

    public boolean checkIfMonsterCanChain(Cards card, Place place){

    }
    public boolean checkIfSpellOrTrapCanChain(Cards card, Place place){

    }


}
