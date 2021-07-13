package sample.view.gameboardview;

import javafx.scene.control.Control;

public interface Vertical {

    default void makeVertical(double x, double y, double prefWidth, double prefHeight, double rotate, Control node) {
        node.setPrefWidth(prefHeight);
        node.setPrefHeight(prefWidth);
        x = x - prefHeight / 2 + prefWidth / 2;
        y = y - prefWidth / 2 + prefHeight / 2;
        node.setLayoutX(x);
        node.setLayoutY(y);
        node.setRotate(rotate);
    }
}
