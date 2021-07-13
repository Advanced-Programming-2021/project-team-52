package sample.controller;

import sample.view.sender.Sender;

import java.util.ArrayList;

public class CardCreatorController {
    private static CardCreatorController cardCreatorController = null;
    private Sender sender = Sender.getInstance();
    private final String PREFIX = "-CCC-";

    static ArrayList<String> newCardNames = new ArrayList<>();

    private CardCreatorController() {
    }

    public static CardCreatorController getInstance() {
        if (cardCreatorController == null)
            cardCreatorController = new CardCreatorController();
        return cardCreatorController;
    }

    public String setName(String name) {
        return sender.getResponseWithToken(PREFIX, "setName", name);
    }

    public String setDescription(String description) {
        return sender.getResponseWithToken(PREFIX, "setDescription", description);
    }

    public String attackCounter(String attackPointInString) {
        return sender.getResponseWithToken(PREFIX, "attackCounter", attackPointInString);
    }

    public String defendCounter(String defendPointInString) {
        return sender.getResponseWithToken(PREFIX, "defendCounter", defendPointInString);
    }

    public String levelCounter(String levelInString) {
        return sender.getResponseWithToken(PREFIX, "levelCounter", levelInString);
    }

    public String statusCounterForMonsters(String status) {
        return sender.getResponseWithToken(PREFIX, "statusCounterForMonsters", status);
    }

    public String statusCounterForSpellAndTrap(String status) {
        return sender.getResponseWithToken(PREFIX, "statusCounterForSpellAndTrap", status);
    }

    public String specialCounterForMonster(String cardName) {
        return sender.getResponseWithToken(PREFIX, "specialCounterForMonster", cardName);
    }

    public String setAttribute(String attribute) {
        return sender.getResponseWithToken(PREFIX, "setAttribute", attribute);
    }

    public String speedCounter(String speedInString) {
        return sender.getResponseWithToken(PREFIX, "speedCounter", speedInString);
    }

    public String specialCounterForSpell(String cardName) {
        return sender.getResponseWithToken(PREFIX, "specialCounterForSpell", cardName);
    }

    public String specialCounterForTrap(String cardName) {
        return sender.getResponseWithToken(PREFIX, "specialCounterForTrap", cardName);
    }

    public String createMonsterCard() {
        return sender.getResponseWithToken(PREFIX, "createMonsterCard");
    }

    public String createSpellCard() {
        return sender.getResponseWithToken(PREFIX, "createSpellCard");
    }

    public String createTrapCard() {
        return sender.getResponseWithToken(PREFIX, "createTrapCard");
    }
}
