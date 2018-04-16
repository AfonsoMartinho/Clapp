package com.project.clapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.project.clapp.impl.EventFirebaseManager;
import com.project.clapp.impl.UserFirebaseManager;
import com.project.clapp.models.Event;
import com.project.clapp.models.User;

import java.util.ArrayList;

public class UserListEvent extends AppCompatActivity {
    RecyclerView rv;
    UserAdapter userAdapter;
    ArrayList<String> usersIDs;
    ArrayList<User> USERS = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list_event);

        rv = findViewById(R.id.userList);
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            String eventID = extras.getString("eventID");
            usersIDs = EventFirebaseManager.getInstance().getEvent(eventID).getUserList();
            for (int i = 0; i < usersIDs.size(); i++) {
                User user = UserFirebaseManager.getInstance().getUser(usersIDs.get(i));
                System.out.println(user.getName());
                USERS.add(user);
            }
            fillUserList();
        }



    }

    public void fillUserList() {

        userAdapter = new UserAdapter(USERS);
        rv.setAdapter(userAdapter);


    }
}
