package com.example.enigmaapp.model;

import android.app.Application;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.enigmaapp.repository.UserRepository;
import com.example.enigmaapp.web.login.LoginResult;

public class UserViewModel extends AndroidViewModel {

    private UserRepository repository;

    public UserViewModel(@NonNull Application application) {
        super(application);
        repository = new UserRepository(application);
    }

    public void fetchUser(String username, String password, TextView loginErrorMsg) {
        repository.fetchUser(username, password, loginErrorMsg);
    }

    public LoginResult getCurrentUser() {
        return repository.getmCurrentUser();
    }

    public void logoutCurrentUser() {
        repository.logoutUser();
    }
}
