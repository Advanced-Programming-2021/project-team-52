package sample.view.chatRoom;

//import animatefx.animation.AnimationFX;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.controller.ChatRoomController;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.sql.Time;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class ChatRoomViewController implements Initializable {
    private ChatRoomController chatRoomController = ChatRoomController.getInstance();

    Scene scene;
    Stage stage;

    @FXML
    AnchorPane chatRoomScenePane;
    @FXML
    JFXButton message1Reply;
    @FXML
    ImageView message1ReplyImage;
    @FXML
    JFXTextArea chatBoxTextArea, allUsersTextArea, enterNewMessageTextArea, searchMessageTextArea,
            searchedMessageTextArea;
    @FXML
    JFXButton messageRemoveButton, messageEditButton, messageReplyButton, messagePinButton, sendMessageButton;

    @FXML
    Tooltip pinButtonToolTip, messageRemoveButtonToolTip, messageEditButtonToolTip, messageReplyButtonToolTip;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        new Thread(() -> {
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        getAllMessages();
                    }
                }, 0, 1000);
        }).start();

        pinButtonToolTip.setStyle("-fx-text-fill: #ffff55");
        messageRemoveButtonToolTip.setStyle("-fx-text-fill: #ff5959");
        messageEditButtonToolTip.setStyle("-fx-text-fill: #59ff59");
        messageReplyButtonToolTip.setStyle("-fx-text-fill: aqua");
        chatRoomScenePane.setStyle("-fx-background-color: black");
        chatBoxTextArea.setStyle("-fx-prompt-text-fill: white; -fx-text-fill: white");
        allUsersTextArea.setStyle("-fx-prompt-text-fill: white; -fx-text-fill: white");
        enterNewMessageTextArea.setStyle("-fx-prompt-text-fill: white; -fx-text-fill: white");
        searchMessageTextArea.setStyle("-fx-prompt-text-fill: white; -fx-text-fill: white");
        searchedMessageTextArea.setStyle("-fx-prompt-text-fill: white; -fx-text-fill: white");
        System.out.println("start");
        System.out.println("start");
    }

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

    public void message1Setting(ActionEvent e) {
        TranslateTransition t = new TranslateTransition();
    }

    public void SendMessage(ActionEvent e) {
        String message = enterNewMessageTextArea.getText();
        if (message != null && !message.equals("")) {
            chatRoomController.sendMessage(message);
        }
    }

    public void getAllMessages() {
        chatBoxTextArea.setText(chatRoomController.getAllMessages());
    }

//    public void updateMessages(){
//        while (true){
//            Timer timer = new Timer();
//            timer.schedule();
//        }
//    }

}
