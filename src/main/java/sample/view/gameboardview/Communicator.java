package sample.view.gameboardview;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sample.controller.Action;
import sample.controller.GamePlayController;
import sample.controller.GameState;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;

public class Communicator implements Runnable {

    private boolean run;
    private AnchorPane anchorPane;
    private GamePlayController gamePlayController;
    private CountDownLatch countDownLatch;
    private Action action;
    private GameBoardView gameBoardView;
    private ArrayBlockingQueue<String> job;
    private HashMap<Integer, CardView> myPlaces, enemyPlaces;
    private ClickInputHandler clickInputHandler;
    private CustomPopup customPopup;
    private ImageView coin;

    public Communicator(AnchorPane anchorPane, GameBoardView gameBoardView,
                        HashMap<Integer, CardView> myPlaces, HashMap<Integer, CardView> enemyPlaces) {
        this.run = true;
        this.anchorPane = anchorPane;
        this.gameBoardView = gameBoardView;
        this.job = new ArrayBlockingQueue<>(1);
        this.myPlaces = myPlaces;
        this.enemyPlaces = enemyPlaces;
        this.clickInputHandler = new ClickInputHandler(anchorPane);
        this.customPopup = new CustomPopup(anchorPane);
        Thread thread = new Thread(clickInputHandler);
        thread.setDaemon(true);
        thread.start();
    }

