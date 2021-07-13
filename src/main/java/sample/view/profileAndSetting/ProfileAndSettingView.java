package sample.view.profileAndSetting;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

public class ProfileAndSettingView extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(new File
                ("./src/main/java/sample/view/profileAndSetting/ProfileAndSettingFxml.fxml").toURI().toURL());
        Scene mainMenuScene = new Scene(root);
        mainMenuScene.getStylesheets().add(new File
                (".src/main/resources/cssFiles/ProfileViewCss.css").toURI().toURL().toExternalForm());
        stage.setScene(mainMenuScene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
