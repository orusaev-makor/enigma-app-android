package com.example.enigmaapp.activity.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
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
import com.example.enigmaapp.ui.CounterpartyFilterMultiAdapter;
import com.example.enigmaapp.ui.CurrencyFilterMultiAdapter;
import com.example.enigmaapp.web.dataset.Currency;
import com.google.android.material.button.MaterialButton;

import java.util.HashMap;
import java.util.List;

import static com.example.enigmaapp.activity.fragment.UnitaryFilterFragment.clearCounterpartiesText;
import static com.example.enigmaapp.activity.fragment.UnitaryFilterFragment.clearCurrenciesText;
import static com.example.enigmaapp.activity.fragment.SettlementFragment.clickedCounterparties;
import static com.example.enigmaapp.activity.fragment.SettlementFragment.clickedCurrencies;
import static com.example.enigmaapp.activity.fragment.SettlementFragment.counterpartyStringBuilder;
//import static com.example.enigmaapp.activity.fragment.UnitaryFilterFragment.setUnitaryFilterParams;
import static com.example.enigmaapp.activity.fragment.SettlementFragment.currencyStringBuilder;

public class UnitaryMultiSelectFilterFragment extends Fragment {

    private String mFilterType;
    private TextView titleText, subtitleText;
    private MaterialButton resetBtn;
    private Button submitBtn;
    private HashMap<String, String> params = new HashMap<>();

    private SettlementViewModel settlementViewModel;
    public static CounterpartyFilterMultiAdapter counterpartyAdapter;

    public UnitaryMultiSelectFilterFragment(String filterType) {
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
        View v = inflater.inflate(R.layout.fragment_unitary_select_filter, container, false);
        titleText = v.findViewById(R.id.unitary_select_title);
        titleText.setText(mFilterType.substring(0, 1).toUpperCase() + mFilterType.substring(1).toLowerCase());

        subtitleText = v.findViewById(R.id.unitary_select_subtitle);
        subtitleText.setText("Select " + mFilterType + " to filter");

        // Submit chosen filters and go back to "Filter Unitary" screen
        submitBtn = v.findViewById(R.id.unitary_select_submit_btn);
        submitBtn.setOnClickListener(v1 -> {
            buildItemsString();
            openUnitaryFilterScreen();
        });

        RecyclerView recyclerView = v.findViewById(R.id.unitary_select_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        settlementViewModel = new ViewModelProvider(requireActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(SettlementViewModel.class);

        switch (mFilterType) {

            case "currency":
                final CurrencyFilterMultiAdapter currencyAdapter = new CurrencyFilterMultiAdapter(requireActivity());
                recyclerView.setAdapter(currencyAdapter);
                LiveData<List<Currency>> currencyData = settlementViewModel.getAllCurrencies();
                currencyData.observe(getViewLifecycleOwner(), currencies -> currencyAdapter.submitList(currencies));
                currencyAdapter.setOnItemClickListener((currencyItem, position) -> {
                    if (currencyItem.getIsChecked()) {
                        currencyItem.setIsChecked(false);
                        int idx = clickedCurrencies.indexOf(currencyItem.getName());
                        clickedCurrencies.remove(idx);
                    } else {
                        currencyItem.setIsChecked(true);
                        clickedCurrencies.add(currencyItem.getName());
                        System.out.println("_______________ clickedCurrencies _______________ : " + clickedCurrencies);
                    }
                    currencyAdapter.notifyDataSetChanged();
                });

                break;

            // TODO: setting this up after counterparty is back in dataset
            case "counterparty":
//                ArrayList<DatasetCounterparty> counterpartyData = viewModel.getCounterpartyDataset();
//                counterpartyAdapter = new CounterpartyFilterMultiAdapter(requireActivity(), counterpartyData);
//                counterpartyAdapter.setCounterparties(counterpartyData);
//                recyclerView.setAdapter(counterpartyAdapter);
//
//                counterpartyAdapter.setOnItemClickListener(new CounterpartyFilterMultiAdapter.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(DatasetCounterparty counterpartyItem, int position) {
//                        if (counterpartyItem.getIsChecked()) {
//                            counterpartyItem.setIsChecked(false);
//                            int idx = clickedCounterparties.indexOf(counterpartyItem.getId());
//                            clickedCounterparties.remove(idx);
//                        } else {
//                            counterpartyItem.setIsChecked(true);
//                            clickedCounterparties.add(counterpartyItem.getId());
//                        }
//                        counterpartyAdapter.notifyDataSetChanged();
//                    }
//                });

                break;

            default:
                break;
        }

        // Reset "Filter List" screen
        resetBtn = v.findViewById(R.id.unitary_select_reset_btn);
        resetBtn.setOnClickListener(v12 -> {
            openUnitarySelectFilter(mFilterType);
            resetParam();
            clearClickedList();
            cleartStringText();
        });

        return v;
    }

    private void buildItemsString() {
        if (mFilterType.equals("currency")) {
            buildCurrencyString();
        } else {
            buildCounterpartyString();
        }
    }

    private void buildCurrencyString() {
        currencyStringBuilder = new StringBuilder();
        if (clickedCurrencies.size() > 0) {
            for (int i = 0; i < clickedCurrencies.size(); i++) {
                currencyStringBuilder.append(clickedCurrencies.get(i));
                if (i == clickedCurrencies.size() - 1) break;
                currencyStringBuilder.append("\n");
            }
        } else {
            currencyStringBuilder.replace(0, currencyStringBuilder.length(), "");
        }
    }

    private void buildCounterpartyString() {
        counterpartyStringBuilder = new StringBuilder();
        if (clickedCounterparties.size() > 0) {
            for (int i = 0; i < clickedCounterparties.size(); i++) {
                counterpartyStringBuilder.append(clickedCounterparties.get(i));
                if (i == clickedCounterparties.size() - 1) break;
                counterpartyStringBuilder.append("\n");
            }
        } else {
            counterpartyStringBuilder.replace(0, counterpartyStringBuilder.length(), "");
        }
    }

    private void resetParam() {
        switch (mFilterType) {
            case "counterparty":
                settlementViewModel.removeFromUnitaryParamsContainsKey("counterparty_id_list");
                break;
            case "currency":
                settlementViewModel.removeFromUnitaryParamsContainsKey("currency_list");
                break;
            default:
                break;
        }
    }

    private void clearClickedList() {
        switch (mFilterType) {
            case "currency":
                clickedCurrencies.clear();
                break;
            case "counterparty":
                clickedCounterparties.clear();
            default:
                break;
        }
    }

    private void cleartStringText() {
        switch (mFilterType) {
            case "currency":
                clearCurrenciesText();
                break;
            case "counterparty":
                clearCounterpartiesText();
            default:
                break;
        }
    }

    private void openUnitarySelectFilter(String type) {
        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        UnitaryMultiSelectFilterFragment frg = new UnitaryMultiSelectFilterFragment(type);
        ft.replace(R.id.frame_layout, frg, "Unitary Select Filter List");
        ft.commit();
    }

    private void openUnitaryFilterScreen() {
        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        UnitaryFilterFragment frg = new UnitaryFilterFragment();
        ft.replace(R.id.frame_layout, frg, "Unitary Filter");
        ft.commit();
    }
}