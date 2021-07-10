package sample.view.deckManager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

public class DeckManagerView extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(new File("./src/main/java/sample/view/deckManager/DeckManagerFxml.fxml")
                .toURI().toURL());
        Parent root = loader.load();
        Scene shopScene = new Scene(root, 1280,720);
        stage.setScene(shopScene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
