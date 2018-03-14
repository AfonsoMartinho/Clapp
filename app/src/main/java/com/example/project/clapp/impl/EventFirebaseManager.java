package com.example.project.clapp.impl;

import com.example.project.clapp.models.Event;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

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
    public ArrayList<Event> getEvents() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dataEvents = database.getReference("https://clapp-28222.firebaseio.com/events");

        final ArrayList<Event> eventList = new ArrayList<Event>();


        dataEvents.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Event event = dataSnapshot.getValue(Event.class);
                eventList.add(event);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return eventList;
    }

    @Override
    public void addEvent(String nome, String userId) {
        DatabaseReference databaseEvents;
        databaseEvents = FirebaseDatabase.getInstance().getReference();
        Event event = new Event(nome, userId);
        databaseEvents.child("events").child(nome).setValue(event);

    }
}
