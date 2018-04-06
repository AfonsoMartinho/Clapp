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

public class LoadingActivity extends AppCompatActivity {

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
                    String local = ds.child("local").getValue(String.class);
                    double latitude = ds.child("latitude").getValue(double.class);
                    double longitude = ds.child("longitude").getValue(double.class);

                    //info about the registration of the event
                    int numR = ds.child("numRegister").getValue(int.class);
                    int maxR = ds.child("maxRegisters").getValue(int.class);
                    String userList = ds.child("userList").getValue(String.class);

                    //additional info about the event
                    String descr = ds.child("descr").getValue(String.class);
                    String tags = ds.child("tags").getValue(String.class);
                    double price = ds.child("priceEvent").getValue(double.class);

                    Event EVENT = new Event(id, name, uID, imgURL, local, latitude, longitude, date, time, duration, descr, userList, numR, maxR, price, tags);
                    EventFirebaseManager.getInstance().setEvents(EVENT);

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
