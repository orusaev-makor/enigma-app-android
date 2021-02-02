package com.example.enigmaapp.activity.fragment;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.enigmaapp.R;
import com.example.enigmaapp.ui.SettingsConfirmationDialog;

import static com.example.enigmaapp.activity.UserActivity.actionBar;


public class SettingsFragment extends Fragment {

    public SettingsFragment() {
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
        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        LinearLayout increaseExposureLimit = (LinearLayout) v.findViewById(R.id.request_increase_exposure_limit);
        increaseExposureLimit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: set "request increase exposure limit" process
                openDialog();
            }
        });

        LinearLayout increaseQuantityLimit = (LinearLayout) v.findViewById(R.id.request_increase_quantity_limit);
        increaseQuantityLimit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: set "request increase quantity limit" process
                openDialog();
            }
        });

        return v;
    }

    private void openDialog() {
        SettingsConfirmationDialog settingsDialog = new SettingsConfirmationDialog();
        settingsDialog.show(getFragmentManager(), "Settings Dialog");
    }
}