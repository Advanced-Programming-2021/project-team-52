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
import sample.controller.AuctionController;
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
import java.util.Timer;
import java.util.TimerTask;

public class ShopViewController implements StringMessages, Initializable {
    ShopController shopController = ShopController.getInstance();
    AuctionController auctionController = AuctionController.getInstance();
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
    JFXTextArea searchInUserCardsTextArea, searchInShopTextArea, userCardsLabel;
    @FXML
    JFXButton buyButton, submitSearchInUserCards, submitSearchInShop;
    @FXML
    Label cardDoNotExistInUserCardsLabel, cardDoNotExistInShopLabel, cardNameLabelUnderUserCard,
            numberOfCardLabelUnderUserCard, cardNameLabelUnderShopCard, numberOfCardLabelUnderShopCard,
            userCardDetailsLabel, shopCardDetailsLabel, shopCardsLabel;
    @FXML
    ImageView userCardInfoImageView, shopCardInfoImageView;

    @FXML
    JFXTextArea auctionPanelSearchACardToAuctionTextArea, auctionPanelSetAPriceToAuctionTextArea, auctionPanelAllCardsInAuctionTextArea,
            auctionPanelSearchAuctionIdTextArea, auctionPanelPriceOfAuctionedCardTextArea, auctionPanelTimeLeftToBidTextArea,
            adminPanelUsernameTextArea, adminPanelPasswordTextArea, adminPanelCardSearchTextArea, suggestedCardTextArea;

    @FXML
    Label auctionPanelSearchCArdToAuctionSituationLabel, auctionPanelSearchAuctionIdSituationLabel;

    boolean isInShopMenu = true;


