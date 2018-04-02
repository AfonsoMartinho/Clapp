package com.project.clapp.models;

import org.parceler.Parcel;

/**
 * Created by ruigo on 12/03/2018.
 */

@Parcel
public class Event {

    public String id;
    public String name;
    public String uID;
    public String imgURL;
    public String local;
    public double latitude;
    public double longitude;
    public String date;
    public String time;
    public String duration;
    public String descr;
    public String userList;
    public int numRegister;
    public int maxRegisters;
    public double preco;
    public String tags;

    public Event() {}

    public Event(String name, String uID) {
        this.name = name;
        this.uID = uID;
    }

    public Event(String id, String name, String uID) {
        this.id = id;
        this.name = name;
        this.uID = uID;
    }

    public Event(String id, String name, String uID, String imgURL, String local, String date, String time, String duration, String descr, String userList, int numRegister, int maxRegisters, double preco) {
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

    public Event(String id, String name, String uID, String imgURL, String local, double latitude, double longitude, String date, String time, String duration, String descr, String userList, int numRegister, int maxRegisters, double preco, String tags) {
        this.id = id;
        this.name = name;
        this.uID = uID;
        this.imgURL = imgURL;
        this.local = local;
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
        this.time = time;
        this.duration = duration;
        this.descr = descr;
        this.userList = userList;
        this.numRegister = numRegister;
        this.maxRegisters = maxRegisters;
        this.preco = preco;
        this.tags = tags;
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

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
