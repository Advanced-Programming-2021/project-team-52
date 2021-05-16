package controller.ai;

import model.cards.monster.MonsterCards;
import model.game.*;

import java.util.ArrayList;

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

    public void getMonsterZone(ArrayList<Place> places, GameBoard gameBoard) {
        for (int i = 1; i < 5; i++) {
            Place place = gameBoard.getPlace(i, PLACE_NAME.MONSTER);
            if (place != null)
                places.add(place);
        }
    }

    public void getMonstersOfHand(ArrayList<Place> places, GameBoard gameBoard) {
        for (int i = 0; i < 5; i++) {
            Place place = gameBoard.getPlace(i, PLACE_NAME.HAND);
            if (place != null)
                if (place.getCard() instanceof MonsterCards)
                    places.add(place);
        }
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

    public Place getBestCardByAttack(ArrayList<Place> AIHand, boolean isBest) {
        Place AIBestCard = null;
        for (Place AIMonsterInHand : AIHand) {
            if (AIMonsterInHand != null) {
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
        return AIBestCard;
    }
}
