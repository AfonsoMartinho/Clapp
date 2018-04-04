package com.project.clapp;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class Calendar extends Fragment {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
    ArrayList<com.project.clapp.models.Event> EVENTS = new ArrayList<>();
    CompactCalendarView compactCalendarView;

    public Calendar() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);

        compactCalendarView = rootView.findViewById(R.id.compactcalendar_view);
        // Set first day of week to Monday, defaults to Monday so calling setFirstDayOfWeek is not necessary
        // Use constants provided by Java Calendar class



        compactCalendarView.setFirstDayOfWeek(java.util.Calendar.MONDAY);

        final TextView month = rootView.findViewById(R.id.monthName);
        month.setText(dateFormat.format(System.currentTimeMillis()));

        getEvents();

        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                List<Event> events = compactCalendarView.getEvents(dateClicked);
                Log.d("gato", "Day was clicked: " + dateClicked + " with events " + events);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                month.setText(dateFormat.format(firstDayOfNewMonth));
                Log.d("gato", "Month was scrolled to: " + firstDayOfNewMonth);
            }
        });

        return rootView;


    }

    public void getEvents() {
        DatabaseReference dataEvents;
        dataEvents = FirebaseDatabase.getInstance().getReference();
        DatabaseReference eventListRef = dataEvents.child("events");
        eventListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                EVENTS.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    String id = ds.child("id").getValue(String.class);
                    String name = ds.child("name").getValue(String.class);
                    String date = ds.child("date").getValue(String.class);
                    String time = ds.child("time").getValue(String.class);
                    com.project.clapp.models.Event event = new com.project.clapp.models.Event(id, name, date, time);
                    EVENTS.add(event);

                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("error", databaseError.toString());
            }
        });

        for (int i = 0; i < EVENTS.size(); i++) {
            toMillis(EVENTS.get(i));
        }
    }

    public void toMillis(com.project.clapp.models.Event event) {
        String str = event.getDate() + " " + event.getTime();
        SimpleDateFormat df = new SimpleDateFormat("mmm dd YYYY HH:mm");
        Date date = null;
        try {
            date = df.parse(str);
            long epoch = date.getTime();
            System.out.println(epoch);
            Event ev1 = new Event(Color.GREEN, epoch, event.getName());
            compactCalendarView.addEvent(ev1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

}
