package sample.controller;

import javafx.scene.image.Image;

public enum Action {
    ATTACK(new Image("./actions/attack.png")),
    DIRECT_ATTACK(new Image("./actions/direct_attack.png")),
    SET(new Image("./actions/set.png")),
    NORMAL_SUMMON(new Image("./actions/normal_summon.png")),
    SPECIAL_SUMMON(new Image("./actions/special_summon.png")),
    CHANGE_POSITION(new Image("./actions/change_position.png")),
    ACTIVATE_EFFECT(new Image("/actions/activate_effect.png")),
    FLIP_SUMMON(new Image("/actions/flip_summon.png")),
    NOTHING(null);

    private final Image image;

    Action(Image image){
        this.image = image;
    }

    public Image getImage() {
        return image;
    }
}
