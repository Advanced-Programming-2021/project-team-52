package sample.view.shop;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sample.controller.PrintBuilderController;
import sample.controller.ShopController;
import sample.model.Shop;
import sample.model.User;
import sample.model.cards.Cards;
import sample.model.tools.StringMessages;
import sample.view.UserKeeper;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class ShopViewController implements StringMessages, Initializable {
    ShopController shopController = ShopController.getInstance();
    PrintBuilderController printBuilderController = PrintBuilderController.getInstance();
    String cardNameInString;
    User user = UserKeeper.getInstance().getCurrentUser();
    @FXML
    JFXButton backButton;
    @FXML
    Pane shopScenePane;
    @FXML
    Label userBalanceInShopHeader;
    @FXML
    ProgressBar shopCardProgressBar;
    @FXML
    JFXTextArea searchInUserCardsTextArea, searchInShopTextArea;
    @FXML
    JFXButton buyButton, submitSearchInUserCards, submitSearchInShop;
    @FXML
    Label cardDoNotExistInUserCardsLabel, cardDoNotExistInShopLabel, cardNameLabelUnderUserCard,
            numberOfCardLabelUnderUserCard, cardNameLabelUnderShopCard, numberOfCardLabelUnderShopCard,
            userCardDetailsLabel, shopCardDetailsLabel, shopCardsLabel,userCardsLabel;
    @FXML
    ImageView userCardInfoImageView, shopCardInfoImageView;

    Scene scene;
    Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        shopScenePane.setStyle("-fx-background-color: black");


//        UserKeeper.getInstance().getCurrentUser().setBalance(1000000);
        userBalanceInShopHeader.setText(String.valueOf(UserKeeper.getInstance().getCurrentUser().getBalance()));


        buyButton.setStyle("-fx-border-width: 0 0 0.5 0; -fx-border-color: #ffff55");

        searchInShopTextArea.setStyle("-fx-prompt-text-fill: white");
        searchInUserCardsTextArea.setStyle("-fx-prompt-text-fill: white");
        searchInUserCardsTextArea.setStyle("-fx-text-fill: white");
        searchInShopTextArea.setStyle("-fx-text-fill: white");

        numberOfCardLabelUnderUserCard.setStyle("-fx-text-fill: white");
        numberOfCardLabelUnderShopCard.setStyle("-fx-text-fill: white");

        cardNameLabelUnderUserCard.setStyle("-fx-text-fill: white");
        cardNameLabelUnderShopCard.setStyle("-fx-text-fill: white");
        showAllCardsOfShop();
        showUnusedAllCardsOfUser();
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

    public void searchInUserCards(ActionEvent e) throws FileNotFoundException {
        String cardName = searchInUserCardsTextArea.getText().trim();
        Cards card = Cards.getCard(cardName);
        if (card == null) {
            cardDoNotExistInUserCardsLabel.setText("there is no card with this name");
            numberOfCardLabelUnderUserCard.setText("");
            userCardDetailsLabel.setText("");
            userCardInfoImageView.setImage(new Image(new FileInputStream("./src/main/resources/cardsInLowerCase/unknown.jpg")));
            return;
        }
        cardDoNotExistInUserCardsLabel.setText("");
        numberOfCardLabelUnderUserCard.setText(String.valueOf(shopController.getNumberOfThisCardOutOfDeck
                (user, cardName)));
        userCardDetailsLabel.setText(printBuilderController.showOneCard(card));
        userCardInfoImageView.setImage(new Image(new FileInputStream(ShopController.getCardImagePathByName(searchInUserCardsTextArea.getText()))));
//
//        if (cardDoNotExistInUserCardsLabel.isVisible()) {
//            cardDoNotExistInUserCardsLabel.setVisible(false);
//        }

//        if(UserKeeper.getInstance().getCurrentUser()
//                .getCards().contains(searchInUserCardsTextArea.getText().trim())){
//
//            cardNameLabelUnderUserCard.setText(searchInShopTextArea.getText().trim());
//            numberOfCardLabelUnderUserCard.setText(String.valueOf(UserKeeper.getInstance().
//                    getCurrentUser().getNumberOfThisCardInCardsOutOfDeck(searchInUserCardsTextArea.getText().trim())));
//
//            InputStream userCardImage = new
//                    FileInputStream(ShopController.getCardImagePathByName(searchInShopTextArea.getText().trim()));
//            userCardInfoImageView.setImage(new Image(userCardImage));


//        } else {
//            cardDoNotExistInUserCardsLabel.setVisible(true);
//        }


    }

    public void searchInShop(ActionEvent e) throws FileNotFoundException {
        String cardName = searchInShopTextArea.getText().trim();
        Cards card = Cards.getCard(cardName);
        if (card == null) {
            shopCardInfoImageView.setImage(new Image(new FileInputStream("./src/main/resources/cardsInLowerCase/unknown.jpg")));
            cardDoNotExistInShopLabel.setText("there is no card with this name");
            cardNameLabelUnderShopCard.setText("");
            numberOfCardLabelUnderShopCard.setText("");
            shopCardDetailsLabel.setText("");
            buyButton.setDisable(true);
            buyButton.setStyle("-fx-background-color: #ff5959");
            return;
        }
        shopCardInfoImageView.setImage(new Image(new FileInputStream(ShopController.getCardImagePathByName(searchInShopTextArea.getText()))));
        cardNameInString = cardName;
        cardDoNotExistInShopLabel.setText("");
        cardNameLabelUnderShopCard.setText(String.valueOf(shopController.getCardPriceByName(cardName)));
        numberOfCardLabelUnderShopCard.setText(String.valueOf(shopController.getNumberOfThisCardOutOfDeck
                (UserKeeper.getInstance().getCurrentUser(), cardName)));
        shopCardDetailsLabel.setText(printBuilderController.showOneCard(card));

        if(shopController.checkBeforeTransaction(cardName, UserKeeper.getInstance().getCurrentUser().getBalance())){
            buyButton.setDisable(false);
            buyButton.setStyle("-fx-background-color: #fff59");
        }else{
            buyButton.setStyle("-fx-background-color: #ff5959");
        }
//        if(cardDoNotExistInUserCardsLabel.isVisible()){
//            cardDoNotExistInUserCardsLabel.setVisible(false);
//        }
//        if(Cards.getAllNames().contains(searchInShopTextArea.getText().trim())){
//
//            cardNameLabelUnderShopCard.setText(searchInShopTextArea.getText().trim());
//            numberOfCardLabelUnderShopCard.setText(String.valueOf(UserKeeper.getInstance().
//                    getCurrentUser().getNumberOfThisCardInCardsOutOfDeck(searchInShopTextArea.getText().trim())));
//
//            InputStream shopCardImage = new
//                    FileInputStream(ShopController.getCardImagePathByName(searchInShopTextArea.getText().trim()));
//            shopCardInfoImageView.setImage(new Image(shopCardImage));


//            if(UserKeeper.getInstance().getCurrentUser().getBalance() >=
//                    shopController.getCardPriceByName(Cards.getCard(searchInShopTextArea.getText().trim()).getName())){
//                buyButton.setDisable(false);
//            } else {
//                buyButton.setDisable(true);
//                buyButton.setStyle("-fx-background-color: #ff5959");
//                buyButton.setText("Not enough money");
//            }
//
//        } else {
//            cardDoNotExistInShopLabel.setVisible(true);
//        }


        Color progressColor = shopController.getColorOfCardBasedOnPrice(cardName);
        if (progressColor == Color.AQUA) {
            shopCardProgressBar.setProgress(1);
            shopCardProgressBar.setStyle("-fx-progress-color: Aqua");
        } else if (progressColor == Color.GOLD) {
            shopCardProgressBar.setProgress(0.77);
            shopCardProgressBar.setStyle("-fx-progress-color: #ffd830");
        } else if (progressColor == Color.SILVER) {
            shopCardProgressBar.setProgress(0.6);
            shopCardProgressBar.setStyle("-fx-progress-color: #c8c8c8");
        } else {
            shopCardProgressBar.setProgress(0.28);
            shopCardProgressBar.setStyle("-fx-progress-color: #b03e3e");
        }

    }


    public void buyACard(ActionEvent e) {
        if(shopController.checkBeforeTransaction(cardNameInString, UserKeeper.getInstance().getCurrentUser().getBalance())){
            buyButton.setDisable(false);
            buyButton.setStyle("-fx-background-color: #fff59");
        }else{
            buyButton.setStyle("-fx-background-color: #ff5959");
            buyButton.setDisable(true);
        }
        String response = "";
        response =  shopController.buy(UserKeeper.getInstance().getCurrentUser(), cardNameInString);
        cardDoNotExistInShopLabel.setText(response);
        numberOfCardLabelUnderShopCard.setText(String.valueOf(shopController.getNumberOfThisCardOutOfDeck
                (user, cardNameInString)));
        userBalanceInShopHeader.setText(String.valueOf(user.getBalance()));
        showUnusedAllCardsOfUser();
        if(searchInUserCardsTextArea.getText().trim().equals(cardNameInString))
            numberOfCardLabelUnderUserCard.setText(String.valueOf(shopController.getNumberOfThisCardOutOfDeck
                    (user, cardNameInString)));
        if(shopController.checkBeforeTransaction(cardNameInString, UserKeeper.getInstance().getCurrentUser().getBalance())){
            buyButton.setDisable(false);
            buyButton.setStyle("-fx-background-color: #fff59");
        }else{
            buyButton.setStyle("-fx-background-color: #ff5959");
            buyButton.setDisable(true);
        }
    }

    public void showAllCardsOfShop(){
        shopCardsLabel.setText(Shop.getInstance().getAllCardsWithPrice());
    }

    public void showUnusedAllCardsOfUser(){
        userCardsLabel.setText(shopController.getAllUnusedCardsByString(user));
    }


}
