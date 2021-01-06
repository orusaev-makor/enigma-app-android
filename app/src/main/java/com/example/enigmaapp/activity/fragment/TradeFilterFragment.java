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
import java.util.Map;
import java.util.TimeZone;

import static com.example.enigmaapp.activity.fragment.MultiSelectFilterFragment.lastBatchedPos;
import static com.example.enigmaapp.activity.fragment.MultiSelectFilterFragment.lastExecutionPos;
import static com.example.enigmaapp.activity.fragment.MultiSelectFilterFragment.lastProductPos;

public class TradeFilterFragment extends Fragment {

    private Button closeBtn;
    private Button submitBtn;
    private MaterialButton resetBtn;
    private TextView dateText;

    private TextView productText;
    private TextView executionText;
    private TextView batchedText;

    private View statusSelectView;

    public static HashMap<String, String> paramsToSend = new HashMap<>();
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

        TradeViewModel viewModel = new ViewModelProvider(requireActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(TradeViewModel.class);

        UserViewModel userViewModel = new ViewModelProvider(requireActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(UserViewModel.class);
        String token = userViewModel.getCurrentUser().getToken();

        viewModel.fetchTradeDataset(token);

        productText = v.findViewById(R.id.filter_trade_product_edit);
        productText.setText(prefs.getString("productReceived", ""));
        productText.setOnClickListener(v1 -> openMultiSelectFilter("product"));

        executionText = v.findViewById(R.id.filter_trade_execution_edit);
        executionText.setText(prefs.getString("executionTypeReceived", ""));
        executionText.setOnClickListener(v12 -> openMultiSelectFilter("execution type"));

        batchedText = v.findViewById(R.id.filter_trade_batched_edit);
        batchedText.setText(prefs.getString("batchedReceived", ""));
        batchedText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMultiSelectFilter("batched");
            }
        });

        // Submit "Filter" and go back to "Trade" screen
        submitBtn = v.findViewById(R.id.filter_trade_submit_btn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                resetPrefs();
                openFilterTradeScreen();
                resetLastPos();
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

        statusSelectView = v.findViewById(R.id.layout_status_select);
        CheckBox reject = (CheckBox) statusSelectView.findViewById(R.id.checkBoxRejected);
        reject.setChecked(prefs.getBoolean("isRejectClicked", false));
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prefs.getBoolean("isRejectClicked", false)) {
                    prefEditor.putBoolean("isRejectClicked", false);
                    prefEditor.apply();
                    paramsToSend.remove("status[0]");
                    viewModel.removeFromParams("status[0]");
                } else {
                    prefEditor.putBoolean("isRejectClicked", true);
                    prefEditor.apply();
                    paramsToSend.put("status[0]", "rejected");
                }
                System.out.println("clicked on reject! " + prefs.getBoolean("isRejectClicked", false));
            }
        });

        CheckBox book = (CheckBox) statusSelectView.findViewById(R.id.checkBoxBooked);
        book.setChecked(prefs.getBoolean("isBookedClicked", false));
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prefs.getBoolean("isBookedClicked", false)) {
                    prefEditor.putBoolean("isBookedClicked", false);
                    prefEditor.apply();
                    paramsToSend.remove("status[1]");
                    viewModel.removeFromParams("status[1]");
                } else {
                    prefEditor.putBoolean("isBookedClicked", true);
                    prefEditor.apply();
                    paramsToSend.put("status[1]", "booked");
                }
            }
        });

        CheckBox validate = (CheckBox) statusSelectView.findViewById(R.id.checkBoxValidated);
        validate.setChecked(prefs.getBoolean("isValidatedClicked", false));
        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prefs.getBoolean("isValidatedClicked", false)) {
                    prefEditor.putBoolean("isValidatedClicked", false);
                    prefEditor.apply();
                    paramsToSend.remove("status[2]");
                    viewModel.removeFromParams("status[2]");
                } else {
                    prefEditor.putBoolean("isValidatedClicked", true);
                    prefEditor.apply();
                    paramsToSend.put("status[2]", "validated");
                }
            }
        });

        CheckBox cancel = (CheckBox) statusSelectView.findViewById(R.id.checkBoxCanceled);
        cancel.setChecked(prefs.getBoolean("isCancelledClicked", false));
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prefs.getBoolean("isCancelledClicked", false)) {
                    prefEditor.putBoolean("isCancelledClicked", false);
                    prefEditor.apply();
                    paramsToSend.remove("status[3]");
                    viewModel.removeFromParams("status[3]");

                } else {
                    prefEditor.putBoolean("isCancelledClicked", true);
                    prefEditor.apply();
                    paramsToSend.put("status[3]", "canceled");
                }
            }
        });

        CheckBox open = (CheckBox) statusSelectView.findViewById(R.id.checkBoxOpen);
        open.setChecked(prefs.getBoolean("isOpenClicked", false));
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prefs.getBoolean("isOpenClicked", false)) {
                    prefEditor.putBoolean("isOpenClicked", false);
                    prefEditor.apply();
                    paramsToSend.remove("status[4]");
                    viewModel.removeFromParams("status[4]");
                } else {
                    prefEditor.putBoolean("isOpenClicked", true);
                    prefEditor.apply();
                    paramsToSend.put("status[4]", "open");
                }
            }
        });

        return v;
    }

    private void resetLastPos() {
        lastProductPos = -1;
        lastExecutionPos = -1;
        lastBatchedPos = -1;
    }

    private void resetPrefs() {
        prefEditor.putString("productReceived", "");
        prefEditor.putString("executionTypeReceived", "");
        prefEditor.putString("batchedReceived", "");
        prefEditor.putBoolean("isRejectClicked", false);
        prefEditor.putBoolean("isBookedClicked", false);
        prefEditor.putBoolean("isValidatedClicked", false);
        prefEditor.putBoolean("isCancelledClicked", false);
        prefEditor.putBoolean("isOpenClicked", false);
        prefEditor.apply();
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
            System.out.println("setting params in trade filter fragment: " + pair.getKey() + " = " + pair.getValue());
            paramsToSend.put(pair.getKey().toString(), pair.getValue().toString());
            it.remove(); // avoids a ConcurrentModificationException
        }
        System.out.println("PARAMS TO SEND: " + paramsToSend);
    }

    public static void removeFromParams(String key) {
        Iterator it = paramsToSend.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            if (entry.getKey().equals(key)) {
                it.remove();
            }
        }
    }
}