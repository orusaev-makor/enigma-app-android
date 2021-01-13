package com.example.enigmaapp.activity.fragment;

import android.content.SharedPreferences;
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
import com.example.enigmaapp.model.UserViewModel;
import com.example.enigmaapp.ui.ProductFilterAdapter;
import com.example.enigmaapp.web.dataset.DatasetProduct;
import com.google.android.material.button.MaterialButton;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.example.enigmaapp.activity.fragment.BatchFilterFragment.removeFromBatchParams;
import static com.example.enigmaapp.activity.fragment.BatchFilterFragment.setBatchFilterParams;

public class BatchSelectFilterFragment extends Fragment {

    private String mFilterType;
    private TextView titleText;
    private TextView subtitleText;
    private SharedPreferences.Editor prefEditor;
    private SharedPreferences prefs;
    private MaterialButton resetBtn;
    private Button submitBtn;
    private HashMap<String, String> params = new HashMap<>();
    public static int lastBatchProductPos = -1;
    public static int lastBatchCounterpartyPos = -1;
    private SettlementViewModel viewModel;

    public BatchSelectFilterFragment(String filterType) {
        this.mFilterType = filterType;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefEditor = androidx.preference.PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
        prefs = androidx.preference.PreferenceManager.getDefaultSharedPreferences(getActivity());
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
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBatchFilterScreen();
                setBatchFilterParams(params);
            }
        });

        RecyclerView recyclerView = v.findViewById(R.id.batch_select_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        viewModel = new ViewModelProvider(requireActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(SettlementViewModel.class);

        switch (mFilterType) {
            case "product":
                final ProductFilterAdapter productAdapter = new ProductFilterAdapter(requireActivity(), false);
                recyclerView.setAdapter(productAdapter);

                viewModel.getProductsDataset().observe(requireActivity(), productItems -> productAdapter.submitList(productItems));
                productAdapter.setOnItemClickListener(new ProductFilterAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(DatasetProduct productItem, int position) {
                        System.out.println(" Clicked : " + productItem.getName());
                        if (productItem.getIsChecked()) {
                            productItem.setIsChecked(false);
                            Iterator it = params.entrySet().iterator();
                            while (it.hasNext()) {
                                Map.Entry entry = (Map.Entry) it.next();
                                if (entry.getKey().equals("product_id") && productItem.getId().equals(entry.getValue())) {
                                    it.remove();
                                }
                            }
                        } else {
                            productAdapter.setLastCheckedPos(position);
                            productItem.setIsChecked(true);
                            lastBatchProductPos = position;
                            params.put("product_id", productItem.getId());
                            prefEditor.putString("productBatchFilter", productItem.getName());
                            prefEditor.apply();
                        }
                        productAdapter.notifyDataSetChanged();
                    }
                });
                break;

            case "counterparty":
//                final CounterpartyFilterAdapter counterpartyAdapter = new CounterpartyFilterAdapter(requireActivity(), true);
//                recyclerView.setAdapter(counterpartyAdapter);

//                viewModel.getCounterpartyDataset().observe(requireActivity(), new Observer<List<TradeDatasetCounterparty>>() {
//                    @Override
//                    public void onChanged(List<TradeDatasetCounterparty> counterpartyItems) {
//                        counterpartyAdapter.submitList(counterpartyItems);
//                    }
//                });

//                counterpartyAdapter.setOnItemClickListener(new CounterpartyFilterAdapter.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(TradeDatasetCounterparty counterpartyItem, int position) {
//                        System.out.println("Batch - Clicked : " + counterpartyItem.getName());
//
//                        if (counterpartyItem.getIsChecked()) {
//                            counterpartyItem.setIsChecked(false);
//                            Iterator it = params.entrySet().iterator();
//                            while (it.hasNext()) {
//                                Map.Entry entry = (Map.Entry) it.next();
//                                if (entry.getKey().equals("counterparty_id") && counterpartyItem.getId().equals(entry.getValue())) {
//                                    it.remove();
//                                }
//                            }
//                        } else {
//                            counterpartyAdapter.setLastCheckedPos(position);
//                            counterpartyItem.setIsChecked(true);
//                            lastBatchCounterpartyPos = position;
//                            params.put("counterparty_id", counterpartyItem.getId());
//                            prefEditor.putString("counterpartyBatchFilter", counterpartyItem.getName());
//                            prefEditor.apply();
//                        }
//                        counterpartyAdapter.notifyDataSetChanged();
//                    }
//                });
                break;

            default:
                break;
        }

        // Reset "Filter List" screen
        resetBtn = v.findViewById(R.id.batch_select_reset_btn);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBatchSelectFilter(mFilterType);
                resetPrefs();
                resetParam();
                resetLastPos();
            }
        });
        return v;
    }

    private void openBatchSelectFilter(String type) {
        SettlementViewModel viewModel = new ViewModelProvider(requireActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(SettlementViewModel.class);

        UserViewModel userViewModel = new ViewModelProvider(requireActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(UserViewModel.class);
        String token = userViewModel.getCurrentUser().getToken();

        viewModel.fetchBatchDataset(token);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        BatchSelectFilterFragment fragment = new BatchSelectFilterFragment(type);
        transaction.replace(R.id.frame_layout, fragment, "Batch Select Filter List");
        transaction.commit();
    }


    private void resetParam() {
        switch (mFilterType) {
            case "product":
                removeFromBatchParams("product_id");
                viewModel.removeFromBatchParams("product_id");
                break;
            case "counterparty":
                removeFromBatchParams("counterparty_id");
                viewModel.removeFromBatchParams("counterparty_id");
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
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        BatchFilterFragment fragment = new BatchFilterFragment();
        transaction.replace(R.id.frame_layout, fragment, "Batch Filter");
        transaction.commit();
    }
}