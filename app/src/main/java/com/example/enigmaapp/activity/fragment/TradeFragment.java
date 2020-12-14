package com.example.enigmaapp.activity.fragment;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.enigmaapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TradeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TradeFragment extends Fragment {
    private FloatingActionButton createTradeBtn;
    private ImageView filterBtn;
    private ImageView uploadBtn;
    private ImageView refreshBtn;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TradeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TradeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TradeFragment newInstance(String param1, String param2) {
        TradeFragment fragment = new TradeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //        Show navbar on "Trade" view:
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_trade, container, false);

        // Move fo "New Trade" screen:
        createTradeBtn = v.findViewById(R.id.trade_create_btn);
        createTradeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                NewTradeCreationFragment fragment = new NewTradeCreationFragment();
                transaction.replace(R.id.frame_layout, fragment, "New Trade");
                transaction.commit();
            }
        });

        // Move fo "Filter Trade" screen:
        filterBtn = v.findViewById(R.id.ic_action_filter);
        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                TradeFilterFragment fragment = new TradeFilterFragment();
                transaction.replace(R.id.frame_layout, fragment, "Filter Trade");
                transaction.commit();
            }
        });

        // Refresh "Trade" screen:
        refreshBtn = v.findViewById(R.id.ic_action_refresh);
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                TradeFragment fragment = new TradeFragment();
                transaction.replace(R.id.frame_layout, fragment, "Trade");
                transaction.commit();
            }
        });

        // Upload "Trade" screen:
        uploadBtn = v.findViewById(R.id.ic_action_upload);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: add upload process
            }
        });
        return v;
    }
}