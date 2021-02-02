package com.example.enigmaapp.activity.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.enigmaapp.R;
import com.example.enigmaapp.model.SettlementViewModel;
import com.example.enigmaapp.ui.CounterpartyFilterAdapter;
import com.example.enigmaapp.ui.ProductFilterAdapter;
import com.google.android.material.button.MaterialButton;

import static com.example.enigmaapp.activity.MainActivity.prefEditor;
import static com.example.enigmaapp.activity.fragment.SettlementFragment.selectedBatchProductId;
import static com.example.enigmaapp.activity.fragment.SettlementFragment.selectedBatchCounterpartyId;

public class BatchSelectFilterFragment extends Fragment {

    private String mFilterType;
    private TextView titleText;
    private TextView subtitleText;
    private MaterialButton resetBtn;
    private Button submitBtn;
    private SettlementViewModel settlementViewModel;

    public static int lastBatchProductPos = -1;
    public static int lastBatchCounterpartyPos = -1;

    public BatchSelectFilterFragment(String filterType) {
        this.mFilterType = filterType;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_batch_select_filter, container, false);
        titleText = v.findViewById(R.id.batch_select_title);
        titleText.setText(mFilterType.substring(0, 1).toUpperCase() + mFilterType.substring(1).toLowerCase());

        subtitleText = v.findViewById(R.id.batch_select_subtitle);
        subtitleText.setText("Select " + mFilterType + " to filter");

        // Submit chosen filters and go back to "Filter Batch" screen
        submitBtn = v.findViewById(R.id.batch_select_submit_btn);
        submitBtn.setOnClickListener(v1 -> {
            openBatchFilterScreen();
        });

        RecyclerView recyclerView = v.findViewById(R.id.batch_select_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        settlementViewModel = new ViewModelProvider(requireActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(SettlementViewModel.class);

        switch (mFilterType) {
            case "product":
                final ProductFilterAdapter productAdapter = new ProductFilterAdapter(requireActivity(), false);
                recyclerView.setAdapter(productAdapter);

                settlementViewModel.getAllProducts().observe(getViewLifecycleOwner(), products -> productAdapter.submitList(products));

                productAdapter.setOnItemClickListener((productItem, position) -> {
                    if (productItem.getIsChecked()) {
                        productItem.setIsChecked(false);
                        selectedBatchProductId = null;
                    } else {
                        productAdapter.setLastCheckedPos(position);
                        productItem.setIsChecked(true);
                        selectedBatchProductId = productItem.getId();
                        lastBatchProductPos = position;
                        prefEditor.putString("productBatchFilter", productItem.getName());
                        prefEditor.apply();
                    }
                    productAdapter.notifyDataSetChanged();
                });
                break;

            // TODO: setting this up after counterparty is back in dataset
            case "counterparty":
                final CounterpartyFilterAdapter counterpartyAdapter = new CounterpartyFilterAdapter(requireActivity(), true);
                recyclerView.setAdapter(counterpartyAdapter);

//                viewModel.getCounterpartyDatasetBatch().observe(requireActivity(), new Observer<List<DatasetCounterparty>>() {
//                    @Override
//                    public void onChanged(List<DatasetCounterparty> counterpartyItems) {
//                        counterpartyAdapter.submitList(counterpartyItems);
//                    }
//                });

                counterpartyAdapter.setOnItemClickListener((counterpartyItem, position) -> {
                    if (counterpartyItem.getIsChecked()) {
                        counterpartyItem.setIsChecked(false);
                        selectedBatchCounterpartyId = null;
                    } else {
                        counterpartyAdapter.setLastCheckedPos(position);
                        counterpartyItem.setIsChecked(true);
                        lastBatchCounterpartyPos = position;
                        selectedBatchCounterpartyId = counterpartyItem.getId();
                        prefEditor.putString("counterpartyBatchFilter", counterpartyItem.getName());
                        prefEditor.apply();
                    }
                    counterpartyAdapter.notifyDataSetChanged();
                });
                break;

            default:
                break;
        }

        // Reset "Filter List" screen
        resetBtn = v.findViewById(R.id.batch_select_reset_btn);
        resetBtn.setOnClickListener(v12 -> {
            openBatchSelectFilter(mFilterType);
            resetPrefs();
            resetParam();
            resetLastPos();
            resetSelectedFilterString();
        });
        return v;
    }

    private void resetSelectedFilterString() {
        switch (mFilterType) {
            case "product":
                selectedBatchProductId = null;
                break;
            case "counterparty":
                selectedBatchCounterpartyId = null;
                break;
            default:
                break;
        }
    }

    private void openBatchSelectFilter(String type) {
        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        BatchSelectFilterFragment frg = new BatchSelectFilterFragment(type);
        ft.replace(R.id.frame_layout, frg, "Batch Select Filter List");
        ft.commit();
    }

    private void resetParam() {
        switch (mFilterType) {
            case "product":
                settlementViewModel.removeFromBatchParams("product_id");
                break;
            case "counterparty":
                settlementViewModel.removeFromBatchParams("counterparty_id");
                break;
            default:
                break;
        }
    }

    private void resetLastPos() {
        switch (mFilterType) {
            case "product":
                lastBatchProductPos = -1;
                break;
            case "counterparty":
                lastBatchCounterpartyPos = -1;
                break;
            default:
                break;
        }
    }

    private void resetPrefs() {
        switch (mFilterType) {
            case "product":
                prefEditor.putString("productBatchFilter", "");
                prefEditor.apply();
                break;
            case "counterparty":
                prefEditor.putString("counterpartyBatchFilter", "");
                prefEditor.apply();
                break;
            default:
                break;
        }
    }

    private void openBatchFilterScreen() {
        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        BatchFilterFragment frg = new BatchFilterFragment();
        ft.replace(R.id.frame_layout, frg, "Batch Filter");
        ft.commit();
    }
}