package com.example.enigmaapp.activity.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.enigmaapp.R;
import com.example.enigmaapp.model.SettlementViewModel;
import com.example.enigmaapp.model.UserViewModel;
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

import static com.example.enigmaapp.activity.fragment.TradeFilterFragment.getTodayDate;
import static com.example.enigmaapp.activity.fragment.UnitarySelectFilterFragment.clearCurrencyAdapterList;
import static com.example.enigmaapp.activity.fragment.UnitarySelectFilterFragment.lastUnitaryCounterpartyPos;


public class SettUnitaryFilterFragment extends Fragment {
    private Button closeBtn;
    private Button submitBtn;
    private MaterialButton resetBtn;
    private static TextView dateText;
    private EditText tradeIdTextEdit;

    private RadioButton send;
    private RadioButton receive;

    private TextView counterpartyText;
    private static TextView currencyText;

    private View statusSelectView;
    private SettlementViewModel viewModel;

    private HashMap<String, String> paramsFromRepository = new HashMap<>();
    public static HashMap<String, String> unitaryParamsToSend = new HashMap<>();
    public static ArrayList<String> clickedCurrencies = new ArrayList<>();
    public static StringBuilder currencyStringBuilder;

    SharedPreferences prefs;
    static SharedPreferences.Editor prefEditor;
    private Activity activity;

    public SettUnitaryFilterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide navbar on "Settlement filter" view:
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        activity = getActivity();
        prefEditor = androidx.preference.PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
        prefs = androidx.preference.PreferenceManager.getDefaultSharedPreferences(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sett_unitary_filter, container, false);

        buildCalender(v);

