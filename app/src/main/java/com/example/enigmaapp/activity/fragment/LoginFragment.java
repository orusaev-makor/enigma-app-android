package com.example.enigmaapp.activity.fragment;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.enigmaapp.R;
import com.example.enigmaapp.web.login.LoginResult;
import com.example.enigmaapp.web.RetrofitClient;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {
    private EditText userNameEdit;
    private EditText passwordEdit;
    private TextView loginErrorMsg;
    private TextView forgotPassword;
    private Button loginBtn;
    public static LoginResult currentUser = new LoginResult();

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Hide navbar on "Login" view:
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        userNameEdit = v.findViewById(R.id.login_username_edit);
        passwordEdit = v.findViewById(R.id.login_password_edit);
        loginErrorMsg = v.findViewById(R.id.login_error_message);

        loginBtn = v.findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin();
            }
        });

        forgotPassword = v.findViewById(R.id.login_forgot_password);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForgotPasswordFragment fragment = new ForgotPasswordFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, fragment, "Forgot Password");
                fragmentTransaction.commit();
            }
        });
        return v;
    }

    private void handleLogin() {
        loginErrorMsg.setText("");

        String username = userNameEdit.getText().toString();
        HashMap<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", passwordEdit.getText().toString());

        Call<LoginResult> call = RetrofitClient.getInstance().getRetrofitInterface().executeLogin(map);

        call.enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Code: " + response.code() + "Error: " + response.message());
                    loginErrorMsg.setText("Wrong Credentials");
                    return;
                }

                // After successful login: moving to next layout + setting up logout listener
                String token = response.body().getToken();

                currentUser.setToken(token);
                currentUser.setUsername(username);
                setUserBalanceView(currentUser);
            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                System.out.println("t.getMessage(): " + t.getMessage());
                Toast.makeText(requireActivity(), "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                loginErrorMsg.setText(t.getMessage());
            }
        });
    }

    private void setUserBalanceView(LoginResult currentUser) {
        BalanceFragment fragment = new BalanceFragment(currentUser);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment, "Balance");
        fragmentTransaction.commit();
    }
}