package controller.specialbilities;

import controller.GamePlayController;
import controller.NewChain;
import controller.PrintBuilderController;
import controller.RegexController;
import model.cards.Cards;
import model.cards.monster.MonsterCards;
import model.game.*;
import model.tools.CHAIN_JOB;
import model.tools.RegexPatterns;
import model.tools.StringMessages;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;

public class ActivateNoChain implements SpecialAbility, StringMessages {

    private Method method;
    private String methodName;
    private GamePlayController gamePlayController;
    private Place place;
    private int amount;
    private boolean onlyForOnePlayer;
    private String type;

    @Override
    public void run(GamePlayController gamePlayController, Place place){
        this.gamePlayController = gamePlayController;
        this.place = place;
        try {
            method.invoke(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setMethod(Method method) {
        this.methodName = method.getName();
        this.method = method;
    }

    @Override
    public String getMethodName() {
        return methodName;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setOnlyForOnePlayer(boolean onlyForOnePlayer) {
        this.onlyForOnePlayer = onlyForOnePlayer;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void sacrificeToGetFromGraveYard(){
        if (!gamePlayController.getGamePlay().getHistory().get(place).contains("noSpecialThisRound")) {
            ArrayList<Cards> graveyard = gamePlayController.getGamePlay().getMyGameBoard().getGraveyard();
            printerAndScanner.printString(PrintBuilderController.getInstance().buildGraveyard(graveyard));
            int emptyHand = -1;
            for (int i = 0; i < 6; i++) {
                if (gamePlayController.getGamePlay().getMyGameBoard().getPlace(i, PLACE_NAME.HAND).getCard() != null) {
                    emptyHand = i;
                    break;
                }
            }
            if (emptyHand != -1) {
                printerAndScanner.printNextLine(askForName);
                String name = printerAndScanner.scanNextLine();
                while ((!graveyard.contains(Cards.getCard(name)) ||
                        !(Cards.getCard(name) instanceof MonsterCards)) && !name.equals("cancel")) {
                    printerAndScanner.printNextLine(wrongCard);
                    name = printerAndScanner.scanNextLine();
                    if (Cards.getCard(name) instanceof MonsterCards) {
                        if (((MonsterCards) Cards.getCard(name)).getLevel() < amount) {
                            name = "";
                        }
                    } else name = "";
                }
                if (!name.equals("cancel")) {
                    gamePlayController.getGamePlay().getMyGameBoard().getPlace(emptyHand, PLACE_NAME.HAND)
                            .setCard(Cards.getCard(name));
                    gamePlayController.getGamePlay().getHistory().get(place).add("noSpecialThisRound");
                }
            } else printerAndScanner.printNextLine(fullHand);
        }
    }

//    public void specialSummonFromGraveYard(){ //TODO ++
//        if (!gamePlayController.getGamePlay().getMyGameBoard().getGraveyard().isEmpty() && !
//                gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getMyGameBoard()
//                        .getGraveyard().isEmpty()){
//            int emptyPlace = gamePlayController.getGamePlay().getMyGameBoard().getFirstEmptyPlace(PLACE_NAME.MONSTER);
//            if (emptyPlace != -1){
//                printerAndScanner.printString(PrintBuilderController.getInstance().buildGraveyard(
//                        gamePlayController.getGamePlay().getMyGameBoard().getGraveyard()));
//                printerAndScanner.printString(PrintBuilderController.getInstance().buildGraveyard(
//                        gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay()
//                                .getMyGameBoard().getGraveyard()));
//                printerAndScanner.printNextLine(askForName);
//                String cardName = printerAndScanner.scanNextLine();
//                while (!(Cards.getCard(cardName) instanceof MonsterCards) ){
//                    printerAndScanner.printNextLine(wrongCard);
//                    cardName = printerAndScanner.scanNextLine();
//                }
//                Place temporaryPlace = new Place(PLACE_NAME.HAND);
//
//                gamePlayController.summon(temporaryPlace, false);
//                new NewChain(gamePlayController, )
//            } else printerAndScanner.printNextLine(cantSpecialSummon);
//        } else printerAndScanner.printNextLine(cantSpecialSummon);
//    }

    public void specialSummonFromGraveYard() { //TODO ++ ++ ++
        if (!gamePlayController.getGamePlay().getMyGameBoard().getGraveyard().isEmpty() ||
        !gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getMyGameBoard().getGraveyard().isEmpty()) {
            int emptyPlace = gamePlayController.getGamePlay().getMyGameBoard().getFirstEmptyPlace(PLACE_NAME.MONSTER);
            if (emptyPlace != -1){
                for (String command = printerAndScanner.scanNextLine(); true; command = printerAndScanner.scanNextLine()) {
                    if (command.equals("show graveyard")) {
                        gamePlayController.showGraveYard();
                        continue;
                    }
                    else if (command.equals("show opponent graveyard")) {
                        gamePlayController.getGamePlay().getOpponentGamePlayController().showGraveYard();
                        continue;
                    }
                    else if (command.startsWith("select card")){
                        Matcher matcher = RegexController.getMatcher(command, RegexPatterns.getCardName);
                        if (matcher != null)
                            if (!(onlyForOnePlayer && RegexController.hasField(matcher, "opponent")))
                            if (tryToSpecialSummon(RegexController.hasField(matcher, "opponent") ?
                                    gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().
                                            getMyGameBoard().getGraveyard() :
                                    gamePlayController.getGamePlay().getMyGameBoard().getGraveyard(),
                                    matcher.group("name"), place))
                                        break;
                    }
                    printerAndScanner.printNextLine(invalidInput);
                }
            }
        }
    }

    private boolean tryToSpecialSummon(ArrayList<Cards> graveYard, String cardName, Place spell){
        Cards card = Cards.getCard(cardName);
        if (card != null)
            if (graveYard.contains(card))
                if (card instanceof MonsterCards)
                    if (((MonsterCards) card).getLevel() >= amount){
                STATUS status = STATUS.getStatusByString(type);
                graveYard.remove(card);
                Place temporary = new Place(PLACE_NAME.HAND);
                temporary.setCard(card);
                temporary.setAffect(place);
                new NewChain(gamePlayController, temporary, CHAIN_JOB.SPECIAL_SUMMON,
                        spell.getCard().getSpecialSpeed(), gamePlayController.sendChainedPlaces());
                if (temporary.getCard() != null)
                    gamePlayController.placeCard(temporary, false, status == null ? gamePlayController.askStatus() : status);
                return true;
            }
        return false;
    }

    public void addFieldSpellToHand(){//TODO ++
        int emptyHand;
        for (emptyHand = 0; emptyHand < 6; emptyHand++)
            if (gamePlayController.getGamePlay().getMyGameBoard().getPlace(emptyHand, PLACE_NAME.HAND).getCard() == null)
                break;
            if (emptyHand != 7) {
                Cards card = gamePlayController.getGamePlay().getMyGameBoard().getACardByType("Field");
                if (card != null){
                    gamePlayController.getGamePlay().getMyGameBoard().getPlace(emptyHand, PLACE_NAME.HAND).setCard(card);
                } else printerAndScanner.printNextLine(couldNotFindASuitableCard);
            } else printerAndScanner.printNextLine(fullHand);
    }

    public void drawCard(){//TODO++
        for (int amount = this.amount; amount > 0; amount--)
            gamePlayController.drawCard();
    }

    public void killAllMonsters(){//TODO ++ ++ ++
        GeneralSpecialAbility.killAllMonsters(onlyForOnePlayer, gamePlayController, true, null);
    }

    public void controlEnemyMonster(){ //TODO ++
        GamePlayController opponentGamePlayController = gamePlayController.getGamePlay().getOpponentGamePlayController();
        int myEmptyMonsterPlace;
            myEmptyMonsterPlace = gamePlayController.getGamePlay().getMyGameBoard().getFirstEmptyPlace(PLACE_NAME.MONSTER);
            if (myEmptyMonsterPlace != -1){
                int monsterCard = printerAndScanner.scanNextInt();
                while (opponentGamePlayController.getGamePlay()
                        .getMyGameBoard().getPlace(monsterCard, PLACE_NAME.MONSTER) == null){
                    printerAndScanner.printNextLine(wrongCard);
                    monsterCard = printerAndScanner.scanNextInt();
                }
                Place opponentPlace = opponentGamePlayController.getGamePlay()
                        .getMyGameBoard().getPlace(monsterCard, PLACE_NAME.MONSTER);
                Place myPlace = gamePlayController.getGamePlay().getMyGameBoard().getPlace(myEmptyMonsterPlace, PLACE_NAME.MONSTER);
                myPlace.setCard(opponentPlace.getCard());
                myPlace.setStatus(opponentPlace.getStatus());
                gamePlayController.getGamePlay().getHistory().get(myPlace).add("forEnemy");
                opponentGamePlayController.killCard(opponentPlace);
                opponentGamePlayController.getGamePlay().getUniversalHistory()
                        .add("MyMonsterCard" + myEmptyMonsterPlace);
            } else printerAndScanner.printNextLine(fullMonsterPlaces);
    }

    public void destroyAllEnemySpellAndTrap(){//TODO ++
        GamePlayController opponentGamePlayController =
                gamePlayController.getGamePlay().getOpponentGamePlayController();
        GameBoard opponentGameBoard = opponentGamePlayController.getGamePlay().getMyGameBoard();
        for (int i = 1; i < 6; i++) {
            Place toKill = opponentGameBoard.getPlace(i, PLACE_NAME.SPELL_AND_TRAP);
            if (toKill.getCard() != null) {
                toKill.getTemporaryFeatures().add(TEMPORARY_FEATURES.FORCE_KILL);
                opponentGamePlayController.killCard(toKill);
            }
        }
    }

    public void showSetOpponentCards(){//TODO ++
        for (int i = 1; i < 6 ; i++) {
            Place place = gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getMyGameBoard()
                    .getPlace(i, PLACE_NAME.MONSTER);
            if (place.getCard() != null){
                if (place.getStatus() == STATUS.SET){
                    gamePlayController.getGamePlay().getOpponentGamePlayController().flip(place,
                            place instanceof MonsterZone ? STATUS.DEFENCE : STATUS.ATTACK, true, true);
                }
            }
        }
    }

    public void cannotAttack(){ //TODO ++
        gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay()
                .getUniversalHistory().add("cannotAttack");
    }

    public void turnsRemaining(){//TODO ++
        gamePlayController.getGamePlay().getHistory().get(place).add("turnsRemaining" + amount * 2);
    }

    public void scanner(){//TODO ++
        ArrayList<Cards> opponentGraveYard =
                gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getMyGameBoard().getGraveyard();
        if (!opponentGraveYard.isEmpty()){
            printerAndScanner.printNextLine(askForName);
            String cardName = printerAndScanner.scanNextLine();
            Cards card;
            while (true){
                card = Cards.getCard(cardName);
                if (opponentGraveYard.contains(card))
                    break;
                printerAndScanner.printNextLine(wrongCard);
                cardName = printerAndScanner.scanNextLine();
            }
            place.setCard(card);
        } else printerAndScanner.printNextLine(emptyOpponentGraveYard);
    }

    public void mindCrush(){ //TODO ++
        printerAndScanner.printNextLine(askForName);
        if (removeAllOfThisCardFromHand(printerAndScanner.scanNextLine())){
            for (int i = 0; i < 6; i++) {
                if (gamePlayController.getGamePlay().getMyGameBoard().getPlace(i, PLACE_NAME.HAND).getCard() != null){
                    removeARandomCardFromMyHand();
                    break;
                }
            }
        }
    }

    private boolean removeAllOfThisCardFromHand(String name){
        boolean notFound = true;
//        GameBoard opponentGameBoard = gamePlayController.getGamePlay().getOpponentGamePlayController().getGamePlay().getMyGameBoard();
        GamePlayController opponentGamePlayController = gamePlayController.getGamePlay().getOpponentGamePlayController();
        for (int i = 0; i < 6; i++) {
            Place place = opponentGamePlayController.getGamePlay().getMyGameBoard().getPlace(i, PLACE_NAME.HAND);
            if (place.getCard() != null) {
                if (place.getCard().getName().equals(name)){
//                    opponentGameBoard.getGraveyard().add(place.getCard());
//                    place.setCard(null);
                    opponentGamePlayController.killCard(place);
                    notFound = false;
                }
            }
        }
        return notFound;
    }

    private void removeARandomCardFromMyHand(){
        int toRemove;
        Place place;
        Random random = new Random();
        do {
            toRemove = random.nextInt(7);
            place = gamePlayController.getGamePlay().getMyGameBoard().getPlace(toRemove, PLACE_NAME.HAND);
        } while (place == null);
//        gamePlayController.getGamePlay().getMyGameBoard().getGraveyard().add(
//                gamePlayController.getGamePlay().getMyGameBoard().getPlace(toRemove, PLACE_NAME.HAND).getCard()
//        );
//        gamePlayController.getGamePlay().getMyGameBoard().getPlace(toRemove, PLACE_NAME.HAND).setCard(null);
        gamePlayController.killCard(place);
    }

    public void enemyCannotDraw(){ //TODO ++
        gamePlayController.getGamePlay().getUniversalHistory().add("cannotDraw");
    }

    public void spawnMonsterFromGraveYard(){
//        if (gamePlayController.getGamePlay().getMyGameBoard().getGraveyard().isEmpty()){
//            printerAndScanner.printNextLine(yourGraveYardIsEmpty);
//            gamePlayController.getGamePlay().getHistory().get(place).add("dontDestroy");
//        } else {
//            boolean thereIsMonster = false;
            ArrayList<Cards> graveYard = gamePlayController.getGamePlay().getMyGameBoard().getGraveyard();
//            for (Cards card : graveYard) {
//                if (card instanceof MonsterCards){
//                    thereIsMonster = true;
//                    break;
//                }
//            }
//            if (thereIsMonster){
//                printerAndScanner.printNextLine(askForName);
//                Cards monsterToSummon = Cards.getCard(printerAndScanner.scanNextLine());
//                while (!graveYard.contains(monsterToSummon) || !(monsterToSummon instanceof MonsterCards)){
//                    printerAndScanner.printNextLine(wrongCard);
//                    monsterToSummon = Cards.getCard(printerAndScanner.scanNextLine());
//                }
                Cards monsterToSummon;
                printerAndScanner.printNextLine(askForName);
                while (true){
                    monsterToSummon = Cards.getCard(printerAndScanner.scanNextLine());
                    if (monsterToSummon != null)
                        if (graveYard.contains(monsterToSummon) && monsterToSummon instanceof MonsterCards &&
                                ((MonsterCards) monsterToSummon).getLevel() >= amount)
                            break;
                    printerAndScanner.printNextLine(wrongCard);
                }
                graveYard.remove(monsterToSummon);
                printerAndScanner.printNextLine(askStatus);
                String status = printerAndScanner.scanNextLine();
                while (!status.equals("attack") && !status.equals("defense")){
                    printerAndScanner.printNextLine(wrongStatus);
                    status = printerAndScanner.scanNextLine();
                }
                Place toPlaceTo = gamePlayController.getGamePlay().getMyGameBoard().getPlace(
                    gamePlayController.getGamePlay().getMyGameBoard().getFirstEmptyPlace(PLACE_NAME.MONSTER),
                    PLACE_NAME.MONSTER);
                toPlaceTo.setCard(monsterToSummon);
                toPlaceTo.setStatus(STATUS.getStatusByString(status));
//            } else {
//                printerAndScanner.printNextLine(noMonsterInGraveYard);
//                gamePlayController.getGamePlay().getHistory().get(place).add("dontDestroy");
//            }
//        }
    }
}
