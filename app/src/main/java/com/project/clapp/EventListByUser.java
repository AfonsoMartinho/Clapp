package com.project.clapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.project.clapp.impl.EventFirebaseManager;
import com.project.clapp.impl.UserFirebaseManager;
import com.project.clapp.models.Event;
import com.project.clapp.models.User;

import java.util.ArrayList;

public class EventListByUser extends AppCompatActivity {
    RecyclerView rv;
    EventAdapter eventAdapter;
    ArrayList<Event> EVENTSFINAL = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list_by_user);

        rv = findViewById(R.id.eventListByUser);
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            String userID = extras.getString("userID");
            ArrayList<Event> EVENTS = EventFirebaseManager.getInstance().getEventList();
            for (int i = 0; i < EVENTS.size(); i++) {
                if (userID.equals(EVENTS.get(i).getuID())) {
                    EVENTSFINAL.add(EVENTS.get(i));
                }
            }
            fillEventList();
        }



    }

    public void fillEventList() {

        eventAdapter = new EventAdapter(EVENTSFINAL, 1);
        rv.setAdapter(eventAdapter);


    }
}
