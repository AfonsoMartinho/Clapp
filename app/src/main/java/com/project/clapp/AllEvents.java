package com.project.clapp;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.clapp.models.Event;


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
    String tags;
    Button filterBtn;


    ArrayList<Event> EVENTS = new ArrayList<>();


    public AllEvents() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_all_events, container, false);

        rv = rootView.findViewById(R.id.recyclerView);
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(rootView.getContext());
        rv.setLayoutManager(llm);

        fillEventList(rootView, "NONE");


        filterBtn = rootView.findViewById(R.id.filtBtn);
        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterEvents(rootView);
            }
        });


        return rootView;
    }

    public void filterEvents(View view) {
        final CharSequence[] items = {"Workshop","Lecture","Documentary","Tutorial","Dinner","Fun Activity"};

        final ArrayList selectedItems=new ArrayList();

        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("Tags to filter by")
                .setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
                        if (isChecked) {
                            // If the user checked the item, add it to the selected items
                            selectedItems.add(indexSelected);
                        } else if (selectedItems.contains(indexSelected)) {
                            // Else, if the item is already in the array, remove it
                            selectedItems.remove(Integer.valueOf(indexSelected));
                        }
                    }
                }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        tags = "";
                        //  Your code when user clicked on OK
                        //  You can write the code  to save the selected item here
                        for (int i = 0; i < selectedItems.size(); i++) {
                            tags = tags + "," + selectedItems.get(i).toString();
                        }
                        tags = tags.substring(1);

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //  Your code when user clicked on Cancel
                    }
                }).create();
        dialog.show();
    }

    public void fillEventList(View view, final String filter) {
        DatabaseReference dataEvents;
        dataEvents = FirebaseDatabase.getInstance().getReference();
        DatabaseReference eventListRef = dataEvents.child("events");
        eventListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                EVENTS.clear();
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

                    if (filter.equals("NONE")) {
                        Event event = new Event(id, nome, uID, imgURL, local, date, time, duration, descr, userList, numR, maxR, price);
                        EVENTS.add(event);
                    }
                }
                eventAdapter = new EventAdapter(EVENTS);
                rv.setAdapter(eventAdapter);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, databaseError.toString());
            }
        });
    }

}
