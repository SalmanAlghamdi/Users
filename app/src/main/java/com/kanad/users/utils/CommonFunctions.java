package com.kanad.users.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.kanad.users.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CommonFunctions {
    static String TAG = CommonFunctions.class.getSimpleName();

    public static void changeActivity(Activity activity, Class cls, boolean isSlideOutRight, boolean isClearTop) {

        Intent i = new Intent(activity, cls);

        if (isClearTop) {
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }

        activity.startActivity(i);
        if (isSlideOutRight) {
            activity.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        } else {
            activity.overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
        }


    }

    public static void finishActivity(Context context) {
        ((Activity) context).finish();
    }


    public static CharSequence changeCustomDateFormat(String input_format, String output_format, String date) {
        SimpleDateFormat sdf = new SimpleDateFormat(input_format);
        SimpleDateFormat sdf1 = new SimpleDateFormat(output_format);
        try {
            date = sdf1.format(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static void hideKeyboard(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static ProgressDialog createProgressDialog(Context mContext) {
        ProgressDialog dialog = null;
        try {
            dialog = new ProgressDialog(mContext, R.style.MyProgressDialog);
            dialog.show();
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setProgressDrawable((new ColorDrawable(Color.GRAY)));
            dialog.setContentView(R.layout.dialog_progress);
        } catch (WindowManager.BadTokenException e) {
            dialog = new ProgressDialog(mContext);
        } catch (Exception e) {
            dialog = new ProgressDialog(mContext);
        }
        return dialog;
    }


    public static void setExitDialog(final Activity activity) {
        try {

            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle(activity.getResources().getString(R.string.app_name));
            builder.setMessage("Are you sure you want to exit?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intent);
                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

        } catch (Exception e) {
        }
    }
}
