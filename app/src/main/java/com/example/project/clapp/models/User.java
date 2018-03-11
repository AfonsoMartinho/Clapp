package com.example.project.clapp.models;

/**
 * Created by Afonso on 11/03/2018.
 */

public class User {

    private String id;
    private String name;
    private String mail;

    private User(){}


    public User(String id, String name, String mail) {
        super();
        this.id = id;
        this.name = name;
        this.mail = mail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
