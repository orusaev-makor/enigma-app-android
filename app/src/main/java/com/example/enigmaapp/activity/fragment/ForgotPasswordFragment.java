package com.example.enigmaapp.activity.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.enigmaapp.R;

public class ForgotPasswordFragment extends Fragment {

    public ForgotPasswordFragment() {
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
        View v = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        // Send "Forgot Password" email and show "Login" screen:
        Button sendBtn = v.findViewById(R.id.forgot_password_send_btn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: add password recovery process
//                openLoginScreen();
            }
        });

        // Back to "Login" screen
        TextView back = v.findViewById(R.id.forgot_password_back_btn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoginScreen();
            }
        });
        return v;
    }

    private void openLoginScreen() {
        LoginFragment fragment = new LoginFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment, "Login");
        fragmentTransaction.commit();
    }
}