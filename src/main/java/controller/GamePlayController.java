package controller;

import java.util.ArrayList;
import java.util.regex.Matcher;

import model.cards.Cards;
import model.cards.monster.MonsterCards;
import model.game.GameBoard;
import model.game.GamePlay;
import model.game.PlaceName;
import model.tools.RegexPatterns;
import view.PrinterAndScanner;
import model.tools.StringMessages;

public class GamePlayController implements RegexPatterns,StringMessages {

    private static PrinterAndScanner printerAndScanner;
    private static PrintBuilderController printBuilderController;
    private static boolean firstTurn;

    private GamePlay gamePlay;
    private GameBoard gameBoard;
    private GameBoard opponentGameBoard;
    private NewGame newGame;
    private boolean isHost;
    private String opponentName;

    static {
        printerAndScanner = PrinterAndScanner.getInstance();
        printBuilderController = PrintBuilderController.getInstance();
    }

    public GamePlayController(GamePlay gameplay, GameBoard gameBoard,GameBoard opponentGameBoard, NewGame newGame, boolean isHost, String opponentName){
        this.gameBoard = gameBoard;
        this.gamePlay = gameplay;
        this.newGame = newGame;
        this.firstTurn = true;
        this.isHost = isHost;
        this.opponentName = opponentName;
        this.opponentGameBoard = opponentGameBoard;
    }
    public void run(){
        if (drawPhase())
            if (standByPhase())
                if (mainPhase()) {
                    battlePhase();
                    mainPhase();
                }
        endPhase();
    }
    private boolean drawPhase(){
        printerAndScanner.printNextLine(drawPhase);
        if (!firstTurn){
            Cards drawCard = gameBoard.drawCard();
            if (drawCard == null){
                newGame.changeHealthAmount(-8000, isHost);
                return false;
            } else {
                printerAndScanner.printNextLine("new card added to the hand: " + drawCard);
                //TODO special abilities may be triggered
                return true;
            }
        } else
            return true;
    }
    private boolean standByPhase(){
        printerAndScanner.printNextLine(standbyPhase);
        //TODO special abilities may be triggered
        return true;
    }
    private boolean mainPhase(){
        printerAndScanner.printNextLine(mainPhase);
        printerAndScanner.printNextLine(gameBoard.toString());
        for (String command = printerAndScanner.scanNextLine();
             !command.equals("next phase"); //TODO may change
             command = printerAndScanner.scanNextLine()){
            if (command.startsWith("select"))
                selectCard(command);
            else if (command.equals("summon"))
                summon();
            else if (command.startsWith("set")){
                Matcher matcher = RegexController.getMatcher(command, setAttackOrDefensePattern);
                if (matcher != null)
                    set(matcher.group("position"));
                    printerAndScanner.printNextLine(invalidCommand);
            }
            else if (command.startsWith("set --position")) //TODO ???????
                changePosition(command);
            else if (command.equals("flip-summon"))
                flipSummon();
            else if (command.equals("activate effect"))
                activateEffectOrSetting();
            else if (command.equals("show graveyard"))
                printBuilderController.buildGraveyard(gameBoard.getGraveyard());
            else if (command.startsWith("card show")){
                Matcher matcher = RegexController.getMatcher(command, cardShowPattern);
                if (matcher.find()){
                    if (!RegexController.hasField(matcher, "card"))
                        showSelectedCard();
                }
            } else commandInWrongPhase(command, true);

        }
    }
    private void battlePhase(){
        for (String command = printerAndScanner.scanNextLine();
             !command.equals("next phase"); //TODO may change
             command = printerAndScanner.scanNextLine()) {
            printerAndScanner.printNextLine(battlePhase);
            if (command.startsWith("attack")) {
                Matcher matcher = RegexController.getMatcher(command, attackPattern);
                if (matcher.group("type").equals("direct"))
                    attackDirectly();
                else if (matcher.group("type").matches("^\\d+$"))
                    attackMonster(Integer.parseInt(matcher.group("type")));
                else printerAndScanner.printNextLine(invalidCommand);
            } else commandInWrongPhase(command, false)
        }
    }
    private void endPhase(){
        printerAndScanner.printNextLine(endPhase);
        printerAndScanner.printString(new StringBuilder("its ").append(opponentName).append("'s turn\n"));
    }
    private void showSelectedCard(){
        Cards selectedCard = gamePlay.getSelectedCard();
        if (selectedCard == null)
            printerAndScanner.printNextLine(noCardsIsSelectedYet);
            //TODO if opponent card is selected and is not visible "card is not visible" message should be shown
            printerAndScanner.printNextLine(selectedCard.toString());
    }
    public void selectCard(String command){
        Matcher matcher = selectCardPattern.matcher(command);
        if (matcher.find()){
            if (RegexController.hasField(matcher, "delete"))
                gamePlay.setSelectedCard(null);
            else {
                PlaceName name = PlaceName.HAND;
                if (RegexController.hasField(matcher, "typeHand"))
                    name = PlaceName.MONSTER;
                else {
                    switch (matcher.group("type")) {
                        case "m":
                        case "monster":
                            name = PlaceName.MONSTER;
                            break;
                        case "s":
                        case "spell":
                            name = PlaceName.SPELL_AND_TRAP;
                            break;
                        case "f":
                        case "field":
                            name = PlaceName.FUSION;
                            break;
                    }
                }
                if (RegexController.hasField(matcher, "opponent") || RegexController.hasField(matcher, "opponent2"))
                    gamePlay.setSelectedCard(opponentGameBoard.getCard(name, Integer.parseInt(matcher.group("select"))));
                else gamePlay.setSelectedCard(gameBoard.getCard(name, Integer.parseInt(matcher.group("select"))));
            }
        } else printerAndScanner.printNextLine(invalidCommand);
    }

