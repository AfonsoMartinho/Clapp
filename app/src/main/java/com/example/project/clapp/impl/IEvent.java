package com.example.project.clapp.impl;

import com.example.project.clapp.models.Event;

/**
 * Created by ruigo on 12/03/2018.
 */

public interface IEvent {

    Event getEvent(int id);

    Event getEvents();

    void addEvent(String nome, String userId);
}
