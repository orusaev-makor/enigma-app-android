package com.example.enigmaapp.activity.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
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
import com.example.enigmaapp.web.ProxyRetrofitQueryMap;
import com.example.enigmaapp.web.trade.dataset.TradeDatasetExecutionType;
import com.example.enigmaapp.web.trade.dataset.TradeDatasetProduct;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.example.enigmaapp.activity.fragment.TradeFilterFragment.setParams;
import static com.example.enigmaapp.activity.fragment.TradeFilterFragment.updateUserInputField;

public class MultiSelectFilterFragment extends Fragment {
    private String mFilterType;
    private TextView titleText;
    private TextView subtitleText;
    private SharedPreferences.Editor prefEditor;
    private SharedPreferences prefs;
    private MaterialButton resetBtn;
    private Button submitBtn;
    private HashMap<String, String> params = new HashMap<>();
    public static int lastProductPos = -1;
    public static int lastExecutionPos = -1;

    public MultiSelectFilterFragment(String filterType) {
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
                    public void onItemClick(TradeDatasetProduct productItem, int position) {
                        System.out.println(" Clicked : " + productItem.getName());

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
                            productFilterAdapter.setLastCheckedPos(position);
                            productItem.setIsChecked(true);
                            lastProductPos = position;
                            params.put("product_id", productItem.getId());
//                            updateUserInputField("product", productItem.getName());
                            setParams(params);
                            prefEditor.putString("productReceived", productItem.getName());
                            prefEditor.apply();
                            System.out.println("---- Prefes chnged : productReceived: " + prefs.getString("productReceived", ""));
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
                    public void onItemClick(TradeDatasetExecutionType executionTypeItem, int position) {
                        System.out.println(" Clicked : " + executionTypeItem.getName());

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
                            executionTypeFilterAdapter.setLastCheckedPos(position);
                            executionTypeItem.setIsChecked(true);
                            lastExecutionPos = position;
                            params.put("execution_type", executionTypeItem.getName());
                            prefEditor.putString("executionTypeReceived", executionTypeItem.getName());
                            prefEditor.apply();
                            setParams(params);
                        }
                        executionTypeFilterAdapter.notifyDataSetChanged();
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
                viewModel.removeFromParams(mFilterType);
                resetLastPos();
            }
        });

        return v;
    }

    private void resetLastPos() {
        switch (mFilterType) {
            case "product":
                lastProductPos = -1;
                break;
            case "execution type":
                lastExecutionPos = -1;
                break;
            default:
                break;
        }
    }

    private void resetPrefs() {
        switch (mFilterType) {
            case "product":
                prefEditor.putString("productReceived", "");
                prefEditor.apply();
                break;
            case "execution type":
                prefEditor.putString("executionTypeReceived", "");
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
        MultiSelectFilterFragment fragment = new MultiSelectFilterFragment(type);
        transaction.replace(R.id.frame_layout, fragment, "Multi Select Filter List");
        transaction.commit();
    }
}