package com.example.enigmaapp.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
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

import java.util.HashMap;

import static com.example.enigmaapp.activity.fragment.BatchFilterFragment.resetBatchLastPos;
import static com.example.enigmaapp.activity.fragment.SettlementFragment.BATCH_FILTER_REQUEST_CODE;
import static com.example.enigmaapp.activity.fragment.SettlementFragment.UNITARY_FILTER_REQUEST_CODE;
import static com.example.enigmaapp.activity.fragment.TradeFilterFragment.getTodayDate;
import static com.example.enigmaapp.activity.fragment.TradeFilterFragment.resetTradeLastPos;
import static com.example.enigmaapp.activity.fragment.TradeFragment.TRADE_FILTER_REQUEST_CODE;

public class UserActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
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
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame_layout, fragment, "Balance");
        ft.commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == TRADE_FILTER_REQUEST_CODE) {
            Fragment frg = null;

            // Reload current fragment to refresh filtered results:
            switch (requestCode) {
                case TRADE_FILTER_REQUEST_CODE:
                    //            if (data.hasExtra("products")) {
////                Bundle wrapper = getIntent().getBundleExtra("stkList");
//                Bundle wrapper = data.getBundleExtra("products");
//                if (wrapper != null) {
//                    HashMap<String, String> myClass3 = (HashMap<String, String>) wrapper.getSerializable("productList");
//                    System.out.println("...serialized data4.." + myClass3);
//                }
//            }
                    frg = getSupportFragmentManager().findFragmentByTag("Trade");
                    break;

                case BATCH_FILTER_REQUEST_CODE:
                    frg = getSupportFragmentManager().findFragmentByTag("Batch");
                    break;

                case UNITARY_FILTER_REQUEST_CODE:
                    frg = getSupportFragmentManager().findFragmentByTag("Unitary");
                    break;
                default:
                    break;
            }

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                ft.detach(frg).commitNow();
                ft.attach(frg).commitNow();
            } else {
                ft.detach(frg).attach(frg).commit();
            }
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        switch (item.getItemId()) {
            case R.id.app_bar_balance:
                BalanceFragment balanceFragment = new BalanceFragment();
                ft.replace(R.id.frame_layout, balanceFragment, "Balance");
                break;
            case R.id.app_bar_trade:
                TradeFragment tradeFragment = new TradeFragment();
                ft.replace(R.id.frame_layout, tradeFragment, "Trade");
                break;
            case R.id.app_bar_settlement:
                SettlementFragment settlementFragment = new SettlementFragment(true);
                ft.replace(R.id.frame_layout, settlementFragment, "Batch");
                break;
            case R.id.app_bar_market:
                MarketFragment marketFragment = new MarketFragment();
                ft.replace(R.id.frame_layout, marketFragment, "Market");
                break;
            case R.id.app_bar_news:
                NewsFragment newsFragment = new NewsFragment();
                ft.replace(R.id.frame_layout, newsFragment, "News");
                break;
            case R.id.app_bar_statistics:
                StatisticsFragment statisticsFragment = new StatisticsFragment();
                ft.replace(R.id.frame_layout, statisticsFragment, "Statistics");
                break;
            case R.id.app_bar_accounts:
                AccountsFragment accountsFragment = new AccountsFragment();
                ft.replace(R.id.frame_layout, accountsFragment, "Accounts");
                break;
            case R.id.app_bar_settings:
                SettingsFragment settingsFragment = new SettingsFragment();
                ft.replace(R.id.frame_layout, settingsFragment, "Settings");
                break;
            case R.id.app_bar_logout:
                // TODO: add logout process
                LoginFragment loginFragment = new LoginFragment();
                ft.replace(R.id.frame_layout, loginFragment, "Login");

            default:
                break;
        }

        ft.commit();
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