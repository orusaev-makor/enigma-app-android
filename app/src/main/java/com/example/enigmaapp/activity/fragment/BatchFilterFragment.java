package com.example.enigmaapp.activity.fragment;

import android.os.Bundle;

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
import com.example.enigmaapp.model.SettlementViewModel;
import com.google.android.material.button.MaterialButton;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.example.enigmaapp.activity.MainActivity.prefEditor;
import static com.example.enigmaapp.activity.MainActivity.prefs;
import static com.example.enigmaapp.activity.fragment.BatchSelectFilterFragment.lastBatchCounterpartyPos;
import static com.example.enigmaapp.activity.fragment.BatchSelectFilterFragment.lastBatchProductPos;


public class BatchFilterFragment extends Fragment {

    private Button closeBtn;
    private Button submitBtn;
    private MaterialButton resetBtn;

    private TextView productText;
    private TextView counterpartyText;

    private View statusSelectView;
    private SettlementViewModel settlementViewModel;

//    private HashMap<String, String> paramsFromRepository = new HashMap<>();
    public static HashMap<String, String> batchParamsToSend = new HashMap<>();

    public BatchFilterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_sett_batch_filter, container, false);

        settlementViewModel = new ViewModelProvider(requireActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(SettlementViewModel.class);

        productText = v.findViewById(R.id.filter_batch_product);
        productText.setText(prefs.getString("productBatchFilter", ""));
        productText.setOnClickListener(v14 -> openBatchSelectFilter("product"));

        counterpartyText = v.findViewById(R.id.filter_batch_counterparty);
        counterpartyText.setText(prefs.getString("counterpartyBatchFilter", ""));
        counterpartyText.setOnClickListener(v13 -> openBatchSelectFilter("counterparty"));

        // Submit "Filter" and go back to "Settlement" screen
        submitBtn = v.findViewById(R.id.filter_settlement_submit_btn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settlementViewModel.setBatchParams(batchParamsToSend);
                openSettlementScreen();
            }
        });

        // Reset "Filter Settlement" screen
        resetBtn = v.findViewById(R.id.filter_settlement_reset_btn);
        resetBtn.setOnClickListener(v1 -> {
            batchParamsToSend.clear();
            settlementViewModel.resetBatchParams();
            resetPrefs();
            openFilterBatchScreen();
            resetBatchLastPos();
        });

        // Close "Filter Settlement" screen and go back to "Settlement Fragment":
        closeBtn = v.findViewById(R.id.close_btn);
        closeBtn.setOnClickListener(v12 -> openSettlementScreen());

        statusSelectView = v.findViewById(R.id.layout_batch_status_select);
        CheckBox reject = (CheckBox) statusSelectView.findViewById(R.id.checkBoxRejectedBatch);
        reject.setChecked(prefs.getBoolean("isRejectBatchFilter", false));
        reject.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                checkBoxSetupToTrue("isRejectBatchFilter", "status[0]", "rejected");
            } else {
                checkBoxSetupToFalse("isRejectBatchFilter", "status[0]");
            }
        });

        CheckBox pending = (CheckBox) statusSelectView.findViewById(R.id.checkBoxPendingBatch);
        pending.setChecked(prefs.getBoolean("isPendingBatchFilter", false));
        pending.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                checkBoxSetupToTrue("isPendingBatchFilter", "status[1]", "pending");
            } else {
                checkBoxSetupToFalse("isPendingBatchFilter", "status[1]");
            }
        });

        CheckBox validate = (CheckBox) statusSelectView.findViewById(R.id.checkBoxValidatedBatch);
        validate.setChecked(prefs.getBoolean("isValidatedBatchFilter", false));
        validate.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                checkBoxSetupToTrue("isValidatedBatchFilter", "status[2]", "validated");
            } else {
                checkBoxSetupToFalse("isValidatedBatchFilter", "status[2]");
            }
        });

        CheckBox settled = (CheckBox) statusSelectView.findViewById(R.id.checkBoxSettledBatch);
        settled.setChecked(prefs.getBoolean("isSettledBatchFilter", false));
        settled.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                checkBoxSetupToTrue("isSettledBatchFilter", "status[3]", "settled");
            } else {
                checkBoxSetupToFalse("isSettledBatchFilter", "status[3]");
            }
        });

        CheckBox inSett = (CheckBox) statusSelectView.findViewById(R.id.checkBoxInSetBatch);
        inSett.setChecked(prefs.getBoolean("isInSettBatchFilter", false));
        inSett.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                checkBoxSetupToTrue("isInSettBatchFilter", "status[4]", "in sett");
            } else {
                checkBoxSetupToFalse("isInSettBatchFilter", "status[4]");
            }
        });

        return v;
    }

    private void checkBoxSetupToTrue(String prefKey, String paramKey, String paramVal) {
        prefEditor.putBoolean(prefKey, true);
        prefEditor.apply();
        batchParamsToSend.put(paramKey, paramVal);
    }

    private void checkBoxSetupToFalse(String prefKey, String paramKey) {
        prefEditor.putBoolean(prefKey, false);
        prefEditor.apply();
        batchParamsToSend.remove(paramKey);
        settlementViewModel.removeFromBatchParams(paramKey);
    }

    private void resetPrefs() {
        prefEditor.putString("productBatchFilter", "");
        prefEditor.putString("counterpartyBatchFilter", "");
        prefEditor.putBoolean("isRejectBatchFilter", false);
        prefEditor.putBoolean("isPendingBatchFilter", false);
        prefEditor.putBoolean("isValidatedBatchFilter", false);
        prefEditor.putBoolean("isSettledBatchFilter", false);
        prefEditor.putBoolean("isInSettBatchFilter", false);
        prefEditor.apply();
    }

    public static void resetBatchLastPos() {
        lastBatchProductPos = -1;
        lastBatchCounterpartyPos = -1;
    }

    private void openFilterBatchScreen() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        BatchFilterFragment fragment = new BatchFilterFragment();
        transaction.replace(R.id.frame_layout, fragment, "Settlement Filter");
        transaction.commit();
    }

    private void openBatchSelectFilter(String type) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        BatchSelectFilterFragment fragment = new BatchSelectFilterFragment(type);
        transaction.replace(R.id.frame_layout, fragment, "Filter Batch");
        transaction.commit();
    }

    private void openSettlementScreen() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        SettlementFragment fragment = new SettlementFragment(true);
        transaction.replace(R.id.frame_layout, fragment, "Settlement");
        transaction.commit();
    }

    public static void setBatchFilterParams(HashMap<String, String> params) {
        Iterator it = params.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry) it.next();
            batchParamsToSend.put(pair.getKey().toString(), pair.getValue().toString());
            it.remove(); // avoids a ConcurrentModificationException
        }
    }

    public static void removeFromBatchParams(String key) {
        Iterator it = batchParamsToSend.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            if (entry.getKey().equals(key)) {
                it.remove();
            }
        }
    }
}