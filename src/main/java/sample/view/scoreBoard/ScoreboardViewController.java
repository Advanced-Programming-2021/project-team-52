package sample.view.scoreBoard;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.controller.ScoreBoardController;
import sample.model.User;
import sample.view.UserKeeper;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ScoreboardViewController implements Initializable {

//    ScoreBoardController scoreBoardController = ScoreBoardController.getInstance();
//
//    @FXML
//    TableView<User> theScoreBoardTableView;
//    @FXML
//    TableColumn<User, String> usernameColumn;
//    @FXML
//    TableColumn<User, Integer> scoreColumn;
//
//    public List<User> scoreBoardList = scoreBoardController.getTopUsers();
//    ObservableList<User> topUsers = FXCollections.observableArrayList();

//    TableView<User> scoreBoard;

    @FXML
    AnchorPane scoreBoardScenePane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        scoreBoardScenePane.setStyle("-fx-background-image: url(media/images/backgrounds/scoreBoardWallpaper.jpg); -fx-background-color: black");
    }

//    public void getObservableList (){
//
//        for (int i = 0; i < scoreBoardList.size(); i++) {
//            if(i < 20)
//            topUsers.add(scoreBoardList.get(i));
//        }
//    }

    public void backButton(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(new File
                ("./src/main/java/sample/view/mainMenu/MainMenuFxml.fxml").toURI().toURL());
//                ("src\\main\\java\\sample\\view\\mainMenu\\MainMenuFxml.fxml").toURI().toURL());
        Parent root = loader.load();
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}