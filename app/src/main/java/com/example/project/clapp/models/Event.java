package com.example.project.clapp.models;

/**
 * Created by ruigo on 12/03/2018.
 */

public class Event {

    private int id;
    private String name;
    private String uID;

    public Event() {
    }

    public Event(String name, String uID) {
        this.name = name;
        this.uID = uID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }
}
