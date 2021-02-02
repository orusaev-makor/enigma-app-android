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
import com.example.enigmaapp.model.LoginViewModel;
import com.example.enigmaapp.ui.CounterpartyFilterMultiAdapter;
import com.example.enigmaapp.ui.CurrencyFilterMultiAdapter;
import com.example.enigmaapp.web.dataset.Currency;
import com.google.android.material.button.MaterialButton;

import java.util.HashMap;
import java.util.List;

import static com.example.enigmaapp.activity.fragment.UnitaryFilterFragment.clearCounterpartiesText;
import static com.example.enigmaapp.activity.fragment.UnitaryFilterFragment.clearCurrenciesText;
import static com.example.enigmaapp.activity.fragment.UnitaryFilterFragment.clickedCounterparties;
import static com.example.enigmaapp.activity.fragment.UnitaryFilterFragment.clickedCurrencies;
import static com.example.enigmaapp.activity.fragment.UnitaryFilterFragment.counterpartyStringBuilder;
import static com.example.enigmaapp.activity.fragment.UnitaryFilterFragment.removeFromUnitaryParams;
import static com.example.enigmaapp.activity.fragment.UnitaryFilterFragment.removeFromUnitaryParamsContainsKey;
import static com.example.enigmaapp.activity.fragment.UnitaryFilterFragment.setUnitaryFilterParams;
import static com.example.enigmaapp.activity.fragment.UnitaryFilterFragment.currencyStringBuilder;

public class UnitaryMultiSelectFilterFragment extends Fragment {

    private String mFilterType;
    private TextView titleText;
    private TextView subtitleText;
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
            openUnitaryFilterScreen();

            // reset existing params in repo before submitting:
            String containKey = mFilterType.equals("currency") ? "currency_list" : "counterparty_id_list";
            settlementViewModel.removeFromUnitaryParamsContainsKey(containKey);
            removeFromUnitaryParamsContainsKey(containKey);

            if (mFilterType.equals("currency")) {
                buildCurrencyString();
                for (int i = 0; i < clickedCurrencies.size(); i++) {
                    params.put("currency_list[" + i + "]", clickedCurrencies.get(i));
                }
            }
            if (mFilterType.equals("counterparty")) {
                buildCounterpartyString();
                for (int i = 0; i < clickedCounterparties.size(); i++) {
                    params.put("counterparty_id_list[" + i + "]", clickedCounterparties.get(i));
                }
            }
            setUnitaryFilterParams(params);
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
                currencyData.observe(getViewLifecycleOwner(), new Observer<List<Currency>>() {
                    @Override
                    public void onChanged(List<Currency> currencies) {
                        currencyAdapter.submitList(currencies);
                    }
                });
                currencyAdapter.setOnItemClickListener(new CurrencyFilterMultiAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Currency currencyItem, int position) {
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

                break;

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
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUnitarySelectFilter(mFilterType);
                resetParam();
                params.clear();
                clearClickedLists();
            }
        });

        return v;
    }

    private void clearClickedLists() {
        switch (mFilterType) {
            case "currency":
                clearCurrenciesText();
//                clearCurrencyAdapterList();
                for (int i = 0; i < clickedCurrencies.size(); i++) {
                    removeFromUnitaryParams("currency_list[" + i + "]");
                    settlementViewModel.removeFromUnitaryParams("currency_list[" + i + "]");
                }
                clickedCurrencies.clear();
                break;
            case "counterparty":
                clearCounterpartiesText();
                clearCounterpartiesAdapterList();
                for (int i = 0; i < clickedCounterparties.size(); i++) {
                    removeFromUnitaryParams("counterparty_id_list[" + i + "]");
                    settlementViewModel.removeFromUnitaryParams("counterparty_id_list[" + i + "]");
                }
                clickedCounterparties.clear();
            default:
                break;
        }
    }

    public static void clearCounterpartiesAdapterList() {
        if (counterpartyAdapter != null) {
            counterpartyAdapter.clearSelected();
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
         // TODO: add back after counterparty is updated in the datasets again....
//        counterpartyStringBuilder = new StringBuilder();
//        if (counterpartyAdapter.getSelected().size() > 0) {
//            for (int i = 0; i < counterpartyAdapter.getSelected().size(); i++) {
//                counterpartyStringBuilder.append(counterpartyAdapter.getSelected().get(i).getName());
//                if (i == counterpartyAdapter.getSelected().size() - 1) break;
//                counterpartyStringBuilder.append("\n");
//            }
//        } else {
//            counterpartyStringBuilder.replace(0, counterpartyStringBuilder.length(), "");
//        }
    }

    private void resetParam() {
        switch (mFilterType) {
            case "counterparty":
                for (int i = 0; i < params.size(); i++) {
                    removeFromUnitaryParams("counterparty_id_list[" + i + "]");
                    settlementViewModel.removeFromUnitaryParams("counterparty_id_list[" + i + "]");
                }
                break;
            case "currency":
                for (int i = 0; i < params.size(); i++) {
                    removeFromUnitaryParams("currency_list[" + i + "]");
                    settlementViewModel.removeFromUnitaryParams("currency_list[" + i + "]");
                }
                break;
            default:
                break;
        }
    }

    private void openUnitarySelectFilter(String type) {
        SettlementViewModel viewModel = new ViewModelProvider(requireActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(SettlementViewModel.class);

        LoginViewModel loginViewModel = new ViewModelProvider(requireActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(LoginViewModel.class);
        String token = loginViewModel.getCurrentUser().getToken();

//        viewModel.fetchUnitaryDataset(token);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        UnitaryMultiSelectFilterFragment frg = new UnitaryMultiSelectFilterFragment(type);
        ft.replace(R.id.frame_layout, frg, "Unitary Select Filter List");
        ft.commit();
    }

    private void openUnitaryFilterScreen() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        UnitaryFilterFragment frg = new UnitaryFilterFragment();
        ft.replace(R.id.frame_layout, frg, "Unitary Filter");
        ft.commit();
    }
}