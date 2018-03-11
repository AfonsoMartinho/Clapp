package com.example.project.clapp.impl;

import com.example.project.clapp.models.User;

/**
 * Created by Afonso on 11/03/2018.
 */

public interface IUser {

    User getUser(String name);

    void addUser(String id, String name, String mail);

    void removeUser(String name);
}
