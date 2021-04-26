package com.kanad.users.activities;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.AuthResult;
import com.kanad.users.R;
import com.kanad.users.firebase.FirebaseAuthListener;
import com.kanad.users.databinding.ActivityRegisterBinding;
import com.kanad.users.utils.PearlTextUtils;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = RegisterActivity.class.getSimpleName();
    ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initContext(RegisterActivity.this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        previousActivityTo(LoginActivity.class);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                    onBackPressed();
                break;
            case R.id.tv_register:
                if (checkValidation()) {
                    postRegister();
                }
                break;
        }
    }

    private boolean checkValidation() {
        if (PearlTextUtils.isBlank(binding.etUsername.getText().toString().trim())) {
            showToast("Please enter username");
            return false;
        } else if (PearlTextUtils.isBlank(binding.etEmail.getText().toString().trim())) {
            showToast("Please enter email");
            return false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.getText().toString()).matches()) {
            showToast("Please enter a valid email address");
            return false;
        } else if (PearlTextUtils.isBlank(binding.etPassword.getText().toString().trim())) {
            showToast("Please enter password");
            return false;
        } else if (PearlTextUtils.isBlank(binding.etConfirmPassword.getText().toString().trim())) {
            showToast("Please enter confirm password");
            return false;
        } else if (!binding.etPassword.getText().toString().trim().equals(binding.etConfirmPassword.getText().toString().trim())) {
            showToast("Please enter same password");
            return false;
        }
        return true;
    }

    private void postRegister() {


            String email = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();

            mFirebaseManager.register((BaseActivity) mActivity, email, password, new FirebaseAuthListener() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    showToast("New User Registered");
                    nextActivityTo(MainActivity.class);
                }

                @Override
                public void onError(String error) {
                    showToast(error);
                }
            });
    }



}