        viewModel = new ViewModelProvider(requireActivity(),
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

        send.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    handleSideCheck("send");
                } else {
                    handleSideCheck("receive");
                }
            }
        });

        paramsFromRepository = viewModel.getUnitaryParams();
        System.out.println("paramsFromRepository : " + paramsFromRepository);

        UserViewModel userViewModel = new ViewModelProvider(requireActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(UserViewModel.class);
        String token = userViewModel.getCurrentUser().getToken();

        viewModel.fetchUnitaryDataset(token);

        counterpartyText = v.findViewById(R.id.filter_unitary_counterparty);
        counterpartyText.setText(prefs.getString("counterpartyUnitaryFilter", ""));
        counterpartyText.setOnClickListener(v1 -> openUnitarySelectFilter("counterparty"));

        currencyText = v.findViewById(R.id.filter_unitary_currencies);
        currencyText.setText(currencyStringBuilder);
        currencyText.setOnClickListener(v12 -> openUnitarySelectFilter("currency"));

        // Submit "Filter" and go back to "Settlement" screen
        submitBtn = v.findViewById(R.id.filter_settlement_submit_btn);
        submitBtn.setOnClickListener(v15 -> {
            System.out.println("unitaryParamsToSend: submiting ::::::::::: " + unitaryParamsToSend);
            viewModel.setUnitaryParams(unitaryParamsToSend);
            openSettlementScreen();
        });

        // Reset "Filter Settlement" screen
        resetBtn = v.findViewById(R.id.filter_settlement_reset_btn);
        resetBtn.setOnClickListener(v14 -> {
            unitaryParamsToSend.clear();
            viewModel.resetUnitaryParams();
            resetPrefs();
            openFilterUnitaryScreen();
            resetUnitaryLastPos();
            clearCurrenciesText();
            clearCurrencyAdapterList();
            clickedCurrencies.clear();
        });

        // Close "Filter Settlement" screen and go back to "Settlement Fragment":
        closeBtn = v.findViewById(R.id.close_btn);
        closeBtn.setOnClickListener(v13 -> openSettlementScreen());

        statusSelectView = v.findViewById(R.id.layout_unitary_status_select);
        CheckBox reject = (CheckBox) statusSelectView.findViewById(R.id.checkBoxRejectedBatch);
        reject.setChecked(prefs.getBoolean("isRejectUnitaryFilter", false));
        reject.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                checkBoxSetupToTrue("isRejectUnitaryFilter", "status[0]", "rejected");
            } else {
                checkBoxSetupToFalse("isRejectUnitaryFilter", "status[0]");
            }
        });

        CheckBox pending = (CheckBox) statusSelectView.findViewById(R.id.checkBoxPendingBatch);
        pending.setChecked(prefs.getBoolean("isPendingUnitaryFilter", false));
        pending.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                checkBoxSetupToTrue("isPendingUnitaryFilter", "status[1]", "pending");
            } else {
                checkBoxSetupToFalse("isPendingUnitaryFilter", "status[1]");
            }
        });

        CheckBox validate = (CheckBox) statusSelectView.findViewById(R.id.checkBoxValidatedBatch);
        validate.setChecked(prefs.getBoolean("isValidatedUnitaryFilter", false));
        validate.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                checkBoxSetupToTrue("isValidatedUnitaryFilter", "status[2]", "validated");
            } else {
                checkBoxSetupToFalse("isValidatedUnitaryFilter", "status[2]");
            }
        });

        CheckBox settled = (CheckBox) statusSelectView.findViewById(R.id.checkBoxSettledBatch);
        settled.setChecked(prefs.getBoolean("isSettledUnitaryFilter", false));
        settled.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                checkBoxSetupToTrue("isSettledUnitaryFilter", "status[3]", "settled");
            } else {
                checkBoxSetupToFalse("isSettledUnitaryFilter", "status[3]");
            }
        });

        CheckBox inSett = (CheckBox) statusSelectView.findViewById(R.id.checkBoxInSetBatch);
        inSett.setChecked(prefs.getBoolean("isInSettUnitaryFilter", false));
        inSett.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkBoxSetupToTrue("isInSettUnitaryFilter", "status[4]", "in sett");
                } else {
                    checkBoxSetupToFalse("isInSettUnitaryFilter", "status[4]");
                }
            }
        });

        return v;
    }

    public static void clearCurrenciesText() {
        if (currencyStringBuilder != null) {
            currencyStringBuilder.replace(0, currencyStringBuilder.length(), "");
        }
    }

    private void handleSideCheck(String side) {
        markRadioBtn(side);
        String str = "to " + side;
        unitaryParamsToSend.put("side", str);
        prefEditor.putString("side", str);
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
        unitaryParamsToSend.put("start_date", startDate);
        unitaryParamsToSend.put("end_date", endDate);
    }

    private void checkBoxSetupToFalse(String prefKey, String paramKey) {
        prefEditor.putBoolean(prefKey, false);
        prefEditor.apply();
        unitaryParamsToSend.remove(paramKey);
        viewModel.removeFromUnitaryParams(paramKey);
    }

    private void checkBoxSetupToTrue(String prefKey, String paramKey, String paramVal) {
        prefEditor.putBoolean(prefKey, true);
        prefEditor.apply();
        unitaryParamsToSend.put(paramKey, paramVal);
    }

    private void resetUnitaryLastPos() {
        lastUnitaryCounterpartyPos = -1;
    }

    private void resetPrefs() {
        prefEditor.putString("currencyUnitaryFilter", "");
        prefEditor.putString("counterpartyUnitaryFilter", "");
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
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        SettUnitaryFilterFragment fragment = new SettUnitaryFilterFragment();
        transaction.replace(R.id.frame_layout, fragment, "Settlement Filter");
        transaction.commit();
    }

    private void openUnitarySelectFilter(String type) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        UnitarySelectFilterFragment fragment = new UnitarySelectFilterFragment(type);
        transaction.replace(R.id.frame_layout, fragment, "Filter Unitary");
        transaction.commit();
    }

    private void openSettlementScreen() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        SettlementFragment fragment = new SettlementFragment(false);
        transaction.replace(R.id.frame_layout, fragment, "Settlement");
        transaction.commit();
    }

    public static void setUnitaryFilterParams(HashMap<String, String> params) {
        Iterator it = params.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry) it.next();
            System.out.println("setting params in unitary filter fragment: " + pair.getKey() + " = " + pair.getValue());
            unitaryParamsToSend.put(pair.getKey().toString(), pair.getValue().toString());
            it.remove(); // avoids a ConcurrentModificationException
        }
        System.out.println("UNITARY PARAMS TO SEND: " + unitaryParamsToSend);
    }

    public static void removeFromUnitaryParams(String key) {
        Iterator it = unitaryParamsToSend.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            System.out.println("in unittary filter - removeFromUnitaryParams - String key (received) : " + key);
            System.out.println("in unittary filter - removeFromUnitaryParams - Map.Entry entry.getKey : " + entry.getKey());
            if (entry.getKey().equals(key)) {
                it.remove();
            }
        }
    }

}