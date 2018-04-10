package com.project.clapp;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
    ArrayList<com.project.clapp.models.Event> EVENTS = new ArrayList<>(), EVENTSFINAL = new ArrayList<>();
    CompactCalendarView compactCalendarView;
    private FirebaseAuth mAuth;
    EventAdapter eventAdapter;
    RecyclerView rv;


    public Calendar() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);
        mAuth = FirebaseAuth.getInstance();
        compactCalendarView = rootView.findViewById(R.id.compactcalendar_view);
        // Set first day of week to Monday, defaults to Monday so calling setFirstDayOfWeek is not necessary
        // Use constants provided by Java Calendar class

        rv = rootView.findViewById(R.id.eventListCalendar);
        rv.setHasFixedSize(true);



        compactCalendarView.setFirstDayOfWeek(java.util.Calendar.MONDAY);

        final TextView month = rootView.findViewById(R.id.monthName);
        final TextView year = rootView.findViewById(R.id.yearName);
        month.setText(monthFormat.format(System.currentTimeMillis()));
        year.setText(yearFormat.format(System.currentTimeMillis()));



        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                EVENTSFINAL.clear();
                List<Event> events = compactCalendarView.getEvents(dateClicked);

                for (int i = 0; i < events.size(); i++) {

                    String data = events.get(i).getData().toString();

                    LinearLayoutManager llm = new LinearLayoutManager(rootView.getContext());
                    rv.setLayoutManager(llm);

                    com.project.clapp.models.Event event = EventFirebaseManager.getInstance().getEvent(data);
                    EVENTSFINAL.add(event);
                    fillEventList(rootView);
                }
                Log.d("gato", Integer.toString(EVENTSFINAL.size()));
                Log.d("gato", "Day was clicked: " + dateClicked + " with events " + events);

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                month.setText(monthFormat.format(firstDayOfNewMonth));
                year.setText(yearFormat.format(firstDayOfNewMonth));
                Log.d("gato", "Month was scrolled to: " + firstDayOfNewMonth);
            }
        });


        getEvents();
        return rootView;


    }


    public void getEvents() {

        ArrayList<com.project.clapp.models.Event> EventList = EventFirebaseManager.getInstance().getEventList();

        System.out.println(EventList.size());

        for (int i = 0; i < EventList.size(); i++) {
            if (EventList.get(i).getUserList().contains(mAuth.getCurrentUser().getUid())) {
                EVENTS.add(EventList.get(i));
            }
        }
        for (int i = 0; i < EVENTS.size(); i++) {
            toMillis(EVENTS.get(i));
        }

    }

    public void toMillis(com.project.clapp.models.Event event) {
        String str = event.getDate() + " " + event.getTime();
        SimpleDateFormat df = new SimpleDateFormat("MMM dd yyyy HH:mm zzz", Locale.ENGLISH);
        Date date;

        try {
            date = df.parse(str);
            long epoch = date.getTime();
            Event ev1 = new Event(Color.YELLOW, epoch, event.getId());
            compactCalendarView.addEvent(ev1);
        } catch (ParseException e) {
            System.out.println(e);
        }



    }

    public void fillEventList(View view) {

        eventAdapter = new EventAdapter(EVENTSFINAL, 2);
        rv.setAdapter(eventAdapter);


    }

}
