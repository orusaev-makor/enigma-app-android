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
import com.example.enigmaapp.model.UserViewModel;
import com.example.enigmaapp.ui.ExecutionTypeFilterAdapter;
import com.example.enigmaapp.ui.ProductFilterAdapter;
import com.example.enigmaapp.web.trade.dataset.TradeDatasetExecutionType;
import com.example.enigmaapp.web.trade.dataset.TradeDatasetProduct;
import com.google.android.material.button.MaterialButton;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static com.example.enigmaapp.activity.fragment.TradeFilterFragment.setParams;

public class MultiSelectFilterFragment extends Fragment {
    private String mFilterType;
    private TextView titleText;
    private TextView subtitleText;

    private MaterialButton resetBtn;
    private Button submitBtn;
    private HashMap<String, String> params = new HashMap<>();

    public MultiSelectFilterFragment(String filterType) {
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
                // TODO: add filter process
                System.out.println(" Params sent from multi select fragmentL: " + params);
                setParams(params);
                openTradeFilterScreen();
            }
        });

        // Reset "Filter List" screen
        resetBtn = v.findViewById(R.id.multi_select_reset_btn);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMultiSelectFilter(mFilterType);
            }
        });

        RecyclerView recyclerView = v.findViewById(R.id.multi_select_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setHasFixedSize(true);



        TradeViewModel viewModel = new ViewModelProvider(requireActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(TradeViewModel.class);


        switch (mFilterType) {

            case "product":
                final ProductFilterAdapter productFilterAdapter = new ProductFilterAdapter(requireActivity());
                recyclerView.setAdapter(productFilterAdapter);
                viewModel.getProductsDataset().observe(requireActivity(), new Observer<List<TradeDatasetProduct>>() {
                    @Override
                    public void onChanged(List<TradeDatasetProduct> tradeDatasetProducts) {
                        productFilterAdapter.submitList(tradeDatasetProducts);
                    }
                });
                productFilterAdapter.setOnItemClickListener(new ProductFilterAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(TradeDatasetProduct productItem) {
                        System.out.println(" Clicked : " + productItem.getName());

//                        setParams(params);
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
                            productItem.setIsChecked(true);
                            params.put("product_id", productItem.getId());
                        }
                        productFilterAdapter.notifyDataSetChanged();
                    }
                });
                break;

            case "execution type":
                final ExecutionTypeFilterAdapter executionTypeFilterAdapter = new ExecutionTypeFilterAdapter(requireActivity());
                recyclerView.setAdapter(executionTypeFilterAdapter);
                viewModel.getExecutionTypeDataset().observe(requireActivity(), new Observer<List<TradeDatasetExecutionType>>() {
                    @Override
                    public void onChanged(List<TradeDatasetExecutionType> tradeDatasetExecutionTypes) {
                        executionTypeFilterAdapter.submitList(tradeDatasetExecutionTypes);
                    }
                });
                executionTypeFilterAdapter.setOnItemClickListener(new ExecutionTypeFilterAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(TradeDatasetExecutionType executionTypeItem) {
                        System.out.println(" Clicked : " + executionTypeItem.getName());


                        if (executionTypeItem.getIsChecked()) {
                            executionTypeItem.setIsChecked(false);
                            Iterator it = params.entrySet().iterator();
                            while (it.hasNext()) {
                                HashMap.Entry entry = (HashMap.Entry) it.next();
                                if (entry.getKey().equals("product_id") && executionTypeItem.getName().equals(entry.getValue())) {
                                    it.remove();
                                }
                            }
                        } else {
                            executionTypeItem.setIsChecked(true);
                            params.put("execution_type", executionTypeItem.getName());
                        }
                        executionTypeFilterAdapter.notifyDataSetChanged();
                    }
                });
                break;

            default:
                break;
        }

        return v;
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

        UserViewModel userViewModel = new ViewModelProvider(requireActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(UserViewModel.class);
        String token = userViewModel.getCurrentUser().getToken();

        viewModel.fetchTradeDataset(token);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        MultiSelectFilterFragment fragment = new MultiSelectFilterFragment(type);
        transaction.replace(R.id.frame_layout, fragment, "Multi Select Filter List");
        transaction.commit();
    }
}