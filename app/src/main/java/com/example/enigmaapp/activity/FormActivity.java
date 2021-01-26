package com.example.enigmaapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.enigmaapp.R;
import com.example.enigmaapp.activity.fragment.TradeFilterFragment;

public class FormActivity extends AppCompatActivity {
    public static final String TAG = "FormActivity";
    private String formType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            formType = intent.getStringExtra("formTypeExtra");
            Log.d(TAG, "onCreate: formType - " + formType);
        }

        switch (formType) {
            case "filterTrade":
                TradeFilterFragment fragment = new TradeFilterFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, fragment, "Filter");
                fragmentTransaction.commit();
                break;
            // TODO: add cases - batch & unitary
            default:
                break;
        }

//        TradeFilterFragment fragment = new TradeFilterFragment();
//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.frame_layout, fragment, "Filter");
//        fragmentTransaction.commit();
    }
}