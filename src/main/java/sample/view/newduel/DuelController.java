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
import javafx.stage.Stage;
import sample.controller.NewDuelController;
import sample.view.UserKeeper;
import sample.view.gameboardview.GameBoardHandler;
import sample.view.sender.Sender;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DuelController implements Initializable{

    @FXML
    private RadioButton oneRound, threeRounds;

    @FXML
    private TextField opponentUsername;

    @FXML
    private Label errorLabel;

    private String rounds = "1";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ToggleGroup toggleGroup = new ToggleGroup();
        oneRound.setToggleGroup(toggleGroup);
        threeRounds.setToggleGroup(toggleGroup);
    }

    @FXML
    private void startGame(ActionEvent actionEvent){
        if (opponentUsername.getText().isEmpty())
            errorLabel.setText("please enter a username");
        else {
//            NewDuelController newDuelController = new NewDuelController(UserKeeper.getInstance().getCurrentUser(),
//                    (Stage) ((Node) actionEvent.getSource()).getScene().getWindow());
//            String result = newDuelController.run("duel -n -r " + rounds + " -sp " + opponentUsername.getText());
            Sender sender = Sender.getInstance();
//            String result = NewDuelController.newDuel(opponentUsername.getText(), rounds);
            errorLabel.setText(sender.getResponse(sender.setMessageWithToken("-ND-", rounds)));
            GameBoardHandler gameBoardHandler = new GameBoardHandler((Stage) ((Node) actionEvent.getSource()).getScene().getWindow());
            Thread thread = new Thread(gameBoardHandler);
            thread.setDaemon(true);
            thread.start();
        }
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
            Parent root = FXMLLoader.load(new File("./src/main/java/sample/view/mainMenu/MainMenuFxml.fxml").toURI().toURL());
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
