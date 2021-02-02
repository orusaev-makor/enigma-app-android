package com.example.enigmaapp.activity.fragment;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.enigmaapp.R;
import com.google.android.material.button.MaterialButton;


public class NewTradeReviewAndSubmitFragment extends Fragment {
    private Button closeBtn;
    private MaterialButton backBtn;
    private Button createBtn;

    public NewTradeReviewAndSubmitFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        actionBar.hide();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_new_trade_review_and_submit, container, false);

//        Creating trade and return to "Trade Fragment"screen
        createBtn = v.findViewById(R.id.new_trade_create_btn);
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: add the creation process
                openTradeScreen();
            }
        });

        // Back to first "Trade creation" screen:
        backBtn = v.findViewById(R.id.new_trade_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                NewTradeCreationFragment fragment = new NewTradeCreationFragment();
                ft.replace(R.id.frame_layout, fragment, "New Trade");
                ft.commit();
            }
        });

        // Close "Trade creation" screen and go back to "Trade Fragment":
        closeBtn = v.findViewById(R.id.new_trade_close_btn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTradeScreen();
            }
        });

        return v;
    }

    private void openTradeScreen() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        TradeFragment fragment = new TradeFragment();
        ft.replace(R.id.frame_layout, fragment, "Trade");
        ft.commit();
    }
}