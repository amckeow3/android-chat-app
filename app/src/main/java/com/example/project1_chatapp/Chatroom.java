package com.example.project1_chatapp;

import java.io.Serializable;

public class Chatroom implements Serializable {
    String name;
    String id;

    public Chatroom() {

    }

    public Chatroom(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Chatroom{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}