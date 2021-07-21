package sample.view.gameboardview;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TitledPane;
import javafx.stage.Stage;
import sample.view.tv.TvCommandReceiver;

public class TvGameBoardHandler extends GameBoardHandler {

    private boolean isHost;
    private TitledPane pane;
    private TvCommandReceiver tvCommandReceiver;

    public TvGameBoardHandler(Stage stage, boolean isHost, TitledPane pane, TvCommandReceiver tvCommandReceiver) {
        super(stage);
        this.isHost = isHost;
        this.pane = pane;
        this.tvCommandReceiver = tvCommandReceiver;
    }

    @Override
    public void run(){
        String message;
        while (super.run){
            message = tvCommandReceiver.receiveTv(isHost);
            handle(message);
        }
    }

    @Override
    protected void startANewDuel(String[] args){
        Platform.runLater(() -> {
            GameBoardView gameBoardView = new GameBoardView(args[1], args[2], args[3], args[4], this);
            gameBoardView.initialize();
            pane.setContent(gameBoardView.getAnchorPane());
        });
    }

    @Override
    protected void handleSelecting(String message) {
        System.out.println("no selecting for you");
    }
}
