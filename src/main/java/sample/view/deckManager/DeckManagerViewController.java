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
import sample.controller.CardCreatorController;
import sample.view.UserKeeper;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DeckManagerViewController implements Initializable {
    CardCreatorController cardCreatorController = CardCreatorController.getInstance();

    Scene scene;
    Stage stage;

    @FXML
    Pane deckManagerPane;
    @FXML
    JFXTextArea monsterAttributeTextArea, cardToGetSpecialFromTextArea, cardDescriptionTextArea, cardNameTextArea,
            cardNameToAddTextArea, cardNameToRemoveTextArea;
    @FXML
    JFXRadioButton trapRadiobutton, monsterRadiobutton, spellRadiobutton, limitedRadioButton,
            halfLimitedRadioButton, unlimitedRadioButton;
    @FXML
    Label LVLLabel, ATKLabel, DEFLabel, SpeedLabel, numberOfAvailableCardToAddLabel;
    @FXML
    JFXButton submitCardTypeButton, submitCardNameButton, submitCardDescriptionButton, submitCardToGetSpecialFromButton,
            submitMonsterLVLButton, submitMonsterATKButton, submitMonsterDEFButton, submitCardStatusButton, submitSpeedButton,
            submitMonsterAttributeButton, submitCardMakingProcessButton, add1ToMainButton;
    @FXML
    JFXSlider LVLBar, ATKBar, DEFBar, SpeedBar;

    @FXML
    Label situationAndPriceLabel;

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
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void submitCardTypeAndSetting(ActionEvent e) {
        if (monsterRadiobutton.isSelected()) {

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

            submitCardStatusButton.setVisible(true);
            submitSpeedButton.setVisible(true);
            SpeedLabel.setVisible(true);
            SpeedBar.setVisible(true);
        }
    }


    public void submitCardType(ActionEvent e) {
//        if (monsterRadiobutton.isSelected())
//            cardType = "Monster";
//        else if (spellRadiobutton.isSelected())
//            cardType = "Spell";
//        else if (trapRadiobutton.isSelected())
//            cardType = "Trap";
    }


    public void submitCardName(ActionEvent e) {
        String name = cardNameTextArea.getText();
        if (!name.isEmpty())
            cardCreatorController.setName(name);
    }

    public void submitCardDescription(ActionEvent e) {
        String description = cardDescriptionTextArea.getText();
        if (!description.isEmpty())
            cardCreatorController.setDescription(description);
    }

    public void submitCardToGetSpecialFrom(ActionEvent e) {
        String CardName = cardToGetSpecialFromTextArea.getText();
        String response = "";
        if (monsterRadiobutton.isSelected())
            response = cardCreatorController.specialCounterForMonster(CardName);
        else if (spellRadiobutton.isSelected())
            response = cardCreatorController.specialCounterForSpell(CardName);
        else if (trapRadiobutton.isSelected())
            response = cardCreatorController.specialCounterForTrap(CardName);
        else
            return;
        situationAndPriceLabel.setText(response);
    }

    public void submitCardStatus(ActionEvent e) {
        String response = "";
        if (monsterRadiobutton.isSelected())
            response = submitCardStatusForMonsters();
        else if (spellRadiobutton.isSelected())
            response = submitCardStatusForSpellAndTraps();
        else if (trapRadiobutton.isSelected())
            response = submitCardStatusForSpellAndTraps();
        else
            return;
        situationAndPriceLabel.setText(response);
    }

    public String submitCardStatusForMonsters() {

        String response = "";
        if (unlimitedRadioButton.isSelected())
            response = cardCreatorController.statusCounterForMonsters("Unlimited");
        else if (halfLimitedRadioButton.isSelected())
            response = cardCreatorController.statusCounterForMonsters("Half limited");
        else if (limitedRadioButton.isSelected())
            response = cardCreatorController.statusCounterForMonsters("Limited");
        return response;

    }

    public String submitCardStatusForSpellAndTraps() {
        String response = "";
        if (unlimitedRadioButton.isSelected())
            response = cardCreatorController.statusCounterForSpellAndTrap("Unlimited");
        else if (halfLimitedRadioButton.isSelected())
            response = cardCreatorController.statusCounterForSpellAndTrap("Half limited");
        else if (limitedRadioButton.isSelected())
            response = cardCreatorController.statusCounterForSpellAndTrap("Limited");
        return response;
    }


    public void submitMonsterLVL(ActionEvent e) {
        String response = "";
        response = cardCreatorController.levelCounter(String.valueOf((int) LVLBar.getValue()));
        situationAndPriceLabel.setText(response);
    }

    public void submitMonsterATK(ActionEvent e) {
        String response = "";
        response = cardCreatorController.attackCounter(String.valueOf((int) ATKBar.getValue()));
        situationAndPriceLabel.setText(response);
    }

    public void submitMonsterDEF(ActionEvent e) {
        String response = "";
        response = cardCreatorController.defendCounter(String.valueOf((int) DEFBar.getValue()));
        situationAndPriceLabel.setText(response);
    }

    public void submitMonsterAttribute(ActionEvent e) {
        String response = "";
        response = cardCreatorController.setAttribute(monsterAttributeTextArea.getText());
//        if (response.equals(StringMessages.invalidInputForCardCreator))
        situationAndPriceLabel.setText(response);
    }


    public void submitSpeed(ActionEvent e) {
        String response = "";
        response = cardCreatorController.speedCounter(String.valueOf((int) SpeedBar.getValue()));
        situationAndPriceLabel.setText(response);
    }

    public void submitCardMakingProcess(ActionEvent e) {
        String response = "";
        if (monsterRadiobutton.isSelected())
            response = cardCreatorController.createMonsterCard(UserKeeper.getInstance().getCurrentUser());
        else if (spellRadiobutton.isSelected())
            response = cardCreatorController.createSpellCard(UserKeeper.getInstance().getCurrentUser());
        else if (trapRadiobutton.isSelected())
            response = cardCreatorController.createTrapCard(UserKeeper.getInstance().getCurrentUser());
        else
            return;
        situationAndPriceLabel.setText(response);
    }
}
