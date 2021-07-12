package sample.view.gameboardview;

import javafx.geometry.Pos;
import javafx.scene.control.Label;

public class VerticalLabel extends Label {

    public VerticalLabel() {
        super();
    }

    public VerticalLabel(String text) {
        super(text);
    }

    public void setValues(double x, double y, double prefWidth, double prefHeight, double rotate, String text) {
        this.setText(text);
        this.setAlignment(Pos.CENTER);
//        this.setFont(new Font(35));
        this.setPrefWidth(prefHeight);
        this.setPrefHeight(prefWidth);
        x = x - prefHeight / 2 + prefWidth / 2;
        y = y - prefWidth / 2 + prefHeight / 2;
        this.setLayoutX(x);
        this.setLayoutY(y);
        this.setRotate(rotate);
    }
}
