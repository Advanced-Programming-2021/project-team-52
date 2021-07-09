package sample.controller;

import sample.model.Deck;
import sample.model.User;
import sample.model.cards.monster.MonsterCards;
import sample.model.cards.spell.SpellCards;
import sample.model.cards.trap.TrapCards;
import sample.model.game.GameBoard;
import sample.model.cards.Cards;
import sample.model.game.MonsterZone;
import sample.model.game.PLACE_NAME;
import sample.model.game.Place;

import java.util.ArrayList;
import java.util.Collections;

public class PrintBuilderController {
    private static PrintBuilderController printPrintBuilderController = null;

    public static PrintBuilderController getInstance() {
        if (printPrintBuilderController == null)
            printPrintBuilderController = new PrintBuilderController();
        return printPrintBuilderController;
    }

    public StringBuilder buildGraveyard(ArrayList<Cards> graveyard) {
        StringBuilder results = new StringBuilder();
        for (int i = 0; i < graveyard.size(); i++) {
            results.append(graveyard.get(i));
            results.append("\n");
        }
        return results;
    }

    public String thisUsernameAlreadyExists(String newUsername) {
        return "user with this username " + newUsername + " already exists";
    }

    public String thisNicknameAlreadyExists(String newNickname) {
        return "user with this nickname " + newNickname + " already exists";
    }

    public String DeckWithThisNameAlreadyExists(String deckName) {
        return "deck with name " + deckName + " already exists";
    }

    public String deckWithThisNameDoesNotExist(String deckName) {
        return "deck with name " + deckName + " does not exist";
    }

    public String cardWithThisNameDoesNotExist(String cardName) {
        return "card with name " + cardName + " does not exist";
    }

    public String thereAreAlreadyThreeCardsWithThisNameInThisDeck
            (String cardName, String deckName, int numberOfCards) {
        return "there are already " + numberOfCards + " cards with name " + cardName + " in deck " + deckName;
    }

    public String cardWithThisNameDoesNotExistInThisDeck(String cardName, boolean isSide) {
        if (!isSide)
            return "card with name " + cardName + " does not exist in main deck";
        else
            return "card with name " + cardName + " does not exist in side deck";
    }

    public String showAllDecks(User user) {
        Deck activeDeck = user.getActiveDeck();
        ArrayList<String> allDecks = new ArrayList<>(user.getDecks().keySet());
        Collections.sort(allDecks);

        StringBuilder response = new StringBuilder();
        response.append("Decks:\nActive deck:\n");
        if (activeDeck != null)
            showOneDeckForAllDecks(activeDeck, response);

        response.append("Other decks:\n");
        for (String deckName : allDecks) {
            if ((activeDeck == null) || (!deckName.equals(activeDeck.getName()))) {
                Deck deck = user.getDeck(deckName);
                showOneDeckForAllDecks(deck, response);
            }
        }
        return response.toString();
    }

    private void showOneDeckForAllDecks(Deck deck, StringBuilder response) {
        response.append(deck.getName());
        response.append(": main deck ");
        response.append(deck.getMainDeckCardCount());
        response.append(", side deck ");
        response.append(deck.getSideDeckCardCount());
        response.append(", ");
        if (deck.isDeckValid())
            response.append("valid\n");
        else
            response.append("invalid\n");
    }

    public String showOneDeck(Deck deck, boolean isSide) {
        ArrayList<String> allCardsName;
        if (!isSide)
            allCardsName = deck.getAllMainCards();
        else
            allCardsName = deck.getAllSideCards();
        ArrayList<String> allMonsterCards = new ArrayList<>();
        ArrayList<String> allSpellCards = new ArrayList<>();
        ArrayList<String> allTrapCards = new ArrayList<>();
        for (String cardName : allCardsName) {
            Cards card = Cards.getCard(cardName);
            specifyCardsType(allMonsterCards, allSpellCards, allTrapCards, card);
        }
        Collections.sort(allMonsterCards);
        Collections.sort(allSpellCards);
        Collections.sort(allTrapCards);
        StringBuilder response = new StringBuilder();
        response.append("Deck: ");
        response.append(deck.getName());
        response.append("\n");
        response.append("Monsters: \n");
        showCardsOfDeckForShowingDeck(allMonsterCards, response);
        response.append("Spell: \n");
        showCardsOfDeckForShowingDeck(allSpellCards, response);
        response.append("Trap: \n");
        showCardsOfDeckForShowingDeck(allTrapCards, response);
        return response.toString();
    }

