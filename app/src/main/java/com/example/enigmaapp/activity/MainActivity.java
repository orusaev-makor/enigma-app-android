package com.example.enigmaapp.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;

import com.example.enigmaapp.activity.fragment.LoginFragment;
import com.example.enigmaapp.R;

import static com.example.enigmaapp.activity.fragment.BatchFilterFragment.resetBatchLastPos;
import static com.example.enigmaapp.activity.fragment.TradeFilterFragment.getTodayDate;
import static com.example.enigmaapp.activity.fragment.TradeFilterFragment.resetTradeLastPos;


public class MainActivity extends AppCompatActivity {
    public static SharedPreferences.Editor prefEditor;
    public static SharedPreferences prefs;
    public static ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // MUST do this before super call or setContentView(...)
        // pick which theme DAY or NIGHT from settings

        AppCompatDelegate.setDefaultNightMode((loadState()) ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = androidx.preference.PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        prefEditor = androidx.preference.PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();

        // reset all filters:
        resetTradeLastPos();
        resetBatchLastPos();
        resetAllPrefs();

//        loginViewModel = new ViewModelProvider(this,
//                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
//                .get(LoginViewModel.class);

        // Initial Fragment:
        LoginFragment fragment = new LoginFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment, "Login");
        fragmentTransaction.commit();
    }

    // hide soft keyboard on touch outside EditText
    @Override
    public void onUserInteraction() {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    private Boolean loadState() {
        SharedPreferences sharedPreferences = getSharedPreferences("ABHPositive", MODE_PRIVATE);
        Boolean state = sharedPreferences.getBoolean("NightMode", false);
        return state;
    }

    private void resetAllPrefs() {
        prefEditor.putString("tradeIdTradeFilter", "");
        prefEditor.putString("productTradeFilter", "");
        prefEditor.putString("executionTradeFilter", "");
        prefEditor.putString("batchedTradeFilter", "");
        prefEditor.putString("startDateTradeFilter", "-");
        prefEditor.putString("endDateTradeFilter", getTodayDate());
        prefEditor.putBoolean("isRejectTradeFilter", false);
        prefEditor.putBoolean("isBookedTradeFilter", false);
        prefEditor.putBoolean("isValidatedTradeFilter", false);
        prefEditor.putBoolean("isCanceledTradeFilter", false);
        prefEditor.putBoolean("isOpenTradeFilter", false);
        prefEditor.putString("productBatchFilter", "");
        prefEditor.putString("counterpartyBatchFilter", "");
        prefEditor.putBoolean("isRejectBatchFilter", false);
        prefEditor.putBoolean("isBookedBatchFilter", false);
        prefEditor.putBoolean("isValidatedBatchFilter", false);
        prefEditor.putBoolean("isCanceledBatchFilter", false);
        prefEditor.putBoolean("isOpenBatchFilter", false);
        prefEditor.putString("startDateUnitaryFilter", "-");
        prefEditor.putString("endDateUnitaryFilter", getTodayDate());
        prefEditor.putBoolean("isRejectUnitaryFilter", false);
        prefEditor.putBoolean("isPendingUnitaryFilter", false);
        prefEditor.putBoolean("isValidatedUnitaryFilter", false);
        prefEditor.putBoolean("isSettledUnitaryFilter", false);
        prefEditor.putBoolean("isInSettUnitaryFilter", false);
        prefEditor.apply();
    }

//    private void handleLogout() {
//        Call<Void> call = RetrofitClient.getInstance().getRetrofitInterface().executeLogout(currentUser.getToken());
//
//        call.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                if (response.code() == 200) {
//                    currentUser = new LoginResult();
//                } else if (response.code() == 400) {
//                    Toast.makeText(MainActivity.this, "Please try again", Toast.LENGTH_LONG).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                Toast.makeText(MainActivity.this, "ERROR: " + t.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        });
//    }


//    private void handleLogout(String token) {
//        String bearerAuth = "bearerAuth: " + token;
//        Call<Void> call = retrofitInterface.executeLogout(bearerAuth);
//        call.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                if(!response.isSuccessful()) {
//                    Toast.makeText(MainActivity.this, "Code: " + response.code(), Toast.LENGTH_LONG).show();
//                    return;
//                }
//
//                // Moving to previous sing in layout after successful logout:
//                setSigninScreen();
//                System.out.println("LOGGED OUT SUCCESSFULLY (:");
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        });
//    }

//    private void handleForgotPassword() {
//        setContentView(R.layout.forgot_password);
//
//        findViewById(R.id.forgot_password_send_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                handleSendForgotPasswordEmail();
//            }
//        });
//
//        findViewById(R.id.forgot_password_back_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setSigninScreen();
//            }
//        });
//    }

//    private void handleSendForgotPasswordEmail() {
//        EditText username = findViewById(R.id.login_username_edit);
//        String user = username.getText().toString();
//        TextView resetErrorMsg = findViewById(R.id.forgot_password_error_message);
//        Button sendBtn = findViewById(R.id.forgot_password_send_button);
//
//        resetErrorMsg.setText("");
//
//        HashMap<String, String> map = new HashMap<>();
//        map.put("username", user);
//
//        Call<Void> call = retrofitInterface.executeForgotPassword(map);
//        call.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                if(!response.isSuccessful()) {
//                    resetErrorMsg.setText("Wrong Username");
//                    return;
//                }
//                sendBtn.setText("Sent!");
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                Toast.makeText(MainActivity.this, "ERROR: " + t.getMessage(),
//                        Toast.LENGTH_LONG).show();
//            }
//        });
//    }
}