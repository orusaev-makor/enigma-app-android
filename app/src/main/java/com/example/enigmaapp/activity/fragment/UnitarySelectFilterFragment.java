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
import com.example.enigmaapp.model.SettlementViewModel;
import com.example.enigmaapp.model.UserViewModel;
import com.example.enigmaapp.ui.CounterpartyFilterAdapter;
import com.example.enigmaapp.ui.CurrencyFilterAdapter;
import com.example.enigmaapp.web.trade.dataset.DatasetCurrency;
import com.example.enigmaapp.web.trade.dataset.TradeDatasetCounterparty;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.example.enigmaapp.activity.fragment.SettUnitaryFilterFragment.clearCurrenciesText;
import static com.example.enigmaapp.activity.fragment.SettUnitaryFilterFragment.clickedCurrencies;
import static com.example.enigmaapp.activity.fragment.SettUnitaryFilterFragment.removeFromUnitaryParams;
import static com.example.enigmaapp.activity.fragment.SettUnitaryFilterFragment.setUnitaryFilterParams;
import static com.example.enigmaapp.activity.fragment.SettUnitaryFilterFragment.currencyStringBuilder;

public class UnitarySelectFilterFragment extends Fragment {

    private String mFilterType;
    private TextView titleText;
    private TextView subtitleText;
    private SharedPreferences.Editor prefEditor;
    private SharedPreferences prefs;
    private MaterialButton resetBtn;
    private Button submitBtn;
    private HashMap<String, String> params = new HashMap<>();

    public static String currenciesToPresent;
    public static int lastUnitaryCounterpartyPos = -1;
    //    public static int lastUnitaryCurrencyPos = -1;
    private SettlementViewModel viewModel;
    private static CurrencyFilterAdapter currencyAdapter;
    private HashMap<String, String> currencyParamsToSend = new HashMap<>();

