package com.example.enigmaapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettlementFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettlementFragment extends Fragment {
    private FloatingActionButton createUnitaryBtn;
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

    public SettlementFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettlementFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettlementFragment newInstance(String param1, String param2) {
        SettlementFragment fragment = new SettlementFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //        Show navbar on "Settlement" view:
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

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
        View v = inflater.inflate(R.layout.fragment_settlement, container, false);

        // Unitary:
//        createUnitaryBtn = v.findViewById(R.id.settlement_create_btn);
//        createUnitaryBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                NewUnitarySettFragment fragment = new NewUnitarySettFragment();
//                transaction.replace(R.id.frame_layout, fragment, "New Unitary Settlement");
//                transaction.commit();
//            }
//        });

        // Batch:
        createUnitaryBtn = v.findViewById(R.id.settlement_create_btn);
        createUnitaryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                NewBatchSettFragment fragment = new NewBatchSettFragment();
                transaction.replace(R.id.frame_layout, fragment, "New Batch Settlement");
                transaction.commit();
            }
        });

        // Move fo "Filter Settlement" screen:
        filterBtn = v.findViewById(R.id.ic_action_filter);
        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                SettBatchFilterFragment fragment = new SettBatchFilterFragment();
                transaction.replace(R.id.frame_layout, fragment, "Filter Settlement");
                transaction.commit();
            }
        });

        // Refresh "Settlement" screen:
        refreshBtn = v.findViewById(R.id.ic_action_refresh);
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                SettlementFragment fragment = new SettlementFragment();
                transaction.replace(R.id.frame_layout, fragment, "Settlement");
                transaction.commit();
            }
        });

        // Upload "Settlement" screen:
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