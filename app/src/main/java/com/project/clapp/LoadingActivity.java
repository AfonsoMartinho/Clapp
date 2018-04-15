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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class LoadingActivity extends AppCompatActivity {
    ArrayList<Event> EVENTS;
    boolean init = true;
    int z = 0;

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
                    sleep(1500);
                } catch (Exception e) {

                } finally {

                    Intent i = new Intent(LoadingActivity.this,
                            Home.class);
                    i.putExtra("goto", "calendar");
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


                    if (init) {
                        Event EVENT = new Event(id, name, uID, imgURL, place, local, latitude, longitude, date, time, duration, descr, userList, numR, maxR, price, tags);
                        loadToLists(EVENT);
                        init = false;



                    } else {
                        boolean exists = false;
                        ArrayList<Event> EVENTSF = EventFirebaseManager.getInstance().getEventList();
                        for (int i = 0; i < EVENTSF.size(); i++) {
                            if (EVENTSF.get(i).getId().equals(id)) {
                                exists = true;
                                z = i;
                            }
                        }
                        if (!exists) {
                            Event EVENT = new Event(id, name, uID, imgURL, place, local, latitude, longitude, date, time, duration, descr, userList, numR, maxR, price, tags);
                            loadToLists(EVENT);
                        } else {
                            Event EVENT = new Event(id, name, uID, imgURL, place, local, latitude, longitude, date, time, duration, descr, userList, numR, maxR, price, tags);
                            EventFirebaseManager.getInstance().registUser(EVENT, z);
                        }
                    }


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
                    String id = ds.child("userId").getValue(String.class);
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

    public void loadToLists(Event event) {
        String str = event.getDate()+ " " + event.getTime();
        SimpleDateFormat df = new SimpleDateFormat("MMM dd yyyy HH:mm zzz", Locale.ENGLISH);
        Date date;
        Date currentDate = new Date();
        df.format(currentDate);
        try {
            date = df.parse(str);
            if (currentDate.before(date)) {
                EventFirebaseManager.getInstance().setEvents(event, 1);
            } else {
                EventFirebaseManager.getInstance().setEvents(event, 2);
            }
        } catch (ParseException e) {
            System.out.println(e);
        }
    }
}
