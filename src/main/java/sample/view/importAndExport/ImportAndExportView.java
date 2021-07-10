package sample.view.importAndExport;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

public class ImportAndExportView extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(new File("./src/main/java" +
                "/sample/view/importAndExport/ImportAndExportFxml.fxml")
                .toURI().toURL());
        Parent root = loader.load();
        Scene shopScene = new Scene(root);
        stage.setScene(shopScene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
