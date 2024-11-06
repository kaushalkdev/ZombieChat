package com.example.zombiechat.account.viewModel;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;
import com.example.zombiechat.account.data.models.UserModel;
import com.example.zombiechat.account.data.repo.AuthRepo;

public class AuthVM extends ViewModel {
    final AuthRepo repo;
    @Nullable
    UserModel currentUser;

    public AuthVM(AuthRepo repo) {
        this.repo = repo;
    }

    @Nullable
    public UserModel getCurrentUser() {
        return currentUser;
    }


    public void signIn() {
    }

    public void signOut() {
    }

    public void createAccount(UserModel user) {
        repo.createNewUser(user);
    }


}
