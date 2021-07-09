package sample.controller;

public class CardCreatorController {
    private static CardCreatorController cardCreatorController = null;

    private CardCreatorController(){}

    public static CardCreatorController getInstance(){
        if(cardCreatorController == null)
            cardCreatorController = new CardCreatorController();
        return cardCreatorController;
    }



}
