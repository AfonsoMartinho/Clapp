package com.project.clapp.clapp.impl;


import com.project.clapp.clapp.models.User;

/**
 * Created by Afonso on 11/03/2018.
 */

public interface IUser {

    User getUser(String name);

    void addUser(String userId, String mail);

    void removeUser(String name);
}
