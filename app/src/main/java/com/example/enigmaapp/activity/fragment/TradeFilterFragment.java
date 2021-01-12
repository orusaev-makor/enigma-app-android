package com.example.enigmaapp.activity.fragment;

import android.app.Activity;
import android.content.SharedPreferences;
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
import android.widget.TextView;

import com.example.enigmaapp.R;
import com.example.enigmaapp.model.TradeViewModel;
import com.example.enigmaapp.model.UserViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import static com.example.enigmaapp.activity.fragment.MultiSelectFilterFragment.lastTradeBatchedPos;
import static com.example.enigmaapp.activity.fragment.MultiSelectFilterFragment.lastTradeExecutionPos;
import static com.example.enigmaapp.activity.fragment.MultiSelectFilterFragment.lastTradeProductPos;

public class TradeFilterFragment extends Fragment {

    private Button closeBtn;
    private Button submitBtn;
    private MaterialButton resetBtn;
    private static TextView dateText;
    private EditText tradeIdTextEdit;

    private TextView productText;
    private TextView executionText;
    private TextView batchedText;

    private View statusSelectView;
    private TradeViewModel viewModel;

    private HashMap<String, String> paramsFromRepository = new HashMap<>();

    public static HashMap<String, String> tradeParamsToSend = new HashMap<>();

    SharedPreferences prefs;
    static SharedPreferences.Editor prefEditor;
    private Activity activity;

    public TradeFilterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide navbar on "Trade filter" view:
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        activity = getActivity();
        prefEditor = androidx.preference.PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
        prefs = androidx.preference.PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_trade_filter, container, false);

        buildCalender(v);

        viewModel = new ViewModelProvider(requireActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(TradeViewModel.class);

        paramsFromRepository = viewModel.getParams();
        System.out.println("paramsFromRepository : " + paramsFromRepository);

        UserViewModel userViewModel = new ViewModelProvider(requireActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(UserViewModel.class);
        String token = userViewModel.getCurrentUser().getToken();

        viewModel.fetchTradeDataset(token);

        tradeIdTextEdit = v.findViewById(R.id.filter_trade_trade_id_edit);
        tradeIdTextEdit.setText(prefs.getString("tradeIdTradeFilter", ""));
        tradeIdTextEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    tradeParamsToSend.put("trade_id", tradeIdTextEdit.getText().toString());
                    prefEditor.putString("tradeIdTradeFilter", tradeIdTextEdit.getText().toString());
                    prefEditor.apply();
                }
            }
        });

        productText = v.findViewById(R.id.filter_trade_product_edit);
        productText.setText(prefs.getString("productTradeFilter", ""));

        String product = getValueFromParams("product_id");
//        System.out.println("FOUND product - getValueFromParams :  " + product);
        String execution = getValueFromParams("execution_type");
