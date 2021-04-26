package com.kanad.users.firebase;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kanad.users.activities.BaseActivity;

public class FirebaseManager {

    private static FirebaseManager firebaseManager;


    private FirebaseAuth firebaseAuth;

    private FirebaseManager() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public static FirebaseManager getInstance() {
        if (firebaseManager == null) {
            firebaseManager = new FirebaseManager();
        }
        return firebaseManager;
    }


    public void login(BaseActivity baseActivity, String email, String pass, FirebaseAuthListener firebaseAuthListener) {
        baseActivity.showProgressDialog();
        Task<AuthResult> authResultTask = firebaseAuth.signInWithEmailAndPassword(email, pass);
        authResultTask.addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                baseActivity.hideProgressDialog();
                firebaseAuthListener.onSuccess(authResult);
            }
        });
        authResultTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                baseActivity.hideProgressDialog();
                firebaseAuthListener.onError(e.getLocalizedMessage());
            }
        });
    }

    public void logout() {
        firebaseAuth.signOut();
    }

    public void register(BaseActivity baseActivity, String email, String pass, FirebaseAuthListener firebaseAuthListener) {
        baseActivity.showProgressDialog();
        Task<AuthResult> authResultTask = firebaseAuth.createUserWithEmailAndPassword(email, pass);
        authResultTask.addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                baseActivity.hideProgressDialog();
                firebaseAuthListener.onSuccess(authResult);
            }
        });
        authResultTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                baseActivity.hideProgressDialog();
                firebaseAuthListener.onError(e.getLocalizedMessage());
            }
        });
    }

    public FirebaseUser getUser() {
        return  firebaseAuth.getCurrentUser();
    }

}
