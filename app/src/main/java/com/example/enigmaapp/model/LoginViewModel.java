package com.example.enigmaapp.model;

import android.app.Application;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.enigmaapp.db.User;
import com.example.enigmaapp.repository.LoginRepository;
import com.example.enigmaapp.web.login.LoginResult;

import java.util.List;

public class LoginViewModel extends AndroidViewModel {

    private LoginRepository repository;
    private LiveData<List<User>> allUsers;

    public LoginViewModel(@NonNull Application app) {
        super(app);
        repository = new LoginRepository(app);
        allUsers = repository.getAllUsers();
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

    public void deleteAllUsers() {
        repository.deleteAllUsers();
    }
    public LiveData<List<User>> getAllUsers() { return allUsers; }
}