    public UnitarySelectFilterFragment(String filterType) {
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
        View v = inflater.inflate(R.layout.fragment_unitary_select_filter, container, false);
        titleText = v.findViewById(R.id.unitary_select_title);
        titleText.setText(mFilterType.substring(0, 1).toUpperCase() + mFilterType.substring(1).toLowerCase());

        subtitleText = v.findViewById(R.id.unitary_select_subtitle);
        subtitleText.setText("Select " + mFilterType + " to filter");

        // Submit chosen filters and go back to "Filter Unitary" screen
        submitBtn = v.findViewById(R.id.unitary_select_submit_btn);
        submitBtn.setOnClickListener(v1 -> {
            openUnitaryFilterScreen();
            if (mFilterType.equals("counterparty")) {
                setUnitaryFilterParams(params);
            }

            if (mFilterType.equals("currency")) {
                buildString();
                for (int i = 0; i < clickedCurrencies.size(); i++) {
                    System.out.println("submitting params from select fragment --- clickedCurrencies(i): " + clickedCurrencies.get(i));
                    params.put("currency_list[" + i + "]", clickedCurrencies.get(i));
                }
                setUnitaryFilterParams(params);
                System.out.println("submitting params from select fragment: " + params);
            }
        });

        RecyclerView recyclerView = v.findViewById(R.id.unitary_select_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        viewModel = new ViewModelProvider(requireActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(SettlementViewModel.class);

        switch (mFilterType) {
            case "counterparty":
                final CounterpartyFilterAdapter counterpartyAdapter = new CounterpartyFilterAdapter(requireActivity(), false);
                recyclerView.setAdapter(counterpartyAdapter);

                viewModel.getCounterpartyDataset().observe(requireActivity(), new Observer<List<TradeDatasetCounterparty>>() {
                    @Override
                    public void onChanged(List<TradeDatasetCounterparty> counterpartyItems) {
                        counterpartyAdapter.submitList(counterpartyItems);
                    }
                });
                counterpartyAdapter.setOnItemClickListener(new CounterpartyFilterAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(TradeDatasetCounterparty counterpartyItem, int position) {
                        System.out.println("unitary - Clicked : " + counterpartyItem.getName());
                        if (counterpartyItem.getIsChecked()) {
                            counterpartyItem.setIsChecked(false);
                            Iterator it = params.entrySet().iterator();
                            while (it.hasNext()) {
                                Map.Entry entry = (Map.Entry) it.next();
                                if (entry.getKey().equals("counterparty_id") && counterpartyItem.getId().equals(entry.getValue())) {
                                    it.remove();
                                }
                            }
                        } else {
                            counterpartyAdapter.setLastCheckedPos(position);
                            counterpartyItem.setIsChecked(true);
                            lastUnitaryCounterpartyPos = position;
                            params.put("counterparty_id", counterpartyItem.getId());
                            prefEditor.putString("counterpartyUnitaryFilter", counterpartyItem.getName());
                            prefEditor.apply();
                        }
                        counterpartyAdapter.notifyDataSetChanged();
                    }
                });
                break;

            case "currency":
                ArrayList<DatasetCurrency> data = viewModel.getCurrencyDataset();
                currencyAdapter = new CurrencyFilterAdapter(requireActivity(), data);
                recyclerView.setAdapter(currencyAdapter);

                currencyAdapter.setCurrencies(data);
                currencyAdapter.notifyDataSetChanged();

                currencyAdapter.setOnItemClickListener(new CurrencyFilterAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(DatasetCurrency currencyItem, int position) {
//                        System.out.println("unitary - Clicked : " + currencyItem.getName());
                        if (currencyItem.getIsChecked()) {
                            currencyItem.setIsChecked(false);
                            int idx = clickedCurrencies.indexOf(currencyItem.getName());
                            clickedCurrencies.remove(idx);
                        } else {
                            currencyItem.setIsChecked(true);
                            clickedCurrencies.add(currencyItem.getName());
                        }
                        currencyAdapter.notifyDataSetChanged();
                    }
                });
            default:
                break;
        }

        // Reset "Filter List" screen
        resetBtn = v.findViewById(R.id.unitary_select_reset_btn);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("----------------------- RESETINGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG -----------------------");
                openUnitarySelectFilter(mFilterType);
                resetPrefs();
                resetParam();
                resetLastPos();
                params.clear();
                System.out.println("submitting params after reset in select fragment: " + params);
                if (mFilterType.equals("currency")) {
                System.out.println("----------------------- currency RESETINGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG -----------------------");
                    clearCurrenciesText();
                    clearCurrencyAdapterList();
                    for (int i = 0; i < clickedCurrencies.size(); i++) {
                System.out.println("----------------------- \"currency_list[\" + i + \"]\" RESETINGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG -----------------------" + "currency_list[" + i + "]");
                        removeFromUnitaryParams("currency_list[" + i + "]");
                        viewModel.removeFromUnitaryParams("currency_list[" + i + "]");
                    }
                    clickedCurrencies.clear();
                }
            }
        });

        return v;
    }

    public static void clearCurrencyAdapterList() {
        currencyAdapter.clearSelected();
    }

    private void buildString() {
        currencyStringBuilder = new StringBuilder();
        if (currencyAdapter.getSelected().size() > 0) {
            for (int i = 0; i < currencyAdapter.getSelected().size(); i++) {
                currencyStringBuilder.append(currencyAdapter.getSelected().get(i).getName());
                currencyStringBuilder.append("\n");
            }
            System.out.println("_____________________buildString: _____________________");
            System.out.println(currencyStringBuilder);
        } else {
            currencyStringBuilder.replace(0, currencyStringBuilder.length(), "");
        }
    }

    private void resetLastPos() {
        switch (mFilterType) {
            case "counterparty":
                lastUnitaryCounterpartyPos = -1;
                break;
            default:
                break;
        }
    }

    private void resetParam() {
        switch (mFilterType) {
            case "counterparty":
                removeFromUnitaryParams("counterparty_id");
                viewModel.removeFromUnitaryParams("counterparty_id");
                break;
            case "currency":
                for (int i = 0; i < params.size(); i++) {
                    System.out.println("__________________________________ is loop - reseting param i: " + i);
                    removeFromUnitaryParams("currency_list[" + i + "]");
                    viewModel.removeFromUnitaryParams("currency_list[" + i + "]");
                }
            default:
                break;
        }
    }

    private void resetPrefs() {
        switch (mFilterType) {
            case "counterparty":
                prefEditor.putString("counterpartyUnitaryFilter", "");
                prefEditor.apply();
                break;
            default:
                break;
        }
    }

    private void openUnitarySelectFilter(String type) {
        SettlementViewModel viewModel = new ViewModelProvider(requireActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(SettlementViewModel.class);

        UserViewModel userViewModel = new ViewModelProvider(requireActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(UserViewModel.class);
        String token = userViewModel.getCurrentUser().getToken();

        viewModel.fetchUnitaryDataset(token);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        UnitarySelectFilterFragment fragment = new UnitarySelectFilterFragment(type);
        transaction.replace(R.id.frame_layout, fragment, "Unitary Select Filter List");
        transaction.commit();
    }

    private void openUnitaryFilterScreen() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        SettUnitaryFilterFragment fragment = new SettUnitaryFilterFragment();
        transaction.replace(R.id.frame_layout, fragment, "Unitary Filter");
        transaction.commit();
    }
}