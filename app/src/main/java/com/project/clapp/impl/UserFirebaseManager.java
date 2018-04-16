package com.project.clapp.impl;


import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.clapp.models.Event;
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
    public User getUser(String userID) {
        ArrayList<User> users = UserFirebaseManager.getInstance().getUserList();
        for (int i = 0; i < users.size(); i++) {
            if (userID.equals(users.get(i).getUserId())) {
                return users.get(i);
            }
        }
        return null;
    }


    @Override
    public void getUsers(User user) {
        userList.add(user);
    }

    @Override
    public void addUser(final String userId, final String mail) {
        final DatabaseReference databaseUsers = FirebaseDatabase.getInstance().getReference().child("users");
        final ArrayList<User> users = new ArrayList<>();
        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    if (ds != null) {
                        String id = ds.child("userId").getValue(String.class);
                        String name = ds.child("name").getValue(String.class);
                        String mail = ds.child("mail").getValue(String.class);
                        String imgURL = ds.child("imgURL").getValue(String.class);
                        int tele = ds.child("tele").getValue(int.class);
                        int rep = ds.child("rep").getValue(int.class);
                        int numEvent = ds.child("numEvent").getValue(int.class);
                        User user = new User(name,id, mail, tele, imgURL, numEvent, rep);
                        users.add(user);
                    }


                }

                if (users.isEmpty()) {
                    User user = new User("rename me",userId, mail, 999999999, "default.png", 0, 50);
                    addToFirebase(databaseUsers, user);
                } else {
                    boolean exists = false;
                    for (int i = 0; i < users.size(); i++) {
                        if (users.get(i).getUserId().equals(userId)) {
                            exists = true;
                        }
                    }

                    if (exists == false) {
                        User user = new User("rename me",userId, mail, 999999999, "default.png", 0, 50);
                        addToFirebase(databaseUsers, user);
                    }
                }




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Error", databaseError.toString());
            }
        });
    }

    @Override
    public void addToFirebase(DatabaseReference dr, User user) {
        dr.child(user.getUserId()).setValue(user);
    }

    @Override
    public void removeUser(String name) {

    }
}
