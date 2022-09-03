package com.example.project1_chatapp;

import com.google.firebase.Timestamp;

import java.util.Date;

public class Message {
    public String id,  message, creator, creatorID;
    public Timestamp dateCreated;
    public int likes;

    public Message() {
        this.id = "00000";
        this.message = "message";
        this.creator = "creator";
        this.likes = 0;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getCreator() {
        return creator;
    }

    public String getCreatorID() {
        return creatorID;
    }

    public void setCreatorID(String creatorID) {
        this.creatorID = creatorID;
    }

    public int getLikes() {
        return likes;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMessage(String messageText) {
        this.message = messageText;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void setLikes(int numLikes) {
        this.likes = numLikes;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", messageText='" + message + '\'' +
                ", dateCreated='" + dateCreated + '\'' +
                ", creator='" + creator + '\'' +
                ", numLikes=" + likes +
                '}';
    }
}
