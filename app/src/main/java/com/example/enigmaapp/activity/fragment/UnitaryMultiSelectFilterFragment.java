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
import com.example.enigmaapp.ui.CounterpartyFilterMultiAdapter;
import com.example.enigmaapp.ui.CurrencyFilterMultiAdapter;
import com.example.enigmaapp.web.dataset.DatasetCounterparty;
import com.example.enigmaapp.web.dataset.DatasetCurrency;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.enigmaapp.activity.fragment.UnitaryFilterFragment.clearCounterpartiesText;
import static com.example.enigmaapp.activity.fragment.UnitaryFilterFragment.clearCurrenciesText;
import static com.example.enigmaapp.activity.fragment.UnitaryFilterFragment.clickedCounterparties;
import static com.example.enigmaapp.activity.fragment.UnitaryFilterFragment.clickedCurrencies;
import static com.example.enigmaapp.activity.fragment.UnitaryFilterFragment.counterpartyStringBuilder;
import static com.example.enigmaapp.activity.fragment.UnitaryFilterFragment.removeFromUnitaryParams;
import static com.example.enigmaapp.activity.fragment.UnitaryFilterFragment.setUnitaryFilterParams;
import static com.example.enigmaapp.activity.fragment.UnitaryFilterFragment.currencyStringBuilder;

public class UnitaryMultiSelectFilterFragment extends Fragment {

    private String mFilterType;
    private TextView titleText;
    private TextView subtitleText;
    private SharedPreferences.Editor prefEditor;
    private SharedPreferences prefs;
    private MaterialButton resetBtn;
    private Button submitBtn;
    private HashMap<String, String> params = new HashMap<>();

    private SettlementViewModel viewModel;
    private static CurrencyFilterMultiAdapter currencyAdapter;
    public static CounterpartyFilterMultiAdapter counterpartyAdapter;

    public UnitaryMultiSelectFilterFragment(String filterType) {
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

        viewModel = new ViewModelProvider(requireActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(SettlementViewModel.class);

        switch (mFilterType) {
            case "currency":
                ArrayList<DatasetCurrency> currencyData = viewModel.getCurrencyDataset();
                currencyAdapter = new CurrencyFilterMultiAdapter(requireActivity(), currencyData);
                currencyAdapter.setCurrencies(currencyData);
                recyclerView.setAdapter(currencyAdapter);

                currencyAdapter.setOnItemClickListener(new CurrencyFilterMultiAdapter.OnItemClickListener() {
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
                break;

            case "counterparty":
                ArrayList<DatasetCounterparty> counterpartyData = viewModel.getCounterpartyDataset();
                counterpartyAdapter = new CounterpartyFilterMultiAdapter(requireActivity(), counterpartyData);
                counterpartyAdapter.setCounterparties(counterpartyData);
                recyclerView.setAdapter(counterpartyAdapter);

                counterpartyAdapter.setOnItemClickListener(new CounterpartyFilterMultiAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(DatasetCounterparty counterpartyItem, int position) {
                        System.out.println("unitary clicked : " + counterpartyItem.getName());
                        if (counterpartyItem.getIsChecked()) {
                            counterpartyItem.setIsChecked(false);
                            int idx = clickedCounterparties.indexOf(counterpartyItem.getId());
                            clickedCounterparties.remove(idx);
                        } else {
                            counterpartyItem.setIsChecked(true);
                            clickedCounterparties.add(counterpartyItem.getId());
                        }
                        counterpartyAdapter.notifyDataSetChanged();
                    }
                });

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
                clearCurrencyAdapterList();
                for (int i = 0; i < clickedCurrencies.size(); i++) {
                    removeFromUnitaryParams("currency_list[" + i + "]");
                    viewModel.removeFromUnitaryParams("currency_list[" + i + "]");
                }
                clickedCurrencies.clear();
                break;
            case "counterparty":
                clearCounterpartiesText();
                clearCounterpartiesAdapterList();
                for (int i = 0; i < clickedCounterparties.size(); i++) {
                    removeFromUnitaryParams("counterparty_id_list[" + i + "]");
                    viewModel.removeFromUnitaryParams("counterparty_id_list[" + i + "]");
                }
                clickedCounterparties.clear();
            default:
                break;
        }
    }

    public static void clearCurrencyAdapterList() {
        if (currencyAdapter != null) {
            currencyAdapter.clearSelected();
        }
    }

    public static void clearCounterpartiesAdapterList() {
        if (counterpartyAdapter != null) {
            counterpartyAdapter.clearSelected();
        }
    }

    private void buildCurrencyString() {
        currencyStringBuilder = new StringBuilder();
        if (currencyAdapter.getSelected().size() > 0) {
            for (int i = 0; i < currencyAdapter.getSelected().size(); i++) {
                currencyStringBuilder.append(currencyAdapter.getSelected().get(i).getName());
                if (i == currencyAdapter.getSelected().size() - 1) break;
                currencyStringBuilder.append("\n");
            }
        } else {
            currencyStringBuilder.replace(0, currencyStringBuilder.length(), "");
        }
    }

    private void buildCounterpartyString() {
        counterpartyStringBuilder = new StringBuilder();
        if (counterpartyAdapter.getSelected().size() > 0) {
            for (int i = 0; i < counterpartyAdapter.getSelected().size(); i++) {
                counterpartyStringBuilder.append(counterpartyAdapter.getSelected().get(i).getName());
                if (i == counterpartyAdapter.getSelected().size() - 1) break;
                counterpartyStringBuilder.append("\n");
            }
        } else {
            counterpartyStringBuilder.replace(0, counterpartyStringBuilder.length(), "");
        }
    }

    private void resetParam() {
        switch (mFilterType) {
            case "counterparty":
                for (int i = 0; i < params.size(); i++) {
                    removeFromUnitaryParams("counterparty_id_list[" + i + "]");
                    viewModel.removeFromUnitaryParams("counterparty_id_list[" + i + "]");
                }
                break;
            case "currency":
                for (int i = 0; i < params.size(); i++) {
                    removeFromUnitaryParams("currency_list[" + i + "]");
                    viewModel.removeFromUnitaryParams("currency_list[" + i + "]");
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

        UserViewModel userViewModel = new ViewModelProvider(requireActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(UserViewModel.class);
        String token = userViewModel.getCurrentUser().getToken();

        viewModel.fetchUnitaryDataset(token);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        UnitaryMultiSelectFilterFragment fragment = new UnitaryMultiSelectFilterFragment(type);
        transaction.replace(R.id.frame_layout, fragment, "Unitary Select Filter List");
        transaction.commit();
    }

    private void openUnitaryFilterScreen() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        UnitaryFilterFragment fragment = new UnitaryFilterFragment();
        transaction.replace(R.id.frame_layout, fragment, "Unitary Filter");
        transaction.commit();
    }
}