package controller.SpecialAbility;

import controller.GameContoller2;
import controller.RegexController;
import model.cards.Cards;
import model.game.MonsterZone;
import model.game.PLACE_NAME;
import model.game.Place;
import model.game.STATUS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpecialController extends GameContoller2 {
    // command knight : attack boost 400 all_cannotBeAttacked

    private static HashMap<Place, ArrayList<String>> history;

    public static void faceUp(Place place, STATUS status) {
        place.setStatus(status);
    }

    public void faceUpHandler(Place place) {
        ArrayList<String> specials = place.getCard().getSpecials();
        for (String special : specials) {
            if (special.startsWith("attack boost")) {
                Pattern extract = Pattern.compile("attack boost (?<number>400) (?<all>all)");
                Matcher matcher = extract.matcher(special);
                if (matcher.find()){
                    boostAttack(Integer.parseInt(matcher.group("number")), RegexController.hasField(matcher, "all"), place.getAffect());
                }
            }
        }
    }

    public static void summon() {}

    public void boostAttack(int amount, boolean doForAll, Place affect){
        if (doForAll){
            for (int i = 0; i < 5; i++) {
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

    public static void destroy(Place place){
        Place affected = place.getAffect();
        for (String s :affected.getCard().getSpecial()) {
            if (s.startsWith("DeathWish"))
                deathWish(place);
        }
        history.remove(affected);
    }

    public static void checkHistory(){
        for (Place place : history.keySet()){
            if (history.get(place).contains("scanner")){
                ((MonsterZone) place).changeToThisCard(Cards.getCard("scanner"));
            }
        }
    }

    public static void deathWish(Place place){
        // remove place
    }


}
