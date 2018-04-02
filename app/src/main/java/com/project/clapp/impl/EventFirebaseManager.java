package com.project.clapp.impl;

import android.util.Log;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.clapp.models.Event;

import java.util.ArrayList;

/**
 * Created by ruigo on 12/03/2018.
 */

public class EventFirebaseManager implements IEvent{
    public ArrayList<Event> eventList = new ArrayList<>();


    public ArrayList<Event> getEventList() {
        return eventList;
    }

    static EventFirebaseManager efm = null;
    private static final String TAG = "FirebaseTest";

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
    public void getEvents() {
        DatabaseReference dataEvents;
        dataEvents = FirebaseDatabase.getInstance().getReference();
        DatabaseReference eventListRef = dataEvents.child("events");
            eventListRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds: dataSnapshot.getChildren()) {
                        String id = ds.child("id").getValue(String.class);
                        String nome = ds.child("name").getValue(String.class);
                        String uID = ds.child("uID").getValue(String.class);
                        Event event = new Event(id, nome, uID);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d(TAG, databaseError.toString());
                }

            });
    }


    @Override
    public void addEvent(String nameEvent, String dateEvent, String timeEvent, String localEvent, String durationEvent, double priceEvent, String descEvent, int capEvent, String userId, double latitude, double longitude, String imgURL, String tags) {
        DatabaseReference databaseEvents;
        databaseEvents = FirebaseDatabase.getInstance().getReference();
        DatabaseReference pushedPostRef = databaseEvents.push();
        String postId = pushedPostRef.getKey();
        String userList = userId;
        Log.d("addEvent","entrou");
        Event event = new Event(postId,
                nameEvent,
                userId,
                imgURL,
                localEvent,
                latitude,
                longitude,
                dateEvent,
                timeEvent,
                durationEvent,
                descEvent,
                userList,
                1,
                capEvent,
                priceEvent,
                tags);
        databaseEvents.child("events").child(postId).setValue(event);

    }
}
