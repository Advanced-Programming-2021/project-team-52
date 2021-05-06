package controller.SpecialAbility;

import model.game.Place;

import java.util.HashMap;

public class SpecialAbilityController {

    public static void run(Place affectBy, Place affecting){
        String[] special = affectBy.getCard().getSpecial();
        for (String s : special) {
            switch (s){
                case "deathWish" : deathWish(affecting);
            }
        }
    }

    public static void deathWish(Place place){
        place.setCard(null);
        place.setAttackModifier(0);
        place.setStatus(null);
    }


}
