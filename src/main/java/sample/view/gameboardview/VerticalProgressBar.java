package sample.view.gameboardview;

import javafx.scene.control.ProgressBar;

public class VerticalProgressBar extends ProgressBar implements Vertical {

    public void setValues(double x, double y, double prefWidth, double prefHeight, double rotate, double progress) {
        this.setProgress(progress);
        makeVertical(x, y, prefWidth, prefHeight, rotate, this);
    }

}
