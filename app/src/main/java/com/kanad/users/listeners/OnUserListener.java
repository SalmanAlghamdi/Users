package com.kanad.users.listeners;

import com.kanad.users.database.entity.User;

import java.util.ArrayList;

public interface OnUserListener {

    public void onUsersAdded(ArrayList<User> users);

    public void onUserAdded(User user);

    public void onUserUpdated(User user);

    public void onUserRemoved(User user);
}
