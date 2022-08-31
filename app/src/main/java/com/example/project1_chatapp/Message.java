package com.example.project1_chatapp;

<<<<<<< Updated upstream
public class Message {
    String messageText;
    String messageDate;
    String posterName;
    //profile image of poster
    int numLikes;
=======
import java.util.Date;

public class Message {
    String id;
    String messageText;
    String creator;
    Date dateCreated;

    public Message() {

    }

    public Message(String messageText, String creator, Date dateCreated) {
        this.messageText = messageText;
        this.creator = creator;
        this.dateCreated = dateCreated;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getCreator() {
        return creator;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", messageText='" + messageText + '\'' +
                ", creator='" + creator + '\'' +
                ", dateCreated=" + dateCreated +
                '}';
    }
>>>>>>> Stashed changes
}
