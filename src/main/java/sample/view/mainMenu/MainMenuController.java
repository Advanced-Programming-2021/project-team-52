package sample.view.mainMenu;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
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
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.controller.LoginController;
import sample.model.User;
import sample.view.UserKeeper;

import java.awt.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {


    @FXML
    MediaView mainMenuBackgroundMediaView;
    static MediaPlayer mainMenuBackgroundMediaPlayer;
    Media mainMenuBackgroundMedia;
    File mainMenuBackgroundMediaFile;


    @FXML
    JFXButton newGameButton, deckManagerButton, shopButton, profileAndSettingButton,
            scoreboardButton, importAndExportButton, logoutButton,chatRoomButton, tvButton;

    @FXML
    ImageView deckManagerImageView, profileAndSettingImageView, chatImageview, importAndExportImageView,
            shopImageView, scoreBoardImageView, newGameImageView;

    @FXML
    JFXTextArea searchPlayerToPlayTextArea, allUsersTextArea;

    @FXML
    JFXRadioButton oneRoundRadioButton, threeRoundRadioButton;

    @FXML
    Label userSearchSituationLabel, theInviteSenderNameLabel, theInviteSenderNumberOfRoundsLabel;

    Glow buttonGlow = new Glow(0.7);
    TranslateTransition onHover = new TranslateTransition();

    Stage stage;
    Scene scene;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
//        mainMenuBackgroundMediaFile = new File("C:\\Users\\paitakht\\IdeaProjects\\project-team-52-Bader03\\project-team-52-Bader03\\src\\main\\resources\\media\\images\\backgrounds\\newProject11.mp4");
        mainMenuBackgroundMediaFile = new File("./src/main/resources/media/images/backgrounds/newProject11.mp4");
        try {
            mainMenuBackgroundMedia = new Media(mainMenuBackgroundMediaFile.toURI().toURL().toExternalForm());
            mainMenuBackgroundMediaPlayer = new MediaPlayer(mainMenuBackgroundMedia);
            mainMenuBackgroundMediaPlayer.play();
            mainMenuBackgroundMediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mainMenuBackgroundMediaView.setMediaPlayer(mainMenuBackgroundMediaPlayer);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        newGameButton.setStyle("-fx-border-width: 0 0 0 0.5; -fx-border-color: #ffff55");
        deckManagerButton.setStyle("-fx-border-width: 0 0 0 0.5; -fx-border-color: #ffff55");
        scoreboardButton.setStyle("-fx-border-width: 0 0 0 0.5; -fx-border-color: #ffff55");
        importAndExportButton.setStyle("-fx-border-width: 0 0 0 0.5; -fx-border-color: #ffff55");
        shopButton.setStyle("-fx-border-width: 0 0 0 0.5; -fx-border-color: #ffff55");
        profileAndSettingButton.setStyle("-fx-border-width: 0 0 0 0.5; -fx-border-color: #ffff55");
        chatRoomButton.setStyle("-fx-border-width: 0 0 0 0.5; -fx-border-color: #ffff55");
        logoutButton.setStyle("-fx-border-width: 0 0 0 0.5; -fx-border-color: #ff5959");
        searchPlayerToPlayTextArea.setStyle("-fx-prompt-text-fill: white; -fx-text-fill: #ffff22");
        allUsersTextArea.setStyle("-fx-text-fill: white");
        tvButton.setStyle("-fx-border-width: 0 0 0 0.4; -fx-border-color: radial-gradient(aqua, #ff5bec)");
    }

    public void onMouseEnteredNewGameButton(MouseEvent e){
        newGameButton.setStyle("-fx-text-fill: #ffff00; -fx-border-width: 0 0 0 4; -fx-border-color: #ffff00");
        newGameButton.setEffect(buttonGlow);
        onHover.setByY(-6);
        onHover.setNode(newGameImageView);
        onHover.setDuration(Duration.millis(100));
        onHover.play();
    }
    public void onMouseExitedNewGameButton(MouseEvent e){
        newGameButton.setStyle("-fx-text-fill: #ffff44; -fx-border-width: 0 0 0 0.5; -fx-border-color: #ffff55");
        newGameButton.setEffect(null);
        onHover.setByY(6);
        onHover.setDuration(Duration.millis(10));
        onHover.setNode(newGameImageView);
        onHover.play();
    }

    public void onMouseEnteredDeckManagerButton(MouseEvent e){
        deckManagerButton.setStyle("-fx-text-fill: #ffff00; -fx-border-width: 0 0 0 4; -fx-border-color: #ffff00");
        deckManagerButton.setEffect(buttonGlow);
        onHover.setByY(-6);
        onHover.setNode(deckManagerImageView);
        onHover.setDuration(Duration.millis(100));
        onHover.play();
    }
    public void onMouseExitedDeckManagerButton(MouseEvent e){
        deckManagerButton.setStyle("-fx-text-fill: #ffff44; -fx-border-width: 0 0 0 0.5; -fx-border-color: #ffff55");
        deckManagerButton.setEffect(null);
        onHover.setByY(6);
        onHover.setDuration(Duration.millis(10));
        onHover.setNode(deckManagerImageView);
        onHover.play();
    }

    public void onMouseEnteredScoreBoardButton(MouseEvent e){
        scoreboardButton.setStyle("-fx-text-fill: #ffff00; -fx-border-width: 0 0 0 4; -fx-border-color: #ffff00");
        scoreboardButton.setEffect(buttonGlow);
        onHover.setByY(-6);
        onHover.setNode(scoreBoardImageView);
        onHover.setDuration(Duration.millis(100));
        onHover.play();
    }
    public void onMouseExitedScoreBoardButton(MouseEvent e){
        scoreboardButton.setStyle("-fx-text-fill: #ffff44; -fx-border-width: 0 0 0 0.5; -fx-border-color: #ffff55");
        scoreboardButton.setEffect(null);
        onHover.setByY(6);
        onHover.setDuration(Duration.millis(10));
        onHover.setNode(scoreBoardImageView);
        onHover.play();
    }

    public void onMouseEnteredShopButton(MouseEvent e) throws MalformedURLException, FileNotFoundException {
        shopButton.setStyle("-fx-text-fill: #ffff00; -fx-border-width: 0 0 0 4; -fx-border-color: #ffff00");
        shopButton.setEffect(buttonGlow);
        InputStream img = new FileInputStream("./src/main/resources/media/images/others/shopCardInMainMenu34.png");
        onHover.setByY(-6);
        onHover.setNode(shopImageView);
        onHover.setDuration(Duration.millis(100));
        onHover.play();
    }
    public void onMouseExitedShopButton(MouseEvent e) throws FileNotFoundException {
        shopButton.setStyle("-fx-text-fill: #ffff44; -fx-border-width: 0 0 0 0.5; -fx-border-color: #ffff55");
        shopButton.setEffect(null);
        onHover.setByY(6);
        onHover.setDuration(Duration.millis(10));
        onHover.setNode(shopImageView);
        onHover.play();
    }

    public void onMouseEnteredProfileAndSettingButton(MouseEvent e){
        profileAndSettingButton.setStyle("-fx-text-fill: #ffff00; -fx-border-width: 0 0 0 4; -fx-border-color: #ffff00");
        profileAndSettingButton.setEffect(buttonGlow);
        onHover.setByY(-6);
        onHover.setNode(profileAndSettingImageView);
        onHover.setDuration(Duration.millis(100));
        onHover.play();
    }
    public void onMouseExitedProfileAndSettingButton(MouseEvent e){
        profileAndSettingButton.setStyle("-fx-text-fill: #ffff44; -fx-border-width: 0 0 0 0.5; -fx-border-color: #ffff55");
        profileAndSettingButton.setEffect(null);
        onHover.setByY(6);
        onHover.setDuration(Duration.millis(10));
        onHover.setNode(profileAndSettingImageView);
        onHover.play();
    }

    public void onMouseEnteredImportAndExportButton(MouseEvent e){
        importAndExportButton.setStyle("-fx-text-fill: #ffff00; -fx-border-width: 0 0 0 4; -fx-border-color: #ffff00");
        importAndExportButton.setEffect(buttonGlow);
        onHover.setByY(-6);
        onHover.setNode(importAndExportImageView);
        onHover.setDuration(Duration.millis(100));
        onHover.play();
    }
    public void onMouseExitedImportAndExportButton(MouseEvent e){
        importAndExportButton.setStyle("-fx-text-fill: #ffff44; -fx-border-width: 0 0 0 0.5; -fx-border-color: #ffff55");
        importAndExportButton.setEffect(null);
        onHover.setByY(6);
        onHover.setDuration(Duration.millis(10));
        onHover.setNode(importAndExportImageView);
        onHover.play();
    }


    public void onMouseEnteredLogoutButton(MouseEvent e){
        logoutButton.setStyle("-fx-text-fill: #ffff00; -fx-border-width: 0 0 0 4; -fx-border-color: #ff5959");
        logoutButton.setEffect(buttonGlow);
        onHover.setByY(-6);
        onHover.setNode(deckManagerImageView);
        onHover.setDuration(Duration.millis(100));
        onHover.play();
    }
    public void onMouseExitedLogoutButton(MouseEvent e){
        logoutButton.setStyle("-fx-text-fill: #ffff44; -fx-border-width: 0 0 0 0.5; -fx-border-color: #ffff55");
        logoutButton.setEffect(null);
        onHover.setByY(6);
        onHover.setDuration(Duration.millis(10));
        onHover.setNode(deckManagerImageView);
        onHover.play();
    }

    public void switchToGameScene(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                new File("./src/main/java/sample/view/newduel/newDuelFxml.fxml").toURI().toURL());
        Parent root = loader.load();
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToDeckManagerScene(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(new File
                ("./src/main/java/sample/view/deckManager/DeckManagerFxml.fxml").toURI().toURL());
//                ("C:\\Users\\paitakht\\IdeaProjects\\project-team-52-Bader03\\project-team-52-Bader03\\src\\main\\java\\sample\\view\\deckManager\\DeckManagerFxml.fxml").toURI().toURL());
        Parent root = loader.load();
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToShopScene(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(new File
//                ("C:\\Users\\paitakht\\IdeaProjects\\project-team-52-Bader03\\project-team-52-Bader03\\src\\main\\java\\sample\\view\\shop\\ShopViewFxml.fxml").toURI().toURL());
                ("./src/main/java/sample/view/shop/ShopViewFxml.fxml").toURI().toURL());
//                ("C:\\Users\\paitakht\\IdeaProjects\\phase3-client\\src\\main\\java\\sample\\view\\shop\\ShopViewFxml.fxml").toURI().toURL());

        Parent root = loader.load();
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToScoreboardScene(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(new File
//                ("C:\\Users\\paitakht\\IdeaProjects\\project-team-52-Bader03\\project-team-52-Bader03\\src\\main\\java\\sample\\view\\deckManager\\DeckManagerFxml.fxml").toURI().toURL());
//                ("C:\\Users\\paitakht\\IdeaProjects\\project-ppp\\src\\main\\java\\sample\\view\\scoreBoard\\ScoreBoardFxml.fxml").toURI().toURL());
                ("./src/main/java/sample/view/scoreBoard/ScoreBoardFxml.fxml").toURI().toURL());
        Parent root = loader.load();
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToProfileAndSettingScene(ActionEvent e) throws IOException {
//        System.out.println(UserKeeper.getInstance().getCurrentUser().getUsername());
        FXMLLoader loader = new FXMLLoader(new File
//                ("C:\\Users\\paitakht\\IdeaProjects\\project-team-52-Bader03\\project-team-52-Bader03\\src\\main\\java\\sample\\view\\deckManager\\DeckManagerFxml.fxml").toURI().toURL());
                ("./src/main/java/sample/view/profileAndSetting/ProfileAndSettingFxml.fxml").toURI().toURL());
        Parent root = loader.load();
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToImportAndExportScene(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(new File
//                ("C:\\Users\\paitakht\\IdeaProjects\\project-ppp\\src\\main\\java\\sample\\view\\importAndExport\\ImportAndExportFxml.fxml").toURI().toURL());
                ("./src/main/java/sample/view/importAndExport/ImportAndExportFxml.fxml").toURI().toURL());
        Parent root = loader.load();
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToEntranceScene(ActionEvent e) throws IOException{
        LoginController.getInstance().logout();
        FXMLLoader loader = new FXMLLoader(new File
//                ("C:\\Users\\paitakht\\IdeaProjects\\project-ppp\\src\\main\\java\\sample\\view\\importAndExport\\ImportAndExportFxml").toURI().toURL());
                ("./src/main/java/sample/view/loginAndIntro/LoginViewFxml.fxml").toURI().toURL());
        Parent root = loader.load();
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void switchToChatRoom(ActionEvent e) throws IOException{
//        LoginController.getInstance().logout();
        FXMLLoader loader = new FXMLLoader(new File
//                ("C:\\Users\\paitakht\\IdeaProjects\\project-ppp\\src\\main\\java\\sample\\view\\importAndExport\\ImportAndExportFxml").toURI().toURL());
                ("./src/main/java/sample/view/chatRoom/ChatRoomFxml.fxml").toURI().toURL());
        Parent root = loader.load();
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToTv(ActionEvent actionEvent) throws IOException{
        FXMLLoader loader = new FXMLLoader(new File("./src/main/java/sample/view/tv/TvViewFxml.fxml")
                .toURI().toURL());
//        FXMLLoader loader = new FXMLLoader(new File("src\\main\\java\\sample\\view\\shop\\ShopViewFxml.fxml")
        Parent root = loader.load();
//        root.getStylesheets().add(new File
//                (".src/main/resources/cssFiles/ShopViewCss.css").toURI().toURL().toExternalForm());
////                ("src\\main\\resources\\cssFiles\\ShopViewCss.css").toURI().toURL().toExternalForm());
        Scene tvScene = new Scene(root, 1280, 720);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(tvScene);
    }


    public static MediaPlayer getMainMenuBackgroundMediaPlayer() {
        return mainMenuBackgroundMediaPlayer;
    }
}
