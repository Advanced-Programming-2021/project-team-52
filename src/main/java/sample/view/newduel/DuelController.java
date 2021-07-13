package sample.view.newduel;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import sample.controller.NewDuelController;
import sample.view.UserKeeper;
import sample.view.mainMenu.MainMenuController;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

public class DuelController implements Initializable{

    @FXML
    private RadioButton oneRound, threeRounds;

    @FXML
    private TextField opponentUsername;

    @FXML
    private Label errorLabel;

    private int rounds = 1;

    @FXML
    Pane startGame;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ToggleGroup toggleGroup = new ToggleGroup();
        oneRound.setToggleGroup(toggleGroup);
        threeRounds.setToggleGroup(toggleGroup);
        startGame.setId("startGame");
        try {
            startGame.getStylesheets().add(new File
                    ("./src/main/resources/cssFiles/NewDuelViewCss.css").toURI().toURL().toExternalForm());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void startGame(ActionEvent actionEvent){
        if (opponentUsername.getText().isEmpty())
            errorLabel.setText("please enter a username");
        else {
            NewDuelController newDuelController = new NewDuelController(UserKeeper.getInstance().getCurrentUser(),
                    (Stage) ((Node) actionEvent.getSource()).getScene().getWindow());
            String result = newDuelController.run("duel -n -r " + rounds + " -sp " + opponentUsername.getText());
            errorLabel.setText(result);
        }
    }

    @FXML
    private void changeRounds(ActionEvent actionEvent){
        if (oneRound.isSelected())
            rounds = 1;
        else rounds = 3;
    }

    @FXML
    private void back(ActionEvent actionEvent){
        try {
            Parent root = FXMLLoader.load(new File("./src/main/java/sample/view/mainMenu/MainMenuFxml.fxml").toURI().toURL());
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            MainMenuController.getGameBackGroundMediaPlayer().stop();
            MainMenuController.getAfterGameMediaPlayer().setCycleCount(MediaPlayer.INDEFINITE);
            MainMenuController.getAfterGameMediaPlayer().play();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
