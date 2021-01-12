package com.example.enigmaapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
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

import static com.example.enigmaapp.activity.fragment.SettBatchFilterFragment.resetBatchLastPos;
import static com.example.enigmaapp.activity.fragment.TradeFilterFragment.getTodayDate;
import static com.example.enigmaapp.activity.fragment.TradeFilterFragment.resetTradeLastPos;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;
    UserViewModel userViewModel;
    SharedPreferences.Editor prefEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefEditor = androidx.preference.PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();

        userViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(UserViewModel.class);

        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        mDrawerLayout = findViewById(R.id.drawerLayout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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
        int id = item.getItemId();

        if (id == R.id.app_bar_balance) {
                BalanceFragment fragment = new BalanceFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, fragment, "Balance");
                fragmentTransaction.commit();
        }
        else if (id == R.id.app_bar_trade) {
            TradeFragment fragment = new TradeFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, fragment, "Trade");
            fragmentTransaction.commit();
        }
        else if (id == R.id.app_bar_settlement) {
            SettlementFragment fragment = new SettlementFragment(true);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, fragment, "Settlement");
            fragmentTransaction.commit();
        }
        else if (id == R.id.app_bar_market) {
            MarketFragment fragment = new MarketFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, fragment, "Market");
            fragmentTransaction.commit();
        }
        else if (id == R.id.app_bar_news) {
            NewsFragment fragment = new NewsFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, fragment, "News");
            fragmentTransaction.commit();
        }
        else if (id == R.id.app_bar_statistics) {
            StatisticsFragment fragment = new StatisticsFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, fragment, "Statistics");
            fragmentTransaction.commit();
        }
        else if (id == R.id.app_bar_accounts) {
            AccountsFragment fragment = new AccountsFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, fragment, "Accounts");
            fragmentTransaction.commit();
        }
        else if (id == R.id.app_bar_settings) {
            SettingsFragment fragment = new SettingsFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, fragment, "Settings");
            fragmentTransaction.commit();
        }
        else if (id == R.id.app_bar_logout) {
            userViewModel.logoutCurrentUser();
            LoginFragment fragment = new LoginFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, fragment, "Login");
            fragmentTransaction.commit();
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
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

//    class MyAdapter extends ArrayAdapter<String> {
//        Context context;
//        String rDate[];
//        float rAmounts[];
//
//        MyAdapter (Context c, String date[], float amount[]) {
//            super(c, R.layout.pnl_list_row, date);
//            this.context = c;
//            this.rDate = date;
//            this.rAmounts = amount;
//        }
//
//        @NonNull
//        @Override
//        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View row = layoutInflater.inflate(R.layout.pnl_list_row, parent, false);
//            TextView myDate = row.findViewById(R.id.pnl_date_text_view);
//            TextView myAmount = row.findViewById(R.id.pnl_amount_text_view);
//
//            myDate.setText(rDate[position]);
//            myAmount.setText(String.valueOf(rAmounts[position]));
//            return row;
//        }
//    }

    private void setNextView(List<User> users, String token) {

        int size = users.size();
        ListView listView;
        String[] Usernames = new String[size];
        int[] Ids = new int[size];

        setContentView(R.layout.activity_next);
//        listView = findViewById(R.id.list_view);
//
//        // set arrays for adapter
//        for (int i = 0; i < size; i++) {
//            int id = users.get(i).getId();
//            String username = users.get(i).getUsername();
//            Usernames[i] = username;
//            Ids[i] = id;
//        }
//
//        UsersListAdapter adapter = new UsersListAdapter(this, Usernames, Ids);
//        listView.setAdapter(adapter);
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                TextView textView = (TextView) view.findViewById(R.id.user_id_text_view);
//                String userId = textView.getText().toString();
//                handleGetHistoricalPnl(token, Integer.valueOf(userId));
//            }
//        });
//
//        findViewById(R.id.logout_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                handleLogout(token);
//            }
//        });
    }

//    private void handleGetHistoricalPnl(String token, int id) {
//        String bearerAuth = "bearerAuth: " + token;
//
//        ListView listView;
//        listView = findViewById(R.id.list_view);
//
//        Call<ArrayList<HistoricalPnl>> call = retrofitInterface.getHistoricalPnl(bearerAuth, id);
//
//        call.enqueue(new Callback<ArrayList<HistoricalPnl>>() {
//            @Override
//            public void onResponse(Call<ArrayList<HistoricalPnl>> call, Response<ArrayList<HistoricalPnl>> response) {
//                if (!response.isSuccessful()) {
//                    System.out.println( "Code: " + response.code() + "Error: " + response.message());
//                    return;
//                }
//
//                ArrayList<HistoricalPnl> pnl = response.body();
//
//                int size = pnl.size();
//                String [] Date = new String[size];
//                float[] Amounts = new float[size];
//
//                // set arrays for adapter
//                for (int i = 0; i < size; i++) {
//                    int month = pnl.get(i).getMonth();
//                    int year = pnl.get(i).getYear();
//                    String date = String.valueOf(month) + "/" + String.valueOf(year);
//                    float amount = pnl.get(i).getPnl();
//
//                    Date[i] = date;
//                    Amounts[i] = amount;
//                }
//
////                PnlListAdapter adapter = new PnlListAdapter(this, Date, Amounts);
////                listView.setAdapter(adapter);
//
//                // TODO: Send the arrays to pnl adpter and remove the code below once its working....
//
//                for (HistoricalPnl historicalPnl : pnl) {
//                    int month = historicalPnl.getMonth();
//                    int year = historicalPnl.getYear();
//                    float salesPnl = historicalPnl.getPnl();
//
//                    // TODO: present result after successful request
//                    System.out.println(month + "/" + year + " // sales pnl - " + salesPnl);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ArrayList<HistoricalPnl>> call, Throwable t) {
//                System.out.println("t.getMessage():  "  + t.getMessage());
//                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
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

//    private void setSigninScreen() {
//        setContentView(R.layout.activity_main);
//        findViewById(R.id.login_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                handleLogin();
//            }
//        });
//
//        findViewById(R.id.login_forgot_password).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                handleForgotPassword();
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
        resetAllPrefs();
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
        prefEditor.putBoolean("isRejectTradeFilter", false);
        prefEditor.putBoolean("isBookedBatchFilter", false);
        prefEditor.putBoolean("isValidatedBatchFilter", false);
        prefEditor.putBoolean("isCanceledBatchFilter", false);
        prefEditor.putBoolean("isOpenBatchFilter", false);
        prefEditor.putString("currencyUnitaryFilter", "");
        prefEditor.putString("counterpartyUnitaryFilter", "");
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