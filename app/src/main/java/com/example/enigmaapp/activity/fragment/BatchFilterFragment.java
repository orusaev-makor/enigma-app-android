package com.example.enigmaapp.activity.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.enigmaapp.R;
import com.example.enigmaapp.model.SettlementViewModel;
import com.google.android.material.button.MaterialButton;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.example.enigmaapp.activity.MainActivity.prefEditor;
import static com.example.enigmaapp.activity.MainActivity.prefs;
import static com.example.enigmaapp.activity.fragment.BatchSelectFilterFragment.lastBatchCounterpartyPos;
import static com.example.enigmaapp.activity.fragment.BatchSelectFilterFragment.lastBatchProductPos;
import static com.example.enigmaapp.activity.fragment.SettlementFragment.selectedBatchCounterpartyId;
import static com.example.enigmaapp.activity.fragment.SettlementFragment.selectedBatchProductId;
import static com.example.enigmaapp.activity.fragment.SettlementFragment.selectedBatchStatuses;
import static com.example.enigmaapp.activity.fragment.SettlementFragment.setBatchParams;


public class BatchFilterFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {

    private Button closeBtn;
    private Button submitBtn;
    private MaterialButton resetBtn;

    private TextView productText;
    private TextView counterpartyText;

    private View statusSelectView;
    private SettlementViewModel settlementViewModel;

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
                sendDataToPrevPg();
                setBatchParams();
//                settlementViewModel.setBatchParams(batchParamsToSend);
//                openSettlementScreen();
            }
        });

        // Reset "Filter Settlement" screen
        resetBtn = v.findViewById(R.id.filter_settlement_reset_btn);
        resetBtn.setOnClickListener(v1 -> {
            resetBatchParams();
            resetPrefs();
            resetBatchLastPos();
            openFilterBatchScreen();
        });

        // Close "Filter Settlement" screen and go back to "Settlement Fragment":
        closeBtn = v.findViewById(R.id.close_btn);
        closeBtn.setOnClickListener(v12 -> sendDataToPrevPg());

        statusSelectView = v.findViewById(R.id.layout_batch_status_select);
        CheckBox reject = statusSelectView.findViewById(R.id.checkBoxRejectedBatch);
        reject.setChecked(prefs.getBoolean("isRejectBatchFilter", false));
        reject.setOnCheckedChangeListener(this);


        CheckBox pending = statusSelectView.findViewById(R.id.checkBoxPendingBatch);
        pending.setChecked(prefs.getBoolean("isPendingBatchFilter", false));
        pending.setOnCheckedChangeListener(this);

        CheckBox validate = statusSelectView.findViewById(R.id.checkBoxValidatedBatch);
        validate.setChecked(prefs.getBoolean("isValidatedBatchFilter", false));
        validate.setOnCheckedChangeListener(this);

        CheckBox settled = statusSelectView.findViewById(R.id.checkBoxSettledBatch);
        settled.setChecked(prefs.getBoolean("isSettledBatchFilter", false));
        settled.setOnCheckedChangeListener(this);

        CheckBox inSett = statusSelectView.findViewById(R.id.checkBoxInSetBatch);
        inSett.setChecked(prefs.getBoolean("isInSettBatchFilter", false));
        inSett.setOnCheckedChangeListener(this);

        return v;
    }

    private void resetBatchParams() {
        selectedBatchProductId = null;
        selectedBatchCounterpartyId = null;
        selectedBatchStatuses.clear();

        settlementViewModel.resetBatchParams();
    }

    private void sendDataToPrevPg() {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        getActivity().setResult(RESULT_OK, intent);
        getActivity().finish();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.checkBoxInSetBatch:
                checkboxSetup(isChecked, "isInSettBatchFilter", "in sett");
                break;
            case R.id.checkBoxSettledBatch:
                checkboxSetup(isChecked, "isSettledBatchFilter", "settled");
                break;
            case R.id.checkBoxValidatedBatch:
                checkboxSetup(isChecked, "isValidatedBatchFilter", "validated");
                break;
            case R.id.checkBoxPendingBatch:
                checkboxSetup(isChecked, "isPendingBatchFilter", "pending");
                break;
            case R.id.checkBoxRejectedBatch:
                checkboxSetup(isChecked, "isRejectBatchFilter", "rejected");
                break;
            default:
                break;

        }
    }

    private void checkboxSetup(boolean isChecked, String prefKey, String status) {
        if (isChecked) {
            prefEditor.putBoolean(prefKey, true);
            selectedBatchStatuses.add(status);
        } else {
            prefEditor.putBoolean(prefKey, false);
            selectedBatchStatuses.remove(selectedBatchStatuses.indexOf(status));
        }
        prefEditor.apply();
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
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        BatchFilterFragment frg = new BatchFilterFragment();
        ft.replace(R.id.frame_layout, frg, "Settlement Filter");
        ft.commit();
    }

    private void openBatchSelectFilter(String type) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        BatchSelectFilterFragment frg = new BatchSelectFilterFragment(type);
        ft.replace(R.id.frame_layout, frg, "Filter Batch");
        ft.commit();
    }
}