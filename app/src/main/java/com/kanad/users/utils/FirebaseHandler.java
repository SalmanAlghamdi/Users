package com.kanad.users.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kanad.users.database.entity.User;
import com.kanad.users.listeners.OnUserAddListener;
import com.kanad.users.listeners.OnUserIDListener;
import com.kanad.users.listeners.OnUserListener;
import com.kanad.users.listeners.OnUserRemoveListener;

public class FirebaseHandler {
    public static final String USERS = "USERS";

    private static final String TAG = FirebaseHandler.class.getSimpleName();
    private static FirebaseHandler singleton;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    OnUserListener onUserListener;

    public FirebaseHandler() {
        singleton = this;
    }

    public static FirebaseHandler getFBContext() {
        if (singleton == null) {
            singleton = new FirebaseHandler();
        }
        return singleton;
    }

    public void addUser(User user, OnUserAddListener onUserAddListener) {
        myRef.child(USERS).child("" + user.getUserId()).setValue(user)
                .addOnCompleteListener(task -> {
                    onUserAddListener.onUserAdded(user);
                }).addOnFailureListener(e -> {
            onUserAddListener.onUserError(user, e.getMessage());
        });
    }


    public void startListenUsers() {
        myRef.child(USERS).addChildEventListener(childEventListener);
    }

    public void removeListenUsers() {
        myRef.child(USERS).removeEventListener(childEventListener);
    }

    ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            User user = snapshot.getValue(User.class);
            if (onUserListener != null) {
                onUserListener.onUserAdded(user);
            }
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            User user = snapshot.getValue(User.class);
            if (onUserListener != null) {
                onUserListener.onUserUpdated(user);
            }
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            User user = snapshot.getValue(User.class);
            if (onUserListener != null) {
                onUserListener.onUserRemoved(user);
            }
        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    public void setUserListener(OnUserListener onUserListener) {
        this.onUserListener = onUserListener;
    }

    public void getUserID(OnUserIDListener onUserIDListener) {
        Query lastQuery = myRef.child(USERS).orderByKey().limitToLast(1);
        lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        String key = child.getKey();
                        int userId = Integer.parseInt(key);
                        onUserIDListener.onGetNewUserID((userId + 1));
                    }
                } catch (Exception e) {
                    onUserIDListener.onGetNewUserError(e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                onUserIDListener.onGetNewUserError(databaseError.getMessage());
            }
        });
    }

    public void deleteUser(User user, OnUserRemoveListener onUserRemoveListener) {
        myRef.child(USERS).child("" + user.getUserId()).removeValue()
                .addOnCompleteListener(task -> {
                    onUserRemoveListener.onUserRemove(user);
                })
                .addOnFailureListener(e -> {
                    onUserRemoveListener.onUserError(user, e.getMessage());
                });
    }
}
