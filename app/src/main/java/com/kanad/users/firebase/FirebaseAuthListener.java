package com.kanad.users.firebase;

import com.google.firebase.auth.AuthResult;

public interface FirebaseAuthListener {
    void onSuccess(AuthResult authResult);
    void onError(String error);
}
