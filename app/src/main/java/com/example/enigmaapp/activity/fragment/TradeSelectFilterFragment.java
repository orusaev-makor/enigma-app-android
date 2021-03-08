package com.example.enigmaapp.activity.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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
import com.example.enigmaapp.model.TradeViewModel;
import com.example.enigmaapp.model.LoginViewModel;
import com.example.enigmaapp.ui.BatchedFilterAdapter;
import com.example.enigmaapp.ui.ExecutionTypeFilterAdapter;
import com.example.enigmaapp.ui.ProductFilterAdapter;
import com.google.android.material.button.MaterialButton;


import static com.example.enigmaapp.activity.MainActivity.prefEditor;
import static com.example.enigmaapp.activity.fragment.TradeFragment.selectedBatched;
import static com.example.enigmaapp.activity.fragment.TradeFragment.selectedExecutionType;
import static com.example.enigmaapp.activity.fragment.TradeFragment.selectedProductId;

public class TradeSelectFilterFragment extends Fragment {
    private String mFilterType;
    private TextView titleText, subtitleText;
    private MaterialButton resetBtn;
    private Button submitBtn;
    private TradeViewModel tradeViewModel;

    public static int lastTradeProductPos = -1;
    public static int lastTradeExecutionPos = -1;
    public static int lastTradeBatchedPos = -1;

    public TradeSelectFilterFragment(String filterType) {
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
        View v = inflater.inflate(R.layout.fragment_multi_select_filter, container, false);
        titleText = v.findViewById(R.id.multi_select_title);
        titleText.setText(mFilterType.substring(0, 1).toUpperCase() + mFilterType.substring(1).toLowerCase());

        subtitleText = v.findViewById(R.id.multi_select_subtitle);
        subtitleText.setText("Select " + mFilterType + " to filter");

        // Submit chosen filters and go back to "Filter Trade" screen
        submitBtn = v.findViewById(R.id.multi_select_submit_btn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTradeFilterScreen();
            }
        });

        RecyclerView recyclerView = v.findViewById(R.id.multi_select_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        tradeViewModel = new ViewModelProvider(requireActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(TradeViewModel.class);


        switch (mFilterType) {

            case "product":
                final ProductFilterAdapter productAdapter = new ProductFilterAdapter(requireActivity(), true);
                recyclerView.setAdapter(productAdapter);

                tradeViewModel.getAllProducts().observe(getViewLifecycleOwner(), products -> productAdapter.submitList(products));

                productAdapter.setOnItemClickListener((productItem, position) -> {
                    if (productItem.getIsChecked()) {
                        productItem.setIsChecked(false);
                        selectedProductId = null;
                    } else {
                        productAdapter.setLastCheckedPos(position);
                        productItem.setIsChecked(true);
                        selectedProductId = productItem.getId();
                        lastTradeProductPos = position;
                        prefEditor.putString("productTradeFilter", productItem.getName());
                        prefEditor.apply();
                    }

                    productAdapter.notifyDataSetChanged();
                });
                break;

            case "execution type":
                final ExecutionTypeFilterAdapter executionTypeAdapter = new ExecutionTypeFilterAdapter(requireActivity());
                recyclerView.setAdapter(executionTypeAdapter);

                tradeViewModel.getAllExecutionTypes().observe(getViewLifecycleOwner(), executionTypes -> executionTypeAdapter.submitList(executionTypes));

                executionTypeAdapter.setOnItemClickListener((executionTypeItem, position) -> {
                    if (executionTypeItem.getIsChecked()) {
                        executionTypeItem.setIsChecked(false);
                        selectedExecutionType = null;
                    } else {
                        executionTypeAdapter.setLastCheckedPos(position);
                        executionTypeItem.setIsChecked(true);
                        lastTradeExecutionPos = position;
                        selectedExecutionType = executionTypeItem.getName();
                        prefEditor.putString("executionTradeFilter", selectedExecutionType);
                        prefEditor.apply();
                    }

                    executionTypeAdapter.notifyDataSetChanged();
                });
                break;

            case "batched":
                final BatchedFilterAdapter batchedAdapter = new BatchedFilterAdapter(requireActivity());
                recyclerView.setAdapter(batchedAdapter);

                tradeViewModel.getAllBatched().observe(getViewLifecycleOwner(), batched -> batchedAdapter.submitList(batched));

                batchedAdapter.setOnItemClickListener((batchItem, position) -> {
                    if (batchItem.getIsChecked()) {
                        batchItem.setIsChecked(false);
                        selectedBatched = null;
                    } else {
                        batchedAdapter.setLastCheckedPos(position);
                        batchItem.setIsChecked(true);
                        lastTradeBatchedPos = position;
                        selectedBatched = batchItem.getValue();
                        prefEditor.putString("batchedTradeFilter", batchItem.getName());
                        prefEditor.apply();
                    }
                    batchedAdapter.notifyDataSetChanged();
                });
                break;

            default:
                break;
        }

        // Reset "Filter List" screen
        resetBtn = v.findViewById(R.id.multi_select_reset_btn);
        resetBtn.setOnClickListener(v1 -> {
            openMultiSelectFilter(mFilterType);
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
                selectedProductId = null;
                break;
            case "execution type":
                selectedExecutionType = null;
                break;
            case "batched":
                selectedBatched = null;
                break;
            default:
                break;
        }
    }

    private void resetParam() {
        switch (mFilterType) {
            case "product":
                tradeViewModel.removeFromParams("product_id");
                break;
            case "execution type":
                tradeViewModel.removeFromParams("execution_type");
                break;
            case "batched":
                tradeViewModel.removeFromParams("already_batched");
                break;
            default:
                break;
        }
    }

    private void resetLastPos() {
        switch (mFilterType) {
            case "product":
                lastTradeProductPos = -1;
                break;
            case "execution type":
                lastTradeExecutionPos = -1;
                break;
            case "batched":
                lastTradeBatchedPos = -1;
                break;
            default:
                break;
        }
    }

    private void resetPrefs() {
        switch (mFilterType) {
            case "product":
                prefEditor.putString("productTradeFilter", "");
                prefEditor.apply();
                break;
            case "execution type":
                prefEditor.putString("executionTradeFilter", "");
                prefEditor.apply();
                break;
            case "batched":
                prefEditor.putString("batchedTradeFilter", "");
                prefEditor.apply();
                break;
            default:
                break;
        }
    }

    private void openTradeFilterScreen() {
//        System.out.println(" ________________________         isAdded(); ________________ " + isAdded());
        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        TradeFilterFragment fragment = new TradeFilterFragment();
        ft.replace(R.id.frame_layout, fragment, "Trade Filter");
        ft.commit();
    }

    private void openMultiSelectFilter(String type) {
        TradeViewModel viewModel = new ViewModelProvider(requireActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(TradeViewModel.class);

        LoginViewModel loginViewModel = new ViewModelProvider(requireActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(LoginViewModel.class);
        String token = loginViewModel.getCurrentUser().getToken();

        viewModel.fetchTradeDataset(token);

        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        TradeSelectFilterFragment fragment = new TradeSelectFilterFragment(type);
        ft.replace(R.id.frame_layout, fragment, "Multi Select Filter List");
        ft.commit();
    }
}