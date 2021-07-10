package sample.view.shop;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;
import java.net.MalformedURLException;

public class ShopView extends Application {



    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(new File("./src/main/java/sample/view/shop/ShopViewFxml.fxml")
                .toURI().toURL());
//        FXMLLoader loader = new FXMLLoader(new File("src\\main\\java\\sample\\view\\shop\\ShopViewFxml.fxml")
        Parent root = loader.load();
        root.getStylesheets().add(new File
                (".src/main/resources/cssFiles/ShopViewCss.css").toURI().toURL().toExternalForm());
//                ("src\\main\\resources\\cssFiles\\ShopViewCss.css").toURI().toURL().toExternalForm());
        Scene shopScene = new Scene(root, 1280, 720);
        stage.setScene(shopScene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
