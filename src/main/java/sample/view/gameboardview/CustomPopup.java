package sample.view.gameboardview;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class CustomPopup {

    private AnchorPane anchorPane;
    private Rectangle background, askBox;
    private HBox optionsBox;
    private Label message;
    private Label[] optionsArr;

    public CustomPopup(AnchorPane anchorPane){
        double width = anchorPane.getPrefWidth();
        double height = anchorPane.getPrefHeight();
        this.anchorPane = anchorPane;
        background = new Rectangle(width, height);
        background.setFill(Color.TRANSPARENT);
        askBox = new Rectangle(500, 300);
        askBox.setX(width / 2 - 250);
        askBox.setY(height / 2 - 150);
        askBox.setFill(Color.DARKBLUE);
        message = new Label("message");
        message.setAlignment(Pos.CENTER);
        message.setPrefWidth(500);
        message.setPrefHeight(200);
        message.setLayoutX(askBox.getX());
        message.setLayoutY(askBox.getY());
        message.setFont(new Font(16));
        message.setTextFill(Color.WHITE);
        message.setWrapText(true);
        optionsArr = new Label[3];
        optionsArr[0] = new Label();
        optionsArr[1] = new Label();
        optionsArr[2] = new Label();
        optionsArr[0].setBackground(new Background(new BackgroundFill(Color.ROYALBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        optionsArr[1].setBackground(new Background(new BackgroundFill(Color.ROYALBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        optionsArr[2].setBackground(new Background(new BackgroundFill(Color.ROYALBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        optionsArr[0].setTextFill(Color.WHITE);
        optionsArr[0].setFont(new Font(16));
        optionsArr[1].setTextFill(Color.WHITE);
        optionsArr[1].setFont(new Font(16));
        optionsArr[2].setTextFill(Color.WHITE);
        optionsArr[2].setFont(new Font(16));
        optionsArr[0].setWrapText(true);
        optionsArr[1].setWrapText(true);
        optionsArr[2].setWrapText(true);
        optionsBox = new HBox();
        optionsBox.setPrefWidth(500);
        optionsBox.setPrefHeight(100);
        optionsBox.setLayoutX(askBox.getX());
        optionsBox.setLayoutY(askBox.getY() + 200);
        optionsBox.setSpacing(10);
    }

    public void setTexts(String message, String... options){
        Platform.runLater(() -> this.message.setText(message));
        int size = Math.min(options.length, 3);
        double width = (500 - (size - 1) * optionsBox.getSpacing()) / size;
        for (int i = 0; i < size; i++) {
            optionsArr[i].setPrefWidth(width);
            optionsArr[i].setPrefHeight(100);
            int j = i;
            Platform.runLater(() -> {
                optionsArr[j].setText(options[j]);
                optionsBox.getChildren().add(optionsArr[j]);
            });
            optionsArr[i].setAlignment(Pos.CENTER);
        }
    }

    public void addToPane(){
        Platform.runLater(() -> {
        ObservableList<Node> observableList = anchorPane.getChildren();
        observableList.add(background);
        observableList.add(askBox);
        observableList.addAll(message, optionsBox);
        });
    }

    public void reset(){
        Platform.runLater(() -> {
            anchorPane.getChildren().removeAll(background, askBox, optionsBox, message);
            optionsBox.getChildren().clear();
        });
    }

    public boolean haveClickedOption(double x, double y){
        for (int i = 0; i < 3; i++) {
            if (optionsArr[i].contains(optionsArr[i].parentToLocal(optionsBox.parentToLocal(x, y))))
                return true;
        }
        return false;
    }

    public String getClickOptionMessage(double x, double y){
        Point2D point2D = optionsBox.parentToLocal(x, y);
        for (int i = 0; i < 3; i++) {
            if (optionsArr[i].contains(point2D))
                return optionsArr[i].getText();
        }
        return "";
    }
}
