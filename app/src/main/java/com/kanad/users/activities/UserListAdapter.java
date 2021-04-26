package com.kanad.users.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.kanad.users.R;
import com.kanad.users.database.entity.User;
import com.kanad.users.listeners.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.SimpleViewHolder> {

    private static final String TAG = UserListAdapter.class.getName();
    private List<User> expiryModels = new ArrayList<>();
    private Context mContext;
    private OnItemClickListener onItemClickListener;

    public UserListAdapter(List<User> projects, Context context, OnItemClickListener onItemClickListener) {
        if (projects != null) {
            this.expiryModels = projects;
        }
        this.mContext = context;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_exp, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {
        holder.bindData(expiryModels.get(position));
    }

    @Override
    public int getItemCount() {
        return expiryModels.size() > 0 ? expiryModels.size() : 0;
    }

    public class SimpleViewHolder extends RecyclerView.ViewHolder {

        TextView tv_fl_name, tv_email, tv_phone;
        LinearLayout ll_user_row;
        ImageView iv_delete, iv_edit;

        public SimpleViewHolder(final View itemView) {
            super(itemView);

            tv_fl_name = itemView.findViewById(R.id.tv_fl_name);
            tv_email = itemView.findViewById(R.id.tv_email);
            tv_phone = itemView.findViewById(R.id.tv_phone);

            iv_delete = itemView.findViewById(R.id.iv_delete);
            iv_edit = itemView.findViewById(R.id.iv_edit);

            ll_user_row = itemView.findViewById(R.id.ll_user_row);
        }

        public void bindData(final User user) {
            try {
                tv_fl_name.setText("" + user.getFirstName() + " " + user.getLastName());
                tv_email.setText(user.getEmailAddress());
                tv_phone.setText(user.getPhoneNumber());

                iv_delete.setOnClickListener(view -> onItemClickListener.onItemClick(view, getAdapterPosition()));
                iv_edit.setOnClickListener(view -> onItemClickListener.onItemClick(view, getAdapterPosition()));
                ll_user_row.setOnClickListener(view -> onItemClickListener.onItemClick(view, getAdapterPosition()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}