package com.example.enigmaapp.activity.fragment;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.enigmaapp.R;
import com.example.enigmaapp.model.LoginViewModel;

public class LoginFragment extends Fragment {
    private static FragmentActivity myContext;

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        EditText userNameEdit = v.findViewById(R.id.login_username_edit);
        EditText passwordEdit = v.findViewById(R.id.login_password_edit);
        TextView loginErrorMsg = v.findViewById(R.id.login_error_message);

        LoginViewModel loginViewModel = new ViewModelProvider(requireActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(LoginViewModel.class);

        Button loginBtn = v.findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(v1 -> {
            String username = userNameEdit.getText().toString();
            String password = passwordEdit.getText().toString();
            loginViewModel.fetchUser(username, password, loginErrorMsg);
        });

        TextView forgotPassword = v.findViewById(R.id.login_forgot_password);
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

    public static void setUserBalanceView() {
        BalanceFragment fragment = new BalanceFragment();
        FragmentManager manager = myContext.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment, "Balance");
        fragmentTransaction.commit();
    }
}