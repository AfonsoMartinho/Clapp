package com.example.project.clapp.impl;

import com.example.project.clapp.models.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Afonso on 11/03/2018.
 */

public class UserFirebaseManager implements IUser {

    static UserFirebaseManager ufm = null;

    public static UserFirebaseManager getInstance() {
        if(ufm == null) {
            ufm = new UserFirebaseManager();
        }
        return ufm;
    }

    @Override
    public User getUser(String name) {
        return null;
    }

    @Override
    public void addUser(String userId, String mail) {
        DatabaseReference databaseUsers;
        databaseUsers = FirebaseDatabase.getInstance().getReference();
        String name= "RENAME ME";
        User user = new User(name,userId, mail);
        databaseUsers.child("users").child(userId).setValue(user);
    }



    @Override
    public void removeUser(String name) {

    }
}
