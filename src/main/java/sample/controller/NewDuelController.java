package sample.controller;

import sample.model.User;
import sample.model.cards.Cards;
import sample.model.game.GameBoard;
import sample.model.game.GamePlay;
import sample.model.game.PLACE_NAME;
import sample.model.tools.RegexPatterns;
import sample.model.tools.StringMessages;
import sample.view.PrinterAndScanner;

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

    public NewDuelController(User host) {
        this.host = host;
        run();
    }

    private void run() {
        String command;
        Matcher matcher;
        while (true) {
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

    private boolean checkBeforeStartingANewGame(Matcher matcher) {
        this.guest = LoginController.getUserByUsername(matcher.group("secondPlayer"));
        if (guest != null) {
            if (host.getActiveDeck() != null) {
                if (guest.getActiveDeck() != null) {
                    rounds = Integer.parseInt(matcher.group("rounds"));
                    if (rounds == 1 || rounds == 3)
                        return true;
                    else PRINTER_AND_SCANNER.printNextLine(numberOfRoundsIsNotSupported);
                } else PRINTER_AND_SCANNER.printString(PRINT_BUILDER_CONTROLLER.
                        userDoesntHaveActiveDeck(guest.getUsername()));
            } else PRINTER_AND_SCANNER.printString(PRINT_BUILDER_CONTROLLER.
                    userDoesntHaveActiveDeck(host.getUsername()));
        } else PRINTER_AND_SCANNER.printNextLine(thereIsNoPlayerWithThisUsername);
        return false;
    }

    private void makeNeededObjects() {
        hostGameBoard = makeCards(host);
        guestGameBoard = makeCards(guest);
        hostGamePlay = new GamePlay(true, hostGameBoard, false, host.getUsername());
        guestGamePlay = new GamePlay(false, guestGameBoard, false, guest.getUsername());
        hostGamePlayController = new GamePlayController(hostGamePlay);
        guestGamePlayController = new GamePlayController(guestGamePlay);
        hostGamePlay.setOpponentGamePlayController(guestGamePlayController);
        guestGamePlay.setOpponentGamePlayController(hostGamePlayController);
        hostGamePlayController.shuffleDeck();
        guestGamePlayController.shuffleDeck();
    }

    private GameBoard makeCards(User user) {
        ArrayList<Cards> hostSideCards;
        ArrayList<Cards> hostMainCards;
        hostMainCards = new ArrayList<>();
        for (String card : user.getActiveDeck().getAllMainCards()) {
            hostMainCards.add(Cards.getCard(card));
        }
        hostSideCards = new ArrayList<>();
        for (String card : user.getActiveDeck().getAllSideCards()) {
            hostSideCards.add(Cards.getCard(card));
        }
        return new GameBoard(hostMainCards, hostSideCards);
    }

    private void startTheGame(int rounds) {
        GamePlayController currentPlayer = flipACoin();
        hostGamePlayController.shuffleDeck();
        guestGamePlayController.shuffleDeck();
        for (int i = 0; i < 5; i++) {
            hostGamePlayController.drawCard();
            guestGamePlayController.drawCard();
        }
        int roundsStatic = rounds;
        int maxHostLP, maxGuestLP, roundsWonCounter;
        maxHostLP = maxGuestLP = roundsWonCounter = 0;
        while (rounds > 0) {
            runGame(currentPlayer);
            rounds--;
            if (guestGamePlayController.getSurrendered() || guestGameBoard.getHealth() == 0) {
                roundsWonCounter++;
                if (hostGameBoard.getHealth() > maxHostLP)
                    maxHostLP = hostGameBoard.getHealth();
                calculateScoreAndMoney(0, hostGameBoard.getHealth(), hostGamePlay.getName(), false, host, guest);
            } else {
                roundsWonCounter--;
                if (guestGameBoard.getHealth() > maxGuestLP)
                    maxGuestLP = guestGameBoard.getHealth();
                calculateScoreAndMoney(0, guestGameBoard.getHealth(), guestGamePlay.getName(), false, guest, host);
            }
            if (Math.abs(roundsWonCounter) >= 2 || hostGamePlayController.getSurrendered() || guestGamePlayController.getSurrendered())
                break;
            if (rounds > 0) {
                resetEverything();
            }
        }
        calculateScoreAndMoney(roundsStatic,
                roundsWonCounter > 0 && !hostGamePlayController.getSurrendered() ? maxHostLP : maxGuestLP,
                roundsWonCounter > 0 && !hostGamePlayController.getSurrendered() ? hostGamePlay.getName() : guestGamePlay.getName(),
                true,
                roundsWonCounter > 0 && !hostGamePlayController.getSurrendered() ? host : guest,
                roundsWonCounter > 0 && !hostGamePlayController.getSurrendered() ? guest : host);
    }

    private void resetEverything() {
        resetGame(hostGameBoard, hostGamePlay, hostGamePlayController);
        resetGame(guestGameBoard, guestGamePlay, guestGamePlayController);
        swapCards(host.getUsername(), hostGameBoard.getMainCards(), hostGameBoard.getSideCards());
        swapCards(guest.getUsername(), guestGameBoard.getMainCards(), guestGameBoard.getSideCards());
        hostGamePlayController.shuffleDeck();
        guestGamePlayController.shuffleDeck();
    }

    private void runGame(GamePlayController currentPlayer) {
        do {
            PRINTER_AND_SCANNER.printString(PRINT_BUILDER_CONTROLLER.playerTurn(currentPlayer.getGamePlay().getName()));
            PRINTER_AND_SCANNER.printString(PRINT_BUILDER_CONTROLLER.buildGameBoard(
                    currentPlayer.getGamePlay().getMyGameBoard(),
                    currentPlayer.getGamePlay().getOpponentGamePlayController().getGamePlay().getMyGameBoard(),
                    currentPlayer.getGamePlay().getName(),
                    currentPlayer.getGamePlay().getOpponentGamePlayController().getGamePlay().getName()
            ));
            currentPlayer.run();
            currentPlayer = currentPlayer == hostGamePlayController ?
                    guestGamePlayController : hostGamePlayController;
        }
        while (hostGameBoard.getHealth() > 0 && guestGameBoard.getHealth() > 0 && !hostGamePlayController.getGameOver());
    }

    private void calculateScoreAndMoney(int rounds, int winnerLp, String winnerName, boolean theWholeMatch, User winner, User loser) {
        int multiplier = (rounds == 3 && theWholeMatch ? 3 : 1);
        int score = (1000 + winnerLp) * multiplier;
        winner.changeBalance(score);
        loser.changeBalance(100 * multiplier);
        winner.setScore(winner.getScore() + 1000 * multiplier);
        PRINTER_AND_SCANNER.printString(PRINT_BUILDER_CONTROLLER.showEndRoundOrGameMessage(winnerName, score, theWholeMatch));
    }

    private GamePlayController flipACoin() {
        if (RANDOM.nextBoolean()) {
            PRINTER_AND_SCANNER.printString(PRINT_BUILDER_CONTROLLER.thisPlayerWillStartTheGame(host.getUsername()));
            hostGamePlayController.getGamePlay().getUniversalHistory().add("starter");
            return hostGamePlayController;
        } else {
            PRINTER_AND_SCANNER.printString(PRINT_BUILDER_CONTROLLER.thisPlayerWillStartTheGame(guest.getUsername()));
            guestGamePlayController.getGamePlay().getUniversalHistory().add("starter");
            return guestGamePlayController;
        }
    }

    private void resetGame(GameBoard board, GamePlay gamePlay, GamePlayController gamePlayController) {
        for (int i = 1; i < 5; i++) {
            gamePlay.getHistory().get(board.getPlace(i, PLACE_NAME.MONSTER)).clear();
            board.killCards(board.getPlace(i, PLACE_NAME.MONSTER));
            gamePlay.getHistory().get(board.getPlace(i, PLACE_NAME.SPELL_AND_TRAP)).clear();
            board.killCards(board.getPlace(i, PLACE_NAME.SPELL_AND_TRAP));
            board.killCards(board.getPlace(i, PLACE_NAME.HAND));
        }
        board.killCards(board.getPlace(0, PLACE_NAME.HAND));
        board.clearArrayLists();
        board.setHealth(8000);
        gamePlay.getUniversalHistory().clear();
        gamePlay.setSelectedCard(null);
        gamePlayController.setGameOver(false);
        gamePlayController.setSurrendered(false);
    }

    private void swapCards(String name, ArrayList<Cards> mainCards, ArrayList<Cards> sideCards) {
        PRINTER_AND_SCANNER.printNextLine(PRINT_BUILDER_CONTROLLER.askSwapCards(name));
        if (PRINTER_AND_SCANNER.scanNextLine().equals("yes")) {
            PRINTER_AND_SCANNER.printNextLine(swapFormat);
            Cards fromMainDeck, fromSideDeck;
            Matcher matcher;
            for (String command = PRINTER_AND_SCANNER.scanNextLine();
                 !command.equals("cancel"); command = PRINTER_AND_SCANNER.scanNextLine()) {
                matcher = RegexController.getMatcher(command, RegexPatterns.swapPattern);
                if (matcher != null) {
                    fromMainDeck = Cards.getCard(matcher.group(1));
                    fromSideDeck = Cards.getCard(matcher.group(2));
                    if (fromMainDeck != null && fromSideDeck != null)
                        if (mainCards.contains(fromMainDeck) && sideCards.contains(fromSideDeck)) {
                            mainCards.remove(fromMainDeck);
                            mainCards.add(fromSideDeck);
                            sideCards.remove(fromSideDeck);
                            sideCards.add(fromMainDeck);
                            PRINTER_AND_SCANNER.printNextLine(swappedSuccessfully);
                        }
                    PRINTER_AND_SCANNER.printNextLine(wrongCardForSwap);
                } else PRINTER_AND_SCANNER.printNextLine(invalidCommand);
            }
        }
    }
}
