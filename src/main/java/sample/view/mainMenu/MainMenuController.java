package sample.view.mainMenu;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import sample.controller.LoginController;
import sample.model.User;
import sample.view.UserKeeper;

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
    ImageView leftHandImageHelper, rightHandImageHelper;

    Glow buttonGlow = new Glow(0.7);

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

    }

    public void onMouseEnteredNewGameButton(MouseEvent e){
        newGameButton.setStyle("-fx-text-fill: #ffff00; -fx-border-width: 0.5 0 0.5 0; -fx-border-color: #ffff22");
        newGameButton.setEffect(buttonGlow);
    }
    public void onMouseExitedNewGameButtos(MouseEvent e){
        newGameButton.setStyle("-fx-text-fill: #ffff44");
        newGameButton.setEffect(null);
    }

    public void onMouseEnteredDeckManagerButton(MouseEvent e){
        deckManagerButton.setStyle("-fx-text-fill: #ffff00; -fx-border-width: 0.5 0 0.5 0; -fx-border-color: #ffff22");
        deckManagerButton.setEffect(buttonGlow);
    }
    public void onMouseExitedDeckManagerButtos(MouseEvent e){
        deckManagerButton.setStyle("-fx-text-fill: #ffff44");
        deckManagerButton.setEffect(null);
    }

    public void onMouseEnteredScoreBoardButton(MouseEvent e){
        scoreboardButton.setStyle("-fx-text-fill: #ffff00; -fx-border-width: 0.5 0 0.5 0; -fx-border-color: #ffff22");
        scoreboardButton.setEffect(buttonGlow);
    }
    public void onMouseExitedScoreBoardButtos(MouseEvent e){
        scoreboardButton.setStyle("-fx-text-fill: #ffff44");
        scoreboardButton.setEffect(null);
    }

    public void onMouseEnteredShopButton(MouseEvent e) throws MalformedURLException, FileNotFoundException {
        shopButton.setStyle("-fx-text-fill: #ffff00; -fx-border-width: 0.5 0 0.5 0; -fx-border-color: #ffff22");
        shopButton.setEffect(buttonGlow);
        InputStream img = new FileInputStream("./src/main/resources/media/images/others/shopCardInMainMenu34.png");
        leftHandImageHelper.setImage(new Image(img));
        leftHandImageHelper.setOpacity(0.5);
    }
    public void onMouseExitedShopButtos(MouseEvent e) throws FileNotFoundException {
        shopButton.setStyle("-fx-text-fill: #ffff44");
        shopButton.setEffect(null);
        leftHandImageHelper.setImage(null);
    }

    public void onMouseEnteredProfileAndSettingButton(MouseEvent e){
        profileAndSettingButton.setStyle("-fx-text-fill: #ffff00; -fx-border-width: 0.5 0 0.5 0; -fx-border-color: #ffff22");
        profileAndSettingButton.setEffect(buttonGlow);
    }
    public void onMouseExitedProfileAndSettingButtos(MouseEvent e){
        profileAndSettingButton.setStyle("-fx-text-fill: #ffff44");
        profileAndSettingButton.setEffect(null);
    }

    public void onMouseEnteredImportAndExportButton(MouseEvent e){
        importAndExportButton.setStyle("-fx-text-fill: #ffff00; -fx-border-width: 0.5 0 0.5 0; -fx-border-color: #ffff22");
        importAndExportButton.setEffect(buttonGlow);
    }
    public void onMouseExitedImportAndExportButton(MouseEvent e){
        importAndExportButton.setStyle("-fx-text-fill: #ffff44");
        importAndExportButton.setEffect(null);
    }

    public void onMouseEnteredLogoutButton(MouseEvent e){
        logoutButton.setStyle("-fx-text-fill: #ffff00; -fx-border-width: 0.5 0 0.5 0; -fx-border-color: #ffff22");
        logoutButton.setEffect(buttonGlow);
    }
    public void onMouseExitedLogoutButton(MouseEvent e){
        logoutButton.setStyle("-fx-text-fill: #ffff44");
        logoutButton.setEffect(null);
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
