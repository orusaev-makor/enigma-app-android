package com.example.enigmaapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.enigmaapp.RetrofitInterface;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://api-pnl-prev.enigma-securities.io/api-docs/";
//    private String BASE_URL = "https://api-pnl.enigma-securities.io";
    private static String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100,TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);


//        setSigninScreen();

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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.app_bar_balance) {
            LogoutFragment fragment = new LogoutFragment();
//            BalanceFragment fragment = new BalanceFragment();
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
            SettlementFragment fragment = new SettlementFragment();
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
        else if (id == R.id.app_bar_dark_mode) {
            DarkModeFragment fragment = new DarkModeFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, fragment, "Dark mode");
            fragmentTransaction.commit();
        }
        else if (id == R.id.app_bar_logout) {
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

//    private void handleLogin() {
//        TextView loginErrorMsg = findViewById(R.id.login_error_message);
//        EditText username = findViewById(R.id.login_username_edit);
//        EditText passwordEdit = findViewById(R.id.login_password_edit);
//
//        loginErrorMsg.setText("");
//
//        HashMap<String, String> map = new HashMap<>();
//        map.put("username", username.getText().toString());
//        map.put("password", passwordEdit.getText().toString());
//
//        Call<User> call = retrofitInterface.executeLogin(map);
//
//        call.enqueue(new Callback<User>() {
//            @Override
//            public void onResponse(Call<User> call, Response<User> response) {
//                if (!response.isSuccessful()) {
//                    System.out.println( "Code: " + response.code() + "Error: " + response.message());
//                    loginErrorMsg.setText("Wrong Credentials");
//                    return;
//                }
//
//                // After successful login: moving to next layout + setting up logout listener
//                token = response.body().getUserDetails().getToken();
//                List<User> users = response.body().getUsers();
//                System.out.println("LOGIN DONE _ TOKEN: " + token);
//                setNextView(users, token);
//            }
//
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//                System.out.println( "t.getMessage(): " + t.getMessage());
//
//                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
//                loginErrorMsg.setText(t.getMessage());
//            }
//        });
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
}