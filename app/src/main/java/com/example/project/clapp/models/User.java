package com.example.project.clapp.models;

/**
 * Created by Afonso on 11/03/2018.
 */

public class User {


    private String name;
    private String userId;
    private String mail;


    private User(){}

    public User(String name, String userId, String mail) {
        this.name = name;
        this.userId = userId;
        this.mail = mail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
