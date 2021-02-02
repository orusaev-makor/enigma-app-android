package com.example.enigmaapp.activity.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
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
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.enigmaapp.R;
import com.example.enigmaapp.model.SettlementViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TimeZone;

import static android.app.Activity.RESULT_OK;
import static com.example.enigmaapp.activity.MainActivity.prefEditor;
import static com.example.enigmaapp.activity.MainActivity.prefs;
import static com.example.enigmaapp.activity.fragment.SettlementFragment.clickedCounterparties;
import static com.example.enigmaapp.activity.fragment.SettlementFragment.clickedCurrencies;
import static com.example.enigmaapp.activity.fragment.SettlementFragment.counterpartyStringBuilder;
import static com.example.enigmaapp.activity.fragment.SettlementFragment.currencyStringBuilder;
import static com.example.enigmaapp.activity.fragment.SettlementFragment.selectedUnitaryEndDate;
import static com.example.enigmaapp.activity.fragment.SettlementFragment.selectedUnitarySide;
import static com.example.enigmaapp.activity.fragment.SettlementFragment.selectedUnitaryStartDate;
import static com.example.enigmaapp.activity.fragment.SettlementFragment.selectedUnitaryStatuses;
import static com.example.enigmaapp.activity.fragment.SettlementFragment.setUnitaryParams;
import static com.example.enigmaapp.activity.fragment.TradeFilterFragment.getTodayDate;

public class UnitaryFilterFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {
    private Button closeBtn;
    private Button submitBtn;
    private MaterialButton resetBtn;
    private static TextView dateText;

    private RadioButton send;
    private RadioButton receive;

    private TextView counterpartyText;
    private TextView currencyText;

    private View statusSelectView;
    private SettlementViewModel settlementViewModel;

