package com.example.project.clapp.impl;

import com.example.project.clapp.models.Event;

import java.util.ArrayList;

/**
 * Created by ruigo on 12/03/2018.
 */

public interface IEvent {

    Event getEvent(int id);

    ArrayList<Event> getEvents();

    void addEvent(String nome, String userId);
}
