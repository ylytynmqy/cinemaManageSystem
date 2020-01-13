package com.example.cinema.po;

import java.sql.Timestamp;

public class Message {
    private int id;
    private Timestamp time;
    private String description;
    private int beenRead;

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public int getBeenRead() {
        return beenRead;
    }

    public void setBeenRead(int beenRead) {
        this.beenRead = beenRead;
    }

    public Message(){

    }
}
