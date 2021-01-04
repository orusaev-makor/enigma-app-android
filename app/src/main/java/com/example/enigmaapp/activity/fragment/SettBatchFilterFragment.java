package com.example.enigmaapp.activity.fragment;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.enigmaapp.R;
import com.google.android.material.button.MaterialButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettBatchFilterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettBatchFilterFragment extends Fragment {
    // TODO: dismiss drop downs on touch event:
//    private View mTouchOutsideView;
//    private OnTouchOutsideViewListener mOnTouchOutsideViewListener;

    private Button closeBtn;
    private Button submitBtn;
    private MaterialButton resetBtn;

    private TextView productText;
    private TextView counterpartyText;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettBatchFilterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettBatchFilterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettBatchFilterFragment newInstance(String param1, String param2) {
        SettBatchFilterFragment fragment = new SettBatchFilterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //        Hide navbar on "Settlement filter" view:
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_sett_batch_filter, container, false);

        productText = v.findViewById(R.id.filter_settlement_product_edit);
        productText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProductList();
            }
        });

        counterpartyText = v.findViewById(R.id.filter_settlement_counterparty_edit);
        counterpartyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCounterpartyList();
            }
        });

        // Submit "Filter" and go back to "Settlement" screen
        submitBtn = v.findViewById(R.id.filter_settlement_submit_btn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: add filter process
                openSettlementScreen();
            }
        });

        // Reset "Filter Settlement" screen
        resetBtn = v.findViewById(R.id.filter_settlement_reset_btn);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: add proper reset process
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                SettBatchFilterFragment fragment = new SettBatchFilterFragment();
                transaction.replace(R.id.frame_layout, fragment, "Settlement Filter");
                transaction.commit();
            }
        });

        // Close "Filter Settlement" screen and go back to "Settlement Fragment":
        closeBtn = v.findViewById(R.id.close_btn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettlementScreen();
            }
        });

        return v;
    }

    private void openCounterpartyList() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        FilterCounterpartyFragment fragment = new FilterCounterpartyFragment();
        transaction.replace(R.id.frame_layout, fragment, "Filter Counterparty");
        transaction.commit();
    }

    private void openProductList() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        FilterProductSettFragment fragment = new FilterProductSettFragment();
        transaction.replace(R.id.frame_layout, fragment, "Filter Product");
        transaction.commit();
    }

    private void openSettlementScreen() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        SettlementFragment fragment = new SettlementFragment();
        transaction.replace(R.id.frame_layout, fragment, "Settlement");
        transaction.commit();
    }
}