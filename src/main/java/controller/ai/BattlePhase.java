package controller.ai;

import model.game.GameBoard;
import model.game.Place;

import java.util.ArrayList;

public class BattlePhase {
    private static BattlePhase battlePhase = null;
    private GameBoard AIGameBoard;
    private GameBoard opponentGameBoard;
    private CommunicatorBetweenAIAndGameBoard gameBoardCommunicator = CommunicatorBetweenAIAndGameBoard.getInstance();
    private CommunicatorBetweenAIAndGamePlay gamePlayCommunicator = CommunicatorBetweenAIAndGamePlay.getInstance();
    private ArrayList<Place> ignoreCards = new ArrayList<>(); // if get error since set or summon, add the card to this
    // initialize these Arraylists
    ArrayList<Place> AIMonsterCardPlacesAlreadyAttacked = new ArrayList<>();
    ArrayList<Place> opponentMonsterCardPlacesAlreadyHaveBeenAttacked = new ArrayList<>();

    private BattlePhase() {
    }

    public BattlePhase getInstance() {
        if (battlePhase == null)
            battlePhase = new BattlePhase();
        return battlePhase;
    }

    public void start() {
        ArrayList<Place> monsterCardPlaces = gameBoardCommunicator.getMonsterZone(AIGameBoard);
        if (monsterCardPlaces != null) {
            boolean canAttack = true;
            while (canAttack) {
                canAttack = attack();
            }
        }
        gamePlayCommunicator.endPhase();
    }

    public boolean attack() {
        ArrayList<Place> AIMonsterCardPlaces = gameBoardCommunicator.getMonsterZone(AIGameBoard);
        ArrayList<Place> opponentMonsterCardPlaces = gameBoardCommunicator.getMonsterZone(opponentGameBoard);
        AIMonsterCardPlaces.removeIf(aiMonsterCardPlace
                -> AIMonsterCardPlacesAlreadyAttacked.contains(aiMonsterCardPlace));
        opponentMonsterCardPlaces.removeIf(opponentMonsterCardPlace
                -> opponentMonsterCardPlacesAlreadyHaveBeenAttacked.contains(opponentMonsterCardPlace));

        if (!AIMonsterCardPlaces.isEmpty()) {
            if (opponentMonsterCardPlaces.isEmpty()) {

                // not considering attack modifier
                Place cardPlaceForAttack = gameBoardCommunicator.
                        findBestMonsterCardByAttackByPlace(AIMonsterCardPlaces);
                if (cardPlaceForAttack == null) {
                    return false;
                } else {
                    // direct attack with cardPlaceForAttack
                    AIMonsterCardPlacesAlreadyAttacked.add(cardPlaceForAttack);
                    return true;
                }
            } else {
                if (gameBoardCommunicator.doesOpponentHaveSetCard(opponentMonsterCardPlaces)) {
                    Place firstOpponentSetCard = gameBoardCommunicator.
                            getFirstSetMonsterCard(opponentMonsterCardPlaces);
                    if (firstOpponentSetCard != null) {
                        // not considering attack modifier
                        Place cardPlaceForAttack = gameBoardCommunicator.
                                findBestMonsterCardByAttackByPlace(AIMonsterCardPlaces);
                        if (cardPlaceForAttack == null) {
                            return false;
                        } else {
                            // attack firstOpponentSetCard.getName
                            AIMonsterCardPlacesAlreadyAttacked.add(cardPlaceForAttack);
                            opponentMonsterCardPlacesAlreadyHaveBeenAttacked.add(firstOpponentSetCard);
                            return true;
                        }
                    }
                } else {
                    // not considering attack modifier
                    Place cardPlaceForAttack = gameBoardCommunicator.
                            findBestMonsterCardByAttackByPlace(AIMonsterCardPlaces);
                    if (cardPlaceForAttack != null) {
                        Place opponentCardPlaceToAttackTo = gameBoardCommunicator.
                                getBestMonsterCardStrengthInMonsterZoneForAttack
                                        (opponentMonsterCardPlaces, cardPlaceForAttack);
                        // attack to opponentCardPlaceToAttackTo
                        AIMonsterCardPlacesAlreadyAttacked.add(cardPlaceForAttack);
                        opponentMonsterCardPlacesAlreadyHaveBeenAttacked.add(opponentCardPlaceToAttackTo);
                        return true;
                    } else
                        return false;
                }
            }
        } else
            return false;
        return false;
    }
}









