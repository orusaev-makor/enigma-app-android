package com.example.enigmaapp.activity.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.enigmaapp.R;
import com.example.enigmaapp.web.accounts.AccountResult;
import com.example.enigmaapp.web.accounts.AccountsItemResult;

import java.io.Serializable;

public class AccountDetailsFragment extends Fragment {
    private static final String ACCOUNT_ITEM__KEY = "accounts_item_key";
    private AccountsItemResult accountsItem;
    private TextView accountName, accountType, accountDetails, accountCurrency;
    private Button submitBtn;

    public AccountDetailsFragment() {
        // Required empty public constructor
    }

    public static AccountDetailsFragment newInstance(AccountsItemResult accountsItem) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ACCOUNT_ITEM__KEY, accountsItem);
        AccountDetailsFragment f = new AccountDetailsFragment();
        f.setArguments(bundle);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        accountsItem = (AccountsItemResult) getArguments().getSerializable(
                ACCOUNT_ITEM__KEY);

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_account_details, container, false);

        accountName = v.findViewById(R.id.account_details_name);
        accountName.setText(accountsItem.getAccountName());

        accountType = v.findViewById(R.id.account_details_account_type);
        accountType.setText(accountsItem.getAccountType());

        accountDetails = v.findViewById(R.id.account_details_details);
        String details = "No details available";
        if (accountsItem.getAccountDetails() != null) {
            details = accountsItem.getAccountDetails();
        }
        accountDetails.setText(details);


        accountCurrency = v.findViewById(R.id.account_details_currency);
        String currency = "";
        if (accountsItem.getCurrency() != null) {
            currency = accountsItem.getCurrency();
        } else {
            currency = accountsItem.getCryptoCurrency();
        }
        accountCurrency.setText(currency);

        // Editing account and go back to "Accounts" screen
        submitBtn = v.findViewById(R.id.account_details_submit_btn);
        submitBtn.setOnClickListener(v1 -> {
            // TODO: add the edit process
            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            AccountsFragment frg = new AccountsFragment();
            ft.replace(R.id.frame_layout, frg, "Accounts");
            ft.commit();
        });

        return v;
    }
}