package com.example.enigmaapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewBatchSettFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewBatchSettFragment extends Fragment {
    private Button createBtn;
    private Button closeBtn;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NewBatchSettFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewBatchSettFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewBatchSettFragment newInstance(String param1, String param2) {
        NewBatchSettFragment fragment = new NewBatchSettFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //        Hides navbar on "create settlement" view:
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

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
        View v = inflater.inflate(R.layout.fragment_new_batch_sett, container, false);

        // Creating settlement and moving to "Select Trade" screen
        createBtn = v.findViewById(R.id.new_batch_create_btn);
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: add the creation process
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                SelectTradeFragment fragment = new SelectTradeFragment();
                transaction.replace(R.id.frame_layout, fragment, "Select Trade");
                transaction.commit();
            }
        });


        // Close "Settlement creation" screen and go back to "Settlement Fragment":
        closeBtn = v.findViewById(R.id.new_settlement_close_btn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                SettlementFragment fragment = new SettlementFragment();
                transaction.replace(R.id.frame_layout, fragment, "Settlement");
                transaction.commit();
            }
        });

        return v;
    }
}