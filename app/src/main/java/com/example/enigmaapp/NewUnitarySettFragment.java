package com.example.enigmaapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewUnitarySettFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewUnitarySettFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private Button createBtn;
    private Button closeBtn;
    ArrayAdapter<String> onBehalfAdapter;
    ArrayAdapter<String> typeAdapter;
    ArrayAdapter<String> currencyAdapter;
    ArrayAdapter<String> accountAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NewUnitarySettFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewSettlementCreationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewUnitarySettFragment newInstance(String param1, String param2) {
        NewUnitarySettFragment fragment = new NewUnitarySettFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //        Hides navbar on "create settlement" view:
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_new_unitary_sett, container, false);

        Spinner spinnerOnBehalf = (Spinner) v.findViewById(R.id.new_unitary_spinner_on_behalf);
        String[] onBehalfOptions = {"Option 1", "Option 2", "On Behalf"}; // Last option is the hint!
        setOnBehalfAdapter(spinnerOnBehalf, onBehalfOptions);

        Spinner spinnerType = (Spinner) v.findViewById(R.id.new_unitary_spinner_type);
        String[] typeOptions = {"Type 1", "Type 2", "Type 3", "Type 4", "Type"}; // Last option is the hint!
        setTypeAdapter(spinnerType, typeOptions);

        Spinner spinnerCurrency = (Spinner) v.findViewById(R.id.new_unitary_spinner_currency);
        String[] currencyOptions = {"BTC", "ETH", "USD", "LTC", "Currency"}; // Last option is the hint!
        setCurrencyAdapter(spinnerCurrency, currencyOptions);

        Spinner spinnerAccount = (Spinner) v.findViewById(R.id.new_unitary_spinner_account);
        String[] accountOptions = {"Account 1", "Account 2", "Account"}; // Last option is the hint!
        setAccountAdapter(spinnerAccount, accountOptions);

        // Creating unitary settlement and return to "Settlement Fragment"screen
        createBtn = v.findViewById(R.id.new_unitary_create_btn);
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: add the creation process
                openSettlementScreen();
            }
        });

        // Close "Settlement creation" screen and go back to "Settlement Fragment":
        closeBtn = v.findViewById(R.id.new_settlement_close_btn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettlementScreen();
            }
        });

        return v;
    }

    private void openSettlementScreen() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        SettlementFragment fragment = new SettlementFragment();
        transaction.replace(R.id.frame_layout, fragment, "Settlement");
        transaction.commit();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // sets Text Colour after item selection:
        String spinnerName = parent.toString().substring(parent.toString().indexOf("app:id/"), parent.toString().length() - 1);

        System.out.println(" ++++++++++++++++++++++++++++++++++++++++ SPINNER NAMEEEEEEEEEEEEE ++++++++++++++++++++++++++++++++++++++++ " + spinnerName);
        switch (spinnerName) {
            case "app:id/new_unitary_spinner_on_behalf":
                if (position != onBehalfAdapter.getCount()) {
                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.textColor));
                }
                break;
            case "app:id/new_unitary_spinner_type":
                if (position != typeAdapter.getCount()) {
                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.textColor));
                }
                break;
                case "app:id/new_unitary_spinner_currency":
                if (position != currencyAdapter.getCount()) {
                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.textColor));
                }
                break;
                case "app:id/new_unitary_spinner_account":
                if (position != accountAdapter.getCount()) {
                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.textColor));
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void setAccountAdapter(Spinner spinnerAccount, String[] accountOptions) {
        accountAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_account) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    TextView textView = (TextView) v.findViewById(R.id.spinner_account);
                    textView.setText("");
                    textView.setHint(getItem(getCount())); //"Hint to be displayed"
                    textView.setHintTextColor(getResources().getColor(R.color.textSecondaryColor));
                }
                return v;
            }

            @Override
            public int getCount() {
                return super.getCount() - 1; // you don't display last item. It is used as hint.
            }
        };

        accountAdapter.setDropDownViewResource(R.layout.spinner_account_dropdown);
        for (int i = 0; i < accountOptions.length; i++) {
            accountAdapter.add(accountOptions[i]);
        }

        spinnerAccount.setAdapter(accountAdapter);
        spinnerAccount.setSelection(accountAdapter.getCount()); //set the hint the default selection so it appears on launch.
        spinnerAccount.setOnItemSelectedListener(this);
    }

    private void setCurrencyAdapter(Spinner spinnerCurrency, String[] currencyOptions) {
        currencyAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_currency) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    TextView textView = (TextView) v.findViewById(R.id.spinner_currency);
                    textView.setText("");
                    textView.setHint(getItem(getCount())); //"Hint to be displayed"
                    textView.setHintTextColor(getResources().getColor(R.color.textSecondaryColor));
                }
                return v;
            }

            @Override
            public int getCount() {
                return super.getCount() - 1; // you don't display last item. It is used as hint.
            }
        };

        currencyAdapter.setDropDownViewResource(R.layout.spinner_currency_dropdown);
        for (int i = 0; i < currencyOptions.length; i++) {
            currencyAdapter.add(currencyOptions[i]);
        }

        spinnerCurrency.setAdapter(currencyAdapter);
        spinnerCurrency.setSelection(currencyAdapter.getCount()); //set the hint the default selection so it appears on launch.
        spinnerCurrency.setOnItemSelectedListener(this);
    }

    private void setTypeAdapter(Spinner spinnerType, String[] typeOptions) {
        typeAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_type) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    TextView textView = (TextView) v.findViewById(R.id.spinner_type);
                    textView.setText("");
                    textView.setHint(getItem(getCount())); //"Hint to be displayed"
                    textView.setHintTextColor(getResources().getColor(R.color.textSecondaryColor));
                }
                return v;
            }

            @Override
            public int getCount() {
                return super.getCount() - 1; // you don't display last item. It is used as hint.
            }
        };

        typeAdapter.setDropDownViewResource(R.layout.spinner_type_dropdown);
        for (int i = 0; i < typeOptions.length; i++) {
            typeAdapter.add(typeOptions[i]);
        }

        spinnerType.setAdapter(typeAdapter);
        spinnerType.setSelection(typeAdapter.getCount()); //set the hint the default selection so it appears on launch.
        spinnerType.setOnItemSelectedListener(this);
    }

    private void setOnBehalfAdapter(Spinner spinnerOnBehalf, String[] onBehalfOptions) {
        onBehalfAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_on_behalf) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    TextView textView = (TextView) v.findViewById(R.id.spinner_on_behalf);
                    textView.setText("");
                    textView.setHint(getItem(getCount())); //"Hint to be displayed"
                    textView.setHintTextColor(getResources().getColor(R.color.textSecondaryColor));
                }
                return v;
            }

            @Override
            public int getCount() {
                return super.getCount() - 1; // you don't display last item. It is used as hint.
            }
        };

        onBehalfAdapter.setDropDownViewResource(R.layout.spinner_on_behalf_dropdown);
        for (int i = 0; i < onBehalfOptions.length; i++) {
            onBehalfAdapter.add(onBehalfOptions[i]);
        }

        spinnerOnBehalf.setAdapter(onBehalfAdapter);
        spinnerOnBehalf.setSelection(onBehalfAdapter.getCount()); //set the hint the default selection so it appears on launch.
        spinnerOnBehalf.setOnItemSelectedListener(this);
    }

}