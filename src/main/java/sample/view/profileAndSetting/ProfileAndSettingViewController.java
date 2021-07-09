package sample.view.profileAndSetting;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import sample.view.UserKeeper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ProfileAndSettingViewController implements Initializable{
    @FXML
    AnchorPane ProfileAndSettingViewPane;
    @FXML
    JFXTextArea newNicknameTextArea,newUsernameTextArea, newPasswordTextArea , newPasswordAgainTextArea, oldPasswordTextArea;
    @FXML
    JFXButton nicknameEditButton, usernameEditButton, changePasswordButton,
            nicknameChangeSubmitButton, usernameChangeSubmitButton, passwordChangeSubmitButton;
    @FXML
    ImageView profileImageImageView;

    @FXML
    Label usernameLabelInProfileScene,nicknameLabelInProfileScene;

//    InputStream defaultProfileImageStream = new FileInputStream
//            ("./src/main/resources/media/images/profile/1.jpg");
    InputStream dragAndDropGuidImageStream = new FileInputStream
            ("./src/main/resources/media/images/others/dragAndDropGuidImage.jpg");
    Image profileImageGuid = new Image(dragAndDropGuidImageStream);
    Image defaultProfileImage = UserKeeper.getInstance().getCurrentUser().getImage();

    public ProfileAndSettingViewController() throws FileNotFoundException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        usernameLabelInProfileScene.setText(UserKeeper.getInstance().getCurrentUser().getUsername());
        nicknameLabelInProfileScene.setText(UserKeeper.getInstance().getCurrentUser().getNickname());
        ProfileAndSettingViewPane.setStyle("-fx-background-color: black");
        newNicknameTextArea.setStyle("-fx-text-fill: White; -fx-prompt-text-fill: white");
        newUsernameTextArea.setStyle("-fx-text-fill: White; -fx-prompt-text-fill: white");
        newPasswordTextArea.setStyle("-fx-text-fill: White; -fx-prompt-text-fill: white");
        newPasswordAgainTextArea.setStyle("-fx-text-fill: White; -fx-prompt-text-fill: white");
        oldPasswordTextArea.setStyle("-fx-text-fill: White; -fx-prompt-text-fill: white");
    }

    public void changeUsername(ActionEvent e){
        newUsernameTextArea.setVisible(!newUsernameTextArea.isVisible());
        usernameChangeSubmitButton.setVisible(!usernameChangeSubmitButton.isVisible());

    }

    public void changeNickname(ActionEvent e){
        newNicknameTextArea.setVisible(!newNicknameTextArea.isVisible());
        nicknameChangeSubmitButton.setVisible(!nicknameChangeSubmitButton.isVisible());
    }

    public void changePassword(ActionEvent e){
        oldPasswordTextArea.setVisible(!oldPasswordTextArea.isVisible());
        newPasswordTextArea.setVisible(!newPasswordTextArea.isVisible());
        newPasswordAgainTextArea.setVisible(!newPasswordAgainTextArea.isVisible());
        passwordChangeSubmitButton.setVisible(!passwordChangeSubmitButton.isVisible());
    }

    public void dragAndDropEnteredOver(DragEvent e) throws FileNotFoundException {
        profileImageImageView.setImage(profileImageGuid);
        if(e.getDragboard().hasFiles()) {
            e.acceptTransferModes(TransferMode.ANY);
        } else {
            profileImageImageView.setImage(defaultProfileImage);
        }
    }
    public void dragAndDropExited(DragEvent e){
        if(profileImageImageView.getImage() == profileImageGuid) {
            profileImageImageView.setImage(profileImageImageView.getImage());
        }
    }
    public void setNewImageProcessDragDropped(DragEvent e){
        List<File> draggedImageFile = e.getDragboard().getFiles();
        try {
            Image draggedImage = new Image(new FileInputStream(draggedImageFile.get(0)));
            profileImageImageView.setImage(draggedImage);
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
    }

}
