package controller;

import model.User;
import model.cards.Cards;
import model.game.GameBoard;
import model.game.GamePlay;
import model.tools.RegexPatterns;
import model.tools.StringMessages;
import view.PrinterAndScanner;

import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;

public class NewDuelController implements RegexPatterns, StringMessages {

    private static final PrinterAndScanner PRINTER_AND_SCANNER;
    private static final PrintBuilderController PRINT_BUILDER_CONTROLLER;
    private static final Random RANDOM;

    static {
        PRINTER_AND_SCANNER = PrinterAndScanner.getInstance();
        PRINT_BUILDER_CONTROLLER = PrintBuilderController.getInstance();
        RANDOM = new Random();
    }

    private User host, guest;
    private int rounds;
    private GameBoard hostGameBoard, guestGameBoard;
    private GamePlay hostGamePlay, guestGamePlay;
    private GamePlayController hostGamePlayController, guestGamePlayController;

    //TODO handle ai
    public NewDuelController(User host){
        this.host = host;
        run();
    }

    private void run(){
        String command;
        Matcher matcher;
        while (true){
            command = PRINTER_AND_SCANNER.scanNextLine();
            matcher = newDuelPattern.matcher(command);
            if (command.equals("menu exit") || matcher.find())
                break;
            else System.out.println(invalidCommand);
        }
        if (!command.equals("menu exit"))
            if (checkBeforeStartingANewGame(matcher)) {
                makeNeededObjects();
                startTheGame(Integer.parseInt(matcher.group("rounds")));
            }
    }

    private boolean checkBeforeStartingANewGame(Matcher matcher){
        this.guest = LoginController.getUserByUsername(matcher.group("secondPlayer"));
        if (guest != null){
            if (host.getActiveDeck() != null){
                if (guest.getActiveDeck() != null){
                    rounds = Integer.parseInt(matcher.group("rounds"));
                    if (rounds == 1 || rounds == 3)
                        return true;
                    else PRINTER_AND_SCANNER.printNextLine(numberOfRoundsIsNotSupported);
                } else PRINTER_AND_SCANNER.printString(PRINT_BUILDER_CONTROLLER.userDoesntHaveActiveDeck(guest.getUsername()));
            } else PRINTER_AND_SCANNER.printString(PRINT_BUILDER_CONTROLLER.userDoesntHaveActiveDeck(host.getUsername()));
        } else PRINTER_AND_SCANNER.printNextLine(thereIsNoPlayerWithThisUsername);
        return false;
    }

    private void makeNeededObjects(){
        hostGameBoard = makeCards(host);
        guestGameBoard = makeCards(guest);
//        guestMainCards = new ArrayList<>();
//        for (String card : guest.getActiveDeck().getAllMainCards()) {
//            guestMainCards.add(Cards.getCard(card));
//        }
//        guestSideCards = new ArrayList<>();
//        for (String card : guest.getActiveDeck().getAllSideCards()) {
//            guestSideCards.add(Cards.getCard(card));
//        }
//        guestGameBoard = new GameBoard(guestMainCards, guestSideCards);
        hostGamePlay = new GamePlay(true, hostGameBoard, false, host.getUsername());
        guestGamePlay = new GamePlay(false, guestGameBoard, false, guest.getUsername());
        hostGamePlayController = new GamePlayController(hostGamePlay);
        guestGamePlayController = new GamePlayController(guestGamePlay);
        hostGamePlay.setOpponentGamePlayController(guestGamePlayController);
        guestGamePlay.setOpponentGamePlayController(hostGamePlayController);
        hostGamePlayController.shuffleDeck();
        guestGamePlayController.shuffleDeck();
        //TODO remove next
        guestGameBoard.getGraveyard().add(Cards.getCard("Suijin"));
        hostGameBoard.getGraveyard().add(Cards.getCard("Suijin"));
    }

    private GameBoard makeCards(User user) {
        ArrayList<Cards> hostSideCards;
        ArrayList<Cards> hostMainCards;
        hostMainCards = new ArrayList<>();
        for (String card : user.getActiveDeck().getAllMainCards()) {
            hostMainCards.add(Cards.getCard(card));
        }
//        mainCards.remove(Cards.getCard("Raigeki"));
//        mainCards.add(Cards.getCard("Raigeki"));
        hostSideCards = new ArrayList<>();
        for (String card : user.getActiveDeck().getAllSideCards()) {
            hostSideCards.add(Cards.getCard(card));
        }
//        sideCards.remove(Cards.getCard("Raigeki"));
//        sideCards.add(Cards.getCard("Raigeki"));
        return new GameBoard(hostMainCards, hostSideCards);
    }

    private void startTheGame(int rounds){
        GamePlayController currentPlayer = flipACoin();
        //TODO delete the next loop
        for (int i = 0; i < 5; i++) {
            hostGamePlayController.drawCard();
            guestGamePlayController.drawCard();
        }
        while (rounds > 0){
            do {
                PRINTER_AND_SCANNER.printString(PRINT_BUILDER_CONTROLLER.playerTurn(currentPlayer.getGamePlay().getName()));
                PRINTER_AND_SCANNER.printString(PRINT_BUILDER_CONTROLLER.buildGameBoard(
                        currentPlayer.getGamePlay().getMyGameBoard(),
                        currentPlayer.getGamePlay().getOpponentGamePlayController().getGamePlay().getMyGameBoard(),
                        currentPlayer.getGamePlay().getName(),
                        currentPlayer.getGamePlay().getOpponentGamePlayController().getGamePlay().getName()
                ));
                currentPlayer.run();
                currentPlayer = currentPlayer == hostGamePlayController ? guestGamePlayController : hostGamePlayController;
            }
            while (hostGameBoard.getHealth() > 0 && guestGameBoard.getHealth() > 0);
            rounds--;
            if (rounds > 0){
                hostGameBoard.setHealth(8000);
                guestGameBoard.setHealth(8000);
            }
        }
    }

    private GamePlayController flipACoin(){
        hostGamePlayController.getGamePlay().getUniversalHistory().add("starter");
        return hostGamePlayController;
//        if (RANDOM.nextBoolean()){
//            PRINTER_AND_SCANNER.printString(PRINT_BUILDER_CONTROLLER.thisPlayerWillStartTheGame(host.getUsername()));
//            hostGamePlayController.getGamePlay().getUniversalHistory().add("starter");
//            return hostGamePlayController;
//        } else {
//            PRINTER_AND_SCANNER.printString(PRINT_BUILDER_CONTROLLER.thisPlayerWillStartTheGame(guest.getUsername()));
//            guestGamePlayController.getGamePlay().getUniversalHistory().add("starter");
//            return guestGamePlayController;
//        }
    }
}
