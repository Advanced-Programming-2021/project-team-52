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

    public static Action getActionByValue(String value){
        switch (value.toUpperCase()){
            case "ATTACK":return ATTACK;
            case "DIRECT_ATTACK":return DIRECT_ATTACK;
            case "SET":return SET;
            case "NORMAL_SUMMON":return NORMAL_SUMMON;
            case "SPECIAL_SUMMON":return SPECIAL_SUMMON;
            case "CHANGE_POSITION":return CHANGE_POSITION;
            case "ACTIVATE_EFFECT":return ACTIVATE_EFFECT;
            case "FLIP_SUMMON":return FLIP_SUMMON;
            default:return NOTHING;
        }
    }
}