    private void specifyCardsType(ArrayList<String> allMonsterCards, ArrayList<String> allSpellCards,
                                  ArrayList<String> allTrapCards, Cards card) {
        if (card instanceof MonsterCards)
            allMonsterCards.add(card.getName());
        if (card instanceof SpellCards)
            allSpellCards.add(card.getName());
        if (card instanceof TrapCards)
            allTrapCards.add(card.getName());
    }

    private void showCardsOfDeckForShowingDeck(ArrayList<String> allMonsterCards, StringBuilder response) {
        for (String monsterCardName : allMonsterCards) {
            response.append(monsterCardName);
            response.append(": ");
            Cards card = Cards.getCard(monsterCardName);
            response.append(card.getDescription());
            response.append("\n");
        }
    }

    public String showAllCardsOfUser(ArrayList<String> cardNames) {
        Collections.sort(cardNames);
        StringBuilder response = new StringBuilder();
        for (String cardName : cardNames) {
            response.append(cardName);
            response.append(": ");
            response.append(Cards.getCard(cardName).getDescription());
            response.append("\n");
        }
        return response.toString();
    }

    public String showOneCard(Cards card) {
        StringBuilder response = new StringBuilder();
        if (card instanceof MonsterCards) {
            MonsterCards monsterCard = (MonsterCards) card;
            response.append("Name: ").append(monsterCard.getName()).append("\n");
            response.append("Level: ").append(monsterCard.getLevel()).append("\n");
            response.append("Type: ").append(monsterCard.getType()).append("\n");
            response.append("ATK: ").append(monsterCard.getAttack()).append("\n");
            response.append("DEF: ").append(monsterCard.getDefense()).append("\n");
            response.append("Description: ").append(monsterCard.getDescription());
            return response.toString();
        }
        if (getSpellOrTrap(response, card)) return response.toString();
        return "card is not valid";
    }

    public String showOneCard(Place place) {
        StringBuilder response = new StringBuilder();
        Cards card = place.getCard();
        if (card instanceof MonsterCards) {
            MonsterCards monsterCard = (MonsterCards) card;
            response.append("Name: ").append(monsterCard.getName()).append("\n");
            response.append("Level: ").append(monsterCard.getLevel()).append("\n");
            response.append("Type: ").append(((MonsterZone) place).getAttack()).append("\n");
            response.append("ATK: ").append(((MonsterZone) place).getDefense()).append("\n");
            response.append("DEF: ").append(monsterCard.getDefense()).append("\n");
            response.append("Description: ").append(monsterCard.getDescription());
            return response.toString();
        }
        if (getSpellOrTrap(response, card)) return response.toString();
        return "card is not valid";
    }

    private boolean getSpellOrTrap(StringBuilder response, Cards card) {
        if (card instanceof SpellCards) {
            SpellCards spellCard = (SpellCards) card;
            response.append("Name: ").append(spellCard.getName()).append("\n");
            response.append("Spell").append("\n");
            response.append("Type: ").append(spellCard.getIcon()).append("\n");
            response.append("Description: ").append(spellCard.getDescription());
            return true;
        }
        if (card instanceof TrapCards) {
            TrapCards trapCard = (TrapCards) card;
            response.append("Name: ").append(trapCard.getName()).append("\n");
            response.append("Trap").append("\n");
            response.append("Type: ").append(trapCard.getIcon()).append("\n");
            response.append("Description: ").append(trapCard.getDescription());
            return true;
        }
        return false;
    }


