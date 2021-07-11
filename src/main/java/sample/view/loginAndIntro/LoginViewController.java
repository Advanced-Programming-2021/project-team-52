package sample.view.loginAndIntro;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.Main;
import sample.controller.LoginController;
import sample.controller.PrintBuilderController;
import sample.model.User;
import sample.view.UserKeeper;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import static sample.model.tools.StringMessages.*;

public class LoginViewController implements Initializable {

    LoginController loginController = LoginController.getInstance();
    PrintBuilderController printBuilderController = PrintBuilderController.getInstance();

    Stage stage;
    Scene scene;

    @FXML
    JFXTextArea nickNameFieldInEntranceScene, usernameFieldInEntranceScene, passwordFieldInEntranceScene;
    @FXML
    JFXButton nextButtonInEntranceScene, loginButtonInEntranceScene, signupButtonInEntranceScene, exitButton;
    @FXML
    DatePicker userBirthDateFieldInEntranceScene;
    @FXML
    Label errorLabelInEntranceMenu;
    @FXML
    AnchorPane entranceScenePane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        nextButtonInEntranceScene.setStyle("-fx-text-fill: white");
        loginButtonInEntranceScene.setStyle("-fx-text-fill: white");
        signupButtonInEntranceScene.setStyle("-fx-text-fill: white");
        nickNameFieldInEntranceScene.setStyle("-fx-prompt-text-fill: white; -fx-text-fill: white");
        usernameFieldInEntranceScene.setStyle("-fx-prompt-text-fill: white; -fx-text-fill: white");
        passwordFieldInEntranceScene.setStyle("-fx-prompt-text-fill: white; -fx-text-fill: white");
        entranceScenePane.setStyle("-fx-background-image: url(media/images/backgrounds/1191_entranceMenuSceneBackground.png); " +
                "-fx-background-size: 720 480 ");


    }

    public void switchToMainMenu(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(new File
//               ("C:\\Users\\paitakht\\IdeaProjects\\project-team-52-Bader03\\project-team-52-Bader03\\src\\main\\java\\sample\\view\\mainMenu\\MainMenuFxml.fxml").toURI().toURL());
                ("./src/main/java/sample/view/mainMenu/MainMenuFxml.fxml").toURI().toURL());
        Parent root = loader.load();
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void onNextButtonClick(ActionEvent e){
        System.out.println(loginController.isUserWithThisUsernameExists(usernameFieldInEntranceScene.getText().trim()));
        if(loginController.isUserWithThisUsernameExists(usernameFieldInEntranceScene.getText().trim())){
            passwordFieldInEntranceScene.setVisible(true);
            loginButtonInEntranceScene.setVisible(true);
            nickNameFieldInEntranceScene.setVisible(false);
            signupButtonInEntranceScene.setVisible(false);
            userBirthDateFieldInEntranceScene.setVisible(false);
        } else {
            passwordFieldInEntranceScene.setVisible(true);
            nickNameFieldInEntranceScene.setVisible(true);
            signupButtonInEntranceScene.setVisible(true);
            userBirthDateFieldInEntranceScene.setVisible(true);
            loginButtonInEntranceScene.setVisible(false);
        }
    }

    public void onLoginButtonClick(ActionEvent e) throws IOException {
        String situation = loginController.loginUser(usernameFieldInEntranceScene.getText().trim()
                , passwordFieldInEntranceScene.getText().trim());
        if(situation.equals(usernameAndPasswordDoNotMatch)){
            errorLabelInEntranceMenu.setDisable(false);
            errorLabelInEntranceMenu.setText(usernameAndPasswordDoNotMatch);
        } else {
//            System.out.println(LoginController.getUserByUsername(usernameFieldInEntranceScene.getText().trim()));
//            UserKeeper.setCurrentUser(LoginController.getUserByUsername(usernameFieldInEntranceScene.getText().trim()));
            System.out.println(UserKeeper.getInstance().getCurrentUser().getUsername());
            switchToMainMenu(e);
        }
    }

    public void onSignupButtonClick(ActionEvent e) throws IOException {
        String situation = loginController.createUser(usernameFieldInEntranceScene.getText().trim(), passwordFieldInEntranceScene.getText()
        , nickNameFieldInEntranceScene.getText(), userBirthDateFieldInEntranceScene.getValue());
//        if(situation.equals(createUserFailedBecauseOfUsername)){
//            errorLabelInEntranceMenu.setDisable(false);
//            errorLabelInEntranceMenu.setText(createUserFailedBecauseOfUsername);
//        } else if (situation.equals(createUserFailedBecauseOfNickname)){
//            errorLabelInEntranceMenu.setDisable(false);
//            errorLabelInEntranceMenu.setText(createUserFailedBecauseOfNickname);
//        } else if (situation.equals(createUserFailedBecauseOfPasswordWeakness)){
//            errorLabelInEntranceMenu.setDisable(false);
//            errorLabelInEntranceMenu.setText(createUserFailedBecauseOfPasswordWeakness);
//        } else if (situation.equals(printBuilderController.thisUsernameAlreadyExists(usernameFieldInEntranceScene.getText()))){
//            errorLabelInEntranceMenu.setDisable(false);
//            errorLabelInEntranceMenu.setText(printBuilderController.thisUsernameAlreadyExists(usernameFieldInEntranceScene.getText()));
//        } else if (situation.equals(printBuilderController.thisNicknameAlreadyExists(nickNameFieldInEntranceScene.getText()))){
//            errorLabelInEntranceMenu.setDisable(false);
//            errorLabelInEntranceMenu.setText(nickNameFieldInEntranceScene.getText());
//        } else {
//            switchToMainMenu(e);
//        }
            if(!situation.equals(createUserSuccessfully)){
                errorLabelInEntranceMenu.setDisable(false);
                errorLabelInEntranceMenu.setText(situation);
            }
            else {
                switchToMainMenu(e);
            }
    }

    public void exitApp(ActionEvent e){
        loginController.saveUsers();
        System.exit(0);
    }

}
