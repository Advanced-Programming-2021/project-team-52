package controller;

import java.util.ArrayList;

public class GamePlayController {
    private GamePlay gamePlay;
    private static printerAndScanner printerAndScanner;
    private GameBoard board;

    public GamePlayController(GamePlay gameplay, Gameboard board){}
    public void run(GamePlay gamePlay, GameBoard board){}
    private void drawPhase(){}
    private void standByPhase(){}
    private void mainPhase(){}
    private void battlePhase(){}
    private void endPhase(){}
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

