package com.kanad.users.listeners;

import com.kanad.users.database.entity.User;

public interface OnUserAddListener {

    public void onUserAdded(User user);

    public void onUserError(User user, String msg);
}
