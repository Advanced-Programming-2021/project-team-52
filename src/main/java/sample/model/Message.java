package sample.model;

import java.time.LocalDate;

public class Message {
    private User author;
    private String content;
    private Message repliedMessage;
    private LocalDate sentDate;
    private String id;
    private int idCounter = 1000;

    public Message(String content, User author) {
        this.author = author;
        this.content = content;
        this.sentDate = LocalDate.now();
//        id =  UUID.randomUUID().toString();
        id = String.valueOf(--idCounter);
    }

    public Message(String content, User author, Message repliedMessageId) {
        this.author = author;
        this.content = content;
        this.repliedMessage = repliedMessageId;
        this.sentDate = LocalDate.now();
//        id =  UUID.randomUUID().toString();
        id = String.valueOf(--idCounter);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Message getRepliedMessage() {
        return repliedMessage;
    }

    public void setRepliedMessage(Message repliedMessage) {
        this.repliedMessage = repliedMessage;
    }

    public LocalDate getSentDate() {
        return sentDate;
    }

    public void setSentDate(LocalDate sentDate) {
        this.sentDate = sentDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    @Override
    public String toString() {
        StringBuilder response = new StringBuilder();
        response.append(author.getUsername());
        if (repliedMessage != null)
            response.append("(Replied ").append(repliedMessage.getId()).append(")");
        response.append(" : ").append(content).append("(ID ").append(id).append(")");
        return response.toString();
    }
}
