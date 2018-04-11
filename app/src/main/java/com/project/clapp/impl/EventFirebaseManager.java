package com.project.clapp.impl;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.clapp.models.Event;

import java.util.ArrayList;

/**
 * Created by ruigo on 12/03/2018.
 */

public class EventFirebaseManager implements IEvent{
    public ArrayList<Event> eventList = new ArrayList<>();
    public ArrayList<Event> oldEvents = new ArrayList<>();

    public ArrayList<Event> getEventList() {
        return eventList;
    }

    public ArrayList<Event> getOldEvents() { return oldEvents; }

    static EventFirebaseManager efm = null;
    private static final String TAG = "FirebaseTest";

    public static EventFirebaseManager getInstance() {
        if(efm == null) {
            efm = new EventFirebaseManager();
        }
        return efm;
    }

    @Override
    public ArrayList<Event> getEventListTagFilter(ArrayList<String> tags) {
        ArrayList<Event> EventsFilt = new ArrayList<>();
        int numFilt = tags.size();
        for (int i = 0; i < eventList.size(); i++) {
            int checker = 0;
            for (int x = 0; x < tags.size(); x++) {
                if (eventList.get(i).getTags().contains(tags.get(x))) {
                    checker++;
                }
            }
            if (checker == numFilt) {
                EventsFilt.add(eventList.get(i));
            }
        }

        return EventsFilt;
    }

    @Override
    public void clearEvents() {
        eventList.clear();
    }

    @Override
    public Event getEvent(String id) {
        Event event = new Event();
        for (int i = 0; i < eventList.size(); i++) {
            if (id.equals(eventList.get(i).getId())) {
                event = eventList.get(i);
            }
        }
        return event;
    }

    @Override
    public void setEvents(Event event, int i) {
        if (i == 1) {
            eventList.add(event);
        } else if (i == 2){
            oldEvents.add(event);
        }

    }

    @Override
    public void registUser(Event event, int i) {
        eventList.set(i, event);
    }

    @Override
    public void addEvent(String nameEvent, String dateEvent, String timeEvent, String placeEvent, String localEvent, String durationEvent, double priceEvent, String descEvent, int capEvent, String userId, double latitude, double longitude, String imgURL, ArrayList<String> tags) {
        DatabaseReference databaseEvents;
        databaseEvents = FirebaseDatabase.getInstance().getReference();
        DatabaseReference pushedPostRef = databaseEvents.push();
        String postId = pushedPostRef.getKey();
        String userList = userId;
        ArrayList<String> users = new ArrayList<String>();
        users.add(userList);
        System.out.println(tags);
        ArrayList<String> tagsEvent = tags;
        Event event = new Event(postId,
                nameEvent,
                userId,
                imgURL,
                placeEvent,
                localEvent,
                latitude,
                longitude,
                dateEvent,
                timeEvent,
                durationEvent,
                descEvent,
                1,
                capEvent,
                priceEvent);
        databaseEvents.child("events").child(postId).setValue(event);

        for (int i = 0; i < tags.size(); i++) {
            DatabaseReference tagListRef = databaseEvents.child("events").child(postId).child("tags").child(tags.get(i).toString());
            tagListRef.setValue(tags.get(i).toString());
        }

        for (int i = 0; i < users.size(); i++) {
            DatabaseReference tagListRef = databaseEvents.child("events").child(postId).child("userList").child(users.get(i).toString());
            tagListRef.setValue(users.get(i).toString());
        }
    }
}
