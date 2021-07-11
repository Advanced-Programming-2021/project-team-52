package sample.view.scoreBoard;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.controller.ScoreBoardController;
import sample.model.User;
import sample.view.UserKeeper;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ScoreboardViewController implements Initializable {

    ScoreBoardController scoreBoardController = ScoreBoardController.getInstance();

    @FXML
    TableView<User> theScoreBoardTableView;
    @FXML
    TableColumn<User, String> usernameColumn;
    @FXML
    TableColumn<User, Integer> scoreColumn;

    public List<User> scoreBoardList = scoreBoardController.getTopUsers();
    ObservableList<User> topUsers = FXCollections.observableArrayList();

//    TableView<User> scoreBoard;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        usernameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("usernameColumn"));
//        scoreColumn.setCellValueFactory(new PropertyValueFactory<User, Integer>("scoreColumn"));
//        theScoreBoardTableView.setItems(scoreBoardList);
//        if(usernameColumn.getCellFactory().equals(UserKeeper.getInstance().getCurrentUser())){
//
//        }
        getObservableList();
        usernameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("usernameColumn"));
//        TableColumn<User, String> nicknameColumn =  new TableColumn<>("NickName");
        usernameColumn =  new TableColumn<>("NickName");
        usernameColumn.setMaxWidth(200);
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("nickname"));
        theScoreBoardTableView = new TableView<>();
        theScoreBoardTableView.setItems(topUsers);
        theScoreBoardTableView.getColumns().add(usernameColumn);
    }

    public void getObservableList (){

        for (int i = 0; i < scoreBoardList.size(); i++) {
            if(i < 20)
            topUsers.add(scoreBoardList.get(i));
        }
    }
}