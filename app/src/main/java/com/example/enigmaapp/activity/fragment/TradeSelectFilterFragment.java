package com.example.enigmaapp.activity.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
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
import com.example.enigmaapp.web.dataset.Batched;
import com.example.enigmaapp.web.dataset.DatasetBatched;
import com.example.enigmaapp.web.dataset.DatasetExecutionType;
import com.example.enigmaapp.web.dataset.ExecutionType;
import com.example.enigmaapp.web.dataset.Product;
import com.google.android.material.button.MaterialButton;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static com.example.enigmaapp.activity.MainActivity.prefEditor;
import static com.example.enigmaapp.activity.fragment.TradeFilterFragment.removeFromTradeParams;
import static com.example.enigmaapp.activity.fragment.TradeFilterFragment.setTradeFilterParams;

public class TradeSelectFilterFragment extends Fragment {
    private String mFilterType;
    private TextView titleText;
    private TextView subtitleText;
    private MaterialButton resetBtn;
    private Button submitBtn;
    private HashMap<String, String> params = new HashMap<>();
    public static int lastTradeProductPos = -1;
    public static int lastTradeExecutionPos = -1;
    public static int lastTradeBatchedPos = -1;
    private TradeViewModel tradeViewModel;

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
                setTradeFilterParams(params);
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

                tradeViewModel.getAllProducts().observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
                    @Override
                    public void onChanged(List<Product> products) {
                        System.out.println("TradeSelectFilterFragment >>> got products size  in __trade__ filter fragment _ " + products.size());
                        productAdapter.submitList(products);
                    }
                });

                productAdapter.setOnItemClickListener((productItem, position) -> {
                    if (productItem.getIsChecked()) {
                        productItem.setIsChecked(false);
                        Iterator it = params.entrySet().iterator();
                        while (it.hasNext()) {
                            HashMap.Entry entry = (HashMap.Entry) it.next();
                            if (entry.getKey().equals("product_id") && productItem.getId().equals(entry.getValue())) {
                                it.remove();
                            }
                        }
                    } else {
                        productAdapter.setLastCheckedPos(position);
                        productItem.setIsChecked(true);
                        lastTradeProductPos = position;
                        params.put("product_id", productItem.getId());
                        prefEditor.putString("productTradeFilter", productItem.getName());
                        prefEditor.apply();
                    }

                    productAdapter.notifyDataSetChanged();
                });
                break;

            case "execution type":
                final ExecutionTypeFilterAdapter executionTypeAdapter = new ExecutionTypeFilterAdapter(requireActivity());
                recyclerView.setAdapter(executionTypeAdapter);

                tradeViewModel.getAllExecutionTypes().observe(getViewLifecycleOwner(), new Observer<List<ExecutionType>>() {
                    @Override
                    public void onChanged(List<ExecutionType> executionTypes) {
                        System.out.println("TradeSelectFilterFragment >>> got executionTypes size  in filter fragment _ " + executionTypes.size());
                        executionTypeAdapter.submitList(executionTypes);
                    }
                });

                executionTypeAdapter.setOnItemClickListener((executionTypeItem, position) -> {
                    if (executionTypeItem.getIsChecked()) {
                        executionTypeItem.setIsChecked(false);
                        Iterator it = params.entrySet().iterator();
                        while (it.hasNext()) {
                            HashMap.Entry entry = (HashMap.Entry) it.next();
                            if (entry.getKey().equals("execution_type") && executionTypeItem.getName().equals(entry.getValue())) {
                                it.remove();
                            }
                        }
                    } else {
                        executionTypeAdapter.setLastCheckedPos(position);
                        executionTypeItem.setIsChecked(true);
                        lastTradeExecutionPos = position;
                        params.put("execution_type", executionTypeItem.getName());
                        prefEditor.putString("executionTradeFilter", executionTypeItem.getName());
                        prefEditor.apply();
                    }

                    executionTypeAdapter.notifyDataSetChanged();
                });
                break;

            case "batched":
                final BatchedFilterAdapter batchedAdapter = new BatchedFilterAdapter(requireActivity());
                recyclerView.setAdapter(batchedAdapter);

                tradeViewModel.getAllBatched().observe(getViewLifecycleOwner(), new Observer<List<Batched>>() {
                    @Override
                    public void onChanged(List<Batched> batched) {
                        System.out.println("TradeSelectFilterFragment >>> got batched size  in filter fragment _ " + batched.size());
                        batchedAdapter.submitList(batched);
                    }
                });

                batchedAdapter.setOnItemClickListener((batchItem, position) -> {
                    if (batchItem.getIsChecked()) {
                        batchItem.setIsChecked(false);
                        Iterator it = params.entrySet().iterator();
                        while (it.hasNext()) {
                            HashMap.Entry entry = (HashMap.Entry) it.next();
                            if (entry.getKey().equals("already_batched") && batchItem.getName().equals(entry.getValue())) {
                                it.remove();
                            }
                        }
                    } else {
                        batchedAdapter.setLastCheckedPos(position);
                        batchItem.setIsChecked(true);
                        lastTradeBatchedPos = position;
                        params.put("already_batched", batchItem.getValue());
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
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMultiSelectFilter(mFilterType);
                resetPrefs();
                resetParam();
                resetLastPos();
            }
        });

        return v;
    }

    private void resetParam() {
        switch (mFilterType) {
            case "product":
                removeFromTradeParams("product_id");
                tradeViewModel.removeFromParams("product_id");
                break;
            case "execution type":
                removeFromTradeParams("execution_type");
                tradeViewModel.removeFromParams("execution_type");
                break;
            case "batched":
                removeFromTradeParams("already_batched");
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
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        TradeFilterFragment fragment = new TradeFilterFragment();
        transaction.replace(R.id.frame_layout, fragment, "Trade Filter");
        transaction.commit();
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

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        TradeSelectFilterFragment fragment = new TradeSelectFilterFragment(type);
        transaction.replace(R.id.frame_layout, fragment, "Multi Select Filter List");
        transaction.commit();
    }
}