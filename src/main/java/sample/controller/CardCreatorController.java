package sample.controller;

import sample.model.Shop;
import sample.model.User;
import sample.model.cards.Cards;
import sample.model.cards.monster.MonsterCards;
import sample.model.cards.spell.SpellCards;
import sample.model.cards.trap.TrapCards;
import sample.model.tools.StringMessages;

public class CardCreatorController implements StringMessages {
    private static CardCreatorController cardCreatorController = null;

    // card price counter
    private int statusPrice = 0;
    private int specialPrice = 0;

    private int levelPrice = 0;
    private int attackPrice = 0;
    private int defendPrice = 0;

    private int speedPrice = 0;


    // card properties
    private String name;
    private String status;
    private Cards cardToUserSpecial;
    private String description;

    private int speed = 0;

    private int level = 0;
    private String attribute;
    private int attackPoint = 0;
    private int defendPoint = 0;


    private CardCreatorController() {
    }

    public static CardCreatorController getInstance() {
        if (cardCreatorController == null)
            cardCreatorController = new CardCreatorController();
        return cardCreatorController;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String attackCounter(String attackPointInString) {
        if (!attackPointInString.matches("^\\d+$"))
            return invalidInputForCardCreator;
        int attackPoint = Integer.parseInt(attackPointInString);
        attackPrice = attackPoint / 2;
        this.attackPoint = attackPoint;
        return String.valueOf(countPriceForMonster());
    }

    public String defendCounter(String defendPointInString) {
        if (!defendPointInString.matches("^\\d+$"))
            return invalidInputForCardCreator;
        int defendPoint = Integer.parseInt(defendPointInString);
        defendPrice = (int) (defendPoint / 2.5);
        this.defendPoint = defendPoint;
        return String.valueOf(countPriceForMonster());
    }

    public String levelCounter(String levelInString) {
        if (!levelInString.matches("^\\d+$"))
            return invalidInputForCardCreator;
        int level = Integer.parseInt(levelInString);
        if (level > 12 || level < 1)
            return invalidInputForCardCreator;
        if (level == 1 || level == 2)
            levelPrice = 300;
        else if (level == 3 || level == 4)
            levelPrice = 350;
        else if (level == 5 || level == 6)
            levelPrice = 200;
        else
            levelPrice = 150;
        this.level = level;
        return String.valueOf(countPriceForMonster());
    }

    public String statusCounter(String status) {
        status = status.toLowerCase();
        if (status.equals("unlimited"))
            statusPrice = 500;
        else if (status.equals("half limited"))
            statusPrice = 300;
        else if (status.equals("limited"))
            statusPrice = 100;
        else
            return invalidInputForCardCreator;
        this.status = status;
        return String.valueOf(countPriceForMonster());
    }

    public String specialCounterForMonster(String cardName) {
        Cards card = Cards.getCard(cardName);
        if (!(card instanceof MonsterCards))
            return THERE_IS_NO_CARD_WITH_THIS_NAME_IN_MONSTERS;
        if (card.getSpecialsInString().equals("nothing") ||
                card.getSpecialsInString().equals("nothing\n"))
            return thisCardDoesNotHaveSpecial;
        specialPrice = (int) (ShopController.getInstance().getCardPriceByName(cardName) / 2.5);
        this.cardToUserSpecial = card;
        return String.valueOf(countPriceForMonster());
    }

    public int countPriceForMonster() {
        return attackPrice + defendPrice + levelPrice + specialPrice + statusPrice;
    }

    public int countPriceForSpellAndTrap() {
        return statusPrice + specialPrice + speedPrice;
    }

    public String setAttribute(String attribute) {
        attribute = attribute.toUpperCase().trim();
        if (attribute.equals("EARTH") || attribute.equals("WATER") ||
                attribute.equals("DARK") || attribute.equals("FIRE") ||
                attribute.equals("LIGHT") || attribute.equals("WIND")) {
            this.attribute = attribute;
            return successful;
        }
        return invalidInputForCardCreator;
    }

    public String speedCounterForSpells(String speedInString) {
        if (!speedInString.matches("^\\d+$"))
            return invalidInputForCardCreator;
        int speed = Integer.parseInt(speedInString);
        if (speed == 1)
            speedPrice = 150;
        else if (speed == 2)
            speedPrice = 350;
        else
            return CHOOSE_1_OR_2;
        this.speed = speed;
        return String.valueOf(countPriceForSpellAndTrap());
    }

    public String speedCounterForTraps(String speedInString) {
        if (!speedInString.matches("^\\d+$"))
            return invalidInputForCardCreator;
        int speed = Integer.parseInt(speedInString);
        if (speed == 1)
            speedPrice = 150;
        else if (speed == 2)
            speedPrice = 350;
        else if (speed == 3)
            speedPrice = 700;
        else
            return chooseNumberBetween1To3;
        this.speed = speed;
        return String.valueOf(countPriceForSpellAndTrap());
    }

    public String specialCounterForSpell(String cardName) {
        Cards card = Cards.getCard(cardName);
        if (!(card instanceof SpellCards))
            return THERE_IS_NO_CARD_WITH_THIS_NAME_IN_SPELLS;
        specialPrice = (int) (ShopController.getInstance().getCardPriceByName(cardName) * 1.15);
        this.cardToUserSpecial = card;
        return String.valueOf(countPriceForSpellAndTrap());
    }

    public String specialCounterForTrap(String cardName) {
        Cards card = Cards.getCard(cardName);
        if (!(card instanceof TrapCards))
            return THERE_IS_NO_CARD_WITH_THIS_NAME_IN_TRAPS;
        specialPrice = (int) (ShopController.getInstance().getCardPriceByName(cardName) * 1.2);
        this.cardToUserSpecial = card;
        return String.valueOf(countPriceForSpellAndTrap());
    }

    public String createMonsterCard(User user) {
        if (name == null || status == null || cardToUserSpecial == null || description == null ||
                level == 0 || attribute == null || attackPoint == 0 || defendPoint == 0)
            return PLEASE_CHOOSE_ALL_PROPERTIES;
        int price = countPriceForMonster();
        MonsterCards monsterCardToUseSpecial = (MonsterCards) cardToUserSpecial;
        try {
            new MonsterCards(name, level, attribute,
                    monsterCardToUseSpecial.getMonsterType(), monsterCardToUseSpecial.getMonsterType(), attackPoint,
                    defendPoint, description, status, monsterCardToUseSpecial.getSpecialSpeed(),
                    InstantiateCards.loadSpecialAbilities((cardToUserSpecial.getSpecialsInString()).split("&&")),
                    cardToUserSpecial.getSpecialsInString());
            Shop.addCard(name, price);
            user.changeBalance(-price / 10);
        } catch (Exception exception) {
            exception.printStackTrace();
            return THERE_IS_AN_ERROR_IN_OUR_SIDE;
        }
        return CARD_CREATED_SUCCESSFULLY;
    }

    public String createSpellCard(User user) {
        if (name == null || status == null || cardToUserSpecial == null ||
                description == null || speed == 0)
            return PLEASE_CHOOSE_ALL_PROPERTIES;
        int price = countPriceForSpellAndTrap();
        SpellCards spellCardToUseSpecial = (SpellCards) cardToUserSpecial;
        try {
            new SpellCards(name, "Spell", spellCardToUseSpecial.getIcon(), description, status, speed,
                    InstantiateCards.loadSpecialAbilities((cardToUserSpecial.getSpecialsInString()).split("&&")),
                    InstantiateCards.getChainJobs(spellCardToUseSpecial.getChainJobInString()),
                    cardToUserSpecial.getSpecialsInString(), spellCardToUseSpecial.getChainJobInString());
            Shop.addCard(name, price);
            user.changeBalance(-price / 10);
        } catch (Exception exception) {
            exception.printStackTrace();
            return THERE_IS_AN_ERROR_IN_OUR_SIDE;
        }
        return CARD_CREATED_SUCCESSFULLY;
    }

    public String createTrapCard(User user) {
        if (name == null || status == null || cardToUserSpecial == null ||
                description == null || speed == 0)
            return PLEASE_CHOOSE_ALL_PROPERTIES;
        int price = countPriceForSpellAndTrap();
        TrapCards spellCardToUseSpecial = (TrapCards) cardToUserSpecial;
        try {
            new SpellCards(name, "Trap", spellCardToUseSpecial.getIcon(), description, status, speed,
                    InstantiateCards.loadSpecialAbilities((cardToUserSpecial.getSpecialsInString()).split("&&")),
                    InstantiateCards.getChainJobs(spellCardToUseSpecial.getChainJobInString()),
                    cardToUserSpecial.getSpecialsInString(), spellCardToUseSpecial.getChainJobInString());
            Shop.addCard(name, price);
            user.changeBalance(-price / 10);
        } catch (Exception exception) {
            exception.printStackTrace();
            return THERE_IS_AN_ERROR_IN_OUR_SIDE;
        }
        return CARD_CREATED_SUCCESSFULLY;
    }
}
