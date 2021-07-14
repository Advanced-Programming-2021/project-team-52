package sample.controller;

import org.apache.commons.beanutils.PropertyUtilsBean;
import sample.view.sender.Sender;

public class ChatRoomController {
    private static ChatRoomController chatRoomController  = null;
    private Sender sender = Sender.getInstance();
    private final String PREFIX = "-CRC-";

    private ChatRoomController(){}

    public static ChatRoomController getInstance(){
        if(chatRoomController == null)
            chatRoomController = new ChatRoomController();
        return chatRoomController;
    }

    public String sendMessage(String content){
        return sender.getResponseWithToken(PREFIX, "sendMessage", content);
    }

    public String getAllMessages(){
        return sender.getResponseWithToken(PREFIX, "getAllMessages");
    }
}
