package com.example.enigmaapp.activity.fragment;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.enigmaapp.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TradeFilterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TradeFilterFragment extends Fragment {
    // TODO: dismiss drop downs on touch event:
//    private View mTouchOutsideView;
//    private OnTouchOutsideViewListener mOnTouchOutsideViewListener;

    private Button closeBtn;
    private Button submitBtn;
    private MaterialButton resetBtn;
    private TextView dateText;

    private TextView productText;
    private TextView executionText;
    private TextView batchedText;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TradeFilterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TradeFilterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TradeFilterFragment newInstance(String param1, String param2) {
        TradeFilterFragment fragment = new TradeFilterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //        Hide navbar on "Trade filter" view:
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
        View v = inflater.inflate(R.layout.fragment_trade_filter, container, false);

        buildCalender(v);

        productText = v.findViewById(R.id.filter_trade_product_edit);
        productText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProductList();
            }
        });

        executionText = v.findViewById(R.id.filter_trade_execution_edit);
        executionText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openExecutionTypeList();
            }
        });
        batchedText = v.findViewById(R.id.filter_trade_batched_edit);
        batchedText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBatchedList();
            }
        });

        // Submit "Filter" and go back to "Trade" screen
        submitBtn = v.findViewById(R.id.filter_trade_submit_btn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: add filter process
                openTradeScreen();
            }
        });

        // Reset "Filter Trade" screen
        resetBtn = v.findViewById(R.id.filter_trade_reset_btn);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: add proper reset process
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                TradeFilterFragment fragment = new TradeFilterFragment();
                transaction.replace(R.id.frame_layout, fragment, "Trade Filter");
                transaction.commit();
            }
        });

        // Close "Filter Trade" screen and go back to "Trade Fragment":
        closeBtn = v.findViewById(R.id.close_btn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTradeScreen();
            }
        });

        return v;
    }

    private void openBatchedList() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        FilterBatchedFragment fragment = new FilterBatchedFragment();
        transaction.replace(R.id.frame_layout, fragment, "Filter List");
        transaction.commit();
    }

    private void openExecutionTypeList() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        FilterExecutionTypeFragment fragment = new FilterExecutionTypeFragment();
        transaction.replace(R.id.frame_layout, fragment, "Filter List");
        transaction.commit();
    }

    private void openProductList() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        FilterListFragment fragment = new FilterListFragment();
        transaction.replace(R.id.frame_layout, fragment, "Filter List");
        transaction.commit();
    }

    private void buildCalender(View v) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.clear();

        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("Select data range");
        //To apply a dialog
        builder.setTheme(R.style.ThemeOverlay_MaterialComponents_MaterialCalendar);

        final MaterialDatePicker materialDatePicker = builder.build();

        dateText = v.findViewById(R.id.filter_trade_date_edit);
        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDatePicker.show(getFragmentManager(), "Data Picker");
            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                dateText.setText(materialDatePicker.getHeaderText());
            }
        });
    }

    private void openTradeScreen() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        TradeFragment fragment = new TradeFragment();
        transaction.replace(R.id.frame_layout, fragment, "Trade");
        transaction.commit();
    }
}