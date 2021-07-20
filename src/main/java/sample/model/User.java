package sample.model;

import javafx.scene.image.Image;
import sample.view.sender.Sender;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class User  {
    private Sender sender = Sender.getInstance();
    private String PREFIX = "-UM-";

    public String getUsername() {
        return sender.getResponseWithToken(PREFIX, "getUsername");
    }

    public String getNickname() {
        return sender.getResponseWithToken(PREFIX, "getNickname");
    }

    public int getBalance() {
        return Integer.parseInt(sender.getResponseWithToken(PREFIX, "getBalance"));
    }


    public String getImageAddress() {
        return sender.getResponseWithToken(PREFIX, "getImageAddress");
    }



}
