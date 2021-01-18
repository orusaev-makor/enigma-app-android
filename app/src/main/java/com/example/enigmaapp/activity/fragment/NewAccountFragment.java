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

import com.example.enigmaapp.R;

import static com.example.enigmaapp.activity.MainActivity.actionBar;

public class NewAccountFragment extends Fragment {

    Button submitBtn;
    TextView currencyEdit;
    TextView accountTypeEdit;
    EditText detailsEdit;
    EditText nameEdit;

    public NewAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (actionBar != null) {
            actionBar.show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_new_account, container, false);

        // Creating account and go back to "Accounts" screen
        submitBtn = v.findViewById(R.id.new_account_submit_btn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: add the creation process
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                AccountsFragment fragment = new AccountsFragment();
                transaction.replace(R.id.frame_layout, fragment, "Accounts");
                transaction.commit();
            }
        });

        return v;
    }
}