package com.example.enigmaapp.activity.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.enigmaapp.R;
import com.google.android.material.button.MaterialButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SelectTradeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectTradeFragment extends Fragment {
    private Button createBtn, closeBtn;
    private MaterialButton resetBtn;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SelectTradeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SelectTradeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SelectTradeFragment newInstance(String param1, String param2) {
        SelectTradeFragment frg = new SelectTradeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        frg.setArguments(args);
        return frg;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_select_trade, container, false);

        // Creating batch settlement and return to "Settlement Fragment"screen
        createBtn = v.findViewById(R.id.select_trade_create_btn);
        createBtn.setOnClickListener(v13 -> {
            // TODO: add the creation process
            openSettlementScreen();
        });

        // Reset selected trades
        resetBtn = v.findViewById(R.id.select_trade_reset_btn);
        resetBtn.setOnClickListener(v1 -> {
            // TODO: add the reset process
            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            SelectTradeFragment fragment = new SelectTradeFragment();
            ft.replace(R.id.frame_layout, fragment, "Select Trade");
            ft.commit();
        });

        // Close "Settlement creation" screen and go back to "Settlement Fragment":
        closeBtn = v.findViewById(R.id.new_settlement_close_btn);
        closeBtn.setOnClickListener(v12 -> openSettlementScreen());

        return v;
    }

    private void openSettlementScreen() {
        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        SettlementFragment fragment = new SettlementFragment(true);
        ft.replace(R.id.frame_layout, fragment, "Settlement");
        ft.commit();
    }
}