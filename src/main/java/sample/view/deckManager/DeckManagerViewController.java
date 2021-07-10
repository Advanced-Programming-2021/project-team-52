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
import sample.controller.DeckController;
import sample.controller.PrintBuilderController;
import sample.controller.ShopController;
import sample.model.User;
import sample.model.cards.Cards;
import sample.model.tools.StringMessages;
import sample.view.UserKeeper;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DeckManagerViewController implements Initializable {
    CardCreatorController cardCreatorController = CardCreatorController.getInstance();
    DeckController deckController = DeckController.getInstance();
    ShopController shopController = ShopController.getInstance();
    User user = UserKeeper.getInstance().getCurrentUser();
    PrintBuilderController printBuilderController = PrintBuilderController.getInstance();
    String cardAdditionInString;
    String cardRemoveInString;
    String deckNameInString;

    Scene scene;
    Stage stage;

    @FXML
    Pane deckManagerPane;
    @FXML
    JFXTextArea monsterAttributeTextArea, cardToGetSpecialFromTextArea, cardDescriptionTextArea, cardNameTextArea,
            cardNameToAddTextArea, cardNameToRemoveTextArea, deckSearchTextArea;
    @FXML
    JFXRadioButton trapRadiobutton, monsterRadiobutton, spellRadiobutton, limitedRadioButton,
            halfLimitedRadioButton, unlimitedRadioButton;
    @FXML
    Label LVLLabel, ATKLabel, DEFLabel, SpeedLabel, deckSearchStatusLabel;
    @FXML
    JFXButton submitCardTypeButton, submitCardNameButton, submitCardDescriptionButton, submitCardToGetSpecialFromButton,
            submitMonsterLVLButton, submitMonsterATKButton, submitMonsterDEFButton, submitCardStatusButton, submitSpeedButton,
            submitMonsterAttributeButton, submitCardMakingProcessButton;
    @FXML
    JFXSlider LVLBar, ATKBar, DEFBar, SpeedBar;

    @FXML
    Label situationAndPriceLabel;

    @FXML
    JFXTextArea newDeckNameTextField;
    @FXML
    JFXButton submitNewDeckNameButton, deleteDeckButton, submitAddCardSearchButton, submitRemoveCardSearchButton,
            addToSideButton, addToMainButton, removeFromMainButton, removeFromSideButton;
    @FXML
    Label newDeckNameSituationLabel, mainDeckLabel, sideDeckLabel, cardAdditionSituationLabel,
            numberOfAvailableCardToAddLabel, numberOfAvailableCardToRemoveLabel,allDecksLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        newDeckNameTextField.setStyle("-fx-prompt-text-fill: white; -fx-text-fill: white");
        deckManagerPane.setStyle("-fx-background-color: black");
        monsterAttributeTextArea.setStyle("-fx-prompt-text-fill: white; -fx-text-fill: white");
        cardToGetSpecialFromTextArea.setStyle("-fx-prompt-text-fill: white; -fx-text-fill: white");
        cardDescriptionTextArea.setStyle("-fx-prompt-text-fill: white; -fx-text-fill: white");
        cardNameTextArea.setStyle("-fx-prompt-text-fill: white; -fx-text-fill: white");
        cardNameToAddTextArea.setStyle("-fx-prompt-text-fill: white");
        cardNameToRemoveTextArea.setStyle("-fx-prompt-text-fill: white");
        deckSearchTextArea.setStyle("-fx-prompt-text-fill: white; -fx-text-fill: white");
        showAllDecksOfUser();
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

    ///////////////////////////////////////////////////////////////////////////////////////Deck Management Start

    public void createDeck(ActionEvent e) {
        String response = "";
        response = deckController.createDeck(newDeckNameTextField.getText(), user);
        newDeckNameSituationLabel.setText(response);
        showAllDecksOfUser();
    }

    public void submitDeckSearch(ActionEvent e) {
        String deckName = deckSearchTextArea.getText().trim();
        String mainDeckResponse = "";
        String sideDeckResponse = "";
        sideDeckResponse = deckController.getSideDeckByString(deckName, user);
        mainDeckResponse = deckController.getMainDeckByString(deckName, user);
        if (mainDeckResponse.equals(printBuilderController.deckWithThisNameDoesNotExist(deckName))) {

        } else {
            deckNameInString = deckName;
            mainDeckLabel.setText(mainDeckResponse);
            sideDeckLabel.setText(sideDeckResponse);
            newDeckNameSituationLabel.setText("");
            newDeckNameTextField.setText("");
        }
    }

    public void deleteDeck(ActionEvent e) {
        String deckName = deckSearchTextArea.getText();
        String response = "";
        response = deckController.deleteDeck(deckName, user);
        cardAdditionSituationLabel.setText(response);
        if (response.equals(StringMessages.deckDeletedSuccessfully)) {
            mainDeckLabel.setText("");
            sideDeckLabel.setText("");
            numberOfAvailableCardToAddLabel.setText("No");
            numberOfAvailableCardToRemoveLabel.setText("No");
            deckSearchTextArea.setText("");
            showAllDecksOfUser();
        }
    }

    public void submitAddCardSearch(ActionEvent e) {
        String cardName = cardNameToAddTextArea.getText().trim();
        Cards card = Cards.getCard(cardName);
        if(card == null){
            numberOfAvailableCardToAddLabel.setText("No");
            cardAdditionSituationLabel.setText("there is no card with this name");
            return;
        }
        cardAdditionInString= cardName;
        numberOfAvailableCardToAddLabel.setText(String.valueOf(shopController.getNumberOfThisCardOutOfDeck
                (user, cardName)));
        cardAdditionSituationLabel.setText("");
    }

    public void addCardToMain(ActionEvent e){
        String response = "";
        response = deckController.addCardToDeck(cardAdditionInString,deckNameInString,false,user);
        if(response.equals(StringMessages.cardAddedToDeckSuccessfully)){
            cardAdditionSituationLabel.setText(response);
            mainDeckLabel.setText(deckController.getMainDeckByString(deckNameInString, user));
            numberOfAvailableCardToRemoveLabel.setText(String.valueOf(shopController.getNumberOfThisCardOutOfDeck
                    (user, cardRemoveInString)));
            numberOfAvailableCardToAddLabel.setText(String.valueOf(shopController.getNumberOfThisCardOutOfDeck
                    (user, cardAdditionInString)));
            showAllDecksOfUser();
        }
        else{
            cardAdditionSituationLabel.setText(response);
        }
    }

    public void addCardToSide(ActionEvent e){
        String response = "";
        response = deckController.addCardToDeck(cardAdditionInString,deckNameInString,true,user);
        if(response.equals(StringMessages.cardAddedToDeckSuccessfully)){
            cardAdditionSituationLabel.setText(response);
            sideDeckLabel.setText(deckController.getSideDeckByString(deckNameInString, user));
            numberOfAvailableCardToRemoveLabel.setText(String.valueOf(shopController.getNumberOfThisCardOutOfDeck
                    (user, cardRemoveInString)));
            numberOfAvailableCardToAddLabel.setText(String.valueOf(shopController.getNumberOfThisCardOutOfDeck
                    (user, cardAdditionInString)));
            showAllDecksOfUser();
        }
        else{
        cardAdditionSituationLabel.setText(response);
        }
    }

    public void submitRemoveCardSearch(ActionEvent e){
        String cardName = cardNameToRemoveTextArea.getText().trim();
        Cards card = Cards.getCard(cardName);
        if(card == null){
            numberOfAvailableCardToRemoveLabel.setText("No");
            cardAdditionSituationLabel.setText("there is no card with this name");
            return;
        }
        cardRemoveInString = cardName;
        numberOfAvailableCardToRemoveLabel.setText(String.valueOf(shopController.getNumberOfThisCardOutOfDeck
                (user, cardName)));
        cardAdditionSituationLabel.setText("");
    }

    public void removeCardFromMain(ActionEvent e){
        String response = "";
        response = deckController.removeCardFromDeck(cardRemoveInString, deckNameInString, false, user);
        if(response.equals(StringMessages.cardRemovedFormDeckSuccessfully)){
            cardAdditionSituationLabel.setText(response);
            mainDeckLabel.setText(deckController.getMainDeckByString(deckNameInString, user));
            numberOfAvailableCardToRemoveLabel.setText(String.valueOf(shopController.getNumberOfThisCardOutOfDeck
                    (user, cardRemoveInString)));
            numberOfAvailableCardToAddLabel.setText(String.valueOf(shopController.getNumberOfThisCardOutOfDeck
                    (user, cardAdditionInString)));
            showAllDecksOfUser();
        }
        else{
            cardAdditionSituationLabel.setText(response);
        }
    }

    public void removeCardFromSide(ActionEvent e){
        String response = "";
        response = deckController.removeCardFromDeck(cardRemoveInString, deckNameInString, true, user);
        if(response.equals(StringMessages.cardRemovedFormDeckSuccessfully)){
            cardAdditionSituationLabel.setText(response);
            sideDeckLabel.setText(deckController.getSideDeckByString(deckNameInString, user));
            numberOfAvailableCardToRemoveLabel.setText(String.valueOf(shopController.getNumberOfThisCardOutOfDeck
                    (user, cardRemoveInString)));
            numberOfAvailableCardToAddLabel.setText(String.valueOf(shopController.getNumberOfThisCardOutOfDeck
                    (user, cardAdditionInString)));
            showAllDecksOfUser();
        }
        else{
            cardAdditionSituationLabel.setText(response);
        }
    }

    public void activateDeck(ActionEvent e){
        cardAdditionSituationLabel.setText(deckController.activateDeck(deckNameInString,user));
        showAllDecksOfUser();
    }

    public void showAllDecksOfUser(){
        allDecksLabel.setText(deckController.showAllDecks(user));
    }
}