    public StringBuilder drawCard(String name) {
        return new StringBuilder("new card added to hand: ").append(name);
    }

    public StringBuilder playerTurn(String name) {
        return new StringBuilder("its ").append(name).append("'s turn");
    }

    public StringBuilder attackToAttackResult(int damage, int result) {
        StringBuilder resultString = new StringBuilder();
        switch (result){
            case 1 :
                resultString.append("your opponent’s monster is destroyed and your opponent receives ").append(damage)
                        .append(" battle damage");
                break;
            case 0:
                resultString.append("no card is destroyed");
                break;
            case -1 :
                resultString.append("Your monster card is destroyed and you received ").append(damage).
                    append(" battle damage");
        }
        return resultString;
    }

    public StringBuilder attackToDefenseResult(boolean defenderWasHidden, String defenderName, int result, int damage) {
        StringBuilder results = new StringBuilder();
        if (defenderWasHidden)
            results.append("opponent’s monster card was ").append(defenderName).append(". and ");
        switch (result) {
            case 1:
                results.append("the defending monster is destroyed");
                break;
            case 0:
                results.append("no card is destroyed");
                break;
            case -1:
                results.append("no card is destroyed and you received ").append(damage).append(" battle damage");
        }
        return results;
    }

    public StringBuilder attackDirectly(int damage) {
        return new StringBuilder("you opponent receives ").append(damage).append(" battle damage");
    }

    public StringBuilder turnComplete(String name, String opponentName, GameBoard myGameBoard, GameBoard opponentGameBoard) {
        return new StringBuilder("now it will be ").append(name).append("’s turn\n").
                append(buildGameBoard(myGameBoard, opponentGameBoard, name, opponentName))/*.
                append(gameBoardBuilder(myGameBoard, opponentGameBoard))*/;
    }

    public StringBuilder askForPayingLp(String amount, String cardName) {
        return new StringBuilder("do you want to pay ").append(amount).append(" LP to keep ")
                .append(cardName).append("?");
    }

    public StringBuilder askForLpForActivation(int amount){
        return new StringBuilder("do you want to pay ").append(amount).append(" LP to activate this special ability?");
    }

    public StringBuilder userDoesntHaveActiveDeck(String username){
        return new StringBuilder(username).append(" has not active deck");
    }

    public StringBuilder thisPlayerWillStartTheGame(String username){
        return new StringBuilder(username).append(" will start the game");
    }

