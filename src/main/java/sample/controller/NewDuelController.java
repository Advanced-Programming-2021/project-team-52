package sample.controller;

import javafx.stage.Stage;
import sample.model.User;
import sample.model.cards.Cards;
import sample.model.game.GameBoard;
import sample.model.game.GamePlay;
import sample.model.game.PLACE_NAME;
import sample.model.tools.RegexPatterns;
import sample.model.tools.StringMessages;
import sample.view.PrinterAndScanner;
import sample.view.UserKeeper;
import sample.view.gameboardview.Communicator;
import sample.view.gameboardview.GameBoardView;
import sample.view.loginAndIntro.LoginView;

import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;

public class NewDuelController implements RegexPatterns, StringMessages {

    private static final PrinterAndScanner PRINTER_AND_SCANNER;
    private static final PrintBuilderController PRINT_BUILDER_CONTROLLER;
    private static final Random RANDOM;
    public static GameBoardView hostGameBoardView, guestGameBoardView;

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
    private Communicator hostCommunicator, guestCommunicator;
    private Stage stage, stage1;

    public NewDuelController(User host, Stage stage) {
        this.host = host;
        this.stage = stage;
    }

    public String run(String command) {
        Matcher matcher;
        String result = "";
        matcher = RegexController.getMatcher(command, RegexPatterns.newDuelPattern);
        if (matcher == null)
        result = invalidCommand;
        else {
            result = checkBeforeStartingANewGame(matcher);
            if (result.isEmpty()) {
                makeNeededObjects();
                int rounds = Integer.parseInt(matcher.group("rounds"));
                Thread thread = new Thread(() -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                    hostGamePlayController.shuffleDeck();
                    guestGamePlayController.shuffleDeck();
                    startTheGame(rounds);
                });
                thread.setDaemon(true);
                thread.start();
            }
        }
        return result;
    }

    private String checkBeforeStartingANewGame(Matcher matcher) {
        this.guest = LoginController.getUserByUsername(matcher.group("secondPlayer"));
        if (guest != null) {
            if (guest != UserKeeper.getInstance().getCurrentUser()) {
                if (host.getActiveDeck() != null) {
                    if (guest.getActiveDeck() != null) {
                        rounds = Integer.parseInt(matcher.group("rounds"));
                        if (rounds == 1 || rounds == 3)
                            return "";
                        else return numberOfRoundsIsNotSupported;
                    } else return PRINT_BUILDER_CONTROLLER.userDoesntHaveActiveDeck(guest.getUsername()).toString();
                } else return PRINT_BUILDER_CONTROLLER.userDoesntHaveActiveDeck(host.getUsername()).toString();
            } else return youCantDuelWithYourself;
        } else return thereIsNoPlayerWithThisUsername;
    }

    private void makeNeededObjects() {
        hostGameBoard = makeCards(host);
        guestGameBoard = makeCards(guest);
        hostGamePlay = new GamePlay(true, hostGameBoard, false, host.getUsername());
        guestGamePlay = new GamePlay(false, guestGameBoard, false, guest.getUsername());
        GameBoardView hostGameBoardView = new GameBoardView(stage,
                host.getNickname(), host.getImageAddress(), guest.getNickname(), guest.getImageAddress());
//        hostGameBoardView.initialize();
        stage1 = new Stage();
        stage1.getIcons().add(LoginView.getWindowIcon());
        stage1.setTitle("Yu Gi Oh");
        GameBoardView guestGameBoardView = new GameBoardView(stage1,
                guest.getNickname(), guest.getImageAddress(), host.getNickname(), host.getImageAddress());
        stage1.show();
//        guestGameBoardView.initialize();
        hostCommunicator = hostGameBoardView.getCommunicator();
        guestCommunicator = guestGameBoardView.getCommunicator();
        hostGameBoardView.setOpponentCommunicator(guestCommunicator);
        guestGameBoardView.setOpponentCommunicator(hostCommunicator);
        hostGamePlayController = new GamePlayController(hostGamePlay, hostGameBoardView.getCommunicator(), guestGameBoardView.getCommunicator());
        guestGamePlayController = new GamePlayController(guestGamePlay, guestGameBoardView.getCommunicator(), hostGameBoardView.getCommunicator());
        hostGameBoardView.setGamePlayController(hostGamePlayController);
        guestGameBoardView.setGamePlayController(guestGamePlayController);
        hostGamePlay.setOpponentGamePlayController(guestGamePlayController);
        guestGamePlay.setOpponentGamePlayController(hostGamePlayController);
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

        int roundsStatic = rounds;
        int maxHostLP, maxGuestLP, roundsWonCounter;
        maxHostLP = maxGuestLP = roundsWonCounter = 0;
        while (rounds > 0) {
            for (int i = 0; i < 5; i++) {
                hostGamePlayController.drawCard();
                guestGamePlayController.drawCard();
            }
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
        hostCommunicator.reset();
        guestCommunicator.reset();
        resetGame(hostGameBoard, hostGamePlay, hostGamePlayController);
        resetGame(guestGameBoard, guestGamePlay, guestGamePlayController);
        swapCards(host.getUsername(), hostGameBoard.getMainCards(), hostGameBoard.getSideCards(),
                hostCommunicator, hostGamePlayController);
        swapCards(guest.getUsername(), guestGameBoard.getMainCards(), guestGameBoard.getSideCards(),
                guestCommunicator, guestGamePlayController);
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
        winner.addToNumberOfRoundsWon(1);
        loser.addToNumberOfRoundsLost(1);
        winner.setScore(winner.getScore() + 1000 * multiplier);
        PRINTER_AND_SCANNER.printString(PRINT_BUILDER_CONTROLLER.showEndRoundOrGameMessage(winnerName, score, theWholeMatch));
        String message = PRINT_BUILDER_CONTROLLER.showEndRoundOrGameMessage(winnerName, score, theWholeMatch).toString();
        hostCommunicator.askOptions(message, "exit");
        guestCommunicator.askOptions(message, "exit");
        hostGamePlayController.takeCommand();
        guestGamePlayController.takeCommand();
        if (theWholeMatch) {
            hostCommunicator.shutdown(stage, stage1, true);
            guestCommunicator.shutdown(stage, stage1, false);
            winner.addToNumberOfGamesWon(1);
            loser.addToNumberOfGamesLost(1);
        } else {
            hostCommunicator.reset();
            guestCommunicator.reset();
        }
    }

    private GamePlayController flipACoin() {
        GamePlayController starter;
        if (RANDOM.nextBoolean()) {
            PRINTER_AND_SCANNER.printString(PRINT_BUILDER_CONTROLLER.thisPlayerWillStartTheGame(host.getUsername()));
            hostGamePlayController.getGamePlay().getUniversalHistory().add("starter");
            starter = hostGamePlayController;
        } else {
            PRINTER_AND_SCANNER.printString(PRINT_BUILDER_CONTROLLER.thisPlayerWillStartTheGame(guest.getUsername()));
            guestGamePlayController.getGamePlay().getUniversalHistory().add("starter");
            starter = guestGamePlayController;
        }
        hostGamePlayController.getMyCommunicator().flipCoin(starter == hostGamePlayController ? 1 : 11);
        guestGamePlayController.getMyCommunicator().flipCoin(starter == guestGamePlayController ? 11 : 1);
        hostGamePlayController.takeCommand();
        guestGamePlayController.takeCommand();
        hostGamePlayController.getMyCommunicator().askOptions(starter == hostGamePlayController ?
                "you will go first" : "your opponent will go first", "ok");
        hostGamePlayController.getMyCommunicator().removeCoin();
        guestGamePlayController.getMyCommunicator().askOptions(starter == guestGamePlayController ?
                "you will go first" : "your opponent will go first", "ok");
        guestGamePlayController.getMyCommunicator().removeCoin();
        hostGamePlayController.takeCommand();
        guestGamePlayController.takeCommand();
        return starter;
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

    private void swapCards(String name, ArrayList<Cards> mainCards, ArrayList<Cards> sideCards,
                           Communicator communicator, GamePlayController gamePlayController) {
        communicator.askOptions(PRINT_BUILDER_CONTROLLER.askSwapCards(name), "yes", "no");
        if (gamePlayController.takeCommand().equals("yes")) {
            Cards fromMainDeck, fromSideDeck;
            Matcher matcher;
            String command;
            while (true){
                communicator.askOptions(swapFormat, "ok");
                gamePlayController.takeCommand();
                communicator.mindCrush();
                command = gamePlayController.takeCommand();
                if (command.equals("cancel"))
                    break;
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
                            communicator.askOptions(swappedSuccessfully, "ok");
                            gamePlayController.takeCommand();
                        }
                    communicator.askOptions(wrongCardForSwap, "ok");
                    gamePlayController.takeCommand();
                } else {
                    communicator.askOptions(invalidCommand, "ok");
                    gamePlayController.takeCommand();
                }
            }
        }
    }
}
