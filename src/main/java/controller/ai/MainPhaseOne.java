package controller.ai;

import model.cards.monster.MonsterCards;
import model.game.*;

import java.util.ArrayList;

public class MainPhaseOne {
    private GameBoard AIGameBoard;
    private GameBoard opponentGameBoard;
    private CommunicatorBetweenAIAndGame communicator = CommunicatorBetweenAIAndGame.getInstance();


    public void setOrSummonMonster() {
        ArrayList<Place> AIHand = new ArrayList<>();
        communicator.getMonstersOfHand(AIHand, AIGameBoard);
        Place AIBestCard;

        ArrayList<Place> opponentMonsterZone = new ArrayList<>();
        communicator.getMonsterZone(opponentMonsterZone, opponentGameBoard);

        boolean opponentHasSetCard = communicator.doesOpponentHaveSetCard(opponentMonsterZone);
        if (opponentHasSetCard) {
            AIBestCard = communicator.getBestCardByAttack(AIHand, true);
            // summon AIBestCard
            // check for AIBestCard being null
            // attack to this card at first
        } else {
            Place weakestOpponentFaceUpMonsterPlace = communicator
                    .getWeakestOpponentFaceUpMonsterPlace(opponentMonsterZone);
            if (weakestOpponentFaceUpMonsterPlace == null) {
                AIBestCard = communicator.getBestCardByAttack(AIHand, true);
                // summon best card
                // check for AIBestCard being null
                //  direct attack
            } else {
                AIBestCard = communicator.getBestCardByAttack(AIHand, true);
                if (AIBestCard == null) {
                    // there is nothing to set or summon
                } else if (((MonsterCards) AIBestCard.getCard()).getAttack() > communicator
                        .getMonsterCardStrengthInMonsterZone(weakestOpponentFaceUpMonsterPlace)) {
                    // summon AIBestCard
                } else {
                    AIBestCard = communicator.getBestCardByAttack(AIHand, false);
                    // set AIBestCard
                }
            }
        }
    }

    public void tribute(int numberOfCardsToTribute) {
        ArrayList<Place> AIMonsterZonePlaces = new ArrayList<>();
        communicator.getMonsterZone(AIMonsterZonePlaces, AIGameBoard);
    }
}
