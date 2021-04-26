package com.kanad.users.activities;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.AuthResult;
import com.kanad.users.R;
import com.kanad.users.firebase.FirebaseAuthListener;
import com.kanad.users.databinding.ActivityLoginBinding;
import com.kanad.users.utils.PearlTextUtils;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = LoginActivity.class.getSimpleName();
    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initContext(LoginActivity.this);

        if(mFirebaseManager.getUser() != null){
            nextActivityTo(MainActivity.class);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login:
                if (checkValidation()) {
                    postLogin();
                }
                break;
            case R.id.tv_register:
                nextActivityTo(RegisterActivity.class);
                break;
        }
    }

    private void postLogin() {

            String email = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();

            mFirebaseManager.login((BaseActivity) mActivity, email, password, new FirebaseAuthListener() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    nextActivityTo(MainActivity.class);
                }

                @Override
                public void onError(String error) {
                    showToast(error);
                }
            });

    }

    private boolean checkValidation() {
         if (PearlTextUtils.isBlank(binding.etEmail.getText().toString().trim())) {
            showToast("Please enter email");
            return false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.getText().toString()).matches()) {
             showToast("Please enter a valid email address");
            return false;
        } else if (PearlTextUtils.isBlank(binding.etPassword.getText().toString().trim())) {
             showToast("Please enter password");
            return false;
        }
        return true;
    }


}