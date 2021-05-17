package controller.ai;

import model.cards.Cards;
import model.cards.monster.MonsterCards;
import model.cards.spell.SpellCards;
import model.cards.trap.TrapCards;
import model.game.*;

import java.util.ArrayList;
import java.util.Locale;

public class CommunicatorBetweenAIAndGame {
    private static CommunicatorBetweenAIAndGame communicatorBetweenAIAndGame = null;
    private GameBoard AIGameBoard;
    private GameBoard opponentGameBoard;

    private CommunicatorBetweenAIAndGame() {
    }

    public static CommunicatorBetweenAIAndGame getInstance() {
        if (communicatorBetweenAIAndGame == null)
            communicatorBetweenAIAndGame = new CommunicatorBetweenAIAndGame();
        return communicatorBetweenAIAndGame;
    }

    public Place getWeakestOpponentFaceUpMonsterPlace(ArrayList<Place> opponentMonsterZone) {
        Place weakestOpponentFaceUpMonsterPlace = null;
        for (Place opponentMonsterPlace : opponentMonsterZone) {
            if (opponentMonsterPlace != null) {
                if (weakestOpponentFaceUpMonsterPlace == null)
                    weakestOpponentFaceUpMonsterPlace = opponentMonsterPlace;
                else {
                    MonsterCards opponentMonsterCards = (MonsterCards) opponentMonsterPlace.getCard();
                    int opponentMonsterCardStrength = getMonsterCardStrengthInMonsterZone(weakestOpponentFaceUpMonsterPlace);
                    MonsterCards weakestOpponentFaceUpMonster = (MonsterCards) weakestOpponentFaceUpMonsterPlace.getCard();
                    int weakestOpponentFaceUpMonsterStrength = getMonsterCardStrengthInMonsterZone(opponentMonsterPlace);
                    if (opponentMonsterCardStrength != -1) {
                        if (opponentMonsterCardStrength < weakestOpponentFaceUpMonsterStrength) {
                            weakestOpponentFaceUpMonster = opponentMonsterCards;
                            weakestOpponentFaceUpMonsterPlace = opponentMonsterPlace;
                        }
                    }
                }
            }
        }
        return weakestOpponentFaceUpMonsterPlace;
    }

    public boolean doesOpponentHaveSetCard(ArrayList<Place> opponentMonsterZone) {
        for (Place opponentMonster : opponentMonsterZone) {
            if (opponentMonster != null)
                if (opponentMonster.getStatus() == STATUS.SET)
                    return true;
        }
        return false;
    }

