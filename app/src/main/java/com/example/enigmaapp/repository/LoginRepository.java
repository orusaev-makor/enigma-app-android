package com.example.enigmaapp.repository;

import android.app.Application;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.enigmaapp.activity.UserActivity;
import com.example.enigmaapp.db.User;
import com.example.enigmaapp.db.UserDao;
import com.example.enigmaapp.db.UserDatabase;
import com.example.enigmaapp.service.DatasetService;
import com.example.enigmaapp.web.RetrofitClient;
import com.example.enigmaapp.web.login.LoginResult;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginRepository {

    // TODO: to be removed after only user saved in room is used all over the app:
    public static LoginResult mCurrentUser = new LoginResult();

    private Application application;
    private UserDao userDao;
    private LiveData<List<User>> allUsers = new MutableLiveData<>();

    public LoginRepository(Application application) {
        this.application = application;
        UserDatabase userDatabase = UserDatabase.getInstance(application);
        userDao = userDatabase.userDao();
        allUsers = userDao.getAllUsers();
    }

    public LoginResult getCurrentUser() {
        return mCurrentUser;
    }

    public void changePassword(HashMap<String, String> map, TextView changePassErrorMsg) {
        Call<Void> call = RetrofitClient.getInstance().getRetrofitInterface().executeChangePassword(mCurrentUser.getToken(), map);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    changePassErrorMsg.setText("Passwords entered incorrectly, please try again.");
                    return;
                }
                changePassErrorMsg.setText("");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println("t.getMessage(): " + t.getMessage());
                Toast.makeText(application, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void fetchUser(String username, String password, TextView loginErrorMsg) {
        loginErrorMsg.setText("");
        HashMap<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);

        Call<LoginResult> call = RetrofitClient.getInstance().getRetrofitInterface().executeLogin(map);

        call.enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Code: " + response.code() + "Error: " + response.message());
                    loginErrorMsg.setText((response.code() == 403) ? "Incorrect username or password" : response.message());
                    return;
                }

                String token = response.body().getToken();
                User loggedUser = new User(token, username);

                SaveLoggedUserRunnable runnable = new SaveLoggedUserRunnable(loggedUser);
                new Thread(runnable).start();
                startUserActivity(username, token);
                enqueueWorkInDatasetService(token);
            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                System.out.println("t.getMessage(): " + t.getMessage());
                Toast.makeText(application, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                loginErrorMsg.setText(t.getMessage());
            }
        });
    }

    private void startUserActivity(String username, String token) {
        mCurrentUser.setToken(token);
        mCurrentUser.setUsername(username);

        // start next activity
        Intent intent = new Intent(application.getBaseContext(), UserActivity.class);
        intent.putExtra("usernameExtra", username);
        intent.putExtra("tokenExtra", token);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        application.getBaseContext().startActivity(intent);
    }

    public void enqueueWorkInDatasetService(String token) {
        Intent serviceIntent = new Intent(application.getBaseContext(), DatasetService.class);
        serviceIntent.putExtra("tokenExtra", token);
        DatasetService.enqueueWork(application.getBaseContext(), serviceIntent);
    }

    public void logoutUser() {
        Call<Void> call = RetrofitClient.getInstance().getRetrofitInterface().executeLogout(mCurrentUser.getToken());

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    mCurrentUser = new LoginResult();
                } else if (response.code() == 400) {
                    Toast.makeText(application, "Please try again", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(application, "ERROR: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void insert(User user) {
        new InsertUserAsyncTask(userDao).execute(user);
    }

    public void deleteAllUsers() {
        new DeleteAllUsersAsyncTask(userDao).execute();
    }

    public LiveData<List<User>> getAllUsers() {
        return allUsers;
    }

    private static class DeleteAllUsersAsyncTask extends AsyncTask<Void, Void, Void> {
        private UserDao userDao;

        private DeleteAllUsersAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            userDao.deleteAllUsers();
            return null;
        }
    }

    private static class InsertUserAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDao userDao;

        private InsertUserAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDao.insert(users[0]);
            return null;
        }
    }

    class SaveLoggedUserRunnable implements Runnable {
        User user;

        public SaveLoggedUserRunnable(User user) {
            this.user = user;
        }

        @Override
        public void run() {
            //                // reset old user info
            if (userDao.getAllUsers() != null) {
                userDao.deleteAllUsers();
            }

            userDao.insert(user);
        }
    }
}
