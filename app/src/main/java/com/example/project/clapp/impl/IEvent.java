package com.example.project.clapp.impl;

import com.example.project.clapp.models.Event;

import java.util.ArrayList;

/**
 * Created by ruigo on 12/03/2018.
 */

public interface IEvent {

    Event getEvent(int id);

    void getEvents();

    ArrayList<Event> getEventList();

    void addEvent(String nameEvent, String dateEvent, String timeEvent, String localEvent, String durationEvent, String priceEvent, String descEvent, String capEvent, String userId);
}
