package com.example.enigmaapp.activity.fragment;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.enigmaapp.R;
import com.example.enigmaapp.model.AccountsViewModel;
import com.example.enigmaapp.model.UserViewModel;
import com.example.enigmaapp.ui.AccountsItemAdapter;
import com.example.enigmaapp.web.accounts.AccountsItemResult;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class AccountsFragment extends Fragment {

    private FloatingActionButton createAccountBtn;
    private View accountLayout;

    public AccountsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Show navbar on "Accounts" view:
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=  inflater.inflate(R.layout.fragment_accounts, container, false);

        // TODO: add back create action button after read only version
        // Move fo "New Account" screen:
//        createAccountBtn = v.findViewById(R.id.account_create_btn);
//        createAccountBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                NewAccountFragment fragment = new NewAccountFragment();
//                transaction.replace(R.id.frame_layout, fragment, "New Account");
//                transaction.commit();
//            }
//        });

        RecyclerView recyclerViewFiat = v.findViewById(R.id.bank_accounts_fiat_recycler_view);
        recyclerViewFiat.setLayoutManager(new LinearLayoutManager(getContext()));

        final AccountsItemAdapter fiatAdapter = new AccountsItemAdapter(requireContext(), true);
        recyclerViewFiat.setAdapter(fiatAdapter);

        AccountsViewModel viewModel = new ViewModelProvider(requireActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(AccountsViewModel.class);

        viewModel.getFiatAccounts().observe(requireActivity(), new Observer<List<AccountsItemResult>>() {
            @Override
            public void onChanged(List<AccountsItemResult> accountsItems) {
                fiatAdapter.submitList(accountsItems);
            }
        });

        fiatAdapter.setOnItemClickListener(new AccountsItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(AccountsItemResult accountsItem) {
                System.out.println("Item Clicked: " + accountsItem.getAccountName()
                        + " / Currency: " + accountsItem.getCurrency()
                        + " / CryptoCurrency: " + accountsItem.getCryptoCurrency());
                openAccountDetailsFragment(accountsItem);
            }
        });

        RecyclerView recyclerViewCrypto = v.findViewById(R.id.bank_accounts_crypto_recycler_view);
        recyclerViewCrypto.setLayoutManager((new LinearLayoutManager(getContext())));

        final AccountsItemAdapter cryptoAdapter = new AccountsItemAdapter(requireContext(), false);
        recyclerViewCrypto.setAdapter(cryptoAdapter);

        viewModel.getCryptoAccounts().observe(requireActivity(), new Observer<List<AccountsItemResult>>() {
            @Override
            public void onChanged(List<AccountsItemResult> accountsItems) {
                cryptoAdapter.submitList(accountsItems);
            }
        });


        cryptoAdapter.setOnItemClickListener(new AccountsItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(AccountsItemResult accountsItem) {
                System.out.println("Item Clicked: " + accountsItem.getAccountName()
                        + " / Currency: " + accountsItem.getCurrency()
                        + " / CryptoCurrency: " + accountsItem.getCryptoCurrency());
                openAccountDetailsFragment(accountsItem);
            }
        });

        UserViewModel userViewModel = new ViewModelProvider(requireActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(UserViewModel.class);
        String token = userViewModel.getCurrentUser().getToken();

        viewModel.fetchAccounts(token);

        return v;
    }

    private void openAccountDetailsFragment(AccountsItemResult accountsItem) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        AccountDetailsFragment fragment = new AccountDetailsFragment(accountsItem);
        transaction.replace(R.id.frame_layout, fragment, "Account Details");
        transaction.commit();
    }
}