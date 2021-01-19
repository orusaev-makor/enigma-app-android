package com.example.enigmaapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.ListView;


import com.example.enigmaapp.activity.fragment.AccountsFragment;
import com.example.enigmaapp.activity.fragment.BalanceFragment;
import com.example.enigmaapp.activity.fragment.LoginFragment;
import com.example.enigmaapp.activity.fragment.MarketFragment;
import com.example.enigmaapp.activity.fragment.NewsFragment;
import com.example.enigmaapp.R;
import com.example.enigmaapp.model.UserViewModel;

import com.example.enigmaapp.activity.fragment.SettingsFragment;
import com.example.enigmaapp.activity.fragment.SettlementFragment;
import com.example.enigmaapp.activity.fragment.StatisticsFragment;
import com.example.enigmaapp.activity.fragment.TradeFragment;
import com.example.enigmaapp.User;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

import static com.example.enigmaapp.activity.fragment.BatchFilterFragment.resetBatchLastPos;
import static com.example.enigmaapp.activity.fragment.TradeFilterFragment.getTodayDate;
import static com.example.enigmaapp.activity.fragment.TradeFilterFragment.resetTradeLastPos;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;
    UserViewModel userViewModel;
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

        resetAllPrefs();

        userViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(UserViewModel.class);

        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        actionBar = MainActivity.this.getSupportActionBar();

        mDrawerLayout = findViewById(R.id.drawerLayout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        MenuItem menuItem = navigationView.getMenu().findItem(R.id.dark_mode_switch);
        View actionMenuView = menuItem.getActionView();
        SwitchCompat switchCompat = actionMenuView.findViewById(R.id.switchForActionBar);
        switchCompat.setChecked(loadState());

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    saveState(true);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    saveState(false);
                }
                recreate();
            }
        });

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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        switch (item.getItemId()) {
            case R.id.app_bar_balance:
                BalanceFragment balanceFragment = new BalanceFragment();
                fragmentTransaction.replace(R.id.frame_layout, balanceFragment, "Balance");
                break;
            case R.id.app_bar_trade:
                TradeFragment tradeFragment = new TradeFragment();
                fragmentTransaction.replace(R.id.frame_layout, tradeFragment, "Trade");
                break;
            case R.id.app_bar_settlement:
                SettlementFragment settlementFragment = new SettlementFragment(true);
                fragmentTransaction.replace(R.id.frame_layout, settlementFragment, "Settlement");
                break;
            case R.id.app_bar_market:
                MarketFragment marketFragment = new MarketFragment();
                fragmentTransaction.replace(R.id.frame_layout, marketFragment, "Market");
                break;
            case R.id.app_bar_news:
                NewsFragment newsFragment = new NewsFragment();
                fragmentTransaction.replace(R.id.frame_layout, newsFragment, "News");
                break;
            case R.id.app_bar_statistics:
                StatisticsFragment statisticsFragment = new StatisticsFragment();
                fragmentTransaction.replace(R.id.frame_layout, statisticsFragment, "Statistics");
                break;
            case R.id.app_bar_accounts:
                AccountsFragment accountsFragment = new AccountsFragment();
                fragmentTransaction.replace(R.id.frame_layout, accountsFragment, "Accounts");
                break;
            case R.id.app_bar_settings:
                SettingsFragment settingsFragment = new SettingsFragment();
                fragmentTransaction.replace(R.id.frame_layout, settingsFragment, "Settings");
                break;
            case R.id.app_bar_logout:
                // TODO: add logout process
                LoginFragment loginFragment = new LoginFragment();
                fragmentTransaction.replace(R.id.frame_layout, loginFragment, "Login");

            default:
                break;
        }

        fragmentTransaction.commit();
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return false;
}

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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

    private void saveState(Boolean state) {
        SharedPreferences sharedPreferences = getSharedPreferences("ABHPositive", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("NightMode", state);
        editor.apply();
    }

    private Boolean loadState() {
        SharedPreferences sharedPreferences = getSharedPreferences("ABHPositive", MODE_PRIVATE);
        Boolean state = sharedPreferences.getBoolean("NightMode", false);
        return state;
    }

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

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // reset all filters:
        resetTradeLastPos();
        resetBatchLastPos();
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
}