    public ArrayList<Place> getMonsterZone(GameBoard gameBoard) {
        ArrayList<Place> places = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            Place place = gameBoard.getPlace(i, PLACE_NAME.MONSTER);
            if (place != null)
                places.add(place);
        }
        return places;
    }

    public ArrayList<Place> getSpellAndTrapZone(GameBoard gameBoard){
        ArrayList<Place> places = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            Place place = gameBoard.getPlace(i, PLACE_NAME.SPELL_AND_TRAP);
            if (place != null)
                places.add(place);
        }
        return places;
    }


    public void getMonstersOfHand(ArrayList<Place> places, GameBoard gameBoard) {
        for (int i = 0; i < 5; i++) {
            Place place = gameBoard.getPlace(i, PLACE_NAME.HAND);
            if (place != null)
                if (place.getCard() instanceof MonsterCards)
                    places.add(place);
        }
    }

    public ArrayList<Place> getCardsOfHand(GameBoard gameBoard, String cardType) {
        cardType = cardType.toLowerCase();
        ArrayList<Place> monsterCards = new ArrayList<>();
        ArrayList<Place> spellCards = new ArrayList<>();
        ArrayList<Place> trapCards = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Place place = gameBoard.getPlace(i, PLACE_NAME.HAND);
            if (place != null) {
                if (place.getCard() instanceof MonsterCards)
                    monsterCards.add(place);
                else if (place.getCard() instanceof SpellCards)
                    spellCards.add(place);
                else if (place.getCard() instanceof TrapCards)
                    spellCards.add(place);
            }
        }
        if (cardType.equals("monster"))
            return monsterCards;
        else if (cardType.equals("spell"))
            return spellCards;
        if (cardType.equals("trap"))
            return trapCards;
        else
            return null; // cardType is invalid
    }

    public int getMonsterCardStrengthInMonsterZone(Place place) {
        MonsterZone monsterZone = (MonsterZone) place;
        if (!(place.getCard() instanceof MonsterCards))
            return -1; // card is not a monster
        if (place.getStatus() == STATUS.ATTACK)
            return monsterZone.getAttack();
        else if (place.getStatus() == STATUS.DEFENCE)
            return monsterZone.getDefense();
        else return -1; // card is set
    }

    public Place getBestCardByAttack(ArrayList<Place> AIHand, boolean isBest, ArrayList<Place> ignoreCards) {
        Place AIBestCard = null;
        for (Place AIMonsterInHand : AIHand) {
            if (AIMonsterInHand != null) {
                for (Place ignoreCard : ignoreCards) {
                    if (AIMonsterInHand != ignoreCard) {
                        if (AIBestCard == null) {
                            AIBestCard = AIMonsterInHand;
                        } else {
                            if (isBest)
                                if (((MonsterCards) AIMonsterInHand.getCard()).getAttack() > ((MonsterCards) AIBestCard.getCard()).getAttack())
                                    AIBestCard = AIMonsterInHand;
                                else if (((MonsterCards) AIMonsterInHand.getCard()).getAttack() < ((MonsterCards) AIBestCard.getCard()).getAttack())
                                    AIBestCard = AIMonsterInHand;
                        }
                    }
                }

            }
        }
        return AIBestCard;
    }

    public ArrayList<Place> monsterFinderByPlace(ArrayList<Place> places) {
        ArrayList<Place> monsterCards = new ArrayList<>();
        for (Place place : places) {
            if (place != null) {
                if (place.getCard() instanceof MonsterCards)
                    monsterCards.add(place);
            }
        }
        return monsterCards;
    }

    public ArrayList<Cards> monsterFinderByCard(ArrayList<Cards> cards) {
        ArrayList<Cards> monsterCards = new ArrayList<>();
        for (Cards card : cards) {
            if (card != null)
                if (card instanceof MonsterCards)
                    monsterCards.add(card);
        }
        return monsterCards;
    }

    public MonsterCards findBestMonsterCardByCard(ArrayList<Cards> cards) {
        ArrayList<Cards> monsterCards = monsterFinderByCard(cards);
        for (Object monsterCard : monsterCards) {
            // fill this place
        }
        return null;
    }

    public Place findBestMonsterCardByAttackByPlace(ArrayList<Place> places) {
        Place bestMonsterPlace = null;
        for (Place place : places) {
            if (place != null) {
                if (bestMonsterPlace == null)
                    bestMonsterPlace = place;
                else if (((MonsterCards) place.getCard()).getAttack() > ((MonsterCards) bestMonsterPlace.getCard()).getAttack())
                    bestMonsterPlace = place;
            }
        }
        return bestMonsterPlace;
    }

    public MonsterCards cardsComparatorByAttack(MonsterCards firstCard, MonsterCards secondCard) {
        if (firstCard == null && secondCard == null)
            return null;
        if (firstCard == null)
            return secondCard;
        if (secondCard == null)
            return firstCard;
        if (firstCard.getAttack() > secondCard.getAttack())
            return firstCard;
        else if (firstCard.getAttack() < secondCard.getAttack())
            return secondCard;
        else {
            if (firstCard.getDefense() > secondCard.getDefense())
                return firstCard;
            if (firstCard.getDefense() < secondCard.getDefense())
                return secondCard;
            else return firstCard;
        }
    }
}