    public StringBuilder buildGameBoard(GameBoard currentPlayer, GameBoard opponent, String currentPlayerUsername, String opponentUsername){
        int cardsInHand = calculateCardsInHand(opponent);
        StringBuilder board = new StringBuilder(opponentUsername);
        board.append(":").append(opponent.getHealth());
        board.append("\n");
        for (int i = 0; i < 5; i++) board.append(cardsInHand).append("\t");
        board.append("\n").append(opponent.numberOfCardsRemainingToBePicked());
        board.append("\n\t").append(getTheThing(opponent.getPlace(4, PLACE_NAME.SPELL_AND_TRAP)));
        board.append("\t").append(getTheThing(opponent.getPlace(2, PLACE_NAME.SPELL_AND_TRAP)));
        board.append("\t").append(getTheThing(opponent.getPlace(1, PLACE_NAME.SPELL_AND_TRAP)));
        board.append("\t").append(getTheThing(opponent.getPlace(3, PLACE_NAME.SPELL_AND_TRAP)));
        board.append("\t").append(getTheThing(opponent.getPlace(5, PLACE_NAME.SPELL_AND_TRAP)));
        board.append("\n\t").append(getTheThing(opponent.getPlace(4, PLACE_NAME.MONSTER)));
        board.append("\t").append(getTheThing(opponent.getPlace(2, PLACE_NAME.MONSTER)));
        board.append("\t").append(getTheThing(opponent.getPlace(1, PLACE_NAME.MONSTER)));
        board.append("\t").append(getTheThing(opponent.getPlace(3, PLACE_NAME.MONSTER)));
        board.append("\t").append(getTheThing(opponent.getPlace(5, PLACE_NAME.MONSTER)));
        board.append("\n").append(opponent.getGraveyard().size() != 0 ? opponent.getGraveyard().size() : "E");
        board.append("\t\t\t\t\t").append(getTheThing(opponent.getPlace(0, PLACE_NAME.FIELD)));
        board.append("\n\n--------------------------\n");
        board.append("\n").append(getTheThing(currentPlayer.getPlace(0, PLACE_NAME.FIELD)));
        board.append("\t\t\t\t\t").append(currentPlayer.getGraveyard().size() != 0 ? currentPlayer.getGraveyard()
                .size() : "E");
        board.append("\n\t").append(getTheThing(currentPlayer.getPlace(5, PLACE_NAME.MONSTER)));
        board.append("\t").append(getTheThing(currentPlayer.getPlace(3, PLACE_NAME.MONSTER)));
        board.append("\t").append(getTheThing(currentPlayer.getPlace(1, PLACE_NAME.MONSTER)));
        board.append("\t").append(getTheThing(currentPlayer.getPlace(2, PLACE_NAME.MONSTER)));
        board.append("\t").append(getTheThing(currentPlayer.getPlace(4, PLACE_NAME.MONSTER)));
        board.append("\n\t").append(getTheThing(currentPlayer.getPlace(5, PLACE_NAME.SPELL_AND_TRAP)));
        board.append("\t").append(getTheThing(currentPlayer.getPlace(3, PLACE_NAME.SPELL_AND_TRAP)));
        board.append("\t").append(getTheThing(currentPlayer.getPlace(1, PLACE_NAME.SPELL_AND_TRAP)));
        board.append("\t").append(getTheThing(currentPlayer.getPlace(2, PLACE_NAME.SPELL_AND_TRAP)));
        board.append("\t").append(getTheThing(currentPlayer.getPlace(4, PLACE_NAME.SPELL_AND_TRAP)));
        board.append("\n\t\t\t\t\t").append(currentPlayer.numberOfCardsRemainingToBePicked());
        board.append("\n");
        cardsInHand = calculateCardsInHand(currentPlayer);
        for (int i = 0; i < 5; i++) board.append(cardsInHand).append("\t");
        board.append("\n").append(currentPlayerUsername).append(":").append(currentPlayer.getHealth());
        return board;
    }

    private int calculateCardsInHand(GameBoard board){
        int amount = 0;
        for (int i = 0; i < 6; i++) {
            if (board.getPlace(i, PLACE_NAME.HAND).getCard() != null)
                amount++;
        }
        return amount;
    }

    private String getTheThing(Place place){
        if (place.getCard() == null)
            return "E";
        if (place.getCard() instanceof MonsterCards){
            switch (place.getStatus()){
                case ATTACK:
                    return "OO";
                case DEFENCE:
                    return "DO";
                default:
                    return "DH";
            }
        } else {
            switch (place.getStatus()){
                case ATTACK:
                    return "O";
                default:
                    return "H";
            }
        }
    }

    public String askActivateMonster(String name){
        return new StringBuilder("do you want to activate ").append(name).append(" special ability ?").toString();
    }

    // AI

    public String selectCardAI(int cardNumber, PLACE_NAME placeName) {
        if (placeName == PLACE_NAME.HAND) {
            return "select --hand " + cardNumber;
        }
        if (placeName == PLACE_NAME.MONSTER) {
            return "select --monster" + cardNumber;
        }
        if (placeName == PLACE_NAME.SPELL_AND_TRAP) {
            return "select --spell" + cardNumber;
        }
        return "";
    }

    public String attackToMonster(int number) {
        return "attack " + number;
    }

    public StringBuilder showEndRoundOrGameMessage(String name, int score, boolean wholeMatch){
        return new StringBuilder(name).append(" won the ").
                append(wholeMatch ? "whole match with score: " : "game and the score is: ").append(score);
    }

    public String askSwapCards(String name){
        return name + " do you want to swap cards between your main deck and your side deck ?";
    }

}
