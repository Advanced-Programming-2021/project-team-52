package sample.view.gameboardview;

import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.util.concurrent.CountDownLatch;

public class ClickInputHandler implements Runnable {

    private CountDownLatch countDownLatch, outsideThreadCountdown;
    private double mouseX, mouseY;
    private MouseButton mouseButton;
    private AnchorPane anchorPane;
    private boolean run;

    public ClickInputHandler(AnchorPane anchorPane) {
        this.anchorPane = anchorPane;
        this.mouseX = this.mouseY = -1;
        this.run = true;
    }

    public double getMouseX() {
        return mouseX;
    }

    public double getMouseY() {
        return mouseY;
    }

    public MouseButton getMouseButton() {
        return mouseButton;
    }

    public void setRun(boolean run) {
        this.run = run;
    }

    public void setOutsideThreadCountdown(CountDownLatch outsideThreadCountdown) {
        this.outsideThreadCountdown = outsideThreadCountdown;
    }

    @Override
    public void run() {
        countDownLatch = new CountDownLatch(1);
        while (run) {
            try {
                countDownLatch.await();
                if (!run)
                    break;
                countDownLatch = new CountDownLatch(1);
                getMouseClickInput();
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }
    }

    private void getMouseClickInput() {
        EventHandler<? super MouseEvent> previousEvent = anchorPane.getOnMouseClicked();
        anchorPane.setOnMouseClicked(mouseEvent -> {
            this.mouseX = mouseEvent.getX();
            this.mouseY = mouseEvent.getY();
            this.mouseButton = mouseEvent.getButton();
            outsideThreadCountdown.countDown();
            anchorPane.setOnMouseClicked(previousEvent);
        });
    }

    public void start() {
        countDownLatch.countDown();
    }
}
