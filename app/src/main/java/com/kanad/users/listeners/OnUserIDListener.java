package com.kanad.users.listeners;

public interface OnUserIDListener {
    public void onGetNewUserID(int userID);
    public void onGetNewUserError(String msg);
}
