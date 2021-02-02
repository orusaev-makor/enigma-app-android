package com.example.enigmaapp.activity.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.example.enigmaapp.R;
import com.example.enigmaapp.model.TradeViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static android.app.Activity.RESULT_OK;
import static com.example.enigmaapp.activity.MainActivity.prefEditor;
import static com.example.enigmaapp.activity.MainActivity.prefs;
import static com.example.enigmaapp.activity.fragment.TradeFragment.selectedBatched;
import static com.example.enigmaapp.activity.fragment.TradeFragment.selectedEndDate;
import static com.example.enigmaapp.activity.fragment.TradeFragment.selectedExecutionType;
import static com.example.enigmaapp.activity.fragment.TradeFragment.enteredTradeId;
import static com.example.enigmaapp.activity.fragment.TradeFragment.selectedProductId;
import static com.example.enigmaapp.activity.fragment.TradeFragment.selectedStartDate;
import static com.example.enigmaapp.activity.fragment.TradeFragment.selectedStatuses;
import static com.example.enigmaapp.activity.fragment.TradeFragment.setTradeParams;
import static com.example.enigmaapp.activity.fragment.TradeSelectFilterFragment.lastTradeBatchedPos;
import static com.example.enigmaapp.activity.fragment.TradeSelectFilterFragment.lastTradeExecutionPos;
import static com.example.enigmaapp.activity.fragment.TradeSelectFilterFragment.lastTradeProductPos;

public class TradeFilterFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {

    private Button closeBtn;
    private Button submitBtn;
    private MaterialButton resetBtn;
    private static TextView dateText;
    private EditText tradeIdTextEdit;

    private TextView productText;
    private TextView executionText;
    private TextView batchedText;

    private View statusSelectView;
    private TradeViewModel tradeViewModel;


    public TradeFilterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_trade_filter, container, false);

        buildCalender(v);

        tradeViewModel = new ViewModelProvider(requireActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(TradeViewModel.class);

        tradeIdTextEdit = v.findViewById(R.id.filter_trade_trade_id_edit);
        tradeIdTextEdit.setText(prefs.getString("tradeIdTradeFilter", ""));

        productText = v.findViewById(R.id.filter_trade_product_edit);
        productText.setText(prefs.getString("productTradeFilter", ""));
        productText.setOnClickListener(v1 -> openMultiSelectFilter("product"));

        executionText = v.findViewById(R.id.filter_trade_execution_edit);
        executionText.setText(prefs.getString("executionTradeFilter", ""));
        executionText.setOnClickListener(v12 -> openMultiSelectFilter("execution type"));

        batchedText = v.findViewById(R.id.filter_trade_batched_edit);
        batchedText.setText(prefs.getString("batchedTradeFilter", ""));
        batchedText.setOnClickListener(v110 -> openMultiSelectFilter("batched"));

        // Submit "Filter" and go back to "Trade" screen
        submitBtn = v.findViewById(R.id.filter_trade_submit_btn);
        submitBtn.setOnClickListener(v19 -> {
            setTradeIdParam();
            sendDataToPrevPg();
            setTradeParams();
        });

        // Reset "Filter Trade" screen
        resetBtn = v.findViewById(R.id.filter_trade_reset_btn);
        resetBtn.setOnClickListener(v18 -> {
            resetTradeParams();
            resetPrefs();
            resetTradeLastPos();
            openFilterTradeScreen();
        });

        // Close "Filter Trade" screen and go back to "Trade Fragment":
        closeBtn = v.findViewById(R.id.close_btn);
        closeBtn.setOnClickListener(v17 -> {
            sendDataToPrevPg();
        });

        statusSelectView = v.findViewById(R.id.layout_trade_status_select);
        CheckBox reject = statusSelectView.findViewById(R.id.checkBoxRejectedTrade);
        reject.setChecked(prefs.getBoolean("isRejectTradeFilter", false));
        reject.setOnCheckedChangeListener(this);

        CheckBox book = statusSelectView.findViewById(R.id.checkBoxBookedTrade);
        book.setChecked(prefs.getBoolean("isBookedTradeFilter", false));
        book.setOnCheckedChangeListener(this);

        CheckBox validate = statusSelectView.findViewById(R.id.checkBoxValidatedTrade);
        validate.setChecked(prefs.getBoolean("isValidatedTradeFilter", false));
        validate.setOnCheckedChangeListener(this);

        CheckBox cancel = statusSelectView.findViewById(R.id.checkBoxCanceledTrade);
        cancel.setChecked(prefs.getBoolean("isCanceledTradeFilter", false));
        cancel.setOnCheckedChangeListener(this);

        CheckBox open = statusSelectView.findViewById(R.id.checkBoxOpenTrade);
        open.setChecked(prefs.getBoolean("isOpenTradeFilter", false));
        open.setOnCheckedChangeListener(this);

        return v;
    }

    private void setTradeIdParam() {
        if (tradeIdTextEdit.getText().toString().equals("")) {
            enteredTradeId = null;
            prefEditor.putString("tradeIdTradeFilter", "");
        } else {
            enteredTradeId = tradeIdTextEdit.getText().toString();
            prefEditor.putString("tradeIdTradeFilter", tradeIdTextEdit.getText().toString());
        }
        prefEditor.apply();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.checkBoxOpenTrade:
                checkboxSetup(isChecked,"isOpenTradeFilter", "open" );
                break;

            case R.id.checkBoxCanceledTrade:
                checkboxSetup(isChecked,"isCanceledTradeFilter", "canceled" );
                break;

            case R.id.checkBoxValidatedTrade:
                checkboxSetup(isChecked,"isValidatedTradeFilter", "validated" );
                break;

            case R.id.checkBoxBookedTrade:
                checkboxSetup(isChecked,"isBookedTradeFilter", "booked" );
                break;

            case R.id.checkBoxRejectedTrade:
                checkboxSetup(isChecked,"isRejectTradeFilter", "rejected" );
                break;

            default:
                break;
        }
    }

    private void resetTradeParams() {
        enteredTradeId = null;
        selectedProductId = null;
        selectedExecutionType = null;
        selectedBatched = null;
        selectedStartDate = null;
        selectedEndDate = null;
        selectedStatuses.clear();

        tradeViewModel.resetParams();
    }

    //    private void sendDataToPrevPg(HashMap<String, String> hm) {
    private void sendDataToPrevPg() {
        // Send to previous activity page
//        System.out.println("sendDataToPrevPg(HashMap<String, String> hm): " + hm);

//        Bundle bndle = new Bundle();

        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//        switch (filterType) {
//            case "product":
//                bndle.putSerializable("productList", (Serializable) hm);
//                intent.putExtra("products", bndle);
//                break;
//            case "execution type":
//                bndle.putSerializable("executionTypeList", (Serializable) hm);
//                intent.putExtra("executionTypes", bndle);
//                break;
//            case "batched":
//                bndle.putSerializable("batchedList", (Serializable) hm);
//                intent.putExtra("batchedOptions", bndle);
//                break;
//            default:
//                break;
//        }

        getActivity().setResult(RESULT_OK, intent);
        getActivity().finish();
    }

    private void checkboxSetup(boolean isChecked, String prefKey, String status) {
        if (isChecked) {
            prefEditor.putBoolean(prefKey, true);
            selectedStatuses.add(status);
        } else {
            prefEditor.putBoolean(prefKey, false);
            selectedStatuses.remove(selectedStatuses.indexOf(status));
        }
        prefEditor.apply();
    }

