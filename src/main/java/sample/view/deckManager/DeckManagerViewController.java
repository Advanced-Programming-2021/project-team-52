package sample.view.deckManager;

import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DeckManagerViewController implements Initializable {

    Scene scene;
    Stage stage;

    @FXML
    Pane deckManagerPane;
    @FXML
    JFXTextArea monsterAttributeTextArea,cardToGetSpecialFromTextArea,cardDescriptionTextArea,cardNameTextArea;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        deckManagerPane.setStyle("-fx-background-color: black");
        monsterAttributeTextArea.setStyle("-fx-prompt-text-fill: white; -fx-text-fill: white");
        cardToGetSpecialFromTextArea.setStyle("-fx-prompt-text-fill: white; -fx-text-fill: white");
        cardDescriptionTextArea.setStyle("-fx-prompt-text-fill: white; -fx-text-fill: white");
        cardNameTextArea.setStyle("-fx-prompt-text-fill: white; -fx-text-fill: white");
    }

    public void backButton(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(new File
                ("./src/main/java/sample/view/mainMenu/MainMenuFxml.fxml").toURI().toURL());
//                ("src\\main\\java\\sample\\view\\mainMenu\\MainMenuFxml.fxml").toURI().toURL());
        Parent root = loader.load();
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
