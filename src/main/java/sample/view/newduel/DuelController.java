package sample.view.newduel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import sample.view.gameboardview.GameBoardHandler;
import sample.view.sender.Sender;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

public class DuelController implements Initializable{

    @FXML
    private RadioButton oneRound, threeRounds;

    @FXML
    private Label errorLabel;

    private String rounds = "1";

    private GameBoardHandler gameBoardHandler;

    private Sender sender = Sender.getInstance();;

    @FXML
    Pane startGame;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ToggleGroup toggleGroup = new ToggleGroup();
        oneRound.setToggleGroup(toggleGroup);
        threeRounds.setToggleGroup(toggleGroup);
        errorLabel.setWrapText(true);
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
        errorLabel.setText(sender.getResponse(sender.setMessageWithToken("-ND-", "startANewGame", rounds)));
        GameBoardHandler gameBoardHandler = new GameBoardHandler((Stage) ((Node) actionEvent.getSource()).getScene().getWindow());
        this.gameBoardHandler = gameBoardHandler;
        Thread thread = new Thread(gameBoardHandler);
        thread.setDaemon(true);
        thread.start();
//        }
    }

    @FXML
    private void changeRounds(ActionEvent actionEvent){
        if (oneRound.isSelected())
            rounds = "1";
        else rounds = "3";
    }

    @FXML
    private void back(ActionEvent actionEvent){
        try {
            if (gameBoardHandler != null){
                gameBoardHandler.setRun(false);
            }
            sender.send(sender.setMessageWithToken("-ND-", "end", rounds));
            Parent root = FXMLLoader.load(new File("./src/main/java/sample/view/mainMenu/MainMenuFxml.fxml").toURI().toURL());
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
