package sample.view.gameboardview;

import javafx.scene.layout.HBox;

public class HandPlace extends HBox {

    public void adjustSpacing() {
        this.setSpacing(18 - 3 * this.getChildren().size());
    }
}
