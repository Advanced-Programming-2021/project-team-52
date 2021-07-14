package sample.controller;


import sample.model.Message;
import sample.model.User;

import java.util.ArrayList;

import static sample.controller.LoginController.getUserByUsername;

public class ChatroomController {
    private static ChatroomController chatroomController = null;
    private static ArrayList<Message> allMessages = new ArrayList<>();
    private static ArrayList<Message> deletedMessages = new ArrayList<>();
    private Message pinnedMessage;

    private ChatroomController() {
    }

    public static ChatroomController getInstance() {
        if (chatroomController == null)
            chatroomController = new ChatroomController();
        return chatroomController;
    }

    public String addMessage(User author, String content, String repliedMessageId) {
        Message message;
        if (repliedMessageId.equals("null"))
            message = new Message(content, author);
        else {
            Message repliedMessage = getMessageById(repliedMessageId);
            if(repliedMessage == null)
                return "Replied message Id is invalid";
            message = new Message(content, author, repliedMessage);
        }
        allMessages.add(message);
        return "Message has been added successfully";
    }

    public String editMessage(User editor,String newContent, String id){

        Message message = getMessageById(id);
        if(id == null)
            return "Id is invalid";
        assert message != null;
        if(!message.getAuthor().getUsername().equals(editor.getUsername()))
            return "Editor is not the writer";
        message.setContent(newContent);
        return "Message has been edited successfully";
    }

    public String deleteMessage(User deleter, String id){
        Message message = getMessageById(id);
        if(id == null)
            return "Id is invalid";
        assert message != null;
        if(!message.getAuthor().getUsername().equals(deleter.getUsername()))
            return "Deleter is not the writer";
        allMessages.remove(message);
        deletedMessages.add(message);
        return "Message has been deleted successfully";
    }

    public static Message getMessageById(String id) {
        for (Message message : allMessages) {
            if (message.getId().equals(id))
                return message;
        }
        return null;
    }

    public String getAllMessages(){
        StringBuilder response = new StringBuilder();
        for (int i = 0; i < allMessages.size(); i++) {
            response.append(allMessages.get(i));
            if(i != allMessages.size() - 1)
                response.append("\n");
        }
        return response.toString();
    }

//    public User getUserByUsername(String username) {
//        for (User user : users) {
//            if (user.getUsername().equals(username))
//                return user;
//        }
//        return null;
//    }

    public String pinMessage(String id){
        Message message = getMessageById(id);
        if(id == null)
            return "Id is invalid";
        pinnedMessage = message;
        return "Message pinned successfully";
    }

    public String getPinnedMessageContent() {
        if(pinnedMessage != null)
        return pinnedMessage.toString();
        return "There is no pinned message";
    }

    public String getProfileOfUser(String username){
        User user = getUserByUsername(username);
        if (user == null)
            return "User is invalid";
        return user.getUserProfile();
    }

    //    public String getUserProfile(String username){
//        User user = getUserByUsername(username);
//        if (user == null)
//            return "User is invalid";
//        return "user profile";
//    }
}