    public void setRun(boolean run) {
        this.run = run;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public void setGamePlayController(GamePlayController gamePlayController) {
        this.gamePlayController = gamePlayController;
    }

    public void setJob(String job) {
        try {
            this.job.put(job);
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
    }

    @Override
    public void run() {
        countDownLatch = new CountDownLatch(1);
        while (run) {
            try {
                countDownLatch.await();
                if (!run)
                    break;
                countDownLatch = new CountDownLatch(1);
                gameBoardView.setBlocked(true);
                runAction();
                gameBoardView.setBlocked(false);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }
        clickInputHandler.setRun(false);
        clickInputHandler.start();
    }

    private void runAction() {
        gameBoardView.setBlocked(true);
        if (action == Action.NOTHING) {
            try {
                gamePlayController.putCommand(job.take());
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        } else if (action == Action.ATTACK) {
            outerLoop:
            while (true) {
                getMouseClick();
                if (clickInputHandler.getMouseButton() == MouseButton.SECONDARY)
                    break;
                for (int i = 11; i < 15; i++) {
                    if (enemyPlaces.get(i).getPaint() != Color.TRANSPARENT)
                    if (enemyPlaces.get(i).contains(clickInputHandler.getMouseX(), clickInputHandler.getMouseY())) {
                        gamePlayController.putCommand("attack " + (i - 10));
                        break outerLoop;
                    }
                }
            }
            gameBoardView.resetSword();
        }
        gameBoardView.setBlocked(false);
    }

    private void getMouseClick() {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        clickInputHandler.setOutsideThreadCountdown(countDownLatch);
        clickInputHandler.start();
        try {
            countDownLatch.await();
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
    }

    public void start() {
        countDownLatch.countDown();
    }

    public void moveFromHandToBoard(int handNumber, int destinationPlaceNumber, boolean enemy, String status,
                                    String cardName, String description) {
        CardPath cardPath = getCardPath(cardName);
        Image previewImage = cardPath.getImage();
        if (status.equalsIgnoreCase("set")) {
            cardPath = CardPath.UNKNOWN;
            if (enemy)
                previewImage = cardPath.getImage();
        }
        gameBoardView.moveFromHandToGame(handNumber, destinationPlaceNumber, enemy, status, cardPath, previewImage, description);
    }

    public void moveToGraveyard(int number, boolean enemy, String cardName, String description) {
        CardPath cardPath = getCardPath(cardName);
        gameBoardView.moveToGraveyard(number, enemy, cardPath, cardPath == CardPath.UNKNOWN && enemy ? "" : description);
    }

    public void addToHand(int handNumber, boolean enemy, String cardName, String description) {
        CardPath cardPath = getCardPath(cardName);
        gameBoardView.addToHand(handNumber, enemy, cardPath, description, cardName);
    }

    public void changePosition(int number, boolean enemy, String status) {
        gameBoardView.changePosition(number, enemy, status);
    }

    public void selectCard(String cardType, boolean me, boolean enemy, boolean addType) {
        gameBoardView.setBlocked(true);
        int type = typeToInt(cardType);
        outerLoop:
        while (true) {
            getMouseClick();
            int i = type != 0 ? 1 : 0;
            for (; i < 5; i++) {
                if (me)
                    if (myPlaces.get(i + type).contains(clickInputHandler.getMouseX(), clickInputHandler.getMouseY())) {
                        gamePlayController.putCommand(String.valueOf(addType ? i + type : i));
                        break outerLoop;
                    }
                if (enemy)
                    if (enemyPlaces.get(i + type).contains(clickInputHandler.getMouseX(), clickInputHandler.getMouseY())) {
                        gamePlayController.putCommand(String.valueOf(addType ? (i + type) * -1 : -i));
                        break outerLoop;
                    }
            }
        }
        gameBoardView.setBlocked(false);
    }

    private int typeToInt(String type) {
        if (type.equalsIgnoreCase("hand"))
            return 0;
        else if (type.equalsIgnoreCase("monster"))
            return 10;
        else return 20;
    }

    private CardPath getCardPath(String cardName) {
        CardPath cardPath = CardPath.UNKNOWN;
        try {
            cardPath = CardPath.valueOf(cardName.replaceAll("[-_ ]", "").toUpperCase());
        } catch (IllegalArgumentException ignored) {
        }
        return cardPath;
    }

    public void reduceHealth(int amount, boolean enemy) {
        gameBoardView.changeHealth(enemy, amount);
    }

    public void askOptions(String message, String... options) {
        customPopup.setTexts(message, options);
        customPopup.addToPane();
        String chosenOption;
        while (true) {
            getMouseClick();
            if (customPopup.haveClickedOption(clickInputHandler.getMouseX(), clickInputHandler.getMouseY())){
                chosenOption = customPopup.getClickOptionMessage(clickInputHandler.getMouseX(), clickInputHandler.getMouseY());
                break;
            }
        }
        customPopup.reset();
        gamePlayController.putCommand(chosenOption);
    }

    public void changePhase(String phase, boolean enemyTurn) {
        gameBoardView.changePhase(phase, enemyTurn);
    }

    public void flipSummon(int num, boolean enemy, String cardName) {
        CardPath cardPath = getCardPath(cardName);
        gameBoardView.flipSummon(num, enemy, cardPath);
    }

    public void specialSummonFromGraveYard(boolean justName) {
        gameBoardView.setBlocked(true);
        ArrayList<CardView> myGraveyard = gameBoardView.getMyGraveyard().getCardViews();
        ArrayList<CardView> opponentGraveyard = gameBoardView.getOpponentGraveyard().getCardViews();
        Graveyard myGraveyardObject = gameBoardView.getMyGraveyard();
        Graveyard opponentGraveyardObject = gameBoardView.getMyGraveyard();
        double x, y;
        Point2D point2D;
        outerLoop:
        while (true) {
            getMouseClick();
            x = clickInputHandler.getMouseX();
            y = clickInputHandler.getMouseY();
            for (int i = 0; i < myGraveyard.size(); i++) {
                if (myGraveyard.get(i).contains(
                        myGraveyard.get(i).parentToLocal(myGraveyardObject.getScrollPane().parentToLocal(x, y)))) {
                    gamePlayController.putCommand(justName ?
                            myGraveyard.get(i).getName() : "select card " + myGraveyard.get(i).getName());
                    break outerLoop;
                }
            }
            for (int i = 0; i < opponentGraveyard.size(); i++) {
                if (opponentGraveyard.get(i).contains(
                        myGraveyard.get(i).localToParent(opponentGraveyardObject.getScrollPane().parentToLocal(x, y)))) {
                    gamePlayController.putCommand(justName ?
                            myGraveyard.get(i).getName() : "select card opponent " + opponentGraveyard.get(i).getName());
                    break outerLoop;
                }
            }
        }
        gameBoardView.setBlocked(false);
    }

    public void scanner() {
        gameBoardView.setBlocked(true);
        ArrayList<CardView> opponentGraveyard = gameBoardView.getOpponentGraveyard().getCardViews();
        outerLoop:
        while (true) {
            getMouseClick();
            for (int i = 0; i < opponentGraveyard.size(); i++) {
                if (opponentGraveyard.get(i).contains(clickInputHandler.getMouseX(), clickInputHandler.getMouseY())) {
                    gamePlayController.putCommand(opponentGraveyard.get(i).getName());
                    break outerLoop;
                }
            }
        }
        gameBoardView.setBlocked(false);
    }

    public void mindCrush() {
        gameBoardView.setBlocked(true);
        Rectangle rectangle = new Rectangle(anchorPane.getWidth(), anchorPane.getHeight());
        rectangle.setFill(Color.TRANSPARENT);
        anchorPane.getChildren().add(rectangle);
        TextField textField = new TextField();
        textField.setPromptText("please enter the card name");
        textField.setLayoutX(100);
        textField.setLayoutY(100);
        textField.setPrefWidth(50);
        textField.setPrefHeight(20);
        Button button = new Button();
        button.setText("send");
        button.setLayoutX(151);
        button.setLayoutY(100);
        button.setOnMouseClicked(e -> {
            anchorPane.getChildren().removeAll(button, textField, rectangle);
            gamePlayController.putCommand(textField.getText());
            gameBoardView.setBlocked(false);
        });
        anchorPane.getChildren().addAll(textField, button);
    }

    public void ritualSummon() {
        gameBoardView.setBlocked(true);
        StringBuilder result = new StringBuilder();
        while (true) {
            if (!hasMonster(myPlaces))
                break;
            getMouseClick();
            if (clickInputHandler.getMouseButton() == MouseButton.PRIMARY)
                break;
            for (int i = 11; i < 16; i++) {
                if (myPlaces.get(i).contains(clickInputHandler.getMouseX(), clickInputHandler.getMouseY()))
                    result.append(" ").append(i - 10);
            }
        }
        gamePlayController.putCommand(result.toString());
        gameBoardView.setBlocked(false);
    }

    private boolean hasMonster(HashMap<Integer, CardView> places) {
        for (int i = 11; i < 15; i++) {
            if (places.get(i).getPaint() != Color.TRANSPARENT)
                return true;
        }
        return false;
    }

    public void sendMessage(String text) {
        gameBoardView.addTextToChat(text);
    }

    public void specialSummonFromAnywhere(String message) {
        customPopup.setTexts(message, "hand", "graveyard", "deck");
        customPopup.addToPane();
        String chosenOption;
        while (true) {
            getMouseClick();
            double x = clickInputHandler.getMouseX();
            double y = clickInputHandler.getMouseY();
            if (customPopup.haveClickedOption(x, y)){
                chosenOption = customPopup.getClickOptionMessage(x, y);
                break;
            }
        }
        gamePlayController.putCommand(chosenOption);
        switch (chosenOption) {
            case "deck":
                mindCrush();
                break;
            case "graveyard":
                specialSummonFromGraveYard(true);
            case "hand":
                selectCard("hand", true, false, false);
        }
    }

    public void reset() {
        gameBoardView.reset();
    }

    public void flipCoin(int finishingPicture) {
        Timeline timeline = new Timeline();
        coin = new ImageView();
        int timeFrame = 40;
        for (int i = 1; i < 21; i++) {
            String path = "./coin/" + i + ".png";
            Image image = new Image(path);
            KeyFrame keyFrame = new KeyFrame(Duration.millis(timeFrame * i), changeImage -> coin.setImage(image));
            timeline.getKeyFrames().add(keyFrame);
        }
        coin.setFitWidth(100);
        coin.setFitHeight(100);
        coin.setX(anchorPane.getPrefWidth() / 2 - coin.getFitWidth() / 2);
        coin.setY(anchorPane.getPrefHeight() / 2 - coin.getFitHeight() / 2 - 300);
        timeline.setCycleCount(1);
        timeline.setOnFinished(e -> {
            Timeline timeline1 = new Timeline();
            for (int i = 1; i < finishingPicture + 1; i++) {
                String path = "./coin/" + i + ".png";
                Image image = new Image(path);
                KeyFrame keyFrame = new KeyFrame(Duration.millis(timeFrame * i), changeImage -> coin.setImage(image));
                timeline1.getKeyFrames().add(keyFrame);
            }
            timeline1.play();
            timeline1.setOnFinished(alert -> gamePlayController.putCommand("finished"));
        });
        Platform.runLater(() -> anchorPane.getChildren().add(coin));
        timeline.play();
    }

    public void changeGameState(String state) {
        gameBoardView.setGameState(GameState.getGameStateByString(state));
    }

    public void removeCoin(){
        Platform.runLater(() -> anchorPane.getChildren().remove(coin));
    }

    public void nextPhase(){
        if (!gameBoardView.getBlocked())
        gamePlayController.putCommand("next phase");
    }

    public void showCard(int placeNumber, String cardName, String description, boolean enemy){
        CardPath cardPath = getCardPath(cardName);
        gameBoardView.showCard(placeNumber, cardPath, description, enemy);
    }

    public void shutdown(Stage stage, Stage stage1, boolean changeScene){
        gameBoardView.shutdown(stage, stage1, changeScene);
    }

    public void pause(){
        gameBoardView.pause();
    }

    public void resume(){
        gameBoardView.resume();
    }
}
