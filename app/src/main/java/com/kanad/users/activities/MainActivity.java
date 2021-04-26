package com.kanad.users.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.kanad.users.R;
import com.kanad.users.database.DatabaseClient;
import com.kanad.users.database.entity.User;
import com.kanad.users.firebase.FirebaseManager;
import com.kanad.users.listeners.OnItemClickListener;
import com.kanad.users.listeners.OnUserListener;
import com.kanad.users.listeners.OnUserRemoveListener;
import com.kanad.users.databinding.ActivityMainBinding;
import com.kanad.users.utils.EqualSpacingItemDecoration;
import com.kanad.users.utils.FirebaseHandler;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static int REQ_CODE_ADD_USER = 202;
    UserListAdapter userListAdapter;
    ArrayList<User> userArrayList = new ArrayList<>();
    Handler handler = new Handler();
    ProgressDialog mProgressDialog;

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initViews();
        setListeners();
    }


    public void initViews() {
        binding.incMainCnt.rvUsersList.addItemDecoration(new EqualSpacingItemDecoration(16, EqualSpacingItemDecoration.VERTICAL));
        userListAdapter = new UserListAdapter(userArrayList, this, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()) {
                    case R.id.iv_edit:
                        startActivityForResult(new Intent(MainActivity.this, AddUserActivity.class)
                                        .putExtra("model", userArrayList.get(position)),
                                REQ_CODE_ADD_USER);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        break;
                    case R.id.iv_delete:
                        new MaterialAlertDialogBuilder(MainActivity.this)
                                .setMessage("Are you sure you want to delete entry?")
                                .setPositiveButton("Delete", (dialog, which) -> {
                                    FirebaseHandler.getFBContext().deleteUser(userArrayList.get(position), new OnUserRemoveListener() {
                                        @Override
                                        public void onUserRemove(User user) {

                                        }

                                        @Override
                                        public void onUserError(User user, String msg) {

                                        }
                                    });
                                })
                                .setNegativeButton("Cancel", (dialog, which) -> {

                                })
                                .show();
                        break;
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        binding.incMainCnt.rvUsersList.setAdapter(userListAdapter);
        new getAllUserData().execute();
    }


    public void setListeners() {
        binding.ivAdd.setOnClickListener(this);
        binding.incMainCnt.tvWeather.setOnClickListener(this);
        FirebaseHandler.getFBContext().setUserListener(onUserListener);
    }

    OnUserListener onUserListener = new OnUserListener() {
        @Override
        public void onUsersAdded(ArrayList<User> users) {

        }

        @Override
        public void onUserAdded(User user) {
            int index = userArrayList.indexOf(user);
            if (index != -1) {
                userArrayList.set(index, user);
            } else {
                userArrayList.add(user);
            }
            handler.removeCallbacks(runnable);
            handler.postDelayed(runnable, 2000);
        }

        @Override
        public void onUserUpdated(User user) {
            int index = userArrayList.indexOf(user);
            if (index != -1) {
                userArrayList.set(index, user);
                userListAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onUserRemoved(User user) {
            int index = userArrayList.indexOf(user);
            if (index != -1) {
                new DeleteExpiryDetails(user).execute();
                userArrayList.remove(index);
                userListAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                FirebaseManager.getInstance().logout();
                startActivity(new Intent(this, LoginActivity.class));
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                finish();
                break;
            case R.id.iv_add:
                startActivityForResult(new Intent(this, AddUserActivity.class),
                        REQ_CODE_ADD_USER);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.tv_weather:
                startActivity(new Intent(this, WeatherActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseHandler.getFBContext().setUserListener(null);
        FirebaseHandler.getFBContext().removeListenUsers();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        handler.removeCallbacks(runnable);
        FirebaseHandler.getFBContext().setUserListener(null);
        FirebaseHandler.getFBContext().removeListenUsers();
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (userArrayList.size() > 0) {
                userListAdapter.notifyDataSetChanged();
                if (!binding.incMainCnt.rvUsersList.isShown()) {
                    binding.incMainCnt.rvUsersList.setVisibility(View.VISIBLE);
                    binding.incMainCnt.llNoData.setVisibility(View.GONE);
                }
                new AddAllUserData().execute();
            }
        }
    };

    private class getAllUserData extends AsyncTask<Void, Void, List<User>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<User> doInBackground(Void... voids) {
            List<User> carBrand = DatabaseClient.getInstance(MainActivity.this).getAppDatabase().getUserAppDao().getAllUserData();
            return carBrand;
        }

        @Override
        protected void onPostExecute(List<User> users) {
            super.onPostExecute(users);
            if (users != null && users.size() > 0) {
                userArrayList.clear();
                userArrayList.addAll(users);

                binding.incMainCnt.rvUsersList.setVisibility(View.VISIBLE);
                binding.incMainCnt.llNoData.setVisibility(View.GONE);
                userListAdapter.notifyDataSetChanged();
            }
            FirebaseHandler.getFBContext().startListenUsers();
        }
    }

    private class AddAllUserData extends AsyncTask<Void, Void, long[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected long[] doInBackground(Void... voids) {
            long[] record_id = DatabaseClient.getInstance(MainActivity.this).getAppDatabase().getUserAppDao().insertAllUser(userArrayList);
            return record_id;
        }

        @Override
        protected void onPostExecute(long[] record_id) {
            super.onPostExecute(record_id);
            if (record_id.length > 0) {

            }
        }
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

    private class DeleteExpiryDetails extends AsyncTask<Void, Void, Integer> {

        User user;

        public DeleteExpiryDetails(User user) {
            this.user = user;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            int i = DatabaseClient.getInstance(MainActivity.this).getAppDatabase().getUserAppDao().deleteUserDetails(user);
            return i;
        }

        @Override
        protected void onPostExecute(Integer entry) {
            super.onPostExecute(entry);
            try {
                if (userArrayList.size() == 0) {
                    binding.incMainCnt.rvUsersList.setVisibility(View.INVISIBLE);
                    binding.incMainCnt.llNoData.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}