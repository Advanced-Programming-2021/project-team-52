package controller;

import model.Deck;
import model.User;
import model.cards.monster.MonsterCards;
import model.cards.spell.SpellCards;
import model.cards.trap.TrapCards;
import model.game.GameBoard;
import model.cards.Cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

public class PrintBuilderController {
    private static PrintBuilderController printPrintBuilderController = null;

    private void PrintBuilderController() {
    }

    public static PrintBuilderController getInstance() {
        if (printPrintBuilderController == null)
            printPrintBuilderController = new PrintBuilderController();
        return printPrintBuilderController;
    }

    public void buildAllCardsInDeck(Deck deck) {
    }

    public void buildAllDecks(User user) {
    }

    public StringBuilder buildGraveyard(ArrayList<Cards> graveyard) {
        return null;
    }

    public void buildGameWinner(String username, int score1, int score2) {
    }

    public void buildMatchWinner(String username, int score1, int score2) {
    }

    public void buildScoreBoard() {
    }

    public void buildChainTurnPromot(String playerName, GameBoard board) {
    }

    public String winingAgainstOO(int damage) {
        return "your opponent’s monster is destroyed" +
                " and your opponent receives" +
                damage +
                "battle damage";
    }

    public String losingAgainstOO(int damage) {
        return "Your monster card is destroyed" +
                " and you received " + damage + " battle" +
                " damage";
    }

    public String losingAgainstDO(int damage) {
        return "no card is destroyed and" +
                " you received " + damage + "battle damage";
    }

    public String hiddenCardAfterAttacking(String cardName) {
        return "opponent’s monster card was " + cardName + " and ";
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

    public String thereAreAlreadyThreeCardsWithThisNameInThisDeck(String cardName, String deckName, int numberOfCards) {
        return "there are already "+numberOfCards+" cards with name " + cardName + " in deck " + deckName;
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
        if(activeDeck != null)
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

    public String userHasNoActiveDeck(String username) {
        return username + " has no active deck";
    }

    public String userDeckIsInvalid(String username) {
        return username + " deck is invalid";
    }


    public StringBuilder drawCard(String name) {
        return new StringBuilder("new card added to hand: ").append(name);
    }

    public StringBuilder playerTurn(String name) {
        return new StringBuilder("its ").append(name).append("'s turn");
    }

    public StringBuilder attackToAttackResult(int damage, boolean opponentLost) {
        if (opponentLost)
            return new StringBuilder("your opponent’s monster is destroyed and your opponent receives ").append(damage)
                    .append("battle damage");
        else
            return new StringBuilder("Your monster card is destroyed and you received ").append(damage).
                    append(" battle damage");
    }

    public StringBuilder attackToDefenseResult(boolean defenderWasHidden, String defenderName, int result, int damage) {
        StringBuilder results = new StringBuilder();
        if (defenderWasHidden)
            results.append("opponent’s monster card was ").append(defenderName).append(" ");
        switch (result) {
            case 1:
                results.append("the defense position monster is destroyed");
            case 0:
                results.append("no card is destroyed");
            case -1:
                results.append("no card is destroyed and you received ").append(damage).append("  battle damage");
        }
        return results;
    }

    public StringBuilder attackDirectly(int damage) {
        return new StringBuilder("you opponent receives ").append(damage).append(" battle damage");
    }

    public StringBuilder turnComplete(String name, GameBoard myGameBoard, GameBoard opponentGameBoard) {
        return new StringBuilder("now it will be ").append(name).append("’s turn\n").
                append(gameBoardBuilder(myGameBoard, opponentGameBoard));
    }

    public StringBuilder gameBoardBuilder(GameBoard myGameBoard, GameBoard opponentGameBoard) {
        return null;
    }

    public StringBuilder askForPayingLp(String amount, String cardName) {
        return new StringBuilder("do you want to lose ").append(amount).append("LP or lose ").append(cardName).append("?");
    }

    public StringBuilder askForLpForActivation(int amount){
        return new StringBuilder("do you want to pay ").append(amount).append(" LP to activate this special ability?");
    }


}
