package com.example.enigmaapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.enigmaapp.R;
import com.example.enigmaapp.activity.fragment.BatchFilterFragment;
import com.example.enigmaapp.activity.fragment.TradeFilterFragment;
import com.example.enigmaapp.activity.fragment.UnitaryFilterFragment;

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

        setCurrentForm(formType);
    }

    private void setCurrentForm(String formType) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (formType.equals("filterTrade")) {
            Log.d(TAG, "onCreate: trade case");
            TradeFilterFragment fragment = new TradeFilterFragment();
            transaction.replace(R.id.frame_layout, fragment, "Filter Trade");

        }
        else if (formType.equals("filterBatch")) {
            Log.d(TAG, "onCreate: batch case");
            BatchFilterFragment fragment = new BatchFilterFragment();
            transaction.replace(R.id.frame_layout, fragment, "Filter Batch");
        }

        else if (formType.equals("filterUnitary")) {
            Log.d(TAG, "onCreate: unitary case");
            UnitaryFilterFragment fragment = new UnitaryFilterFragment();
            transaction.replace(R.id.frame_layout, fragment, "Filter Unitary");
        }

        transaction.commit();

    }
}