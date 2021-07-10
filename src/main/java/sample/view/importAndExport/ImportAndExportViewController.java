package sample.view.importAndExport;

import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ImportAndExportViewController implements Initializable {

    @FXML
    JFXRadioButton exportCsvButton;

    Scene scene;
    Stage stage;

    @FXML
    AnchorPane importAndExportScenePane;
    @FXML
    JFXTextArea infoTextArea;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        importAndExportScenePane.setStyle("-fx-background-color: black");
        infoTextArea.setStyle("-fx-prompt-text-fill: white; -fx-text-fill: white");
    }

    public void backButton(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(new File
                ("./src/main/java/sample/view/mainMenu/MainMenuFxml.fxml").toURI().toURL());
        Parent root = loader.load();
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


}
