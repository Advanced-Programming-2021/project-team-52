package sample.view.shop;

import com.jfoenix.controls.JFXButton;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import sample.model.Shop;
import sample.model.tools.StringMessages;

import java.awt.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

public class ShopViewController implements StringMessages , Initializable{

    @FXML
    JFXButton backButton;
    @FXML
    Pane shopScenePane;
    @FXML
    Label userBalanceInShopHeader;
    @FXML
    ProgressBar card1ProgressBar;

    Scene scene;
    Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        shopScenePane.setStyle("-fx-background-color: black");
        userBalanceInShopHeader.setText("user's money");
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