    private void commandInWrongPhase(String command, boolean isMain){
        if (isMain ){
            if (RegexController.getMatcher(command, attackPattern) != null)
                printerAndScanner.printNextLine(invalidPhase);
            else printerAndScanner.printNextLine(invalidCommand);
        } else {
            if (!command.equals("summon"))
                if (!command.equals("flip-summon"))
                    if (!command.equals("activate effect"))
                        if (!command.equals("show graveyard"))
                            if (RegexController.getMatcher(command, cardShowPattern) == null)
                                if (RegexController.getMatcher(command, RegexPatterns.selectCardPattern) == null)
                                    printerAndScanner.printNextLine(invalidCommand);
        }
        printerAndScanner.printNextLine(invalidPhase);
    }

    public void summon() {
        Cards card = gamePlay.getSelectedCard();
        if (handleSummonAndSetErrors(card)) return;
        MonsterCards monsterCard = (MonsterCards) card;
        boolean isTributeDone = false;
        if (monsterCard.getLevel() <= 4)
            isTributeDone = true;
        else if (monsterCard.getLevel() <= 6)
            isTributeDone = getTribute(1);
        else
            isTributeDone = getTribute(2);

        if (isTributeDone) {
            int emptyPlace = gameBoard.getFirstEmptyPlace(PlaceName.MONSTER);
            gameBoard.addCard(monsterCard, emptyPlace, PlaceName.MONSTER);
            // todo : add status to addCard func in GameBoard
            // todo : make enum for cardStatus if it needs
            gamePlay.setAlreadySummonedOrSet(true);
            printerAndScanner.printNextLine(StringMessages.summonedSuccessfully);
        }
    }

    public void set() {
        Cards card = gamePlay.getSelectedCard();
        if (handleSummonAndSetErrors(card)) return;

        MonsterCards monsterCard = (MonsterCards) card;
        int emptyPlace = gameBoard.getFirstEmptyPlace(PlaceName.MONSTER);
        gameBoard.addCard(monsterCard, emptyPlace, PlaceName.MONSTER);
        // todo : add status to addCard func in GameBoard
        // todo : make enum for cardStatus if it needs
        gamePlay.setAlreadySummonedOrSet(true);
        printerAndScanner.printNextLine(StringMessages.setSuccessfully);
    }

    private boolean handleSummonAndSetErrors(Cards card) {
        if (card == null) {
            printerAndScanner.printNextLine(StringMessages.noCardsIsSelectedYet);
            return true;
        }
        if (!gameBoard.isThisCardExistsInThisPlace(card, PlaceName.HAND) || !(card instanceof MonsterCards)) { // add "aya summon addi mishe?"
            printerAndScanner.printNextLine(StringMessages.cantSummonThisCard);
            return true;
        }
        // if(can't do in this phase){
        // printerAndScanner.printNextLine(StringMessages.actionNotAllowedInThisPhase);
        // return;
        // }
        if (gameBoard.getNumberOfCardsInThisPlace(PlaceName.MONSTER) >= 5) {
            printerAndScanner.printNextLine(StringMessages.monsterCardZoneIsFull);
            return true;
        }
        if (gamePlay.getAlreadySummonedOrSet()) {
            printerAndScanner.printNextLine(StringMessages.alreadySummonedORSetOnThisTurn);
            return true;
        }
        return false;
    }

    public boolean getTribute(int amount) {
        int NumberOfCardInMonsterZone = gameBoard.getNumberOfCardsInThisPlace(PlaceName.MONSTER);
        if (NumberOfCardInMonsterZone < amount) {
            printerAndScanner.printNextLine(StringMessages.thereAreNotEnoughCardsForTribute);
            return false;
        }

        ArrayList<Integer> cardAddresses = new ArrayList<>();
        ArrayList<Cards> cardsToRemove = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            String response = printerAndScanner.scanNextLine();
            if (response.matches("^\\d$"))
                cardAddresses.add(Integer.valueOf(response));
            else
                printerAndScanner.printNextLine(StringMessages.invalidCommand);
        }

        for (int cardAddress : cardAddresses) {
            Cards cardToRemove = gameBoard.getCardByAddressAndPlace(cardAddress, PlaceName.MONSTER);
            if (cardToRemove == null) {
                printerAndScanner.printNextLine(StringMessages.thereNoMonstersOneThisAddress);
                return false;
            } else
                cardsToRemove.add(cardToRemove);
        }

        for (int i = 0; i < cardAddresses.size(); i++) {
            gameBoard.removeCard(cardsToRemove.get(i), cardAddresses.get(i), PlaceName.MONSTER);
        }
        return true;
    }
    public void changePosition(String position){}
    public void flipSummon(String name){}
    public void attackMonster(int place){}
    public boolean checkBeforeAttacking(int place){}
    public void attackDirectly(){}

    public void activateEffectOrSetting(boolean isSet){}
    private boolean checkBeforeActivingOrSetting(boolean isSet){}
    private boolean doChain(){}
    private boolean chainCanBeDone(){}
    private void runChain(ArrayList<String> chain){}
    public void ritualChain(){}
    public void showGraveyard(){
        for (int i = 0; i < board.getGraveYardCards().size(); i++) {
            System.out.println(board.getGraveYardCards() + ":" + board.getGraveYardCards().get(i));
        }
    }
    public void increaseLpWithCheat(int amount){
        int currentHealth = gamePlay.getHealth();
        gamePlay.setHealth(currentHealth + amount);
    }
}

