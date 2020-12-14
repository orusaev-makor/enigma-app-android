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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewTradeReviewAndSubmitFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewTradeReviewAndSubmitFragment extends Fragment {
    private Button closeBtn;
    private MaterialButton backBtn;
    private Button createBtn;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NewTradeReviewAndSubmitFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewTradeReviewAndSubmitFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewTradeReviewAndSubmitFragment newInstance(String param1, String param2) {
        NewTradeReviewAndSubmitFragment fragment = new NewTradeReviewAndSubmitFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //        Hides navbar on "create trade" view:
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                NewTradeCreationFragment fragment = new NewTradeCreationFragment();
                transaction.replace(R.id.frame_layout, fragment, "New Trade");
                transaction.commit();
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
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        TradeFragment fragment = new TradeFragment();
        transaction.replace(R.id.frame_layout, fragment, "Trade");
        transaction.commit();
    }
}