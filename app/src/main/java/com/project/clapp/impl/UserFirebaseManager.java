package com.project.clapp.impl;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.clapp.models.User;

import java.util.ArrayList;

/**
 * Created by Afonso on 11/03/2018.
 */

public class UserFirebaseManager implements IUser {

    static UserFirebaseManager ufm = null;
    public ArrayList<User> userList = new ArrayList<>();

    public static UserFirebaseManager getInstance() {
        if(ufm == null) {
            ufm = new UserFirebaseManager();
        }
        return ufm;
    }

    @Override
    public ArrayList<User> getUserList() {return userList;}

    @Override
    public void clearUsers() {
        userList.clear();
    }

    @Override
    public User getUser(String name) {
        return null;
    }

    @Override
    public void getUsers(User user) {
        userList.add(user);
    }

    @Override
    public void addUser(String userId, String mail) {
        DatabaseReference databaseUsers;
        databaseUsers = FirebaseDatabase.getInstance().getReference();
        if (databaseUsers.child("users").child(userId).equals(null)) {
            String name= "RENAME ME";
            int tele = 901356233;
            User user = new User(name,userId, mail, tele, "https://upload.wikimedia.org/wikipedia/commons/thumb/6/61/Luis_Filipe_Vieira.jpg/330px-Luis_Filipe_Vieira.jpg", 0, 50);
            databaseUsers.child("users").child(userId).setValue(user);
        }
    }



    @Override
    public void removeUser(String name) {

    }
}
