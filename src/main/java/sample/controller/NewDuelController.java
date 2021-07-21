package sample.controller;

import sample.model.User;
import sample.model.cards.Cards;
import sample.model.game.GameBoard;
import sample.model.game.GamePlay;
import sample.model.game.PLACE_NAME;
import sample.model.tools.RegexPatterns;
import sample.model.tools.StringMessages;
import sample.view.PrinterAndScanner;
import sample.view.UserKeeper;
//import sample.view.gameboardview.GameBoardView;
import sample.view.listener.Communicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.regex.Matcher;

public class NewDuelController implements RegexPatterns, StringMessages {

    private static final PrinterAndScanner PRINTER_AND_SCANNER;
    private static final PrintBuilderController PRINT_BUILDER_CONTROLLER;
    private static final Random RANDOM;
    private static final HashMap<Integer, NewDuelController> ACTIVE_GAMES;
    private static int counter;
//    public static GameBoardView hostGameBoardView, guestGameBoardView;

    static {
        PRINTER_AND_SCANNER = PrinterAndScanner.getInstance();
        PRINT_BUILDER_CONTROLLER = PrintBuilderController.getInstance();
        RANDOM = new Random();
        counter = 0;
        ACTIVE_GAMES = new HashMap<>();
    }

    private User host, guest;
    private int rounds;
    private GameBoard hostGameBoard, guestGameBoard;
    private GamePlay hostGamePlay, guestGamePlay;
    private GamePlayController hostGamePlayController, guestGamePlayController;
    private Communicator hostCommunicator, guestCommunicator;
//    private ArrayList<Communicator> streamedTo;

    public NewDuelController(User host) {
        this.host = host;
//        this.streamedTo = new ArrayList<>();
    }

//    public static String getActiveGames(){
//        StringBuilder result = new StringBuilder();
//        for (Integer integer : ACTIVE_GAMES.keySet()) {
//            result.append(integer).append("\n");
//        }
//        return result.toString();
//    }

//    public static String stream(int id, Communicator communicator){
//        NewDuelController newDuelController = ACTIVE_GAMES.get(id);
//        if (newDuelController == null)
//            return "nothing";
//        else {
//            newDuelController.getStreamedTo().add(communicator);
//            return "stream*" + newDuelController.getHost().getUsername() + "*" + newDuelController.getHost().getImageAddress() + "*" +
//                    newDuelController.getGuest().getUsername() + "*" + newDuelController.getGuest().getImageAddress() + "*" +
//                    "*" + newDuelController.getHostGamePlayController().getSituation() +
//                    "*" + newDuelController.getGuestGamePlayController().getSituation();
//        }
//    }

//    public void sendToViewers(String hostMessage, String guestMessage){
//        hostMessage = "host*" + hostMessage;
//        guestMessage = "guest*" + guestMessage;
//        for (int i = 0; i < streamedTo.size(); i++) {
//            streamedTo.get(i).sendMessage(hostMessage);
//            streamedTo.get(i).sendMessage(guestMessage);
//        }
//    }
//
//    public void sendToViewers(boolean isHost, String message){
//        message = (isHost ? "host" : "guest") + message;
//        for (int i = 0; i < streamedTo.size(); i++) {
//            streamedTo.get(i).sendMessage(message);
//        }
//    }

    public User getHost() {
        return host;
    }

    public User getGuest() {
        return guest;
    }

    public GamePlayController getHostGamePlayController() {
        return hostGamePlayController;
    }

    public GamePlayController getGuestGamePlayController() {
        return guestGamePlayController;
    }

//    public ArrayList<Communicator> getStreamedTo() {
//        return streamedTo;
//    }

