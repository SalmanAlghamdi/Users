package com.kanad.users.webservices;

public interface ApiResponseListener {
    void onSuccess(String data);
    void onError(String error);
}
