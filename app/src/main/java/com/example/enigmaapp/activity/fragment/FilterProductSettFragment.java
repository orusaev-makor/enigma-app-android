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
 * Use the {@link FilterProductSettFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FilterProductSettFragment extends Fragment {
    private MaterialButton resetBtn;
    private Button submitBtn;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FilterProductSettFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FilterProductSettFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FilterProductSettFragment newInstance(String param1, String param2) {
        FilterProductSettFragment fragment = new FilterProductSettFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        View v = inflater.inflate(R.layout.fragment_filter_product_sett, container, false);
        // Submit chosen filters and go back to "Filter Batch" screen
        submitBtn = v.findViewById(R.id.filter_submit_btn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: add filter process
                openSettBatchFilterScreen();
            }
        });

        // Reset "Filter List" screen
        resetBtn = v.findViewById(R.id.filter_reset_btn);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                FilterProductSettFragment fragment = new FilterProductSettFragment();
                transaction.replace(R.id.frame_layout, fragment, "Batch Filter");
                transaction.commit();
            }
        });

        return v;
    }


    private void openSettBatchFilterScreen() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        SettBatchFilterFragment fragment = new SettBatchFilterFragment();
        transaction.replace(R.id.frame_layout, fragment, "Batch Filter");
        transaction.commit();
    }
}