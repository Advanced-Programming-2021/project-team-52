package sample.view.scoreBoard;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.controller.LoginController;
import sample.controller.ScoreBoardController;
import sample.model.User;
import sample.view.UserKeeper;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class ScoreboardViewController implements Initializable {
    ScoreBoardController scoreBoardController = ScoreBoardController.getInstance();
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
    Label player01Score, player02Score, player03Score, player04Score, player05Score, player06Score, player07Score,
            player08Score, player09Score, player10Score, player11Score, player12Score, player13Score, player14Score,
            player15Score, player16Score, player17Score, player18Score, player19Score, player20Score;

    @FXML
    Label player01, player02, player03, player04, player05, player06, player07, player08, player09, player10,
            player11, player12, player13, player14, player15, player16, player17, player18, player19, player20;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        LoginController.getUserByUsername("ali").setScore(1000);
//        LoginController.getUserByUsername("mamad").setScore(100);
//        LoginController.getUserByUsername("reza").setScore(100);
//        LoginController.getUserByUsername("asghar").setScore(0);
//        LoginController.getUserByUsername("haji").setScore(10);
        scoreBoardScenePane.setStyle("-fx-background-image: url(media/images/backgrounds/scoreBoardWallpaper.png); -fx-background-color: black");

        String topUsersInOneString = scoreBoardController.toString();
        String[] eachUser = topUsersInOneString.split("\n");
        List<String> listOfUsers = Arrays.asList(eachUser);
        int size = listOfUsers.size();

//        player10.setStyle("-fx-background-color: #357264; -fx-font-style: italic");
//        player10Score.setStyle("-fx-background-color: #357264; -fx-font-style: italic");
//        player10Score.setStyle("-fx-background-color: #9b5656; -fx-font-style: italic");
//        player20Score.setStyle("-fx-background-color: #ffffff; -fx-font-style: italic");
//        player20Score     .setStyle("-fx-text-fill: brown; -fx-font-style: italic");

        String[] properties;
        String firstProperties;
        if (size > 0) {
            properties = getProperties(listOfUsers, 0);
            firstProperties = properties[0];
            player01.setText(firstProperties);
            player01Score.setText(properties[1]);
            if (checkIfUser(firstProperties))
                setUserStyle(player01, player01Score);
        }
        if (size > 1) {
            properties = getProperties(listOfUsers, 1);
            firstProperties = properties[0];
            player02.setText(firstProperties);
            player02Score.setText(properties[1]);
            if (checkIfUser(firstProperties))
                setUserStyle(player02, player02Score);
        }
        if (size > 2) {
            properties = getProperties(listOfUsers, 2);
            firstProperties = properties[0];
            player03.setText(firstProperties);
            player03Score.setText(properties[1]);
            if (checkIfUser(firstProperties))
                setUserStyle(player03, player03Score);
        }
        if (size > 3) {
            properties = getProperties(listOfUsers, 3);
            firstProperties = properties[0];
            player04.setText(firstProperties);
            player04Score.setText(properties[1]);
            if (checkIfUser(firstProperties))
                setUserStyle(player04, player04Score);
        }
        if (size > 4) {
            properties = getProperties(listOfUsers, 4);
            firstProperties = properties[0];
            player05.setText(firstProperties);
            player05Score.setText(properties[1]);
            if (checkIfUser(firstProperties))
                setUserStyle(player05, player05Score);
        }
        if (size > 5) {
            properties = getProperties(listOfUsers, 5);
            firstProperties = properties[0];
            player06.setText(firstProperties);
            player06Score.setText(properties[1]);
            if (checkIfUser(firstProperties))
                setUserStyle(player06, player06Score);
        }
        if (size > 6) {
            properties = getProperties(listOfUsers, 6);
            firstProperties = properties[0];
            player07.setText(firstProperties);
            player07Score.setText(properties[1]);
            if (checkIfUser(firstProperties))
                setUserStyle(player07, player07Score);
        }
        if (size > 7) {
            properties = getProperties(listOfUsers, 7);
            firstProperties = properties[0];
            player08.setText(firstProperties);
            player08Score.setText(properties[1]);
            if (checkIfUser(firstProperties))
                setUserStyle(player08, player08Score);
        }
        if (size > 8) {
            properties = getProperties(listOfUsers, 8);
            firstProperties = properties[0];
            player09.setText(firstProperties);
            player09Score.setText(properties[1]);
            if (checkIfUser(firstProperties))
                setUserStyle(player09, player09Score);
        }
        if (size > 9) {
            properties = getProperties(listOfUsers, 9);
            firstProperties = properties[0];
            player10.setText(firstProperties);
            player10Score.setText(properties[1]);
            if (checkIfUser(firstProperties))
                setUserStyle(player10, player10Score);
        }
        if (size > 10) {
            properties = getProperties(listOfUsers, 10);
            firstProperties = properties[0];
            player11.setText(firstProperties);
            player11Score.setText(properties[1]);
            if (checkIfUser(firstProperties))
                setUserStyle(player11, player11Score);
        }
        if (size > 11) {
            properties = getProperties(listOfUsers, 11);
            firstProperties = properties[0];
            player12.setText(firstProperties);
            player12Score.setText(properties[1]);
            if (checkIfUser(firstProperties))
                setUserStyle(player12, player12Score);
        }
        if (size > 12) {
            properties = getProperties(listOfUsers, 12);
            firstProperties = properties[0];
            player13.setText(firstProperties);
            player13Score.setText(properties[1]);
            if (checkIfUser(firstProperties))
                setUserStyle(player13, player13Score);
        }
        if (size > 13) {
            properties = getProperties(listOfUsers, 13);
            firstProperties = properties[0];
            player14.setText(firstProperties);
            player14Score.setText(properties[1]);
            if (checkIfUser(firstProperties))
                setUserStyle(player14, player14Score);
        }
        if (size > 14) {
            properties = getProperties(listOfUsers, 14);
            firstProperties = properties[0];
            player15.setText(firstProperties);
            player15Score.setText(properties[1]);
            if (checkIfUser(firstProperties))
                setUserStyle(player15, player15Score);
        }
        if (size > 15) {
            properties = getProperties(listOfUsers, 15);
            firstProperties = properties[0];
            player16.setText(firstProperties);
            player16Score.setText(properties[1]);
            if (checkIfUser(firstProperties))
                setUserStyle(player16, player16Score);
        }
        if (size > 16) {
            properties = getProperties(listOfUsers, 16);
            firstProperties = properties[0];
            player17.setText(firstProperties);
            player17Score.setText(properties[1]);
            if (checkIfUser(firstProperties))
                setUserStyle(player17, player17Score);
        }
        if (size > 17) {
            properties = getProperties(listOfUsers, 17);
            firstProperties = properties[0];
            player18.setText(firstProperties);
            player18Score.setText(properties[1]);
            if (checkIfUser(firstProperties))
                setUserStyle(player18, player18Score);
        }
        if (size > 18) {
            properties = getProperties(listOfUsers, 18);
            firstProperties = properties[0];
            player19.setText(firstProperties);
            player19Score.setText(properties[1]);
            if (checkIfUser(firstProperties))
                setUserStyle(player19, player19Score);
        }
        if (size > 19) {
            properties = getProperties(listOfUsers, 19);
            firstProperties = properties[0];
            player20.setText(firstProperties);
            player20Score.setText(properties[1]);
            if (checkIfUser(firstProperties))
                setUserStyle(player20, player20Score);
        }
    }

    private String[] getProperties(List<String> listOfUsers, int i) {
        return listOfUsers.get(i).split(",");
    }

    private void setUserStyle(Label player, Label score) {
        player.setStyle("-fx-background-color: #357264; -fx-font-style: italic");
        score.setStyle("-fx-background-color: #357264; -fx-font-style: italic");
    }

    private boolean checkIfUser(String toCheck) {
        return toCheck.contains("." + user.getNickname() + " ");
    }

//    public void getObservableList (){
//
//        for (int i = 0; i < scoreBoardList.size(); i++) {
//            if(i < 20)
//            topUsers.add(scoreBoardList.get(i));
//        }
//    }

    public void backButton(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(new File
                ("./src/main/java/sample/view/mainMenu/MainMenuFxml.fxml").toURI().toURL());
//                ("src\\main\\java\\sample\\view\\mainMenu\\MainMenuFxml.fxml").toURI().toURL());
        Parent root = loader.load();
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}