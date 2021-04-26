package com.kanad.users.listeners;

import com.kanad.users.database.entity.User;

public interface OnUserRemoveListener {

    public void onUserRemove(User user);

    public void onUserError(User user, String msg);
}