    Scene scene;
    Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        isInShopMenu = true;
        new Thread(() -> {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (isInShopMenu) {
                        getAllActiveAuctions();
//                    userBalanceInShopHeader.setText(String.valueOf(user.getBalance()));
                        showUnusedAllCardsOfUser();
                    } else {
                        timer.cancel();
                        timer.purge();
                    }
                }
            }, 5000, 2000);
        }).start();

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

        auctionPanelSearchACardToAuctionTextArea.setStyle("-fx-prompt-text-fill: white; -fx-text-fill: white");
        auctionPanelSetAPriceToAuctionTextArea.setStyle("-fx-prompt-text-fill: white; -fx-text-fill: white");
        auctionPanelAllCardsInAuctionTextArea.setStyle("-fx-prompt-text-fill: white; -fx-text-fill: white");
        auctionPanelSearchAuctionIdTextArea.setStyle("-fx-prompt-text-fill: white; -fx-text-fill: white");
        auctionPanelPriceOfAuctionedCardTextArea.setStyle("-fx-prompt-text-fill: white; -fx-text-fill: white");
        auctionPanelTimeLeftToBidTextArea.setStyle("-fx-prompt-text-fill: white; -fx-text-fill: white");
        adminPanelUsernameTextArea.setStyle("-fx-prompt-text-fill: white; -fx-text-fill: white");
        adminPanelPasswordTextArea.setStyle("-fx-prompt-text-fill: white; -fx-text-fill: white");
        adminPanelCardSearchTextArea.setStyle("-fx-prompt-text-fill: white; -fx-text-fill: white");
        userCardsLabel.setStyle("-fx-prompt-text-fill: white; -fx-text-fill: white");
        suggestedCardTextArea.setStyle("-fx-prompt-text-fill: white; linear-gradient(to right, aqua , blue);");

        showAllCardsOfShop();
        showUnusedAllCardsOfUser();
    }

    public void backButton(ActionEvent e) throws IOException {
        isInShopMenu = false;
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
        String card = Cards.isCardWithThisNameExist(cardName);
        if (card == null) {
            cardDoNotExistInUserCardsLabel.setText("there is no card with this name");
            numberOfCardLabelUnderUserCard.setText("");
            userCardDetailsLabel.setText("");
            userCardInfoImageView.setImage(new Image(new FileInputStream("./src/main/resources/cardsInLowerCase/unknown.jpg")));
            return;
        }
        cardDoNotExistInUserCardsLabel.setText("");
        numberOfCardLabelUnderUserCard.setText(String.valueOf(shopController.getNumberOfThisCardOutOfDeck
                (cardName)));
        userCardDetailsLabel.setText(printBuilderController.showOneCard2(card));
        userCardInfoImageView.setImage(new Image(new FileInputStream(shopController.getCardImagePathByName(searchInUserCardsTextArea.getText()))));
//        System.out.println(shopController.getCardImagePathByName(searchInUserCardsTextArea.getText()));
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
        String card = Cards.isCardWithThisNameExist(cardName);
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
        shopCardInfoImageView.setImage(new Image(new FileInputStream(shopController.getCardImagePathByName(searchInShopTextArea.getText()))));
        cardNameInString = cardName;
        cardDoNotExistInShopLabel.setText("");
        cardNameLabelUnderShopCard.setText(String.valueOf(shopController.getCardPriceByName(cardName)));
        numberOfCardLabelUnderShopCard.setText(String.valueOf(shopController.getNumberOfThisCardOutOfDeck
                (cardName)));
        shopCardDetailsLabel.setText(printBuilderController.showOneCard2(card));

        if (shopController.checkBeforeTransaction(cardName, UserKeeper.getInstance().getCurrentUser().getBalance())) {
            buyButton.setDisable(false);
            buyButton.setStyle("-fx-background-color: #fff59");
        } else {
            buyButton.setStyle("-fx-background-color: #ff5959");
        }
    }


    public void buyACard(ActionEvent e) {
        if (shopController.checkBeforeTransaction(cardNameInString, UserKeeper.getInstance().getCurrentUser().getBalance())) {
            buyButton.setDisable(false);
            buyButton.setStyle("-fx-background-color: #fff59");
        } else {
            buyButton.setStyle("-fx-background-color: #ff5959");
            buyButton.setDisable(true);
        }
        String response = "";
        response = shopController.buy(cardNameInString);
        cardDoNotExistInShopLabel.setText(response);
        numberOfCardLabelUnderShopCard.setText(String.valueOf(shopController.getNumberOfThisCardOutOfDeck
                (cardNameInString)));
        userBalanceInShopHeader.setText(String.valueOf(user.getBalance()));
        showUnusedAllCardsOfUser();
        if (searchInUserCardsTextArea.getText().trim().equals(cardNameInString))
            numberOfCardLabelUnderUserCard.setText(String.valueOf(shopController.getNumberOfThisCardOutOfDeck
                    (cardNameInString)));
        if (shopController.checkBeforeTransaction(cardNameInString, UserKeeper.getInstance().getCurrentUser().getBalance())) {
            buyButton.setDisable(false);
            buyButton.setStyle("-fx-background-color: #fff59");
        } else {
            buyButton.setStyle("-fx-background-color: #ff5959");
            buyButton.setDisable(true);
        }
    }

    public void showAllCardsOfShop() {
        shopCardsLabel.setText(Shop.getInstance().getAllCardsWithPrice());
    }

    public void showUnusedAllCardsOfUser() {
        userCardsLabel.setText(shopController.getAllUnusedCardsByString());
    }

    public void getAllActiveAuctions() {
        auctionPanelAllCardsInAuctionTextArea.setText(auctionController.getAllActiveActionsInString());
    }

    public void makeAnAuction(ActionEvent e) {
        String firstPrice = auctionPanelSetAPriceToAuctionTextArea.getText();
        String cardName = auctionPanelSearchACardToAuctionTextArea.getText();
        if (firstPrice != null && cardName != null && !firstPrice.equals("") && !cardName.equals("")) {
            String response = auctionController.makeAnAuction(firstPrice, cardName);
            if (response.startsWith("Error")) {
                auctionPanelSearchCArdToAuctionSituationLabel.setText(response.split(" : ")[1]);
            } else {
                auctionPanelSearchCArdToAuctionSituationLabel.setText("");
                auctionPanelSetAPriceToAuctionTextArea.setText("");
                auctionPanelSearchACardToAuctionTextArea.setText("");
            }
        }
    }

    public void participateToAuction(ActionEvent e) {
        String id = auctionPanelSearchAuctionIdTextArea.getText();
        String offer = auctionPanelPriceOfAuctionedCardTextArea.getText();
        if (id != null && offer != null && !id.equals("") && !offer.equals("")) {
            String response = auctionController.participateToAuction(id, offer);
            if (response.startsWith("Error")) {
                auctionPanelSearchAuctionIdSituationLabel.setText(response.split(" : ")[1]);
            } else {
                auctionPanelSearchAuctionIdTextArea.setText("");
                auctionPanelSetAPriceToAuctionTextArea.setText("");
                auctionPanelSearchAuctionIdSituationLabel.setText("");
            }
        }
    }

    public void sellCard(ActionEvent e) {
        String cardName = searchInUserCardsTextArea.getText().trim();
        String response = "";
        if (!cardName.equals("")) {
            response = shopController.sellCard(cardName);
            numberOfCardLabelUnderUserCard.setText(String.valueOf(shopController.getNumberOfThisCardOutOfDeck
                    (cardName)));
        }
    }


}
