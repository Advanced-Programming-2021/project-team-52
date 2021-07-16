package sample.view.gameboardview;

import javafx.scene.control.ToggleButton;
import javafx.scene.text.Font;

public class VerticalToggleButton extends ToggleButton implements Vertical {

    public void setValues(double x, double y, double prefWidth, double prefHeight, double rotate, String text) {
        this.setText(text);
//        this.setFont(new Font(14));
        makeVertical(x, y, prefWidth, prefHeight, rotate, this);
    }
}
