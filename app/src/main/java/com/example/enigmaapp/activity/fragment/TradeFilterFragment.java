package com.example.enigmaapp.activity.fragment;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.enigmaapp.R;
import com.example.enigmaapp.model.TradeViewModel;
import com.example.enigmaapp.model.UserViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TimeZone;

public class TradeFilterFragment extends Fragment {

    private Button closeBtn;
    private Button submitBtn;
    private MaterialButton resetBtn;
    private TextView dateText;

    private TextView productText;
    private TextView executionText;
    private TextView batchedText;

    public static HashMap<String, String> paramsToSend = new HashMap<>();

    public TradeFilterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide navbar on "Trade filter" view:
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_trade_filter, container, false);

        buildCalender(v);

        TradeViewModel viewModel = new ViewModelProvider(requireActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(TradeViewModel.class);

        UserViewModel userViewModel = new ViewModelProvider(requireActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(UserViewModel.class);
        String token = userViewModel.getCurrentUser().getToken();

        viewModel.fetchTradeDataset(token);

        productText = v.findViewById(R.id.filter_trade_product_edit);
        productText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMultiSelectFilter("product");
//                openProductList();
            }
        });

        executionText = v.findViewById(R.id.filter_trade_execution_edit);
        executionText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMultiSelectFilter("execution type");
//                openExecutionTypeList();
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
                viewModel.setParams(paramsToSend);
                openTradeScreen();
            }
        });

        // Reset "Filter Trade" screen
        resetBtn = v.findViewById(R.id.filter_trade_reset_btn);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: add proper reset process
                paramsToSend.clear();
                viewModel.resetParams();
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

    private void openMultiSelectFilter(String type) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        MultiSelectFilterFragment fragment = new MultiSelectFilterFragment(type);
        transaction.replace(R.id.frame_layout, fragment, "Multi Select Filter List");
        transaction.commit();
    }

    private void openBatchedList() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        FilterBatchedFragment fragment = new FilterBatchedFragment();
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

    public static void setParams(HashMap<String, String> params) {
        Iterator it = params.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry) it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            paramsToSend.put(pair.getKey().toString(), pair.getValue().toString());
            it.remove(); // avoids a ConcurrentModificationException
        }
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
}