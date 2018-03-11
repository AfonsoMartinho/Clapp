package com.example.project.clapp.firebase;

import com.example.project.clapp.models.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Afonso on 11/03/2018.
 */

public class Firebase {

    //Insere o user com o unique id
    public void  addUser(String name, String mail) {
        DatabaseReference databaseUsers;
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");
        String id = databaseUsers.push().getKey();

        User user = new User(id, name, mail);
        databaseUsers.child(id).setValue(user);
    }
}
