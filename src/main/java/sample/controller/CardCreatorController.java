package sample.controller;

import sample.model.Shop;
import sample.model.cards.Cards;
import sample.model.cards.monster.MonsterCards;
import sample.model.tools.StringMessages;

import java.util.Locale;

public class CardCreatorController implements StringMessages {
    private static CardCreatorController cardCreatorController = null;

    private int attackPrice= 0;
    private int defendPrice= 0;
    private int levelPrice= 0;

    private int iconPrice= 0;
    private int speedCounter= 0;

    private int specialPrice= 0;
    private int statusPrice= 0;


    private CardCreatorController(){}

    public static CardCreatorController getInstance(){
        if(cardCreatorController == null)
            cardCreatorController = new CardCreatorController();
        return cardCreatorController;
    }

    public String attackCounter(String attackPointInString){
        if(!attackPointInString.matches("^\\d+$"))
            return invalidInputForCardCreator;
        int attackPoint = Integer.parseInt(attackPointInString);
        attackPrice = attackPoint/ 2;
        return String.valueOf(countPriceForMonster());
    }

    public String defendCounter(String defendPointInString){
        if(!defendPointInString.matches("^\\d+$"))
            return invalidInputForCardCreator;
        int defendPoint = Integer.parseInt(defendPointInString);
        defendPrice = (int) (defendPoint/ 2.5);
        return String.valueOf(countPriceForMonster());
    }
    public String levelCounter(String levelInString){
        if(!levelInString.matches("^\\d+$"))
            return invalidInputForCardCreator;
        int level = Integer.parseInt(levelInString);
        if(level > 9 || level < 1)
            return invalidInputForCardCreator;
        if(level == 1 || level == 2)
            levelPrice = 300;
        else if(level == 3 || level == 4)
            levelPrice = 350;
        else if(level == 5 || level == 6)
            levelPrice = 200;
        else
            levelPrice = 150;
        return String.valueOf(countPriceForMonster());
    }

    public String statusCounter(String status){
        status = status.toLowerCase();
        if(status.equals("unlimited"))
            statusPrice = 500;
        else if(status.equals("half limited"))
            statusPrice = 300;
        else if(status.equals("limited"))
            statusPrice = 100;
        else
            return invalidInputForCardCreator;
        return String.valueOf(countPriceForMonster());
    }

    public String specialCounterForMonster(String cardName){
        Cards card = Cards.getCard(cardName);
        if(!(card instanceof MonsterCards))
            return thereIsNoCardWithThisName;
        if(card.getSpecialsInString().equals("nothing") ||
                card.getSpecialsInString().equals("nothing\n"))
            return thisCardDoesNotHaveSpecial;
        specialPrice = ShopController.getInstance().getCardPriceByName(cardName);
        return String.valueOf(countPriceForMonster());
    }

    public int countPriceForMonster(){
        return attackPrice + defendPrice + levelPrice + specialPrice + statusPrice;
    }



}
