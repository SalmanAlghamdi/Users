package com.kanad.users.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.kanad.users.R;
import com.kanad.users.model.WeatherResponse;
import com.kanad.users.webservices.ApiResponseListener;
import com.kanad.users.databinding.ActivityDetailBinding;

public class WeatherActivity extends BaseActivity {

    private String TAG = WeatherActivity.class.getSimpleName();
    private ActivityDetailBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = (ActivityDetailBinding) DataBindingUtil.setContentView(this, R.layout.activity_detail);
        initContext(WeatherActivity.this);
        handleClick();
    }

    public void handleClick() {

        mBinding.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidated()) {
                    hideKeyBoard(mBinding.etCity);
                    checkEmailisExist();
                }
            }
        });

    }

    private void checkEmailisExist() {
            mApiManager.getCityDetail((BaseActivity) mActivity, mBinding.etCity.getText().toString(), new ApiResponseListener() {
                @Override
                public void onSuccess(String data) {

                    WeatherResponse weatherResponse = mGsonUtils.getGson().fromJson(data, WeatherResponse.class);

                    String title = "Weather of " + weatherResponse.getName() + " (" + weatherResponse.getSys().getCountry() + ")" + "\n";
                    String temp = "Temp : " + weatherResponse.getMain().getTemp() + "\n";
                    String visibility = "Visibility : " + weatherResponse.getVisibility() + "\n";
                    String description = "Description : " + weatherResponse.getWeather().get(0).getDescription() + "\n";
                    String wind = "Wind Speed : " + weatherResponse.getWind().getSpeed() + "\n";
                    String clouds = "cloudiness : " + weatherResponse.getClouds().getAll() + "\n";
                    String pressure = "pressure : " + weatherResponse.getMain().getPressure() + "\n";

                    mBinding.tvDetail.setText(title + temp + visibility + description + wind + clouds + pressure);
                }

                @Override
                public void onError(String error) {
                    showToast(error);
                    mBinding.tvDetail.setText(error);
                }
            });
    }

    private boolean isValidated() {
        boolean validated = true;

        if (mBinding.etCity.getText().toString().isEmpty()) {
            Toast.makeText(WeatherActivity.this,
                    "Please enter city name", Toast.LENGTH_LONG).show();
            validated = false;

        }

        return validated;
    }
}