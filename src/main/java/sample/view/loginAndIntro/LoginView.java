package sample.view.loginAndIntro;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextArea;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

public class LoginView extends Application {

    public static void main(String[] args) {
        launch(args);
    }


    MediaView introMediaView = new MediaView();
    AnchorPane introScenePane = new AnchorPane();

    Stage pStage;
    Scene pScene;

    @Override
    public void start(Stage stage) throws IOException {

        Media media = new Media(new File
//                ("C:\\Users\\paitakht\\IdeaProjects\\project-team-52-Bader03\\project-team-52-Bader03\\src\\main\\resources\\media\\videos\\introVideo.mp4").toURI().toURL().toExternalForm());
//        Media media = new Media(new File
                ("./src/main/resources/media/videos/introVideo.mp4").toURI().toURL().toExternalForm());
        Media media1 = new Media(new File
//                ("C:\\Users\\paitakht\\IdeaProjects\\project-team-52-Bader03\\project-team-52-Bader03\\src\\main\\resources\\media\\videos\\welcomeVideo.mp4").toURI().toURL().toExternalForm());
//        Media media1 = new Media(new File
                ("./src/main/resources/media/videos/welcomeVideo.mp4").toURI().toURL().toExternalForm());

        MediaPlayer introMediaPlayer = new MediaPlayer(media);
        MediaPlayer welcomeMediaPlayer = new MediaPlayer(media1);
        introMediaView.setMediaPlayer(introMediaPlayer);

        introMediaPlayer.setAutoPlay(true);
        introScenePane.getChildren().add(introMediaView);
        introMediaView.setFitHeight(720);
        introMediaView.setFitWidth(1280);
        Scene introScene = new Scene(introScenePane, 1280,720);

//        FXMLLoader loader = new FXMLLoader(new File("C:\\Users\\paitakht\\IdeaProjects\\project-team-52-Bader03\\project-team-52-Bader03\\src\\main\\java\\sample\\view\\loginAndIntro\\LoginViewFxml.fxml")
        FXMLLoader loader = new FXMLLoader(new File("./src/main/java/sample/view/loginAndIntro/LoginViewFxml.fxml")
                .toURI().toURL());
        Parent root = loader.load();
        root.getStylesheets().add(new File
//                ("C:\\Users\\paitakht\\IdeaProjects\\project-team-52-Bader03\\project-team-52-Bader03\\src\\main\\resources\\cssFiles\\LoginViewCss.css").toURI().toURL().toExternalForm());
                ("./src/main/resources/cssFiles/LoginViewCss.css").toURI().toURL().toExternalForm());
        Scene entranceScene = new Scene(root);


        introScene.setOnKeyPressed(e -> {
            introMediaPlayer.stop();
            introMediaView.setMediaPlayer(welcomeMediaPlayer);
            welcomeMediaPlayer.play();
            introScene.setOnKeyPressed(k -> {
                stage.setScene(entranceScene);
            });
        });

        stage.setScene(introScene);
        stage.show();
    }

//    public void nextButtonAction(ActionEvent e) throws IOException {
//        passwordFieldInEntranceScene.setVisible(true);
//        nickNameFieldInEntranceScene.setVisible(true);
//        loginButtonInEntranceScene.setVisible(true);
//        signupButtonInEntranceScene.setVisible(true);
//
//    }
}
