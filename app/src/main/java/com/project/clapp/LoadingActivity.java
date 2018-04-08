package com.project.clapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.clapp.impl.EventFirebaseManager;
import com.project.clapp.impl.UserFirebaseManager;
import com.project.clapp.models.Event;
import com.project.clapp.models.User;

import java.util.ArrayList;

public class LoadingActivity extends AppCompatActivity {
    ArrayList<Event> EVENTS;
    boolean init = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        loadEvents();
        loadUsers();

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
                //| View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        Thread welcomeThread = new Thread() {

            @Override
            public void run() {
                try {
                    super.run();
                    sleep(2000);  //Delay of 10 seconds
                } catch (Exception e) {

                } finally {

                    Intent i = new Intent(LoadingActivity.this,
                            Home.class);
                    startActivity(i);
                    finish();
                }
            }
        };
        welcomeThread.start();
    }
    public void loadEvents() {
        EVENTS = EventFirebaseManager.getInstance().getEventList();
        EventFirebaseManager.getInstance().clearEvents();
        DatabaseReference dataEvents;
        dataEvents = FirebaseDatabase.getInstance().getReference();
        DatabaseReference eventListRef = dataEvents.child("events");
        eventListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    //general info about the event
                    String id = ds.child("id").getValue(String.class);
                    String name = ds.child("name").getValue(String.class);
                    String imgURL = ds.child("imgURL").getValue(String.class);
                    String uID = ds.child("uID").getValue(String.class);

                    //info about the time of the event
                    String date = ds.child("date").getValue(String.class);
                    String time = ds.child("time").getValue(String.class);
                    String duration = ds.child("duration").getValue(String.class);

                    //info about the place of the event
                    String place = ds.child("place").getValue(String.class);
                    String local = ds.child("local").getValue(String.class);
                    double latitude = ds.child("latitude").getValue(double.class);
                    double longitude = ds.child("longitude").getValue(double.class);

                    //info about the registration of the event
                    int numR = ds.child("numRegister").getValue(int.class);
                    int maxR = ds.child("maxRegisters").getValue(int.class);
                    ArrayList<String> userList = new ArrayList<>();
                    for (DataSnapshot userShot: ds.child("userList").getChildren()) {
                        userList.add(userShot.getValue().toString());
                    }

                    //additional info about the event
                    String descr = ds.child("descr").getValue(String.class);
                    double price = ds.child("priceEvent").getValue(double.class);
                    ArrayList<String> tags = new ArrayList<>();
                    for (DataSnapshot tagShot: ds.child("tags").getChildren()) {
                        tags.add(tagShot.getValue().toString());
                    }
                    Event EVENT = new Event(id, name, uID, imgURL, place, local, latitude, longitude, date, time, duration, descr, userList, numR, maxR, price, tags);
                    EventFirebaseManager.getInstance().setEvents(EVENT);


                    /*boolean exists = false;
                    int i2 = 0;
                    for (int i = 0; i < EVENTS.size(); i++) {
                        if (!EVENTS.get(i).getId().equals(id)) {
                            exists = true;
                            i2 = i;
                        }

                    }
                    if (exists) {
                        EVENTS.get(i2).setDate(date);
                        EVENTS.get(i2).setDescr(descr);
                        EVENTS.get(i2).setDuration(duration);
                        EVENTS.get(i2).setImgURL(imgURL);
                        EVENTS.get(i2).setLatitude(latitude);
                        EVENTS.get(i2).setLongitude(longitude);
                        EVENTS.get(i2).setLocal(local);
                        EVENTS.get(i2).setMaxRegisters(maxR);
                        EVENTS.get(i2).setName(name);
                        EVENTS.get(i2).setPlace(place);
                        EVENTS.get(i2).setUserList(userList);
                        EVENTS.get(i2).setTags(tags);
                        EVENTS.get(i2).setTime(time);
                        EVENTS.get(i2).setNumRegister(numR);
                        EVENTS.get(i2).setPriceEvent(price);
                    } else {
                        Event EVENT = new Event(id, name, uID, imgURL, place, local, latitude, longitude, date, time, duration, descr, userList, numR, maxR, price, tags);
                        EventFirebaseManager.getInstance().setEvents(EVENT);
                    }*/



                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Error", databaseError.toString());
            }


        });
    }

    public void loadUsers() {
        UserFirebaseManager.getInstance().clearUsers();
        DatabaseReference dataEvents;
        dataEvents = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userListRef = dataEvents.child("users");
        userListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    String id = ds.child("id").getValue(String.class);
                    String name = ds.child("name").getValue(String.class);
                    String mail = ds.child("mail").getValue(String.class);
                    String imgURL = ds.child("imgURL").getValue(String.class);
                    int tel = ds.child("tele").getValue(int.class);
                    int rep = ds.child("rep").getValue(int.class);
                    int numEvent = ds.child("numEvent").getValue(int.class);


                    User user = new User(name, id, mail, tel, imgURL, numEvent, rep);
                    UserFirebaseManager.getInstance().getUsers(user);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Error", databaseError.toString());
            }
        });
    }
}
