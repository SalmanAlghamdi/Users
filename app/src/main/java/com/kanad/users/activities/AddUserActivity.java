package com.kanad.users.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kanad.users.R;
import com.kanad.users.database.entity.User;
import com.kanad.users.listeners.OnUserAddListener;
import com.kanad.users.listeners.OnUserIDListener;
import com.kanad.users.databinding.ActivityAddUserBinding;
import com.kanad.users.utils.FirebaseHandler;
import com.kanad.users.utils.PearlTextUtils;

public class AddUserActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = AddUserActivity.class.getSimpleName();
    ActivityAddUserBinding binding;
    User userModel = null;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initViews();
        setListeners();
    }


    public void initViews() {
        try {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null && bundle.containsKey("model")) {
                userModel = (User) bundle.getSerializable("model");

                binding.tvHeader.setText("Update User");
                binding.tvAdd.setText("Update");

                binding.edtFname.setText(userModel.getFirstName());
                binding.edtLname.setText(userModel.getLastName());
                binding.edtPhone.setText(userModel.getPhoneNumber());
                binding.edtEmail.setText("" + userModel.getEmailAddress());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setListeners() {
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_add:
                if (checkValidation()) {
                    showProgressDialog();
                    if (userModel != null) {
                        userModel.setFirstName(binding.edtFname.getText().toString().trim());
                        userModel.setLastName(binding.edtLname.getText().toString().trim());
                        userModel.setPhoneNumber(binding.edtPhone.getText().toString().trim());
                        userModel.setEmailAddress(binding.edtEmail.getText().toString().trim());
                        //user.setUpdatedAt(ServerValue.TIMESTAMP);
                        addUser(userModel);
                    } else {
                        FirebaseHandler.getFBContext().getUserID(new OnUserIDListener() {
                            @Override
                            public void onGetNewUserID(int userID) {
                                userModel = new User();
                                userModel.setUserId(userID);
                                userModel.setFirstName(binding.edtFname.getText().toString().trim());
                                userModel.setLastName(binding.edtLname.getText().toString().trim());
                                userModel.setPhoneNumber(binding.edtPhone.getText().toString().trim());
                                userModel.setEmailAddress(binding.edtEmail.getText().toString().trim());
                                //user.setUpdatedAt(ServerValue.TIMESTAMP);
                                addUser(userModel);
                            }

                            @Override
                            public void onGetNewUserError(String msg) {
                                hideDialog();
                                showToastMSG("Something went wrong");
                            }
                        });
                    }
                }
                break;
        }
    }

    private boolean checkValidation() {
        if (PearlTextUtils.isBlank(binding.edtFname.getText().toString().trim())) {
            showToastMSG("Please enter first name");
            return false;
        } else if (PearlTextUtils.isBlank(binding.edtLname.getText().toString().trim())) {
            showToastMSG("Please enter last name");
            return false;
        } else if (PearlTextUtils.isBlank(binding.edtPhone.getText().toString().trim())) {
            showToastMSG("Please enter phone number");
            return false;
        } else if (binding.edtPhone.getText().toString().trim().length() < 10) {
            showToastMSG("Please enter valid phone number");
            return false;
        } else if (PearlTextUtils.isBlank(binding.edtEmail.getText().toString().trim())) {
            showToastMSG("Please enter email number");
            return false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(binding.edtEmail.getText().toString()).matches()) {
            showToastMSG("Please enter a valid email address");
            return false;
        }
        return true;
    }

    private void showToastMSG(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void addUser(User user) {
        FirebaseHandler.getFBContext().addUser(user, new OnUserAddListener() {
            @Override
            public void onUserAdded(User user) {
                hideDialog();
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                onBackPressed();
            }

            @Override
            public void onUserError(User user, String msg) {
                hideDialog();
                showToastMSG(msg);
            }
        });
    }

    protected void showProgressDialog() {
        try {
            if (mProgressDialog == null)
                mProgressDialog = ProgressDialog.show(this, "", "Loading...", false,
                        false);
            if (!mProgressDialog.isShowing()) mProgressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void hideDialog() {
        try {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}