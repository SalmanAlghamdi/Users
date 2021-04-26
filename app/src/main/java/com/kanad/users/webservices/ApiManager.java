package com.kanad.users.webservices;


import com.kanad.users.model.ApiResponse;
import com.kanad.users.model.ErrorResponse;
import com.kanad.users.activities.BaseActivity;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiManager {

    private static ApiInterface apiInterface;

    private static ApiManager apiManager;

    private ApiManager() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/data/2.5/")
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiInterface = retrofit.create(ApiInterface.class);
    }

    public static ApiManager getInstance() {
        if (apiManager == null) {
            apiManager = new ApiManager();
        }
        return apiManager;
    }

    public void getCityDetail(BaseActivity baseActivity, String q, final ApiResponseListener apiResponseListener) {

        baseActivity.showProgressDialog();

        apiInterface.getCityDetail(q).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                baseActivity.hideProgressDialog();

                ApiResponse apiResponse = baseActivity.mGsonUtils.getGson().fromJson(response.body(), ApiResponse.class);

                try {

                    String code = "" + apiResponse.getCod();

                    if (code.equals("200")) {

                        apiResponseListener.onSuccess(response.body());
                    } else {

                        ErrorResponse errorResponse = baseActivity.mGsonUtils.getGson().fromJson(response.body(), ErrorResponse.class);
                        apiResponseListener.onError(errorResponse.getMessage());
                    }
                } catch (Exception e) {
                    apiResponseListener.onError("Data Not Found");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                baseActivity.hideProgressDialog();
                baseActivity.showToast(t.getMessage());
            }
        });
    }

}
