package sample.view.deckManager;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
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
    JFXTextArea monsterAttributeTextArea,cardToGetSpecialFromTextArea,cardDescriptionTextArea,cardNameTextArea,
            cardNameToAddTextArea, cardNameToRemoveTextArea;
    @FXML
    JFXRadioButton trapRadiobutton, monsterRadiobutton, spellRadiobutton, limitedRadioButton,
    halfLimitedRadioButton,unlimitedRadioButton;
    @FXML
    Label LVLLabel, ATKLabel, DEFLabel, SpeedLabel, numberOfAvailableCardToAddLabel;
    @FXML
    JFXButton submitCardTypeButton, submitCardNameButton, submitCardDescriptionButton, submitCardToGetSpecialFromButton,
    submitMonsterLVLButton, submitMonsterATKButton, submitMonsterDEFButton, submitCardStatusButton, submitSpeedButton,
    submitMonsterAttributeButton, add1ToMainButton;
    @FXML
    JFXSlider LVLBar, ATKBar, DEFBar, SpeedBar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        deckManagerPane.setStyle("-fx-background-color: black");
        monsterAttributeTextArea.setStyle("-fx-prompt-text-fill: white; -fx-text-fill: white");
        cardToGetSpecialFromTextArea.setStyle("-fx-prompt-text-fill: white; -fx-text-fill: white");
        cardDescriptionTextArea.setStyle("-fx-prompt-text-fill: white; -fx-text-fill: white");
        cardNameTextArea.setStyle("-fx-prompt-text-fill: white; -fx-text-fill: white");
        cardNameToAddTextArea.setStyle("-fx-prompt-text-fill: aqua");
        cardNameToRemoveTextArea.setStyle("-fx-prompt-text-fill: aqua");
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

    public void submitCardTypeAndSetting(ActionEvent e){
        if(monsterRadiobutton.isSelected()){

            submitSpeedButton.setVisible(false);
            SpeedLabel.setVisible(false);
            SpeedBar.setVisible(false);

            LVLBar.setVisible(true);
            ATKBar.setVisible(true);
            DEFBar.setVisible(true);

            LVLLabel.setVisible(true);
            ATKLabel.setVisible(true);
            DEFLabel.setVisible(true);

            submitMonsterLVLButton.setVisible(true);
            submitMonsterATKButton.setVisible(true);
            submitMonsterDEFButton.setVisible(true);
            submitCardStatusButton.setVisible(true);

            monsterAttributeTextArea.setVisible(true);
            submitMonsterAttributeButton.setVisible(true);
        } else {
            LVLBar.setVisible(false);
            ATKBar.setVisible(false);
            DEFBar.setVisible(false);

            LVLLabel.setVisible(false);
            ATKLabel.setVisible(false);
            DEFLabel.setVisible(false);

            submitMonsterLVLButton.setVisible(false);
            submitMonsterATKButton.setVisible(false);
            submitMonsterDEFButton.setVisible(false);
            submitCardStatusButton.setVisible(false);

            monsterAttributeTextArea.setVisible(false);
            submitMonsterAttributeButton.setVisible(false);

            submitSpeedButton.setVisible(true);
            SpeedLabel.setVisible(true);
            SpeedBar.setVisible(true);
        }
    }
}