    public UnitaryFilterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sett_unitary_filter, container, false);

        buildCalender(v);

        settlementViewModel = new ViewModelProvider(requireActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(SettlementViewModel.class);

        send = v.findViewById(R.id.radioButtonUnitarySendSide);
        receive = v.findViewById(R.id.radioButtonUnitaryReceiveSide);

        // init radio with "send"
        send.setChecked(true);
        if (send.isChecked()) {
            handleSideCheck("send");
        } else {
            handleSideCheck("receive");
        }

        send.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                handleSideCheck("send");
            } else {
                handleSideCheck("receive");
            }
        });

        counterpartyText = v.findViewById(R.id.filter_unitary_counterparty);
        counterpartyText.setText(counterpartyStringBuilder);
        counterpartyText.setOnClickListener(v1 -> openUnitarySelectFilter("counterparty"));

        currencyText = v.findViewById(R.id.filter_unitary_currencies);
        currencyText.setText(currencyStringBuilder);
        currencyText.setOnClickListener(v12 -> openUnitarySelectFilter("currency"));

        // Submit "Filter" and go back to "Settlement" screen
        submitBtn = v.findViewById(R.id.filter_settlement_submit_btn);
        submitBtn.setOnClickListener(v15 -> {
            sendDataToPrevPg();
            setUnitaryParams();
        });

        // Reset "Filter Settlement" screen
        resetBtn = v.findViewById(R.id.filter_settlement_reset_btn);
        resetBtn.setOnClickListener(v14 -> {
            resetUnitaryParams();
            resetPrefs();
            openFilterUnitaryScreen();
            clearCurrenciesText();
            clickedCurrencies.clear();
            clickedCounterparties.clear();
            clearCounterpartiesText();
        });

        // Close "Filter Settlement" screen and go back to "Settlement Fragment":
        closeBtn = v.findViewById(R.id.close_btn);
        closeBtn.setOnClickListener(v13 -> sendDataToPrevPg());

        statusSelectView = v.findViewById(R.id.layout_unitary_status_select);
        CheckBox reject = statusSelectView.findViewById(R.id.checkBoxRejectedBatch);
        reject.setChecked(prefs.getBoolean("isRejectUnitaryFilter", false));
        reject.setOnCheckedChangeListener(this);

        CheckBox pending = statusSelectView.findViewById(R.id.checkBoxPendingBatch);
        pending.setChecked(prefs.getBoolean("isPendingUnitaryFilter", false));
        pending.setOnCheckedChangeListener(this);

        CheckBox validate = statusSelectView.findViewById(R.id.checkBoxValidatedBatch);
        validate.setChecked(prefs.getBoolean("isValidatedUnitaryFilter", false));
        validate.setOnCheckedChangeListener(this);

        CheckBox settled = statusSelectView.findViewById(R.id.checkBoxSettledBatch);
        settled.setChecked(prefs.getBoolean("isSettledUnitaryFilter", false));
        settled.setOnCheckedChangeListener(this);

        CheckBox inSett = statusSelectView.findViewById(R.id.checkBoxInSetBatch);
        inSett.setChecked(prefs.getBoolean("isInSettUnitaryFilter", false));
        inSett.setOnCheckedChangeListener(this);
        return v;
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.checkBoxInSetBatch:
                checkboxSetup(isChecked, "isInSettUnitaryFilter", "in sett");
                break;
            case R.id.checkBoxSettledBatch:
                checkboxSetup(isChecked, "isSettledUnitaryFilter", "settled");
                break;
            case R.id.checkBoxValidatedBatch:
                checkboxSetup(isChecked, "isValidatedUnitaryFilter", "validated");
                break;
            case R.id.checkBoxPendingBatch:
                checkboxSetup(isChecked, "isPendingUnitaryFilter", "pending");
                break;
            case R.id.checkBoxRejectedBatch:
                checkboxSetup(isChecked, "isRejectUnitaryFilter", "rejected");
                break;
            default:
                break;
        }
    }

    private void checkboxSetup(boolean isChecked, String prefKey, String status) {
        if (isChecked) {
            prefEditor.putBoolean(prefKey, true);
            selectedUnitaryStatuses.add(status);
        } else {
            prefEditor.putBoolean(prefKey, false);
            selectedUnitaryStatuses.remove(selectedUnitaryStatuses.indexOf(status));
        }
        prefEditor.apply();
    }

    private void resetUnitaryParams() {
        selectedUnitaryStartDate = null;
        selectedUnitaryEndDate = null;
        selectedUnitarySide = null;

        settlementViewModel.resetUnitaryParams();
    }

    private void sendDataToPrevPg() {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        getActivity().setResult(RESULT_OK, intent);
        getActivity().finish();
    }

    public static void clearCounterpartiesText() {
        if (counterpartyStringBuilder != null) {
            counterpartyStringBuilder.replace(0, counterpartyStringBuilder.length(), "");
        }
    }

    public static void clearCurrenciesText() {
        if (currencyStringBuilder != null) {
            currencyStringBuilder.replace(0, currencyStringBuilder.length(), "");
        }
    }

    private void handleSideCheck(String side) {
        markRadioBtn(side);
        selectedUnitarySide = "to " + side;
        prefEditor.putString("side", selectedUnitarySide);
        prefEditor.apply();
    }

    @SuppressLint("NewApi")
    private void markRadioBtn(String side) {
        if (side.equals("send")) {
            send.setTextColor(getContext().getResources().getColor(R.color.textColor));
            send.setButtonTintList(ColorStateList.valueOf(getContext().getResources().getColor(R.color.textColor)));
            receive.setTextColor(getContext().getResources().getColor(R.color.textSecondaryColor));
            receive.setButtonTintList(ColorStateList.valueOf(getContext().getResources().getColor(R.color.textSecondaryColor)));
        } else {
            receive.setTextColor(getContext().getResources().getColor(R.color.textColor));
            receive.setButtonTintList(ColorStateList.valueOf(getContext().getResources().getColor(R.color.textColor)));
            send.setTextColor(getContext().getResources().getColor(R.color.textSecondaryColor));
            send.setButtonTintList(ColorStateList.valueOf(getContext().getResources().getColor(R.color.textSecondaryColor)));
        }
    }

    private void buildCalender(View v) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.clear();

        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("Select data range");
        //To apply a dialog
        builder.setTheme(R.style.ThemeOverlay_MaterialComponents_MaterialCalendar);

        final MaterialDatePicker materialDatePicker = builder.build();

        dateText = v.findViewById(R.id.filter_unitary_date);
        String today = getTodayDate();
        dateText.setHint(today);
        String dateTextFromPrefs = "";
        dateTextFromPrefs = prefs.getString("startDateUnitaryFilter", "-");
        if (!dateTextFromPrefs.equals("-")) {
            dateTextFromPrefs += " - " + prefs.getString("endDateUnitaryFilter", "");
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
        prefEditor.putString("startDateUnitaryFilter", startDate);
        prefEditor.putString("endDateUnitaryFilter", endDate);
        prefEditor.apply();
    }

    private void setDateParams(String startDate, String endDate) {
        selectedUnitaryStartDate = startDate;
        selectedUnitaryEndDate = endDate;
    }

    private void resetPrefs() {
        prefEditor.putBoolean("isRejectUnitaryFilter", false);
        prefEditor.putBoolean("isPendingUnitaryFilter", false);
        prefEditor.putBoolean("isValidatedUnitaryFilter", false);
        prefEditor.putBoolean("isSettledUnitaryFilter", false);
        prefEditor.putBoolean("isInSettUnitaryFilter", false);
        prefEditor.putString("startDateUnitaryFilter", "-");
        prefEditor.putString("endDateUnitaryFilter", getTodayDate());
        prefEditor.apply();
        dateText.setText("");
    }

    private void openFilterUnitaryScreen() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        UnitaryFilterFragment frg = new UnitaryFilterFragment();
        ft.replace(R.id.frame_layout, frg, "Settlement Filter");
        ft.commit();
    }

    private void openUnitarySelectFilter(String type) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        UnitaryMultiSelectFilterFragment frg = new UnitaryMultiSelectFilterFragment(type);
        ft.replace(R.id.frame_layout, frg, "Filter Unitary");
        ft.commit();
    }

//    public static void setUnitaryFilterParams(HashMap<String, String> params) {
//        Iterator it = params.entrySet().iterator();
//        while (it.hasNext()) {
//            HashMap.Entry pair = (HashMap.Entry) it.next();
//            if (!unitaryParamsToSend.containsValue(pair.getValue())) {
//                unitaryParamsToSend.put(pair.getKey().toString(), pair.getValue().toString());
//            }
//            it.remove(); // avoids a ConcurrentModificationException
//        }
//    }
}