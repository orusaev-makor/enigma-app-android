package com.example.enigmaapp.repository;

import android.app.Activity;
import android.app.Application;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.enigmaapp.R;
import com.example.enigmaapp.activity.fragment.BalanceFragment;
import com.example.enigmaapp.activity.fragment.LoginFragment;
import com.example.enigmaapp.web.RetrofitClient;
import com.example.enigmaapp.web.login.LoginResult;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private LoginResult mCurrentUser = new LoginResult();
    private Application application;

    public UserRepository(Application application) {
        this.application = application;
    }

    public LoginResult getmCurrentUser() {
        return mCurrentUser;
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
                    loginErrorMsg.setText("Wrong Credentials");
                    return;
                }

                String token = response.body().getToken();
                mCurrentUser.setToken(token);
                mCurrentUser.setUsername(username);
                LoginFragment.setUserBalanceView();
            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                System.out.println("t.getMessage(): " + t.getMessage());
                Toast.makeText(application, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                loginErrorMsg.setText(t.getMessage());
            }
        });
    }

    public void logoutUser() {
        System.out.println("USER BEFORE LOGOUT ++++++++++++++++++++++++++ " + mCurrentUser);

        Call<Void> call = RetrofitClient.getInstance().getRetrofitInterface().executeLogout(mCurrentUser.getToken());

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    mCurrentUser = new LoginResult();
                    System.out.println("USER AFTER LOGOUT ++++++++++++++++++++++++++ " + mCurrentUser);
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
}
