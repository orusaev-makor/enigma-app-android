package com.example.enigmaapp.activity.fragment;

import android.content.SharedPreferences;
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
import com.example.enigmaapp.ui.BatchedFilterAdapter;
import com.example.enigmaapp.ui.ExecutionTypeFilterAdapter;
import com.example.enigmaapp.ui.ProductFilterAdapter;
import com.example.enigmaapp.web.dataset.DatasetBatched;
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
    private TradeViewModel viewModel;

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
                // TODO: add this section back when multi select is supported by server
//                List<String> strList = new ArrayList<>();
//                strList.add("17");
//                strList.add("6");
//                HashMap<String, Object> mapToSend = new HashMap<>();
//
//                mapToSend.put("product_id", strList);
//                ProxyRetrofitQueryMap map = new ProxyRetrofitQueryMap(mapToSend);
//                System.out.println(" MAP : " + map);

//                List<String> values1 = new ArrayList<>();
//                values1.add("10");
//                values1.add("11");
//                map.put("filter[1]", values1);
//
//                List<String> values2 = new ArrayList<>();
//                values1.add("20");
//                values1.add("21");
//                map.put("filter[2]", values2);
            }
        });

        RecyclerView recyclerView = v.findViewById(R.id.multi_select_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        viewModel = new ViewModelProvider(requireActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(TradeViewModel.class);


        switch (mFilterType) {

            case "product":
                final ProductFilterAdapter productAdapter = new ProductFilterAdapter(requireActivity(), true);
                recyclerView.setAdapter(productAdapter);

                viewModel.getProductsDataset().observe(requireActivity(), productItems -> productAdapter.submitList(productItems));

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

                viewModel.getExecutionTypeDataset().observe(requireActivity(), executionTypeItems -> executionTypeAdapter.submitList(executionTypeItems));

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

                viewModel.getBatchedDataset().observe(requireActivity(), new Observer<List<DatasetBatched>>() {
                    @Override
                    public void onChanged(List<DatasetBatched> batchedItems) {
                        batchedAdapter.submitList(batchedItems);
                    }
                });

                batchedAdapter.setOnItemClickListener(new BatchedFilterAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(DatasetBatched batchedItem, int position) {
                        if (batchedItem.getIsChecked()) {
                            batchedItem.setIsChecked(false);
                            Iterator it = params.entrySet().iterator();
                            while (it.hasNext()) {
                                HashMap.Entry entry = (HashMap.Entry) it.next();
                                if (entry.getKey().equals("already_batched") && batchedItem.getValue().equals(entry.getValue())) {
                                    it.remove();
                                }
                            }
                        } else {
                            batchedAdapter.setLastCheckedPos(position);
                            batchedItem.setIsChecked(true);
                            lastTradeBatchedPos = position;
                            params.put("already_batched", batchedItem.getValue());
                            prefEditor.putString("batchedTradeFilter", batchedItem.getName());
                            prefEditor.apply();
                        }

                        batchedAdapter.notifyDataSetChanged();
                    }
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
                viewModel.removeFromParams("product_id");
                break;
            case "execution type":
                removeFromTradeParams("execution_type");
                viewModel.removeFromParams("execution_type");
                break;
            case "batched":
                removeFromTradeParams("already_batched");
                viewModel.removeFromParams("already_batched");
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

        UserViewModel userViewModel = new ViewModelProvider(requireActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(UserViewModel.class);
        String token = userViewModel.getCurrentUser().getToken();

        viewModel.fetchTradeDataset(token);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        TradeSelectFilterFragment fragment = new TradeSelectFilterFragment(type);
        transaction.replace(R.id.frame_layout, fragment, "Multi Select Filter List");
        transaction.commit();
    }
}