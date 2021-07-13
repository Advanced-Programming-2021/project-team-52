package sample.view.gameboardview;

import javafx.animation.Transition;
import sample.controller.Action;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;


public class CardView extends Rectangle {

    private final ImageView IMAGE_VIEW;
    private final int PLACE_NUMBER;
    private final boolean IS_ENEMY;
    private String description;
    private Paint paint;
    private boolean isInGraveYard;
    private Action[] actions;
    private String name;
    private Image preview;
    public boolean playExitMouseTransition;

    public CardView(int placeNumber, boolean isEnemy){
        this.PLACE_NUMBER = placeNumber;
        this.IS_ENEMY = isEnemy;
        this.IMAGE_VIEW = new ImageView();
        this.IMAGE_VIEW.setFitHeight(36.666666666666664 * 1.5);
        this.IMAGE_VIEW.setFitWidth(36.666666666666664 * 1.5);
        this.playExitMouseTransition = true;
        this.isInGraveYard = false;
    }

    public void setImageViewX(double x){
        IMAGE_VIEW.setX(x);
    }

    public void setImageViewY(double y){
        IMAGE_VIEW.setY(y);
    }

    public int getPLACE_NUMBER() {
        return PLACE_NUMBER;
    }

    public boolean getIsEnemy(){
        return IS_ENEMY;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public Paint getPaint() {
        return paint;
    }

    public ImageView getIMAGE_VIEW() {
        return IMAGE_VIEW;
    }

    public Action[] getActions() {
        return actions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setActions(Action[] actions) {
        this.actions = actions;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
        this.setFill(paint);
    }

    public void setInGraveYard(boolean inGraveYard) {
        isInGraveYard = inGraveYard;
    }

    public void setPreview(Image preview) {
        this.preview = preview;
    }

    public Image getPreview() {
        return preview;
    }

    public void changeImageViewOpacity(double opacity){
        IMAGE_VIEW.setOpacity(opacity);
    }

    public void changeImageViewImage(Image image){
        IMAGE_VIEW.setImage(image);
    }

    public boolean canHaveEffects(){
        return !paint.equals(Color.TRANSPARENT) && !isInGraveYard;
    }

    @Override
    public Object clone(){
        CardView cardView = new CardView(-1, this.IS_ENEMY);
        cardView.description = this.description;
        cardView.name = this.name;
        cardView.setPaint(this.paint == Color.TRANSPARENT ?
                Color.TRANSPARENT : new ImagePattern(((ImagePattern) paint).getImage()));
        cardView.setHeight(this.getHeight());
        cardView.setWidth(this.getWidth());
        cardView.preview = preview;
        cardView.IMAGE_VIEW.setOpacity(0);
        return cardView;
    }
}
