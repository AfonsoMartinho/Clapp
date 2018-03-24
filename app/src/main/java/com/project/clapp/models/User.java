package com.project.clapp.clapp.models;

/**
 * Created by Afonso on 11/03/2018.
 */

public class User {


    private String name;
    private String userId;
    private String mail;
    private int tele;
    private String imgURL;
    private int numEvent;
    private int rep;



    private User(){}

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public int getRep() {
        return rep;
    }

    public void setRep(int rep) {
        this.rep = rep;
    }

    public User(String name, String userId, String mail, int tele, String imgURL, int numEvent, int rep) {
        this.name = name;
        this.userId = userId;
        this.mail = mail;
        this.tele = tele;
        this.rep = rep;
        this.numEvent = numEvent;
        this.imgURL = imgURL;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public int getNumEvent() {
        return numEvent;
    }

    public void setNumEvent(int numEvent) {
        this.numEvent = numEvent;
    }

    public int getTele() {
        return tele;
    }

    public void setTele(int tele) {
        this.tele = tele;
    }
}
