package com.example.project1_chatapp;

public class Message {
    String id;
    String messageText;
    String dateCreated;
    String creator;
    int numLikes;

    public Message() {

    }

    public Message(String id, String messageText, String dateCreated, String creator, int numLikes) {
        this.id = id;
        this.messageText = messageText;
        this.dateCreated = dateCreated;
        this.creator = creator;
        this.numLikes = numLikes;
    }

    public String getId() {
        return id;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public String getCreator() {
        return creator;
    }

    public int getNumLikes() {
        return numLikes;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void setNumLikes(int numLikes) {
        this.numLikes = numLikes;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", messageText='" + messageText + '\'' +
                ", dateCreated='" + dateCreated + '\'' +
                ", creator='" + creator + '\'' +
                ", numLikes=" + numLikes +
                '}';
    }
}
