package com.example.project.clapp.impl;

import com.example.project.clapp.models.Event;
import com.example.project.clapp.models.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by ruigo on 12/03/2018.
 */

public class EventFirebaseManager implements IEvent{


    static EventFirebaseManager efm = null;

    public static EventFirebaseManager getInstance() {
        if(efm == null) {
            efm = new EventFirebaseManager();
        }
        return efm;
    }

    @Override
    public Event getEvent(int id) {
        return null;
    }

    @Override
    public Event getEvents() {
        return null;
    }

    @Override
    public void addEvent(String nome, String userId) {
        DatabaseReference databaseEvents;
        databaseEvents = FirebaseDatabase.getInstance().getReference();
        Event event = new Event(nome, userId);
        databaseEvents.child("events").child(nome).setValue(event);

    }
}