    public String run(String username, String rounds, Communicator hostCommunicator, Communicator guestCommunicator) {
        String result = "";
        if (!rounds.matches("^\\d+$"))
        result = invalidCommand;
        else {
            result = checkBeforeStartingANewGame(username, Integer.parseInt(rounds));
            if (result.isEmpty()) {
                this.hostCommunicator = hostCommunicator;
                this.guestCommunicator = guestCommunicator;
                makeNeededObjects();
                int roundsNum = Integer.parseInt(rounds);
                Thread thread = new Thread(() -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                    hostGamePlayController.shuffleDeck();
                    guestGamePlayController.shuffleDeck();
                    ACTIVE_GAMES.put(counter++, this);
                    startTheGame(roundsNum);
                });
                thread.setDaemon(true);
                thread.start();
            }
        }
        return result;
    }

    private String checkBeforeStartingANewGame(String username, int rounds) {
        this.guest = LoginController.getUserByUsername(username);
        if (guest != null) {
            if (guest != UserKeeper.getInstance().getCurrentUser()) {
                if (host.getActiveDeck() != null) {
                    if (guest.getActiveDeck() != null) {
                        if (rounds == 1 || rounds == 3) {
                            this.rounds = rounds;
                            return "";
                        } else return numberOfRoundsIsNotSupported;
                    } else return PRINT_BUILDER_CONTROLLER.userDoesntHaveActiveDeck(guest.getUsername()).toString();
                } else return PRINT_BUILDER_CONTROLLER.userDoesntHaveActiveDeck(host.getUsername()).toString();
            } else return youCantDuelWithYourself;
        } else return thereIsNoPlayerWithThisUsername;
    }

    private void makeNeededObjects() {
        host.getActionFinder().getCommunicator().resetArr();
        guest.getActionFinder().getCommunicator().resetArr();
        hostGameBoard = makeCards(host);
        guestGameBoard = makeCards(guest);
        hostGamePlay = new GamePlay(true, hostGameBoard, false, host.getUsername());
        guestGamePlay = new GamePlay(false, guestGameBoard, false, guest.getUsername());
//        GameBoardView hostGameBoardView = new GameBoardView(stage);
////        hostGameBoardView.initialize();
//        stage1 = new Stage();
//        GameBoardView guestGameBoardView = new GameBoardView(stage1);
//        stage1.show();
////        guestGameBoardView.initialize();
//        hostCommunicator = hostGameBoardView.getCommunicator();
//        guestCommunicator = guestGameBoardView.getCommunicator();
//        hostGameBoardView.setOpponentCommunicator(guestCommunicator);
//        guestGameBoardView.setOpponentCommunicator(hostCommunicator);
        hostGamePlayController = new GamePlayController(hostGamePlay, hostCommunicator, guestCommunicator/*, this*/);
        guestGamePlayController = new GamePlayController(guestGamePlay, guestCommunicator, hostCommunicator/*, this*/);
//        hostGameBoardView.setGamePlayController(hostGamePlayController);
//        guestGameBoardView.setGamePlayController(guestGamePlayController);
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
        hostCommunicator.sendMessage("reset");
        guestCommunicator.sendMessage("reset");
//        sendToViewers("reset", "reset");
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
        hostCommunicator.sendMessage(Communicator.askOption(message, "exit"));
        guestCommunicator.sendMessage(Communicator.askOption(message, "exit"));
        hostGamePlayController.takeCommand();
        guestGamePlayController.takeCommand();
        if (theWholeMatch) {
            hostCommunicator.sendMessage("reset changeScene");
            guestCommunicator.sendMessage("reset changeScene");
//            sendToViewers("reset changeScene", "reset changeScene");
            winner.addToNumberOfGamesWon(1);
            loser.addToNumberOfGamesLost(1);
            hostGamePlayController.setRun(false);
            guestGamePlayController.setRun(false);
        } else {
            hostCommunicator.sendMessage("reset");
            guestCommunicator.sendMessage("reset");
//            sendToViewers("reset", "reset");
        }
    }

    private GamePlayController flipACoin() {
        System.out.println("gameStart");
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
        guestGamePlayController.getMyCommunicator().flipCoin(starter == hostGamePlayController ? 11 : 1);
        hostGamePlayController.takeCommand();
        guestGamePlayController.takeCommand();
        hostGamePlayController.getMyCommunicator().sendMessage(Communicator.askOption(starter == hostGamePlayController ?
                "you will go first" : "your opponent will go first", "ok"));
        hostGamePlayController.getMyCommunicator().sendMessage("removeCoin");
        guestGamePlayController.getMyCommunicator().sendMessage(Communicator.askOption(starter == guestGamePlayController ?
                "you will go first" : "your opponent will go first", "ok"));
        guestGamePlayController.getMyCommunicator().sendMessage("removeCoin");
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
        host.getActionFinder().getCommunicator().resetArr();
        guest.getActionFinder().getCommunicator().resetArr();
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
