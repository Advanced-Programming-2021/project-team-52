package sample.view.gameboardview;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.text.Font;
import sample.controller.Action;

import sample.controller.GameState;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.model.game.STATUS;
import sample.view.sender.Sender;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;

public class GameBoardView{

    public static double DIVIDER, CARD_HEIGHT, CARD_WIDTH;

    static {
        CARD_HEIGHT = 130.0;
        CARD_WIDTH = 89.13680781758957;
        DIVIDER = 4.7230769230769230769230769230769;
    }

    private AnchorPane anchorPane;
//    private GamePlayController gamePlayController;
    private boolean primaryMouse;
//    private Stage stage;
    private HashMap<Integer, CardView> myPlaces;
    private HashMap<Integer, CardView> enemyPlaces;
    private HandPlace myHand, enemyHand;
    private Rectangle previewRectangle;
    private Label previewLabel;
    private TextArea chatTexts;
    private TextField messageField;
    private Button sendMessageButton, pauseButton, surrenderButton;
    private VerticalProgressBar myHealthBar, enemyHealthBar;
    private Graveyard myGraveyard, opponentGraveyard;
    private VerticalToggleButton myGraveyardToggleButton, opponentGraveyardToggleButton;
    private GameState gameState;
    private ImageView sword;
    private Communicator communicator, opponentCommunicator;
    private Rectangle drawPhase, standbyPhase, mainPhaseOne, battlePhase, mainPhaseTwo, endPhase;
    private boolean blocked;
    private ToggleButton darkMode;
    private BackgroundImage lightBackground, darkBackground;
    private Sender sender;
    private GameBoardHandler gameBoardHandler;
    private ImageView myProfileImage, enemyProfileImage;
    private VerticalLabel myNickname, enemyNickname;
    private String myNicknameString;

