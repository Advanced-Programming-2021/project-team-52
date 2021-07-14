package sample.view.profileAndSetting;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.controller.ProfileController;
import sample.model.User;
import sample.model.tools.StringMessages;
import sample.view.UserKeeper;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ProfileAndSettingViewController implements Initializable {
    User user = UserKeeper.getInstance().getCurrentUser();

    Scene scene;
    Stage stage;

    private ProfileController profileController = ProfileController.getInstance();
    @FXML
    AnchorPane ProfileAndSettingViewPane;
    @FXML
    JFXTextArea newNicknameTextArea, newUsernameTextArea, newPasswordTextArea, newPasswordAgainTextArea,
            oldPasswordTextArea, newPhotoTextArea;
    @FXML
    JFXButton nicknameEditButton, usernameEditButton, changePasswordButton,
            nicknameChangeSubmitButton, usernameChangeSubmitButton, passwordChangeSubmitButton, backButton,
            photoChangeSubmitButton;
    @FXML
    ImageView profileImageImageView;

    @FXML
    Label usernameLabelInProfileScene, nicknameLabelInProfileScene;
    @FXML
    Label situationLabel;

    //    InputStream defaultProfileImageStream = new FileInputStream
//            ("./src/main/resources/media/images/profile/1.jpg");
    InputStream dragAndDropGuidImageStream = new FileInputStream
            ("./src/main/resources/media/images/others/dragAndDropGuidImage.jpg");
    Image profileImageGuid = new Image(dragAndDropGuidImageStream);
    //    Image defaultProfileImage = UserKeeper.getInstance().getCurrentUser().getImage();
    String imageAddress = user.getImageAddress();
    Image defaultProfileImage;

    public ProfileAndSettingViewController() throws FileNotFoundException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            defaultProfileImage = new Image(new FileInputStream(imageAddress));
//            System.out.println(imageAddress);
            profileImageImageView.setImage(defaultProfileImage);

        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        }
        usernameLabelInProfileScene.setText(UserKeeper.getInstance().getCurrentUser().getUsername());
        nicknameLabelInProfileScene.setText(UserKeeper.getInstance().getCurrentUser().getNickname());
        ProfileAndSettingViewPane.setStyle("-fx-background-color: black");
        newNicknameTextArea.setStyle("-fx-text-fill: White; -fx-prompt-text-fill: white");
        newUsernameTextArea.setStyle("-fx-text-fill: White; -fx-prompt-text-fill: white");
        newPasswordTextArea.setStyle("-fx-text-fill: White; -fx-prompt-text-fill: white");
        newPasswordAgainTextArea.setStyle("-fx-text-fill: White; -fx-prompt-text-fill: white");
        oldPasswordTextArea.setStyle("-fx-text-fill: White; -fx-prompt-text-fill: white");
        newPhotoTextArea.setStyle("-fx-text-fill: White; -fx-prompt-text-fill: white");
    }

    public void changeUsername(ActionEvent e) {
        newUsernameTextArea.setVisible(!newUsernameTextArea.isVisible());
        usernameChangeSubmitButton.setVisible(!usernameChangeSubmitButton.isVisible());

    }

    public void changeNickname(ActionEvent e) {
        newNicknameTextArea.setVisible(!newNicknameTextArea.isVisible());
        nicknameChangeSubmitButton.setVisible(!nicknameChangeSubmitButton.isVisible());

    }

    public void changePassword(ActionEvent e) {
        oldPasswordTextArea.setVisible(!oldPasswordTextArea.isVisible());
        newPasswordTextArea.setVisible(!newPasswordTextArea.isVisible());
        newPasswordAgainTextArea.setVisible(!newPasswordAgainTextArea.isVisible());
        passwordChangeSubmitButton.setVisible(!passwordChangeSubmitButton.isVisible());
    }

    public void dragAndDropEnteredOver(DragEvent e) throws FileNotFoundException {
        profileImageImageView.setImage(profileImageGuid);
        if (e.getDragboard().hasFiles()) {
            e.acceptTransferModes(TransferMode.ANY);
        } else {
            profileImageImageView.setImage(defaultProfileImage);
        }
    }

    public void dragAndDropExited(DragEvent e) {
        if (profileImageImageView.getImage() == profileImageGuid) {
            profileImageImageView.setImage(profileImageImageView.getImage());
        }
    }

    public void setNewImageProcessDragDropped(DragEvent e) {
        List<File> draggedImageFile = e.getDragboard().getFiles();
        try {
            Image draggedImage = new Image(new FileInputStream(draggedImageFile.get(0)));
            profileImageImageView.setImage(draggedImage);
//            profileController.changeProfileImage(draggedImage, UserKeeper.getInstance().getCurrentUser());
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
    }

    public void changePasswordFromController() {
        String response = profileController.changePassword(newPasswordTextArea.getText(),
                newPasswordAgainTextArea.getText(), oldPasswordTextArea.getText());
        situationLabel.setText(response);
    }

    public void changeNicknameFromController() {
        String response = profileController.changeNickname(newNicknameTextArea.getText());
        nicknameLabelInProfileScene.setText(UserKeeper.getInstance().getCurrentUser().getNickname());
        situationLabel.setText(response);
    }

    public void changeImageByPath(ActionEvent e) {
        String response = profileController.changeProfileImage(newPhotoTextArea.getText());
        if (!response.equals(StringMessages.THERE_IS_NO_IMAGE_WITH_THIS_PATH)) {
            try {
                profileImageImageView.setImage(new Image(new FileInputStream(newPhotoTextArea.getText())));
                newPhotoTextArea.setText("");
            } catch (FileNotFoundException exception) {
                situationLabel.setText(StringMessages.THERE_IS_NO_IMAGE_WITH_THIS_PATH);
                return;
            }
        }
        situationLabel.setText(response);

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

}