//    private void checkBoxSetupToTrue(String prefKey, String status) {
//        prefEditor.putBoolean(prefKey, true);
//        prefEditor.apply();
//        selectedStatuses.add(status);
//    }
//
//    private void checkBoxSetupToFalse(String prefKey, String status) {
//        prefEditor.putBoolean(prefKey, false);
//        prefEditor.apply();
//        selectedStatuses.remove(selectedStatuses.indexOf(status));
//    }

    public static void resetTradeLastPos() {
        lastTradeProductPos = -1;
        lastTradeExecutionPos = -1;
        lastTradeBatchedPos = -1;
    }

    private void resetPrefs() {
        prefEditor.putString("tradeIdTradeFilter", "");
        prefEditor.putString("productTradeFilter", "");
        prefEditor.putString("executionTradeFilter", "");
        prefEditor.putString("batchedTradeFilter", "");
        prefEditor.putString("startDateTradeFilter", "-");
        prefEditor.putString("endDateTradeFilter", getTodayDate());
        prefEditor.putBoolean("isRejectTradeFilter", false);
        prefEditor.putBoolean("isBookedTradeFilter", false);
        prefEditor.putBoolean("isValidatedTradeFilter", false);
        prefEditor.putBoolean("isCanceledTradeFilter", false);
        prefEditor.putBoolean("isOpenTradeFilter", false);
        prefEditor.apply();
        dateText.setText("");
    }

    private void openFilterTradeScreen() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        TradeFilterFragment frg = new TradeFilterFragment();
        ft.replace(R.id.frame_layout, frg, "Trade Filter");
        ft.commit();
    }

    private void openMultiSelectFilter(String type) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        TradeSelectFilterFragment frg = new TradeSelectFilterFragment(type);
        ft.replace(R.id.frame_layout, frg, "Multi Select Filter List");
        ft.commit();
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
        String today = getTodayDate();
        dateText.setHint(today);
        String dateTextFromPrefs = "";
        dateTextFromPrefs = prefs.getString("startDateTradeFilter", "");
        if (!dateTextFromPrefs.equals("-")) {
            dateTextFromPrefs += " - " + prefs.getString("endDateTradeFilter", "");
            dateText.setText(dateTextFromPrefs);
        }

        dateText.setOnClickListener(v1 -> materialDatePicker.show(getFragmentManager(), "Data Picker"));

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                Pair<Long, Long> dates = (Pair<Long, Long>) materialDatePicker.getSelection();
                Long startDate = dates.first;
                Long endDate = dates.second;

                // format and send to backend:
                SimpleDateFormat serverFormat = new SimpleDateFormat("YYYY-MM-dd");
                String startDateForServer = serverFormat.format(startDate);
                String endDateForServer = serverFormat.format(endDate);
                setDateParams(startDateForServer, endDateForServer);

                // format for display:
                SimpleDateFormat clientFormat = new SimpleDateFormat("MMM dd YYYY");
                String startDateForClient = clientFormat.format(startDate);
                String endDateForClient = clientFormat.format(endDate);
                dateText.setText(startDateForClient + " - " + endDateForClient);
                setDatePrefs(startDateForClient, endDateForClient);
            }
        });
    }

    private void setDatePrefs(String startDate, String endDate) {
        prefEditor.putString("startDateTradeFilter", startDate);
        prefEditor.putString("endDateTradeFilter", endDate);
        prefEditor.apply();
    }

    private void setDateParams(String startDate, String endDate) {
        selectedStartDate = startDate;
        selectedEndDate = endDate;
    }

    public static String getTodayDate() {
        return new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(new Date());
    }

}