//        System.out.println("FOUND execution - getValueFromParams :  " + execution);
        productText.setOnClickListener(v1 -> openMultiSelectFilter("product"));
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
            viewModel.setParams(tradeParamsToSend);
            openTradeScreen();
        });

        // Reset "Filter Trade" screen
        resetBtn = v.findViewById(R.id.filter_trade_reset_btn);
        resetBtn.setOnClickListener(v18 -> {
            tradeParamsToSend.clear();
            viewModel.resetParams();
            resetPrefs();
            openFilterTradeScreen();
            resetTradeLastPos();
        });

        // Close "Filter Trade" screen and go back to "Trade Fragment":
        closeBtn = v.findViewById(R.id.close_btn);
        closeBtn.setOnClickListener(v17 -> {
            openTradeScreen();
//            tradeParamsToSend.clear();
//            viewModel.resetParams();
//            resetLastPos();
        });

        statusSelectView = v.findViewById(R.id.layout_trade_status_select);
        CheckBox reject = (CheckBox) statusSelectView.findViewById(R.id.checkBoxRejectedTrade);
        reject.setChecked(prefs.getBoolean("isRejectTradeFilter", false));
        reject.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                checkBoxSetupToTrue("isRejectTradeFilter", "status[0]", "rejected");
            } else {
                checkBoxSetupToFalse("isRejectTradeFilter", "status[0]");
            }
        });

        CheckBox book = (CheckBox) statusSelectView.findViewById(R.id.checkBoxBookedTrade);
        book.setChecked(prefs.getBoolean("isBookedTradeFilter", false));
        book.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                checkBoxSetupToTrue("isBookedTradeFilter", "status[1]", "booked");
            } else {
                checkBoxSetupToFalse("isBookedTradeFilter", "status[1]");
            }
        });

        CheckBox validate = (CheckBox) statusSelectView.findViewById(R.id.checkBoxValidatedTrade);
        validate.setChecked(prefs.getBoolean("isValidatedTradeFilter", false));
        validate.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                checkBoxSetupToTrue("isValidatedTradeFilter", "status[2]", "validated");
            } else {
                checkBoxSetupToFalse("isValidatedTradeFilter", "status[2]");
            }
        });

        CheckBox cancel = (CheckBox) statusSelectView.findViewById(R.id.checkBoxCanceledTrade);
        cancel.setChecked(prefs.getBoolean("isCanceledTradeFilter", false));
        cancel.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                checkBoxSetupToTrue("isCanceledTradeFilter", "status[3]", "canceled");
            } else {
                checkBoxSetupToFalse("isCanceledTradeFilter", "status[3]");
            }
        });


        CheckBox open = (CheckBox) statusSelectView.findViewById(R.id.checkBoxOpenTrade);
        open.setChecked(prefs.getBoolean("isOpenTradeFilter", false));
        open.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkBoxSetupToTrue("isOpenTradeFilter", "status[4]", "open");
                } else {
                    checkBoxSetupToFalse("isOpenTradeFilter", "status[4]");
                }
            }
        });

        return v;
    }

    private void checkBoxSetupToTrue(String prefKey, String paramKey, String paramVal) {
        prefEditor.putBoolean(prefKey, true);
        prefEditor.apply();
        tradeParamsToSend.put(paramKey, paramVal);
    }

    private void checkBoxSetupToFalse(String prefKey, String paramKey) {
        prefEditor.putBoolean(prefKey, false);
        prefEditor.apply();
        tradeParamsToSend.remove(paramKey);
        viewModel.removeFromParams(paramKey);
    }

    private String getValueFromParams(String key) {
        System.out.println("in getValueFromParams looooooooking for key: " + key);
        Iterator it = paramsFromRepository.entrySet().iterator();
        String res = "";
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            if (entry.getKey().equals(key)) {
                res = entry.getValue().toString();
                System.out.println("in getValueFromParams fooooooooound res: " + res);
                System.out.println("removeFromParams in repository - found key! -> " + key);
                break;
//                it.remove();
            }
        }
        return res;
    }

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
        prefEditor.putString("endDateTradeFilter", "-");
        prefEditor.putBoolean("isRejectTradeFilter", false);
        prefEditor.putBoolean("isBookedTradeFilter", false);
        prefEditor.putBoolean("isValidatedTradeFilter", false);
        prefEditor.putBoolean("isCanceledTradeFilter", false);
        prefEditor.putBoolean("isOpenTradeFilter", false);
        prefEditor.apply();
        dateText.setText("");
    }

    private void openFilterTradeScreen() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        TradeFilterFragment fragment = new TradeFilterFragment();
        transaction.replace(R.id.frame_layout, fragment, "Trade Filter");
        transaction.commit();
    }

    private void openMultiSelectFilter(String type) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        MultiSelectFilterFragment fragment = new MultiSelectFilterFragment(type);
        transaction.replace(R.id.frame_layout, fragment, "Multi Select Filter List");
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
        tradeParamsToSend.put("start_date", startDate);
        tradeParamsToSend.put("end_date", endDate);
    }

    public static String getTodayDate() {
        return new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(new Date());
    }

    private void openTradeScreen() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        TradeFragment fragment = new TradeFragment();
        transaction.replace(R.id.frame_layout, fragment, "Trade");
        transaction.commit();
    }

    public static void setTradeFilterParams(HashMap<String, String> params) {
        Iterator it = params.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry) it.next();
            System.out.println("setting params in trade filter fragment: " + pair.getKey() + " = " + pair.getValue());
            tradeParamsToSend.put(pair.getKey().toString(), pair.getValue().toString());
            it.remove(); // avoids a ConcurrentModificationException
        }
        System.out.println("TRADE PARAMS TO SEND: " + tradeParamsToSend);
    }

    public static void removeFromTradeParams(String key) {
        Iterator it = tradeParamsToSend.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            if (entry.getKey().equals(key)) {
                it.remove();
            }
        }
    }
}