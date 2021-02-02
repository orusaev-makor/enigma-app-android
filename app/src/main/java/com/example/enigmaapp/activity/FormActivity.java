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
        }

        setCurrentForm(formType);
    }

    private void setCurrentForm(String formType) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        switch (formType) {

            case "filterTrade":
                Log.d(TAG, "onCreate: trade case");
                TradeFilterFragment tradeFilterFragment = new TradeFilterFragment();
                ft.replace(R.id.frame_layout, tradeFilterFragment, "Filter Trade");
                break;

            case "filterBatch":
                Log.d(TAG, "onCreate: batch case");
                BatchFilterFragment batchFilterFragment = new BatchFilterFragment();
                ft.replace(R.id.frame_layout, batchFilterFragment, "Filter Batch");
                break;

            case "filterUnitary":
                Log.d(TAG, "onCreate: unitary case");
                UnitaryFilterFragment unitaryFilterFragment = new UnitaryFilterFragment();
                ft.replace(R.id.frame_layout, unitaryFilterFragment, "Filter Unitary");
                break;

            default:
                break;
        }
//        if (formType.equals("filterTrade")) {
//            Log.d(TAG, "onCreate: trade case");
//            TradeFilterFragment frg = new TradeFilterFragment();
//            ft.replace(R.id.frame_layout, frg, "Filter Trade");
//
//        }
//        else if (formType.equals("filterBatch")) {
//            Log.d(TAG, "onCreate: batch case");
//            BatchFilterFragment frg = new BatchFilterFragment();
//            ft.replace(R.id.frame_layout, frg, "Filter Batch");
//        }
//        else if (formType.equals("filterUnitary")) {
//            Log.d(TAG, "onCreate: unitary case");
//            UnitaryFilterFragment frg = new UnitaryFilterFragment();
//            ft.replace(R.id.frame_layout, frg, "Filter Unitary");
//        }
        ft.commit();
    }
}