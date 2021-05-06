package controller;

import model.cards.Cards;
import model.game.MonsterZone;
import model.game.Place;
import model.game.STATUS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class SpecialController extends GameContoller2 {
    private HashMap<Place, ArrayList<String>> history;

    public static void faceUp(Place place, STATUS status) {
        place.setStatus(status);
    }

    public static void faceUpHandler(Place place) {
        ArrayList<String> specials = place.getCard().getSpecials();
        for (String special : specials) {
            if (special.startsWith("attack boost")) {

            }
        }
    }

    public static void summon() {

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


}
