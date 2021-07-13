package sample.view.gameboardview;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;

import java.util.ArrayList;

public class Graveyard{

    private static final double graveyardWidth;

    static {
        graveyardWidth = GameBoardView.CARD_WIDTH * 5;
    }

    private ArrayList<CardView> cardViews;
    private HBox hBox;
    private ScrollPane scrollPane;

    public Graveyard(double layoutX, double layoutY){
        cardViews = new ArrayList<>();
        hBox = new HBox();
        hBox.setPrefHeight(GameBoardView.CARD_HEIGHT);
        scrollPane = new ScrollPane();
        scrollPane.setPrefHeight(GameBoardView.CARD_HEIGHT + 20);
        scrollPane.setPrefWidth(graveyardWidth + 20);
        scrollPane.setLayoutX(layoutX);
        scrollPane.setLayoutY(layoutY);
        scrollPane.setContent(hBox);
    }

    public ScrollPane getScrollPane() {
        return scrollPane;
    }

    public ScrollPane build(){
        hBox.getChildren().clear();
        hBox.getChildren().addAll(cardViews);
        hBox.setSpacing(10);
        return scrollPane;
    }

    public void add(CardView cardView){
        cardViews.add(cardView);
    }

    public ArrayList<CardView> getCardViews() {
        return cardViews;
    }
}
