package sample.view.tv;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

public class TvView extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(new File("./src/main/java/sample/view/tv/TvViewFxml.fxml")
                .toURI().toURL());
//        FXMLLoader loader = new FXMLLoader(new File("src\\main\\java\\sample\\view\\shop\\ShopViewFxml.fxml")
        Parent root = loader.load();
//        root.getStylesheets().add(new File
//                (".src/main/resources/cssFiles/ShopViewCss.css").toURI().toURL().toExternalForm());
////                ("src\\main\\resources\\cssFiles\\ShopViewCss.css").toURI().toURL().toExternalForm());
        Scene shopScene = new Scene(root, 1280, 720);
        stage.setScene(shopScene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
