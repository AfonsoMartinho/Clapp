package com.project.clapp.models;

/**
 * Created by ruigo on 12/03/2018.
 */

public class Event {

    private String id;
    private String name;
    private String uID;
    private String imgURL;
    private String local;
    private String date;
    private String time;
    private String duration;
    private String descr;
    private String userList;
    private int numRegister;
    private int maxRegisters;
    private int preco;

    public Event(String name, String uID) {
        this.name = name;
        this.uID = uID;
    }

    public Event(String id, String name, String uID) {
        this.id = id;
        this.name = name;
        this.uID = uID;
    }

    public Event(String id, String name, String uID, String imgURL, String local, String date, String time, String duration, String descr, String userList, int numRegister, int maxRegisters, int preco) {
        this.id = id;
        this.name = name;
        this.uID = uID;
        this.imgURL = imgURL;
        this.local = local;
        this.date = date;
        this.time = time;
        this.duration = duration;
        this.descr = descr;
        this.userList = userList;
        this.numRegister = numRegister;
        this.maxRegisters = maxRegisters;
        this.preco = preco;
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

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getUserList() {
        return userList;
    }

    public void setUserList(String userList) {
        this.userList = userList;
    }

    public int getNumRegister() {
        return numRegister;
    }

    public void setNumRegister(int numRegister) {
        this.numRegister = numRegister;
    }

    public int getMaxRegisters() {
        return maxRegisters;
    }

    public void setMaxRegisters(int maxRegisters) {
        this.maxRegisters = maxRegisters;
    }

    public int getPreco() {
        return preco;
    }

    public void setPreco(int preco) {
        this.preco = preco;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
