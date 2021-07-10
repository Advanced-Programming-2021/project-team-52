package sample.view.loginAndIntro;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextArea;
import com.opencsv.exceptions.CsvException;
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
import sample.controller.LoginController;
import sample.model.User;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

public class LoginView extends Application {

    public static void main(String[] args) {
        try {
            LoginController.instantiateCards();
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
//        LoginController.getInstance().readUser();
//        User user1 = User.createUser("ali", "123ASDasd","ali", null, LocalDate.now());
//        User user2= User.createUser("mamad", "123ASDasd","mamad", null, LocalDate.now());
//        User user3 = User.createUser("reza", "123ASDasd","reza", null, LocalDate.now());
//        User user4 = User.createUser("asghar", "123ASDasd","asghar", null, LocalDate.now());
//        User user5 = User.createUser("haji", "123ASDasd","haji", null, LocalDate.now());
//        user1.setScore(100);
//        user2.setScore(100);
//        user3.setScore(10);
//        user4.setScore(200);
//        user5.setScore(10);
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
