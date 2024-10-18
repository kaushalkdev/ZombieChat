package com.example.zombiechat.account.viewModel;

import androidx.lifecycle.ViewModel;

import com.example.zombiechat.account.data.models.UserModel;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;

public class AuthVM extends ViewModel {
    final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    UserModel currentUser;

    public UserModel getCurrentUser() {
        return currentUser;
    }


    public void signIn() {
    }

    public void signOut() {
    }

    public void createAccount() {
    }


}