    public GameBoardView(String myNickname, String myProfileImageAddress, String enemyNickname,
                         String enemyImageAddress, GameBoardHandler gameBoardHandler){
//        this.stage = stage;
        this.anchorPane = new AnchorPane();
        this.primaryMouse = true;
        this.myPlaces = new HashMap<>();
        this.enemyPlaces = new HashMap<>();
        this.myHand = new HandPlace();
        this.enemyHand = new HandPlace();
        this.previewRectangle = new Rectangle(279, 409, Color.TRANSPARENT);
        this.previewLabel = new Label();
        this.messageField = new TextField();
        this.chatTexts = new TextArea();
        this.sendMessageButton = new Button();
        this.myHealthBar = new VerticalProgressBar();
        this.enemyHealthBar = new VerticalProgressBar();
        this.myGraveyard = new Graveyard(668, 377);
        this.opponentGraveyard = new Graveyard(668, 194);
        this.myGraveyardToggleButton = new VerticalToggleButton();
        this.opponentGraveyardToggleButton = new VerticalToggleButton();
        this.pauseButton = new Button();
        this.surrenderButton = new Button();
        this.drawPhase = new Rectangle(55, 93);
        this.standbyPhase = new Rectangle(55, 93);
        this.mainPhaseOne = new Rectangle(55, 93);
        this.battlePhase = new Rectangle(55, 93);
        this.mainPhaseTwo = new Rectangle(55, 93);
        this.endPhase = new Rectangle(55, 93);
        this.sword = new ImageView();
        this.gameState = GameState.OTHER;
        this.anchorPane.setPrefWidth(1317);
        this.anchorPane.setPrefHeight(734);
        this.communicator = new Communicator(anchorPane, this, myPlaces, enemyPlaces);
        this.darkMode = new ToggleButton();
        this.sender = Sender.getInstance();
        this.gameBoardHandler = gameBoardHandler;
        this.darkMode = new ToggleButton();
        try {
            this.myProfileImage = new ImageView(new Image(new FileInputStream(myProfileImageAddress)));
            this.enemyProfileImage = new ImageView(new Image(new FileInputStream(enemyImageAddress)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.myNickname = new VerticalLabel(myNickname);
        this.myNicknameString = myNickname;
        this.enemyNickname = new VerticalLabel(enemyNickname);
        Thread thread = new Thread(communicator);
        thread.setDaemon(true);
        thread.start();
//        Thread thread1 = new Thread(gameBoardHandler);
//        thread1.setDaemon(true);
//        thread1.start();
        initialize();
//        Scene scene = new Scene(anchorPane);
//        stage.setScene(scene);
//        initialize();
    }

    public Communicator getCommunicator() {
        return communicator;
    }

//    public void setGamePlayController(GamePlayController gamePlayController) {
//        this.gamePlayController = gamePlayController;
//        communicator.setGamePlayController(gamePlayController);
//    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public void setOpponentCommunicator(Communicator opponentCommunicator) {
        this.opponentCommunicator = opponentCommunicator;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public HandPlace getMyHand() {
        return myHand;
    }

    public HandPlace getEnemyHand() {
        return enemyHand;
    }

    public Graveyard getMyGraveyard() {
        return myGraveyard;
    }

    public Graveyard getOpponentGraveyard() {
        return opponentGraveyard;
    }

    public boolean getBlocked(){
        return blocked;
    }

    public AnchorPane getAnchorPane() {
        return anchorPane;
    }

    public void initialize() {
        anchorPane.getChildren().clear();
        Rectangle dummy = new Rectangle(1280, 720);
        dummy.setFill(Color.TRANSPARENT);
        anchorPane.getChildren().add(dummy);
        Image lightImageOfTheBackground = new Image("./misc/b1.jpg");
        lightBackground = new BackgroundImage(lightImageOfTheBackground, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        Image darkImageOfTheBackground = new Image("./misc/b2.png");
        darkBackground = new BackgroundImage(darkImageOfTheBackground, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        Background background = new Background(lightBackground);
        anchorPane.setBackground(background);
        initializeCardPlaces(myPlaces, anchorPane, false);
        initializeCardPlaces(enemyPlaces, anchorPane, true);
        placeCardsInTheBoard(myPlaces, 369, false);
        placeCardsInTheBoard(enemyPlaces, 79, true);
        setHBoxComponents(myHand, false, anchorPane);
        setHBoxComponents(enemyHand, true, anchorPane);
        previewRectangle.setLayoutX(3);
        previewRectangle.setLayoutY(10);
        anchorPane.getChildren().add(previewRectangle);
        previewLabel.setPrefWidth(279);
        previewLabel.setPrefHeight(82);
        previewLabel.setLayoutX(3);
        previewLabel.setLayoutY(417);
        previewLabel.setAlignment(Pos.CENTER_LEFT);
        previewLabel.setTextFill(Color.WHITE);
        previewLabel.setFont(new Font(14));
        previewLabel.setWrapText(true);
        anchorPane.getChildren().add(previewLabel);
        messageField.setPrefWidth(215);
        messageField.setPrefHeight(26);
        messageField.setLayoutX(3);
        messageField.setLayoutY(680);
        messageField.setAlignment(Pos.TOP_LEFT);
        messageField.setPromptText("enter your message here");
        sendMessageButton.setText("send");
        sendMessageButton.setAlignment(Pos.CENTER);
        sendMessageButton.setPrefWidth(63);
        sendMessageButton.setPrefHeight(26);
        sendMessageButton.setLayoutX(219);
        sendMessageButton.setLayoutY(680);
        chatTexts.setFont(new Font(12));
        chatTexts.setWrapText(true);
        chatTexts.setPrefWidth(279);
        chatTexts.setPrefHeight(162);
        chatTexts.setLayoutX(3);
        chatTexts.setLayoutY(518);
        chatTexts.setEditable(false);
        putSendMessageEvent(sendMessageButton, anchorPane);
        anchorPane.getChildren().addAll(chatTexts, messageField, sendMessageButton);
        myHealthBar.setValues(1202, 377, 63, 261, 270, 1);
        enemyHealthBar.setValues(1202, 82, 63, 261, 90, 1);
        anchorPane.getChildren().addAll(enemyHealthBar, myHealthBar);
        pauseButton.setPrefWidth(63);
        pauseButton.setPrefHeight(38);
        pauseButton.setLayoutX(1202);
        pauseButton.setLayoutY(14);
        pauseButton.setText("pause");
        setPause(pauseButton);
        surrenderButton.setPrefWidth(63);
        surrenderButton.setPrefHeight(38);
        surrenderButton.setLayoutX(1202);
        surrenderButton.setLayoutY(661);
        surrenderButton.setText("surrender");
        surrenderButton.setOnMouseClicked(e -> surrender());
        anchorPane.getChildren().addAll(pauseButton, surrenderButton);
        myGraveyardToggleButton.setValues(1143, 377, 34, 205, 90, "show graveyard");
        opponentGraveyardToggleButton.setValues(1143, 138, 34, 205, 90, "show graveyard");
        putToggleButtonEventForGraveyard(myGraveyardToggleButton, false);
        putToggleButtonEventForGraveyard(opponentGraveyardToggleButton, true);
        anchorPane.getChildren().addAll(myGraveyardToggleButton, opponentGraveyardToggleButton);
        putPhase(drawPhase, "DP", 289, 218, anchorPane);
        putPhase(standbyPhase, "SB", 289, 314, anchorPane);
        putPhase(mainPhaseOne, "M1", 289, 410, anchorPane);
        putPhase(battlePhase, "BP", 344, 218, anchorPane);
        putPhase(mainPhaseTwo, "M2", 344, 314, anchorPane);
        putPhase(endPhase, "EP", 344, 410, anchorPane);
        Image swordImage = new Image("./misc/sword.png");
        double divider = swordImage.getHeight() / (CARD_HEIGHT / 1.5);
        sword.setImage(swordImage);
        sword.setFitHeight(swordImage.getHeight() / divider);
        sword.setFitWidth(swordImage.getWidth() / divider);
        anchorPane.setOnMouseMoved(mouseEvent -> {
            double xDistance = mouseEvent.getSceneX() - sword.getX() - sword.getFitWidth() / 2;
            double yDistance = mouseEvent.getSceneY() - sword.getY() - sword.getFitHeight() / 2;
            double angleToTurn = Math.toDegrees(Math.atan2(yDistance, xDistance)) + 45;
            sword.setRotate(angleToTurn);
        });
        sword.setX(0);
        sword.setY(0);
        sword.setOpacity(0);
        anchorPane.getChildren().add(sword);
        anchorPane.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.SECONDARY)
                primaryMouse =!primaryMouse;
        });
        darkMode.setLayoutX(693);
        darkMode.setLayoutY(327);
        darkMode.setPrefWidth(144);
        darkMode.setPrefHeight(66);
        darkMode.setText("dark mode");
        changeBackground(darkMode);
        myProfileImage.setFitWidth(51);
        myProfileImage.setFitHeight(46);
        myProfileImage.setX(1134);
        myProfileImage.setY(674);
        enemyProfileImage.setFitWidth(51);
        enemyProfileImage.setFitHeight(46);
        enemyProfileImage.setX(1134);
        enemyProfileImage.setY(0);
        myNickname.setValues(1134, 598, 51, 76, 90, myNickname.getText());
        myNickname.setTextFill(Color.WHITE);
        enemyNickname.setValues(1134, 46, 51, 76, 90, enemyNickname.getText());
        enemyNickname.setTextFill(Color.WHITE);
        anchorPane.getChildren().addAll(myProfileImage, myNickname, enemyProfileImage, enemyNickname);
        this.gameBoardHandler.setGameBoardView(this);
        this.gameBoardHandler.setCommunicator(this.communicator);
    }

    private void changeBackground(ToggleButton toggleButton){
        toggleButton.setOnAction(actionEvent -> {
            if (toggleButton.isSelected()){
                anchorPane.setBackground(new Background(darkBackground));
            } else anchorPane.setBackground(new Background(lightBackground));
        });
    }

    private void setPause(Button button){
        button.setOnAction(mouseEvent -> {
            if (button.getText().equals("pause")) {
                button.setText("resume");
                pause();
                opponentCommunicator.pause();
            } else {
                button.setText("pause");
                resume();
                opponentCommunicator.resume();
            }
        });
    }

    public void pause(){
        blocked = true;
        pauseButton.setText("resume");
        anchorPane.getChildren().add(darkMode);
    }

    public void resume(){
        blocked = false;
        pauseButton.setText("pause");
        anchorPane.getChildren().remove(darkMode);
    }

    private void setNextPhase(Rectangle... rectangles){
        for (int i = 0; i < rectangles.length; i++) {
            rectangles[i].setOnMouseClicked(mouseEvent -> {
                if (!blocked)
                if (gameState == GameState.MAIN_PHASE || gameState == GameState.BATTLE_PHASE)
                if (mouseEvent.getButton() == MouseButton.PRIMARY)
                    communicator.nextPhase();
            });
        }
    }

    private void setNextPhase(Label label){
        label.setOnMouseClicked(mouseEvent -> {
            if (gameState == GameState.MAIN_PHASE || gameState == GameState.BATTLE_PHASE)
                if (mouseEvent.getButton() == MouseButton.PRIMARY)
                    communicator.nextPhase();
        });
    }

    private void putPhase(Rectangle rectangle, String text, double x, double y, Pane pane){
        rectangle.setX(x);
        rectangle.setY(y);
        rectangle.setFill(Color.ROYALBLUE);
        VerticalLabel verticalLabel = new VerticalLabel();
        verticalLabel.setValues(x, y, 55, 93, 90, text);
        setNextPhase(verticalLabel);
        pane.getChildren().addAll(rectangle, verticalLabel);
    }

    private void putToggleButtonEventForGraveyard(ToggleButton toggleButton, boolean opponent){
        toggleButton.setOnAction(event -> openOrCloseGraveYard(toggleButton.isSelected(), opponent));
    }

    private void putSendMessageEvent(Button button, AnchorPane anchorPane){
        button.setOnMouseClicked(e -> sendMessage());
        anchorPane.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)
                sendMessage();
        });
    }

    private void sendMessage() {
        String text = messageField.getText();
        if (!text.isEmpty()){
            messageField.clear();
            text = "\n" + myNicknameString + " : " + text;
            chatTexts.appendText(text);
            opponentCommunicator.sendMessage(text);
        }
    }

    public void addTextToChat(String message){
        chatTexts.appendText(message);
    }

    private void initializeCardPlaces(HashMap<Integer, CardView> places, AnchorPane anchorPane, boolean enemy){
        places.put(0, getCardView(false, anchorPane, enemy, 0, enemy));
        places.put(40, getCardView(false, anchorPane, enemy, 40, enemy));
        places.put(6, getCardView(false, anchorPane, enemy, 6, enemy));
        for (int i = 1; i < 6; i++) {
            places.put(i, getCardView(false, anchorPane, enemy, i, enemy));
            places.put(i + 10, getCardView(true, anchorPane, enemy, i + 10, enemy));
            places.put(i + 20, getCardView(true, anchorPane, enemy, i + 20, enemy));
        }
    }

    private CardView getCardView(boolean addToAnchorPane, AnchorPane anchorPane, boolean enemy, int placeNumber, boolean isEnemy){
        CardView cardView = new CardView(placeNumber, isEnemy);
        if (addToAnchorPane) {
            anchorPane.getChildren().add(cardView);
            anchorPane.getChildren().add(cardView.getIMAGE_VIEW());
        }
        if (enemy)
            cardView.setRotate(180);
        return cardView;
    }

    private void placeCardsInTheBoard(HashMap<Integer, CardView> places, int startingY, boolean enemy){
        int startingX = 433;
        CardView cardView;
        for (int i = 1; i < 6; i++) {
            cardView = places.get(i + 10);
            cardView.setX(startingX + (i - 1) * 145);
            setHeightAndWidth(cardView);
            if (enemy) cardView.setRotate(180);
            cardView.setY(enemy ? startingY + 145 : startingY);
            putMonsterAndSpellMouseEvent(cardView);
            cardView = places.get(i + 20);
            cardView.setX(startingX + (i - 1) * 145);
            cardView.setY(enemy ? startingY : startingY + 139);
            setHeightAndWidth(cardView);
            if(enemy) cardView.setRotate(180);
            putMonsterAndSpellMouseEvent(cardView);
            cardView = places.get(i);
            setHeightAndWidth(cardView);
            if (enemy) cardView.setRotate(180);
            putHandMouseEvent(cardView, enemy);
        }
        if (enemy){
            places.get(0).setRotate(180);
            places.get(40).setRotate(180);
            places.get(40).setX(300);
            places.get(40).setY(84);
        } else {
            places.get(40).setX(300);
            places.get(40).setY(509);
        }
        anchorPane.getChildren().add(places.get(40));
        setHeightAndWidth(places.get(0));
        setHeightAndWidth(places.get(40));
        putHandMouseEvent(places.get(0), enemy);
        places.get(6).setLayoutX(399);
        places.get(6).setLayoutY(enemy ? startingY : startingY + 139);
    }

    private void setHeightAndWidth(CardView cardView){
        cardView.setHeight(CARD_HEIGHT);
        cardView.setWidth(CARD_WIDTH);
        cardView.setPaint(Color.TRANSPARENT);
    }

    private void askActions(int number, boolean isEnemy, GameState gameState){
        sender.send(Sender.GAME_PLAY_CONTROLLER_PREFIX + "getPossibleAction," + number + "," + isEnemy + "," + gameState.name());
    }

    private Action[] getAction(){
        String[] actionsString = sender.receive().split(",");
        Action[] actions = new Action[2];
        actions[0] = Action.getActionByValue(actionsString[0]);
        actions[1] = Action.getActionByValue(actionsString[1]);
        return actions;
    }

    private void putMonsterAndSpellMouseEvent(CardView cardView){
        cardView.setOnMouseEntered(mouseEvent -> {
            if (cardView.canHaveEffects())
            if (!blocked || gameState == GameState.CHAIN){
                askActions(cardView.getPLACE_NUMBER(), cardView.getIsEnemy(), gameState);
//                cardView.setActions(getAction());
//                getActionImage(cardView);
            } else cardView.changeImageViewOpacity(0);
            if (cardView.getPaint() != Color.TRANSPARENT) {
                previewRectangle.setFill(new ImagePattern(cardView.getPreview()));
                previewLabel.setText(cardView.getDescription());
            }
        });
        cardView.setOnMouseExited(mouseEvent -> {
            if (!cardView.getPaint().equals(Color.TRANSPARENT))
            if (!cardView.contains(mouseEvent.getSceneX() + 5, mouseEvent.getSceneY() - 30))
                cardView.changeImageViewOpacity(0);
        });
        putMouseMovedAndClickedEvents(cardView);
    }

    private void getActionImage(CardView cardView) {
        Action[] actions = cardView.getActions();
        Image image = primaryMouse ? actions[0].getImage() : actions[1].getImage();
        if (image != null) {
            cardView.changeImageViewImage(image);
            cardView.changeImageViewOpacity(1);
        } else cardView.changeImageViewOpacity(0);
    }

    private void putHandMouseEvent(CardView cardView, boolean enemy){
        cardView.setTranslateY(enemy ? -31 : 45);
        double originalY = cardView.getTranslateY();
        cardView.setOnMouseEntered(mouseEvent -> {
            if (cardView.canHaveEffects())
            if (!blocked){
                askActions(cardView.getPLACE_NUMBER(), cardView.getIsEnemy(), gameState);
//                cardView.setActions(getAction());
//                getActionImage(cardView);
                if (cardView.playExitMouseTransition) {
                    TranslateTransition translateTransition = new TranslateTransition();
                    translateTransition.setToY(originalY + (enemy ? 40 : -40));
                    translateTransition.setDuration(Duration.millis(400));
                    translateTransition.setNode(cardView);
                    translateTransition.play();
                }
            } else cardView.changeImageViewOpacity(0);
            if (cardView.getPaint() != Color.TRANSPARENT) {
                previewRectangle.setFill(new ImagePattern(cardView.getPreview()));
                previewLabel.setText(cardView.getDescription());
            }
        });
        cardView.setOnMouseExited(mouseEvent -> {
            if (cardView.canHaveEffects())
            if (!cardView.contains(mouseEvent.getSceneX() + 5, mouseEvent.getSceneY() - 30)) {
                cardView.changeImageViewOpacity(0);
                if (cardView.playExitMouseTransition) {
                    TranslateTransition translateTransition = new TranslateTransition();
                    translateTransition.setToY(originalY);
                    translateTransition.setDuration(Duration.millis(400));
                    translateTransition.setNode(cardView);
                    translateTransition.play();
                }
            }
        });
        putMouseMovedAndClickedEvents(cardView);
    }

    private void putMouseMovedAndClickedEvents(CardView cardView) {
        cardView.setOnMouseMoved(mouseEvent -> {
            if (!blocked || gameState == GameState.CHAIN)
            if (cardView.canHaveEffects()) {
                cardView.setImageViewX(mouseEvent.getSceneX() + 5);
                cardView.setImageViewY(mouseEvent.getSceneY() - 30);
            }
        });
        cardView.setOnMouseClicked(mouseEvent -> {
            if (!blocked || gameState == GameState.CHAIN)
            if (cardView.canHaveEffects())
                if (mouseEvent.getButton() == MouseButton.PRIMARY){
                    if (cardView.getActions() != null) {
                        Action action = primaryMouse ? cardView.getActions()[0] : cardView.getActions()[1];
                        primaryMouse = true;
                        if (action != Action.NOTHING && !cardView.getIsEnemy())
                            doAction(cardView.getPLACE_NUMBER(), action, gameState, cardView);
                    }
            } else {
                    askActions(cardView.getPLACE_NUMBER(), cardView.getIsEnemy(), gameState);
//                    cardView.setActions(getAction());
//                    getActionImage(cardView);
                }
        });
    }

    private void setHBoxComponents(HBox hBox, boolean enemy, AnchorPane anchorPane){
        hBox.setAlignment(Pos.CENTER);
        hBox.setLayoutX(399);
        hBox.setLayoutY(enemy ? 0 : 627);
        hBox.setPrefWidth(735);
        hBox.setPrefHeight(79);
        hBox.setMinHeight(79);
        if (!anchorPane.getChildren().contains(hBox))
            anchorPane.getChildren().add(hBox);
    }

    private void addToGraveyard(CardView cardView, boolean enemy){
        Graveyard graveyard = enemy ? opponentGraveyard : myGraveyard;
        graveyard.add(cardView);
    }

    public void changeHealth(boolean enemy, int amount){
        double change = ((double) amount) / 8000;
        ProgressBar progressBar = enemy ? enemyHealthBar : myHealthBar;
        double progress = progressBar.getProgress() + change;
        progressBar.setProgress(Math.max(0, progress));
    }

    private void surrender(){
        communicator.setAction(Action.NOTHING);
        communicator.setJob("surrender");
        communicator.start();
    }

    private void openOrCloseGraveYard(boolean open, boolean opponentGraveyard){
        Graveyard graveyard = opponentGraveyard ? this.opponentGraveyard : myGraveyard;
        if (open)
            anchorPane.getChildren().add(graveyard.build());
        else anchorPane.getChildren().remove(graveyard.getScrollPane());
    }

    public void doAction(int placeNumber, Action action, GameState gameState, CardView cardView){
        communicator.setAction(Action.NOTHING);
        communicator.setJob("select " + getType(placeNumber) + " " + placeNumber % 10);
        communicator.start();
        try {
            Thread.sleep(150);
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
        if (gameState == GameState.MAIN_PHASE){
            switch (action){
                case SET:
                    communicator.setAction(Action.NOTHING);
                    communicator.setJob("set");
                    communicator.start();
                    break;
                case NORMAL_SUMMON:
                    communicator.setAction(Action.NOTHING);
                    communicator.setJob("summon");
                    communicator.start();
                    break;
                case FLIP_SUMMON:
                    communicator.setAction(Action.NOTHING);
                    communicator.setJob("flip-summon");
                    communicator.start();
                    break;
                case CHANGE_POSITION:
                    sender.send(Sender.GAME_PLAY_CONTROLLER_PREFIX + "getSelectedCardStatus");
                    STATUS status = cardView.getStatus();
                    if (status != STATUS.SET) {
                        communicator.setAction(Action.NOTHING);
                        communicator.setJob("set --position " + (status == STATUS.ATTACK ? "defense" : "attack"));
                        communicator.start();
                    }
                    break;
                case ACTIVATE_EFFECT:
                    communicator.setAction(Action.NOTHING);
                    communicator.setJob("activate effect");
                    communicator.start();
                    break;
            }
        } else if (gameState == GameState.BATTLE_PHASE){
            switch (action){
                case DIRECT_ATTACK:
                    communicator.setAction(Action.NOTHING);
                    communicator.setJob("attack direct");
                    communicator.start();
                    break;
                case ATTACK:
                    sword.setX(cardView.getX() + cardView.getWidth() / 2 - sword.getFitWidth() / 2);
                    sword.setY(cardView.getY() + cardView.getHeight() / 2 - sword.getFitHeight() / 2);
                    sword.setOpacity(1);
                    communicator.setAction(Action.ATTACK);
                    communicator.start();
                    break;
            }
        } else if (gameState == GameState.CHAIN){
            if (action == Action.ACTIVATE_EFFECT){
                communicator.setAction(Action.NOTHING);
                communicator.setJob("activate effect");
                communicator.start();
            }
        }
    }

    private String getType(int placeNumber){
        if (placeNumber < 10)
            return "--hand";
        if (placeNumber < 20 )
            return "--monster";
        if (placeNumber < 30)
            return "--spell";
        return "--field";

    }

    public void moveFromHandToGame(int handPlaceNumber, int destinationPlaceNumber, boolean opponent, String status,
                                   CardPath cardPath, Image previewImage, String description){
        CardView cardView = opponent ? enemyPlaces.get(handPlaceNumber) : myPlaces.get(handPlaceNumber);
        CardView cardView1 = opponent ? enemyPlaces.get(destinationPlaceNumber) : myPlaces.get(destinationPlaceNumber);
        double originalTranslateX;
        originalTranslateX = cardView.getTranslateX();
        HBox hBox = (HBox) cardView.getParent();
        Point2D point2D = hBox.localToParent(cardView.localToParent(0, 0));
        TranslateTransition translateTransition = new TranslateTransition();
        translateTransition.setNode(cardView);
        translateTransition.setByX(cardView1.getX() - point2D.getX() + (opponent ? CARD_WIDTH : 0));
        translateTransition.setByY(cardView1.getY() - point2D.getY() + (opponent ? CARD_HEIGHT : 0));
        translateTransition.setDuration(Duration.millis(500));
        translateTransition.setInterpolator(Interpolator.LINEAR);
        translateTransition.setOnFinished(e -> {
            Platform.runLater(() -> hBox.getChildren().remove(cardView));
            cardView1.setPaint(new ImagePattern(cardPath.getImage()));
            cardView1.setPreview(previewImage);
            cardView1.setDescription(cardPath == CardPath.UNKNOWN && opponent ? "" : description);
            cardView.setPaint(Color.TRANSPARENT);
            cardView.setTranslateX(originalTranslateX);
            cardView.setTranslateY(opponent ? -31 : 45);
            anchorPane.getChildren().remove(cardView.getIMAGE_VIEW());
            cardView.playExitMouseTransition = true;
            cardView1.setStatus(STATUS.getStatusByString(status));
        });
        if (!status.equalsIgnoreCase("attack")){
            RotateTransition rotateTransition = new RotateTransition();
            rotateTransition.setNode(cardView);
            rotateTransition.setDuration(Duration.millis(500));
            rotateTransition.setByAngle(-90);
            rotateTransition.setOnFinished(e -> {
                cardView1.setRotate(-90);
                cardView.setRotate(opponent ? 180 : 0);
            });
            rotateTransition.setAutoReverse(false);
            rotateTransition.play();
        } else {
            cardView1.setRotate(opponent ? 180 : 0);
        }
        cardView.playExitMouseTransition = false;
        translateTransition.play();
    }

    public void moveToGraveyard(int placeNumber, boolean enemy, CardPath cardPath, String description){
        CardView cardView = enemy ? enemyPlaces.get(placeNumber) : myPlaces.get(placeNumber);
        if (cardView.getPaint() != Color.TRANSPARENT){
            CardView cardView1 = (CardView) cardView.clone();
            cardView.setOpacity(0);
            cardView1.setX(cardView.getX());
            cardView1.setY(cardView.getY());
            Platform.runLater(() -> anchorPane.getChildren().add(cardView1));
            ToggleButton toggleButton = enemy ? opponentGraveyardToggleButton : myGraveyardToggleButton;
            TranslateTransition translateTransition = new TranslateTransition();
            translateTransition.setToX(toggleButton.getLayoutX() + toggleButton.getWidth() / 2 - cardView.getWidth() / 2 - cardView.getX());
            translateTransition.setToY(toggleButton.getLayoutY() + toggleButton.getHeight() / 2 - cardView.getHeight() / 2 - cardView.getY());
            translateTransition.setNode(cardView1);
            translateTransition.setDuration(Duration.millis(500));
            translateTransition.setOnFinished(onFinished -> {
                anchorPane.getChildren().remove(cardView1);
                if (placeNumber < 10)
                    if (enemy)
                        enemyHand.getChildren().remove(cardView);
                    else myHand.getChildren().remove(cardView);
                cardView.changeImageViewOpacity(0);
                cardView.setTranslateX(0);
                cardView.setTranslateY(0);
                CardView cardView2 = (CardView) cardView.clone();
                cardView.setPaint(Color.TRANSPARENT);
                cardView.setDescription("");
                cardView2.setPaint(new ImagePattern(cardPath.getImage()));
                cardView2.setDescription(description);
                cardView2.setInGraveYard(true);
                cardView2.setStatus(STATUS.ATTACK);
                addToGraveyard(cardView2, enemy);
                cardView.changeImageViewOpacity(0);
                cardView.setOpacity(1);
            });
            translateTransition.play();
        }
    }

    public void addToHand(int handNumber, boolean enemy, CardPath cardPath, String description, String name){
        CardView cardView = enemy ? enemyPlaces.get(handNumber) : myPlaces.get(handNumber);
        HBox hBox = enemy ? enemyHand : myHand;
        cardView.setPaint(new ImagePattern(cardPath.getImage()));
        cardView.setPreview(cardPath.getImage());
        cardView.setDescription(description);
        cardView.setName(name);
        cardView.setOpacity(0);
        if (!hBox.getChildren().contains(cardView))
            Platform.runLater(() -> hBox.getChildren().add(cardView));
        if (!anchorPane.getChildren().contains(cardView.getIMAGE_VIEW()))
            Platform.runLater(() -> anchorPane.getChildren().add(cardView.getIMAGE_VIEW()));
        CardView animationCard = (CardView) cardView.clone();
        animationCard.setX(300);
        animationCard.setY(enemy ? 0 : 627);
        animationCard.setTranslateY(enemy ? -55 : +20);
        Platform.runLater(() -> anchorPane.getChildren().add(animationCard));
        TranslateTransition translateTransition = new TranslateTransition();
        translateTransition.setByX((enemy ? enemyHand : myHand).localToParent(cardView.localToParent(0, 0)).getX());
        translateTransition.setNode(animationCard);
        translateTransition.setDuration(Duration.millis(500));
        translateTransition.setOnFinished(e -> {
            Platform.runLater(() -> anchorPane.getChildren().remove(animationCard));
            cardView.setOpacity(1);
        });
        translateTransition.play();
    }

    public void changePosition(int number, boolean enemy, String status){
        CardView cardView = enemy ? enemyPlaces.get(number) : myPlaces.get(number);
        cardView.setStatus(STATUS.getStatusByString(status));
        int angle = status.equalsIgnoreCase("attack") ? (enemy ? 180 : 0) : -90;
        RotateTransition rotateTransition = new RotateTransition();
        rotateTransition.setNode(cardView);
        rotateTransition.setDuration(Duration.millis(500));
        rotateTransition.setToAngle(angle);
        rotateTransition.setAutoReverse(false);
        rotateTransition.play();
    }

    public void changePhase(String phase, boolean enemyTurn){
        System.out.println(enemyTurn);
        blocked = enemyTurn;
        Color phaseColor = enemyTurn ? Color.INDIANRED : Color.ROYALBLUE;
        Color defaultColor = enemyTurn ? Color.PALEVIOLETRED : Color.LIGHTSKYBLUE;
        Rectangle[] phases = {drawPhase, standbyPhase, mainPhaseOne, battlePhase, mainPhaseTwo, endPhase};
        int notToChangeColor;
        switch (phase){
            case "DP":
                drawPhase.setFill(phaseColor);
                notToChangeColor = 0;
                break;
            case "SB":
                standbyPhase.setFill(phaseColor);
                notToChangeColor = 1;
                break;
            case "M1":
                mainPhaseOne.setFill(phaseColor);
                notToChangeColor = 2;
                break;
            case "BP":
                battlePhase.setFill(phaseColor);
                notToChangeColor = 3;
                break;
            case "M2":
                mainPhaseTwo.setFill(phaseColor);
                notToChangeColor = 4;
                break;
            default:
                endPhase.setFill(phaseColor);
                notToChangeColor = 5;
                break;
        }
        for (int i = 0; i < phases.length; i++) {
            if (i == notToChangeColor)
                continue;
            phases[i].setFill(defaultColor);
        }
    }

    public void flipSummon(int num, boolean enemy, CardPath cardPath){
        CardView cardView = enemy ? enemyPlaces.get(num) : myPlaces.get(num);
        cardView.setRotate(enemy ? 180 : 0);
        cardView.setFill(new ImagePattern(cardPath.getImage()));
    }

    public void resetSword(){
        sword.setOpacity(0);
        sword.setX(0);
        sword.setY(0);
    }

    public void reset(){
        for (CardView cardView : myPlaces.values()) {
            cardView.setPaint(Color.TRANSPARENT);
            cardView.changeImageViewOpacity(0);
        }
        for (CardView cardView : enemyPlaces.values()) {
            cardView.setPaint(Color.TRANSPARENT);
            cardView.changeImageViewOpacity(0);
        }
        Platform.runLater(() -> {
            myGraveyard.getCardViews().clear();
            opponentGraveyard.getCardViews().clear();
            myHand.getChildren().clear();
            enemyHand.getChildren().clear();
        });
        myHealthBar.setProgress(1);
        enemyHealthBar.setProgress(1);
    }

    public void showCard(int placeNumber, CardPath cardPath, String description, boolean enemy){
        CardView cardView = enemy ? enemyPlaces.get(placeNumber) : myPlaces.get(placeNumber);
        cardView.setFill(new ImagePattern(cardPath.getImage()));
        cardView.setDescription(description);
    }

    public void shutdown(Stage stage, boolean changeScene){
        communicator.setRun(false);
        communicator.start();
        if (changeScene) {
            Platform.runLater(() -> {
                try {
                    Parent root = FXMLLoader.load(
                            new File("./src/main/java/sample/view/mainMenu/MainMenuFxml.fxml").toURI().toURL());
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
//                    stage1.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            });
            gameBoardHandler.setRun(false);
            sender.send(Sender.GAME_PLAY_CONTROLLER_PREFIX + "shutdown");
        }
    }

    public void setActions(String action1, String action2, int placeNumber, boolean isEnemy){
        CardView cardView = isEnemy ? enemyPlaces.get(placeNumber) : myPlaces.get(placeNumber);
        Action[] actions = {Action.getActionByValue(action1), Action.getActionByValue(action2)};
        cardView.setActions(actions);
        getActionImage(cardView);
    }
}
