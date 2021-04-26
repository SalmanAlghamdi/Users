package com.kanad.users.webservices;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {
    @POST("weather?appid=50217f4ce05f5c63b9de0f0c332e84d8")
    Call<String> getCityDetail(@Query("q") String q);
}
