package com.project.clapp.impl;

import com.project.clapp.models.Event;

import java.util.ArrayList;

/**
 * Created by ruigo on 12/03/2018.
 */

public interface IEvent {

    Event getEvent(String id);

    ArrayList<Event> getEventList();

    ArrayList<Event> getEventListTagFilter(ArrayList<String> tags);

    void registUser(Event event, int i);

    void clearEvents();

    void setEvents(Event event, int i);

    void addEvent(String nameEvent, String dateEvent, String timeEvent, String placeEvent, String localEvent, String durationEvent, double priceEvent, String descEvent, int capEvent, String userId, double latitude, double longitude, String imgURL, ArrayList<String> tags);
}
