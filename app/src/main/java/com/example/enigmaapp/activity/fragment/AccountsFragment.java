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

import com.example.enigmaapp.R;
import com.example.enigmaapp.model.AccountsViewModel;
import com.example.enigmaapp.model.LoginViewModel;
import com.example.enigmaapp.ui.AccountsItemAdapter;
import com.example.enigmaapp.web.accounts.AccountsItemResult;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import static com.example.enigmaapp.activity.UserActivity.actionBar;


public class AccountsFragment extends Fragment {

    private FloatingActionButton addAccountFab;
    private View accountLayout;

    public AccountsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (actionBar != null) {
            actionBar.show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=  inflater.inflate(R.layout.fragment_accounts, container, false);

        // TODO: add back create action button after read only version
        // Move fo "New Account" screen:
//        addAccountFab = v.findViewById(R.id.account_create_fab);
//        addAccountFab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FragmentTransaction ft = getParentFragmentManager().beginTransaction();
//                NewAccountFragment frg = new NewAccountFragment();
//                ft.replace(R.id.frame_layout, frg, "New Account");
//                ft.commit();
//            }
//        });

        RecyclerView recyclerViewFiat = v.findViewById(R.id.bank_accounts_fiat_recycler_view);
        recyclerViewFiat.setLayoutManager(new LinearLayoutManager(getContext()));

        final AccountsItemAdapter fiatAdapter = new AccountsItemAdapter(requireContext(), true);
        recyclerViewFiat.setAdapter(fiatAdapter);

        AccountsViewModel viewModel = new ViewModelProvider(requireActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(AccountsViewModel.class);

        viewModel.getFiatAccounts().observe(requireActivity(), accountsItems -> fiatAdapter.submitList(accountsItems));

        fiatAdapter.setOnItemClickListener(accountsItem -> openAccountDetailsFragment(accountsItem));

        RecyclerView recyclerViewCrypto = v.findViewById(R.id.bank_accounts_crypto_recycler_view);
        recyclerViewCrypto.setLayoutManager((new LinearLayoutManager(getContext())));

        final AccountsItemAdapter cryptoAdapter = new AccountsItemAdapter(requireContext(), false);
        recyclerViewCrypto.setAdapter(cryptoAdapter);

        viewModel.getCryptoAccounts().observe(requireActivity(), accountsItems -> cryptoAdapter.submitList(accountsItems));


        cryptoAdapter.setOnItemClickListener(accountsItem -> {
            System.out.println("Item Clicked: " + accountsItem.getAccountName()
                    + " / Currency: " + accountsItem.getCurrency()
                    + " / CryptoCurrency: " + accountsItem.getCryptoCurrency());
            openAccountDetailsFragment(accountsItem);
        });

        LoginViewModel loginViewModel = new ViewModelProvider(requireActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(LoginViewModel.class);
        String token = loginViewModel.getCurrentUser().getToken();

        viewModel.fetchAccounts(token);

        return v;
    }

    private void openAccountDetailsFragment(AccountsItemResult accountsItem) {
        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        AccountDetailsFragment frg = AccountDetailsFragment.newInstance(accountsItem);
//        AccountDetailsFragment frg = new AccountDetailsFragment(accountsItem);
        ft.replace(R.id.frame_layout, frg, "Account Details");
        ft.commit();
    }
}