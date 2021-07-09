package sample.view.mainMenu;

import com.jfoenix.controls.JFXButton;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

import static javafx.application.Application.launch;

public class MainMenuView extends Application{

    @FXML
    JFXButton newGameButton;

    public static void main(String[] args) {
        launch(args);
    }

    File mainMenuMusicFile;
    MediaPlayer mainMenuMusicPlayer;
    Media mainMenuMusic;


    @Override
    public void start(Stage stage) throws Exception {

        mainMenuMusicFile = new File("C:\\Users\\paitakht\\IdeaProjects\\project-team-52-Bader03\\project-team-52-Bader03\\src\\main\\resources\\media\\sounds\\mainMenuMusic.mp3");
//        mainMenuMusicFile = new File("src\\main\\resources\\media\\sounds\\mainMenuMusic.mp3");
        try {
            mainMenuMusic = new Media(mainMenuMusicFile.toURI().toURL().toExternalForm());
            mainMenuMusicPlayer = new MediaPlayer(mainMenuMusic);
            if(!mainMenuMusicPlayer.getStatus().equals(MediaPlayer.Status.PLAYING)){
                mainMenuMusicPlayer.play();
            }
            mainMenuMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Parent root = FXMLLoader.load(new File
                ("C:\\Users\\paitakht\\IdeaProjects\\project-team-52-Bader03\\project-team-52-Bader03\\src\\main\\java\\sample\\view\\mainMenu\\MainMenuFxml.fxml").toURI().toURL());
//                ("src\\main\\java\\sample\\view\\mainMenu\\MainMenuFxml.fxml").toURI().toURL());
        Scene mainMenuScene = new Scene(root);
        mainMenuScene.getStylesheets().add(new File
                ("C:\\Users\\paitakht\\IdeaProjects\\project-team-52-Bader03\\project-team-52-Bader03\\src\\main\\resources\\cssFiles\\MainMenuViewCss.css").toURI().toURL().toExternalForm());
//                ("src\\main\\resources\\cssFiles\\MainMenuViewCss.css").toURI().toURL().toExternalForm());
        stage.setScene(mainMenuScene);
        stage.show();
    }

}
