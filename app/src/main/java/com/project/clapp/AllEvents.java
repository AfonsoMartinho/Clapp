package com.project.clapp.clapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.clapp.clapp.models.Event;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllEvents extends Fragment {

    private FirebaseAuth mAuth;
    private DrawerLayout mDrawerLayout;
    private static final String TAG = "FirebaseTest";
    EventAdapter eventAdapter;
    RecyclerView rv;

    ArrayList<Event> EVENTS = new ArrayList<>();
    //private EventAdapter eventAdapter;


    public AllEvents() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_all_events, container, false);

        rv = rootView.findViewById(R.id.recyclerView);
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(rootView.getContext());
        rv.setLayoutManager(llm);

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
                    String imgURL = ds.child("imgURL").getValue(String.class);
                    String date = ds.child("date").getValue(String.class);
                    String time = ds.child("time").getValue(String.class);
                    String local = ds.child("local").getValue(String.class);
                    int maxR = ds.child("maxRegisters").getValue(int.class);
                    int numR = ds.child("numRegister").getValue(int.class);
                    String duration = ds.child("duration").getValue(String.class);
                    int price = ds.child("preco").getValue(int.class);
                    String descr = ds.child("descr").getValue(String.class);
                    String userList = ds.child("userList").getValue(String.class);

                    Event event = new Event(id, nome, uID, imgURL, local, date, time, duration, descr, userList, numR, maxR, price);
                    EVENTS.add(event);
                }
                eventAdapter = new EventAdapter(EVENTS);
                rv.setAdapter(eventAdapter);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, databaseError.toString());
            }
        });

        return rootView;
    }

}
