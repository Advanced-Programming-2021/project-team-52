package sample.view.tv;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import sample.controller.Action;
import sample.view.sender.Sender;

import javax.swing.text.html.ImageView;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TvViewController implements Initializable {

    private boolean isFocus = false;
    private TvCommandReceiver tvCommandReceiver;

    @FXML
    AnchorPane TvViewPane;

    @FXML
    TitledPane topPlayerOpponentTitledPane, topPlayerTitledPane, searchedPlayerOpponentTitledPane, searchedPlayerTitledPane;

    @FXML
    VBox bRectangle;

    @FXML
    Label tvHeaderLabel,searchGameIdSituationLabel,allGamesIdLabel;

    @FXML
    Pane tvHeaderImageViewPane;

    @FXML
    JFXTextArea searchGameIdTextArea, AllGamesIdsTextArea;

    @FXML
    Line tvHeaderLine;

    @FXML
    JFXButton searchSubmitButton, backButton;

    @FXML
    Tooltip focusModeToolTip;

    Stage stage;
    Scene scene;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TvViewPane.setStyle("-fx-background-color: black");
        searchGameIdTextArea.setStyle("-fx-text-fill: white; -fx-prompt-text-fill: white");
        AllGamesIdsTextArea.setStyle("-fx-text-fill: white; -fx-prompt-text-fill: white");
        this.tvCommandReceiver = new TvCommandReceiver(searchedPlayerTitledPane, searchedPlayerOpponentTitledPane);
//        Thread thread = new Thread(tvCommandReceiver);
//        thread.setDaemon(true);
//        thread.start();
//        String activeGames = tvCommandReceiver.getActiveGames();
//        AllGamesIdsTextArea.setText(activeGames);
    }

    public void focusMode(ActionEvent e){
        if(!isFocus) {
            ColorAdjust b = new ColorAdjust();
            b.setBrightness(-0.5);
            GaussianBlur a = new GaussianBlur(6);
            a.setInput(b);
            tvHeaderImageViewPane.setEffect(a);
            tvHeaderLine.setEffect(a);
            searchGameIdTextArea.setEffect(a);
            AllGamesIdsTextArea.setEffect(a);
            tvHeaderLabel.setEffect(a);
            searchGameIdSituationLabel.setEffect(a);
            allGamesIdLabel.setEffect(a);
            allGamesIdLabel.setStyle("-fx-text-fill: #8bffff");
            searchSubmitButton.setEffect(a);
            backButton.setEffect(a);
            focusModeToolTip.setText("normal mode");
            isFocus = true;
        } else {
            tvHeaderImageViewPane.setEffect(null);
            tvHeaderLine.setEffect(null);
            searchGameIdTextArea.setEffect(null);
            AllGamesIdsTextArea.setEffect(null);
            tvHeaderLabel.setEffect(null);
            searchGameIdSituationLabel.setEffect(null);
            allGamesIdLabel.setEffect(null);
            allGamesIdLabel.setStyle("-fx-text-fill: #00ffff");
            searchSubmitButton.setEffect(null);
            backButton.setEffect(null);
            focusModeToolTip.setText("chill mode");
            isFocus = false;
        }
    }

    public void backButton(ActionEvent e) throws IOException {
        isFocus = false;
        tvCommandReceiver.setRun(false);
        FXMLLoader loader = new FXMLLoader(new File
                ("./src/main/java/sample/view/mainMenu/MainMenuFxml.fxml").toURI().toURL());
//                ("src\\main\\java\\sample\\view\\mainMenu\\MainMenuFxml.fxml").toURI().toURL());
        Parent root = loader.load();
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void watchGame(){
        if (searchGameIdTextArea.getText().matches("^\\d+$")){
            tvCommandReceiver.watchGame(Integer.parseInt(searchGameIdTextArea.getText()));
        }
        searchGameIdTextArea.setText("");
    }

}
