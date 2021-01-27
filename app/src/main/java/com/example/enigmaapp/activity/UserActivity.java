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

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;

import com.example.enigmaapp.R;
import com.example.enigmaapp.activity.fragment.AccountsFragment;
import com.example.enigmaapp.activity.fragment.BalanceFragment;
import com.example.enigmaapp.activity.fragment.LoginFragment;
import com.example.enigmaapp.activity.fragment.MarketFragment;
import com.example.enigmaapp.activity.fragment.NewsFragment;
import com.example.enigmaapp.activity.fragment.SettingsFragment;
import com.example.enigmaapp.activity.fragment.SettlementFragment;
import com.example.enigmaapp.activity.fragment.StatisticsFragment;
import com.example.enigmaapp.activity.fragment.TradeFragment;
import com.google.android.material.navigation.NavigationView;

import static com.example.enigmaapp.activity.fragment.BatchFilterFragment.resetBatchLastPos;
import static com.example.enigmaapp.activity.fragment.TradeFilterFragment.getTodayDate;
import static com.example.enigmaapp.activity.fragment.TradeFilterFragment.resetTradeLastPos;

public class UserActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    public static final String TAG = "UserActivity";
    private String passedUsername;
    private String passedToken;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

//    public static SharedPreferences.Editor prefEditor;
//    public static SharedPreferences prefs;
    public static ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            passedUsername = intent.getStringExtra("usernameExtra");
            passedToken = intent.getStringExtra("tokenExtra");
        }

        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        actionBar = UserActivity.this.getSupportActionBar();

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
        BalanceFragment fragment = new BalanceFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment, "Balance");
        fragmentTransaction.commit();
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
}