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
import com.google.firebase.auth.FirebaseAuth;
import com.project.clapp.impl.EventFirebaseManager;

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

    private SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.getDefault());
    private SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
    ArrayList<com.project.clapp.models.Event> EVENTS = new ArrayList<>();
    CompactCalendarView compactCalendarView;
    private FirebaseAuth mAuth;

    public Calendar() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);
        mAuth = FirebaseAuth.getInstance();
        compactCalendarView = rootView.findViewById(R.id.compactcalendar_view);
        // Set first day of week to Monday, defaults to Monday so calling setFirstDayOfWeek is not necessary
        // Use constants provided by Java Calendar class



        compactCalendarView.setFirstDayOfWeek(java.util.Calendar.MONDAY);

        final TextView month = rootView.findViewById(R.id.monthName);
        final TextView year = rootView.findViewById(R.id.yearName);
        month.setText(monthFormat.format(System.currentTimeMillis()));
        year.setText(yearFormat.format(System.currentTimeMillis()));

        getEvents();

        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                List<Event> events = compactCalendarView.getEvents(dateClicked);
                Log.d("gato", "Day was clicked: " + dateClicked + " with events " + events);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                month.setText(monthFormat.format(firstDayOfNewMonth));
                year.setText(yearFormat.format(firstDayOfNewMonth));
                Log.d("gato", "Month was scrolled to: " + firstDayOfNewMonth);
            }
        });

        return rootView;


    }

    public void getEvents() {

        EVENTS = EventFirebaseManager.getInstance().getEventList();
        /*if (userList.contains(user.getUid())) {
            EVENTS.add(event);

        }*/
        for (int i = 0; i < EVENTS.size(); i++) {
            toMillis(EVENTS.get(i));
        }


    }

    public void toMillis(com.project.clapp.models.Event event) {
        String str = event.getDate() + " " + event.getTime();
        SimpleDateFormat df = new SimpleDateFormat("MMM dd yyyy HH:mm zzz");
        Date date;
        try {
            date = df.parse(str);
            long epoch = date.getTime();
            System.out.println(epoch);
            Event ev1 = new Event(Color.YELLOW, epoch, event.getName());
            compactCalendarView.addEvent(ev1);
        } catch (ParseException e) {
            Log.d("gato", e.toString());
        }

    }

}
