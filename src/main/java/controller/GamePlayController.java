package controller;

import java.util.ArrayList;

import model.cards.Cards;
import model.game.GameBoard;
import model.game.GamePlay;
import view.PrinterAndScanner;
import model.tools.StringMessages;

public class GamePlayController {

    private static PrinterAndScanner printerAndScanner;
    private static PrintBuilderController printBuilderController;
    private static boolean firstTurn;

    private GamePlay gamePlay;
    private GameBoard gameBoard;
    private NewGame newGame;
    private boolean isHost;
    private String opponentName;

    static {
        printerAndScanner = PrinterAndScanner.getInstance();
        printBuilderController = PrintBuilderController.getInstance();
    }

    public GamePlayController(GamePlay gameplay, GameBoard gameBoard, NewGame newGame, boolean isHost, String opponentName){
        this.gameBoard = gameBoard;
        this.gamePlay = gameplay;
        this.newGame = newGame;
        this.firstTurn = true;
        this.isHost = isHost;
        this.opponentName = opponentName;
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
        printerAndScanner.printNextLine(StringMessages.drawPhase);
        String drawCard = gameBoard.drawCard(firstTurn);
        if (drawCard == null){
            newGame.changeHealthAmount(-8000, isHost);
            return false;
        } else if (!drawCard.equals("firstTurn"))
            printerAndScanner.printNextLine("new card added to the hand: " + drawCard);
        //TODO special abilities may be triggered
        return true;
    }
    private boolean standByPhase(){
        printerAndScanner.printNextLine(StringMessages.standbyPhase);
        //TODO special abilities may be triggered
        return true;
    }
    private boolean mainPhase(){
        printerAndScanner.printNextLine(StringMessages.mainPhase);
        printerAndScanner.printNextLine(gameBoard.toString());
        for (String command = printerAndScanner.scanNextLine();
             !command.equals("end"); //TODO may change
             command = printerAndScanner.scanNextLine()){
            if (command.startsWith("select"))
                selectCard(command);
            else if (command.equals("summon"))
                summon();
            else if (command.equals("set"))
                set();
            else if (command.startsWith("set --position"))
                changePosition(command);
            else if (command.equals("flip-summon"))
                flipSummon();
            else if (command.equals("activate effect"))
                activateEffectOrSetting();
            else if (command.equals("show graveyard"))
                printBuilderController.buildGraveyard(gameBoard.getGraveyardCards());
            else if (command.matches("card show (?:--selected|-s)"))
                showSelectedCard();
            else printerAndScanner.printNextLine(StringMessages.invalidCommand);
        }
    }
    private void battlePhase(){
        for (String command = printerAndScanner.scanNextLine();
             !command.equals("end"); //TODO may change
             command = printerAndScanner.scanNextLine()) {
            printerAndScanner.printNextLine(StringMessages.battlePhase);
            if (command.startsWith("attack")) {
                String info = RegexController.getAttackInfo(command);
                if (info.equals("direct"))
                    attackDirectly();
                else if (info.matches("^\\d+$"))
                    attackMonster(Integer.parseInt(info));
                else printerAndScanner.printNextLine(StringMessages.invalidCommand);
            }
        }
    }
    private void endPhase(){
        printerAndScanner.printNextLine(StringMessages.endPhase);
        printerAndScanner.printString(new StringBuilder("its ").append(opponentName).append("'s turn\n"));
    }
    private void showSelectedCard(){
        ArrayList<String> selectedCards = GamePlay.getSelectedCards();
        if (selectedCards.isEmpty())
            printerAndScanner.printNextLine(StringMessages.noCardsIsSelectedYet);
        else for (String selectedCard : selectedCards) {
            //TODO if opponent card is selected and is not visible "card is not visible" message should be shown
            printerAndScanner.printNextLine(Cards.getCard(selectedCard).toString());
        }
    }
    public void selectCard(String command){}
    
    public void summon(String name){}
    public void set(String name){}
    public boolean getTribute(int amount){}
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
    public void showGraveyard(){}
    public void increaseLpWithCheat(int amount){}
}

