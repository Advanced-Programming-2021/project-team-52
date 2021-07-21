package sample.view.scoreBoard;

import com.jfoenix.controls.JFXTextArea;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.controller.ChatRoomController;
import sample.controller.ScoreBoardController;
import sample.model.User;
import sample.view.UserKeeper;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ScoreboardViewController implements Initializable {
    ScoreBoardController scoreBoardController = ScoreBoardController.getInstance();
    ChatRoomController chatRoomController = ChatRoomController.getInstance();
    User user = UserKeeper.getInstance().getCurrentUser();

//    ScoreBoardController scoreBoardController = ScoreBoardController.getInstance();
//
//    @FXML
//    TableView<User> theScoreBoardTableView;
//    @FXML
//    TableColumn<User, String> usernameColumn;
//    @FXML
//    TableColumn<User, Integer> scoreColumn;
//
//    public List<User> scoreBoardList = scoreBoardController.getTopUsers();
//    ObservableList<User> topUsers = FXCollections.observableArrayList();

//    TableView<User> scoreBoard;

    @FXML
    AnchorPane scoreBoardScenePane;

    Scene scene;
    Stage stage;

    @FXML
    JFXTextArea player01Score, player02Score, player03Score, player04Score, player05Score, player06Score, player07Score,
            player08Score, player09Score, player10Score, player11Score, player12Score, player13Score, player14Score,
            player15Score, player16Score, player17Score, player18Score, player19Score, player20Score;

    @FXML
    JFXTextArea player01, player02, player03, player04, player05, player06, player07, player08, player09, player10,
            player11, player12, player13, player14, player15, player16, player17, player18, player19, player20;


    boolean isBackButtonPushed = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        LoginController.getUserByUsername("ali").setScore(1000);
//        LoginController.getUserByUsername("mamad").setScore(100);
//        LoginController.getUserByUsername("reza").setScore(100);
//        LoginController.getUserByUsername("asghar").setScore(0);
//        LoginController.getUserByUsername("haji").setScore(10);
        scoreBoardScenePane.setStyle("-fx-background-image: url(media/images/backgrounds/scoreBoardWallpaper.png); -fx-background-color: black");
        isBackButtonPushed = false;

//        Timeline fiveSecondsWonder = new Timeline(
//                new KeyFrame(Duration.seconds(1),
//                        new EventHandler<ActionEvent>() {
//
//                            @Override
//                            public void handle(ActionEvent event) {
//                                System.out.println("this is called every 5 seconds on UI thread");
//                                if (isBackButtonPushed) {
////                                    timer.cancel();
////                                    timer.purge();
//                                } else {
//                                    makeEveryThingNull();
//                                    setUsersInBoard();
////                    getOnlineUsernames();
//
//                                }
//                            }
//                        }));
//        fiveSecondsWonder.setCycleCount(Timeline.INDEFINITE);
//        fiveSecondsWonder.play();
//        Platform.runLater()
//        String[] properties;
//        String firstProperties;
//        new Thread(() -> {
//            Platform.runLater(() -> {
//                Timer timer = new Timer();
//                timer.schedule(new TimerTask() {
//                    @Override
//                    public void run() {
//                        if (isBackButtonPushed) {
//                            timer.cancel();
//                            timer.purge();
//                        } else {
//                            makeEveryThingNull();
//                            setUsersInBoard();
////                    getOnlineUsernames();
//
//                        }
//                    }
//                }, 1000, 2000);
//            });
//        }).start();

//        new Thread(() -> {
//            Timer timer = new Timer();
//            timer.schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    if (isBackButtonPushed) {
//                        timer.cancel();
//                        timer.purge();
//                    } else {
//                        makeEveryThingNull();
//                        setUsersInBoard();
////                    getOnlineUsernames();
//
//                    }
//                }
//            }, 2000, 2000);
//        }).start();


//        new Thread(() -> {
//            Platform.runLater(() -> {
//                Timer timer = new Timer();
//                timer.schedule(new TimerTask() {
//                    @Override
//                    public void run() {
//                        if (isBackButtonPushed) {
//                            timer.cancel();
//                            timer.purge();
//                        } else {
//                            makeEveryThingNull();
//                            setUsersInBoard();
////                    getOnlineUsernames();
//
//                        }
//                    }
//                }, 1000, 2000);
//            });
//        }).start();
//        Platform.runLater(() -> {
//        makeEveryThingNull();
//            setUsersInBoard();
//        });

        new Thread(()->{
            while (true){
                makeEveryThingNull();
                setUsersInBoard();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public void makeEveryThingNull() {
        player01.setStyle(null);
        player01Score.setStyle(null);
        player02.setStyle(null);
        player02Score.setStyle(null);
        player03.setStyle(null);
        player03Score.setStyle(null);
        player04.setStyle(null);
        player04Score.setStyle(null);
        player05.setStyle(null);
        player05Score.setStyle(null);
        player06.setStyle(null);
        player06Score.setStyle(null);
        player07.setStyle(null);
        player07Score.setStyle(null);
        player08.setStyle(null);
        player08Score.setStyle(null);
        player09.setStyle(null);
        player09Score.setStyle(null);
        player10.setStyle(null);
        player10Score.setStyle(null);
        player11.setStyle(null);
        player11Score.setStyle(null);
        player12.setStyle(null);
        player12Score.setStyle(null);
        player13.setStyle(null);
        player13Score.setStyle(null);
        player14.setStyle(null);
        player14Score.setStyle(null);
        player15.setStyle(null);
        player15Score.setStyle(null);
        player16.setStyle(null);
        player16Score.setStyle(null);
        player17.setStyle(null);
        player17Score.setStyle(null);
        player18.setStyle(null);
        player18Score.setStyle(null);
        player19.setStyle(null);
        player19Score.setStyle(null);
        player20.setStyle(null);
        player20Score.setStyle(null);
    }

    public void setUsersInBoard() {
        String topUsersInOneString = scoreBoardController.toString();
        String[] eachUser = topUsersInOneString.split("\n");
        List<String> listOfUsers = Arrays.asList(eachUser);
        String onlineUsers = chatRoomController.getOnlineUsernames();
        int size = listOfUsers.size();
        String[] properties;
        String firstProperties;
        a(listOfUsers, onlineUsers, size, 0, player01, player01Score);
        a(listOfUsers, onlineUsers, size, 1, player02, player02Score);
        a(listOfUsers, onlineUsers, size, 2, player03, player03Score);
        a(listOfUsers, onlineUsers, size, 3, player04, player04Score);
        if (size > 4) {
            properties = getProperties(listOfUsers, 4);
            firstProperties = properties[0];
            player05.setText(firstProperties);
            player05Score.setText(properties[1]);
            if (checkIfUser(firstProperties))
                setUserStyle(player05, player05Score);
            else
                setOnlineUsersStyle(player05, player05Score, onlineUsers);
        }
        if (size > 5) {
            properties = getProperties(listOfUsers, 5);
            firstProperties = properties[0];
            player06.setText(firstProperties);
            player06Score.setText(properties[1]);
            if (checkIfUser(firstProperties))
                setUserStyle(player06, player06Score);
            else
                setOnlineUsersStyle(player06, player06Score, onlineUsers);
        }
        if (size > 6) {
            properties = getProperties(listOfUsers, 6);
            firstProperties = properties[0];
            player07.setText(firstProperties);
            player07Score.setText(properties[1]);
            if (checkIfUser(firstProperties))
                setUserStyle(player07, player07Score);
            else
                setOnlineUsersStyle(player07, player07Score, onlineUsers);
        }
        if (size > 7) {
            properties = getProperties(listOfUsers, 7);
            firstProperties = properties[0];
            player08.setText(firstProperties);
            player08Score.setText(properties[1]);
            if (checkIfUser(firstProperties))
                setUserStyle(player08, player08Score);
            else
                setOnlineUsersStyle(player08, player08Score, onlineUsers);
        }
        if (size > 8) {
            properties = getProperties(listOfUsers, 8);
            firstProperties = properties[0];
            player09.setText(firstProperties);
            player09Score.setText(properties[1]);
            if (checkIfUser(firstProperties))
                setUserStyle(player09, player09Score);
            else
                setOnlineUsersStyle(player09, player09Score, onlineUsers);
        }
        if (size > 9) {
            properties = getProperties(listOfUsers, 9);
            firstProperties = properties[0];
            player10.setText(firstProperties);
            player10Score.setText(properties[1]);
            if (checkIfUser(firstProperties))
                setUserStyle(player10, player10Score);
            else
                setOnlineUsersStyle(player10, player10Score, onlineUsers);
        }
        if (size > 10) {
            properties = getProperties(listOfUsers, 10);
            firstProperties = properties[0];
            player11.setText(firstProperties);
            player11Score.setText(properties[1]);
            if (checkIfUser(firstProperties))
                setUserStyle(player11, player11Score);
            else
                setOnlineUsersStyle(player11, player11Score, onlineUsers);
        }
        if (size > 11) {
            properties = getProperties(listOfUsers, 11);
            firstProperties = properties[0];
            player12.setText(firstProperties);
            player12Score.setText(properties[1]);
            if (checkIfUser(firstProperties))
                setUserStyle(player12, player12Score);
            else
                setOnlineUsersStyle(player12, player12Score, onlineUsers);
        }
        if (size > 12) {
            properties = getProperties(listOfUsers, 12);
            firstProperties = properties[0];
            player13.setText(firstProperties);
            player13Score.setText(properties[1]);
            if (checkIfUser(firstProperties))
                setUserStyle(player13, player13Score);
            else
                setOnlineUsersStyle(player13, player13Score, onlineUsers);
        }
        if (size > 13) {
            properties = getProperties(listOfUsers, 13);
            firstProperties = properties[0];
            player14.setText(firstProperties);
            player14Score.setText(properties[1]);
            if (checkIfUser(firstProperties))
                setUserStyle(player14, player14Score);
            else
                setOnlineUsersStyle(player14, player14Score, onlineUsers);
        }
        if (size > 14) {
            properties = getProperties(listOfUsers, 14);
            firstProperties = properties[0];
            player15.setText(firstProperties);
            player15Score.setText(properties[1]);
            if (checkIfUser(firstProperties))
                setUserStyle(player15, player15Score);
            else
                setOnlineUsersStyle(player15, player15Score, onlineUsers);
        }
        if (size > 15) {
            properties = getProperties(listOfUsers, 15);
            firstProperties = properties[0];
            player16.setText(firstProperties);
            player16Score.setText(properties[1]);
            if (checkIfUser(firstProperties))
                setUserStyle(player16, player16Score);
            else
                setOnlineUsersStyle(player16, player16Score, onlineUsers);
        }
        if (size > 16) {
            properties = getProperties(listOfUsers, 16);
            firstProperties = properties[0];
            player17.setText(firstProperties);
            player17Score.setText(properties[1]);
            if (checkIfUser(firstProperties))
                setUserStyle(player17, player17Score);
            else
                setOnlineUsersStyle(player17, player17Score, onlineUsers);
        }
        if (size > 17) {
            properties = getProperties(listOfUsers, 17);
            firstProperties = properties[0];
            player18.setText(firstProperties);
            player18Score.setText(properties[1]);
            if (checkIfUser(firstProperties))
                setUserStyle(player18, player18Score);
            else
                setOnlineUsersStyle(player18, player18Score, onlineUsers);
        }
        if (size > 18) {
            properties = getProperties(listOfUsers, 18);
            firstProperties = properties[0];
            player19.setText(firstProperties);
            player19Score.setText(properties[1]);
            if (checkIfUser(firstProperties))
                setUserStyle(player19, player19Score);
            else
                setOnlineUsersStyle(player19, player19Score, onlineUsers);
        }
        if (size > 19) {
            properties = getProperties(listOfUsers, 19);
            firstProperties = properties[0];
            player20.setText(firstProperties);
            player20Score.setText(properties[1]);
            if (checkIfUser(firstProperties))
                setUserStyle(player20, player20Score);
            else
                setOnlineUsersStyle(player20, player20Score, onlineUsers);
        }
    }

    private void a(List<String> listOfUsers, String onlineUsers, int size, int i, JFXTextArea player02, JFXTextArea player02Score) {
        String[] properties;
        String firstProperties;
        if (size > i) {
            properties = getProperties(listOfUsers, i);
            firstProperties = properties[0];
            player02.setText(firstProperties);
            player02Score.setText(properties[1]);
            if (checkIfUser(firstProperties))
                setUserStyle(player02, player02Score);
            else
                setOnlineUsersStyle(player02, player02Score, onlineUsers);
        }
    }

    private String[] getProperties(List<String> listOfUsers, int i) {
        return listOfUsers.get(i).split(" ,");
    }

    private void setUserStyle(JFXTextArea player, JFXTextArea score) {
        player.setStyle("-fx-background-color: #357264; -fx-font-style: italic");
        score.setStyle("-fx-background-color: #357264; -fx-font-style: italic");
    }

    public void setOnlineUsersStyle(JFXTextArea player, JFXTextArea score, String onlineUsers) {
//        System.out.println("in setOnlineUsersStyle     " + onlineUsers);
        String[] onlineUser = onlineUsers.split("\n");
//        System.out.println("herererereer    " + player.getText());
        for (String s : onlineUser) {
            System.out.println(1 + s + 1);
        }
        System.out.println("lenght   " + onlineUser.length);
        System.out.println(2 + player.getText().split("\\.")[1] + 2);
        if (player.getText().contains(".")) {
            System.out.println("I am here");
            for (int i = 0; i < onlineUser.length; i++) {
//                if (i != onlineUser.length - 1) {
//                    System.out.println(player.getText().split(".")[0]);
                System.out.println(1 + player.getText().split("\\.")[1] + 1);
                System.out.println(onlineUser[i]);
                if (player.getText().split("\\.")[1].equals(onlineUser[i])) {
                    System.out.println("I am here 2");
                    player.setStyle("-fx-background-color: #c46fe2; -fx-font-style: italic");
                    score.setStyle("-fx-background-color: #c46fe2; -fx-font-style: italic");
                }
//                }
            }
        }
    }

    private boolean checkIfUser(String toCheck) {
        return toCheck.contains("." + user.getNickname());
    }

//    public void getObservableList (){
//
//        for (int i = 0; i < scoreBoardList.size(); i++) {
//            if(i < 20)
//            topUsers.add(scoreBoardList.get(i));
//        }
//    }

    public void backButton(ActionEvent e) throws IOException {
        isBackButtonPushed = true;
        FXMLLoader loader = new FXMLLoader(new File
                ("./src/main/java/sample/view/mainMenu/MainMenuFxml.fxml").toURI().toURL());
//                ("src\\main\\java\\sample\\view\\mainMenu\\MainMenuFxml.fxml").toURI().toURL());
        Parent root = loader.load();
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

//    public void getOnlineUsernames() {

//        System.out.println("in getOnlineUsernames       " + onlineUsers);
//    }

}