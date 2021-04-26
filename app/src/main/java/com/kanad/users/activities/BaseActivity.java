package com.kanad.users.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kanad.users.MyApplication;
import com.kanad.users.firebase.FirebaseManager;
import com.kanad.users.webservices.ApiManager;
import com.kanad.users.utils.CommonFunctions;
import com.kanad.users.utils.GsonUtils;

public class BaseActivity extends AppCompatActivity {

    private static String TAG = BaseActivity.class.getSimpleName();

    protected Activity mActivity;

    protected Context mContext;

    private ProgressDialog mProgressDialog;

    private Toast toast;

    public GsonUtils mGsonUtils;

    protected ApiManager mApiManager;

    public FirebaseManager mFirebaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = CommonFunctions.createProgressDialog(mContext);
        }
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    protected void initContext(Activity activity) {

        mContext = activity.getApplicationContext();

        mActivity = activity;

        mGsonUtils = GsonUtils.getInstance();

        mApiManager = MyApplication.apiManager;

        mFirebaseManager = FirebaseManager.getInstance();
    }


    public void showToast(String message) {

        if (toast != null) {
            toast.cancel();
        }

        toast = Toast.makeText(mContext, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void hideKeyBoard(View view) {
        CommonFunctions.hideKeyboard(mContext, view);
    }

    public void nextActivityTo(Class cls) {

        CommonFunctions.changeActivity(mActivity, cls, true, true);
    }

    public void previousActivityTo(Class cls) {

        CommonFunctions.changeActivity(mActivity, cls, false, true);
    }

    protected void finishActivity() {
        CommonFunctions.finishActivity(mActivity);
    }

    protected void showExit() {
        CommonFunctions.setExitDialog(mActivity);
    }
}
