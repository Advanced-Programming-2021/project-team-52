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

    public String pinMessage(String content){
        return sender.getResponseWithToken(PREFIX, "pinMessage",content);
    }

    public String searchMessage(String id){
        return sender.getResponseWithToken(PREFIX, "searchMessage" ,id);
    }

    public String editMessage(String newContent, String id){
        return sender.getResponseWithToken(PREFIX, "editMessage",newContent, id);
    }

    public String deleteMessage(String id){
        return sender.getResponseWithToken(PREFIX,"deleteMessage" , id);
    }

    public String replyMessage(String content, String id){
        return sender.getResponseWithToken(PREFIX,"replyMessage" ,content, id);
    }

    public String getPinMessage(){
        return sender.getResponseWithToken(PREFIX,"getPinMessage");
    }
    public String getSenderProfile(String id){
        return sender.getResponseWithToken(PREFIX,"getSenderProfile", id);
    }
    public String getAvatarAddress(String id){
        return sender.getResponseWithToken(PREFIX,"getAvatarAddress", id);
    }
}
