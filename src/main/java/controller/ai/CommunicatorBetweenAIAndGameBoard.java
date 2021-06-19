package controller.ai;

import model.cards.Cards;
import model.cards.monster.MonsterCards;
import model.cards.spell.SpellCards;
import model.cards.trap.TrapCards;
import model.game.*;

import java.util.ArrayList;

public class CommunicatorBetweenAIAndGameBoard {
    private static CommunicatorBetweenAIAndGameBoard communicatorBetweenAIAndGameBoard = null;

    protected CommunicatorBetweenAIAndGameBoard() {
    }

    public static CommunicatorBetweenAIAndGameBoard getInstance() {
        if (communicatorBetweenAIAndGameBoard == null)
            communicatorBetweenAIAndGameBoard = new CommunicatorBetweenAIAndGameBoard();
        return communicatorBetweenAIAndGameBoard;
    }

    public Place getWeakestOpponentFaceUpMonsterPlace(ArrayList<Place> opponentMonsterZone) {
        Place weakestOpponentFaceUpMonsterPlace = null;
        for (Place opponentMonsterPlace : opponentMonsterZone) {
            if (opponentMonsterPlace != null) {
                if (weakestOpponentFaceUpMonsterPlace == null)
                    weakestOpponentFaceUpMonsterPlace = opponentMonsterPlace;
                else {
                    MonsterCards opponentMonsterCards = (MonsterCards) opponentMonsterPlace.getCard();
                    int opponentMonsterCardStrength = getMonsterCardStrengthInMonsterZone
                            (weakestOpponentFaceUpMonsterPlace);
                    MonsterCards weakestOpponentFaceUpMonster = (MonsterCards)
                            weakestOpponentFaceUpMonsterPlace.getCard();
                    int weakestOpponentFaceUpMonsterStrength = getMonsterCardStrengthInMonsterZone
                            (opponentMonsterPlace);
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
        for (int i = 1; i < 6; i++) {
            Place place = gameBoard.getPlace(i, PLACE_NAME.MONSTER);
            if (place != null)
                places.add(place);
        }
        return places;
    }

    public ArrayList<Place> getSpellAndTrapZone(GameBoard gameBoard) {
        ArrayList<Place> places = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            Place place = gameBoard.getPlace(i, PLACE_NAME.SPELL_AND_TRAP);
            if (place != null)
                places.add(place);
        }
        return places;
    }

    public ArrayList<Place> getSpells(ArrayList<Place> places) {
        ArrayList<Place> spellsPlaces = new ArrayList<>();
        for (Place place : places) {
            if (place.getCard() instanceof SpellCards) {
                spellsPlaces.add(place);
            }
        }
        return spellsPlaces;
    }

    public ArrayList<Place> getTrapCards(ArrayList<Place> places){
        ArrayList<Place> suitedCards = new ArrayList<>();
        for (Place place : places) {
            if (place.getCard() instanceof TrapCards) {
                suitedCards.add(place);
            }
        }
        return suitedCards;
    }

    public void getMonstersOfHand(ArrayList<Place> places, GameBoard gameBoard, boolean isRitual) {
        for (int i = 0; i < 6; i++) {
            Place place = gameBoard.getPlace(i, PLACE_NAME.HAND);
            if (place != null) {
                if (isRitual) {
                    if ((place.getCard() instanceof MonsterCards) && place.getCard().getType().equals("Ritual"))
                        places.add(place);
                } else {
                    if ((place.getCard() instanceof MonsterCards) && !place.getCard().getType().equals("Ritual"))
                        places.add(place);
                }
            }
        }
    }

    public ArrayList<Place> getCardsOfHand(GameBoard gameBoard, String cardType) {
        cardType = cardType.toLowerCase();
        ArrayList<Place> monsterCards = new ArrayList<>();
        ArrayList<Place> spellCards = new ArrayList<>();
        ArrayList<Place> trapCards = new ArrayList<>();
        for (int i = 1; i < 7; i++) {
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

    public Place getBestMonsterCardStrengthInMonsterZoneForAttack(ArrayList<Place> places, Place AICardForAttack) {
        ArrayList<Place> monsterPlaces = monsterFinderByPlace(places);
        Place bestCardToAttack = null;
        for (Place monsterPlace : monsterPlaces) {
            if (monsterPlace != null) {
                if (((MonsterZone) AICardForAttack).getAttack() > getMonsterCardStrengthInMonsterZone(monsterPlace)) {
                    if (bestCardToAttack == null)
                        bestCardToAttack = monsterPlace;
                    else if (getMonsterCardStrengthInMonsterZone(monsterPlace) > getMonsterCardStrengthInMonsterZone(bestCardToAttack))
                        bestCardToAttack = monsterPlace;
                }
            }
        }
        return bestCardToAttack;
    }

    public Place getBestCardByAttack(ArrayList<Place> AIHand, boolean isBest) {
        Place AIBestCard = null;
        for (Place AIMonsterInHand : AIHand) {
            if (AIMonsterInHand != null) {
                if (AIBestCard == null) {
                    AIBestCard = AIMonsterInHand;
                } else {
                    if (isBest)
                        if (((MonsterCards) AIMonsterInHand.getCard()).getAttack() >
                                ((MonsterCards) AIBestCard.getCard()).getAttack())
                            AIBestCard = AIMonsterInHand;
                        else if (((MonsterCards) AIMonsterInHand.getCard()).getAttack() <
                                ((MonsterCards) AIBestCard.getCard()).getAttack())
                            AIBestCard = AIMonsterInHand;
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

    public ArrayList<MonsterCards> monsterFinderByCard(ArrayList<Cards> cards) {
        ArrayList<MonsterCards> monsterCards = new ArrayList<>();
        for (Cards card : cards) {
            if (card != null)
                if (card instanceof MonsterCards)
                    monsterCards.add((MonsterCards) card);
        }
        return monsterCards;
    }

    // until now just used for finding best monster card of graveyard
    public MonsterCards findBestMonsterCardByCard(ArrayList<Cards> cards) {
        ArrayList<MonsterCards> monsterCards = monsterFinderByCard(cards);
        MonsterCards bestMonsterCard = null;
        for (MonsterCards monsterCard : monsterCards) {
            if (monsterCard != null) {
                if (bestMonsterCard == null)
                    bestMonsterCard = monsterCard;
                else if (monsterCard.getAttack() + (0.7 * monsterCard.getDefense()) >
                        bestMonsterCard.getAttack() + (0.7 * bestMonsterCard.getDefense()))
                    bestMonsterCard = monsterCard;
            }
        }
        return bestMonsterCard;
    }

    public Place findBestMonsterCardByAttackByPlace(ArrayList<Place> places) {
        Place bestMonsterPlace = null;
        for (Place place : places) {
            if (place != null) {
                if (bestMonsterPlace == null)
                    bestMonsterPlace = place;
                else if (((MonsterCards) place.getCard()).getAttack() >
                        ((MonsterCards) bestMonsterPlace.getCard()).getAttack())
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

    public int numberThisTypeMonsterInThisZone(GameBoard gameBoard, String type) {
        ArrayList<Place> monsterZone = getMonsterZone(gameBoard);
        int cardCounter = 0;
        for (Place place : monsterZone) {
            if (place != null) {
                Cards card = place.getCard();
                if (card instanceof MonsterCards) {
                    MonsterCards monsterCard = (MonsterCards) card;
                    if (monsterCard.getMonsterType().equals(type))
                        ++cardCounter;
                }
            }
        }
        return cardCounter;
    }

    public boolean weightedCardCounter(ArrayList<String> goodCards, ArrayList<String> badCards,
                                       GameBoard AIGameBoard, GameBoard opponentGameBoard) {
        int AIGoodCardCounter = 0;
        AIGoodCardCounter = getGoodCardCounter(goodCards, AIGameBoard, AIGoodCardCounter);
        AIGoodCardCounter = getBadCardCounter(badCards, AIGameBoard, AIGoodCardCounter);

        int opponentGoodCardCounter = 0;
        AIGoodCardCounter = getGoodCardCounter(goodCards, opponentGameBoard, AIGoodCardCounter);
        AIGoodCardCounter = getBadCardCounter(badCards, opponentGameBoard, AIGoodCardCounter);
        return AIGoodCardCounter >= opponentGoodCardCounter;
    }

    private int getBadCardCounter(ArrayList<String> badCards, GameBoard AIGameBoard, int AIGoodCardCounter) {
        for (String badCard : badCards) {
            AIGoodCardCounter -= numberThisTypeMonsterInThisZone(AIGameBoard, badCard);
        }
        return AIGoodCardCounter;
    }

    private int getGoodCardCounter(ArrayList<String> goodCards, GameBoard AIGameBoard, int AIGoodCardCounter) {
        for (String goodCard : goodCards) {
            AIGoodCardCounter += numberThisTypeMonsterInThisZone(AIGameBoard, goodCard);
        }
        return AIGoodCardCounter;
    }


    public Place getFirstSetMonsterCard(ArrayList<Place> places) {
        if (places != null) {
            for (Place place : places) {
                if (place.getStatus() == STATUS.SET)
                    return place;
            }
        }
        return null;
    }

    public int getNumberOfPlaceInGameBoard(GameBoard gameBoard, Place placeToFind, PLACE_NAME placeName) {
        if (placeName == PLACE_NAME.HAND) {
            for (int i = 1; i < 7; i++) {
                Place place = gameBoard.getPlace(i, PLACE_NAME.HAND);
                if (place != null) {
                    if (place.getCard().getName().equals(placeToFind.getCard().getName()))
                        return i;
                }
            }
        }
        if (placeName == PLACE_NAME.MONSTER) {
            for (int i = 1; i < 6; i++) {
                Place place = gameBoard.getPlace(i, PLACE_NAME.MONSTER);
                if (place != null) {
                    if (place.getCard().getName().equals(placeToFind.getCard().getName()))
                        return i;
                }
            }
        }
        if (placeName == PLACE_NAME.SPELL_AND_TRAP) {
            for (int i = 1; i < 6; i++) {
                Place place = gameBoard.getPlace(i, PLACE_NAME.SPELL_AND_TRAP);
                if (place != null) {
                    if (place.getCard().getName().equals(placeToFind.getCard().getName()))
                        return i;
                }
            }
        }
        return -1;
    }
}
