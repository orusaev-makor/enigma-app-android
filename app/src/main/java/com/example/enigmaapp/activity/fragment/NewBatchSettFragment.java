package com.example.enigmaapp.activity.fragment;

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

import com.example.enigmaapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewBatchSettFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewBatchSettFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private Button createBtn;
    private Button closeBtn;
    ArrayAdapter<String> counterpartyAdapter;
    ArrayAdapter<String> productAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NewBatchSettFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewBatchSettFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewBatchSettFragment newInstance(String param1, String param2) {
        NewBatchSettFragment fragment = new NewBatchSettFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //        Hides navbar on "create settlement" view:
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_new_batch_sett, container, false);


        Spinner spinnerProduct = (Spinner) v.findViewById(R.id.new_batch_spinner_product);
        String[] productOptions = {"Product 1", "Product 2", "Product"}; // Last option is the hint!
        setProductAdapter(spinnerProduct, productOptions);

        Spinner spinnerCounterparty = (Spinner) v.findViewById(R.id.new_batch_spinner_counterparty);
        String[] counterpartyOptions = {"Counterparty 1", "Counterparty 2", "Counterparty"}; // Last option is the hint!
        setCounterpartyAdapter(spinnerCounterparty, counterpartyOptions);


        // Creating settlement and moving to "Select Trade" screen
        createBtn = v.findViewById(R.id.new_batch_create_btn);
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: add the creation process
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                SelectTradeFragment fragment = new SelectTradeFragment();
                transaction.replace(R.id.frame_layout, fragment, "Select Trade");
                transaction.commit();
            }
        });


        // Close "Settlement creation" screen and go back to "Settlement Fragment":
        closeBtn = v.findViewById(R.id.new_settlement_close_btn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                SettlementFragment fragment = new SettlementFragment(true);
                transaction.replace(R.id.frame_layout, fragment, "Settlement");
                transaction.commit();
            }
        });

        return v;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // sets Text Colour after item selection:
        String spinnerName = parent.toString().substring(parent.toString().indexOf("app:id/"), parent.toString().length() - 1);

        System.out.println(" ++++++++++++++++++++++++++++++++++++++++ SPINNER NAMEEEEEEEEEEEEE ++++++++++++++++++++++++++++++++++++++++ " + spinnerName);
        switch (spinnerName) {
            case "app:id/new_batch_spinner_product":
                if (position != productAdapter.getCount()) {
                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.textColor));
                }
                break;
            case "app:id/new_batch_spinner_counterparty":
                if (position != counterpartyAdapter.getCount()) {
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


    private void setCounterpartyAdapter(Spinner spinnerCounterparty, String[] counterpartyOptions) {
        counterpartyAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_counterparty) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    TextView textView = (TextView) v.findViewById(R.id.spinner_counterparty);
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

        counterpartyAdapter.setDropDownViewResource(R.layout.spinner_counterparty_dropdown);
        for (int i = 0; i < counterpartyOptions.length; i++) {
            counterpartyAdapter.add(counterpartyOptions[i]);
        }

        spinnerCounterparty.setAdapter(counterpartyAdapter);
        spinnerCounterparty.setSelection(counterpartyAdapter.getCount()); //set the hint the default selection so it appears on launch.
        spinnerCounterparty.setOnItemSelectedListener(this);
    }


    private void setProductAdapter(Spinner spinnerProduct, String[] productOptions) {
        productAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_product) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    TextView textView = (TextView) v.findViewById(R.id.spinner_product);
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

        productAdapter.setDropDownViewResource(R.layout.spinner_product_dropdown);
        for (int i = 0; i < productOptions.length; i++) {
            productAdapter.add(productOptions[i]);
        }

        spinnerProduct.setAdapter(productAdapter);
        spinnerProduct.setSelection(productAdapter.getCount()); //set the hint the default selection so it appears on launch.
        spinnerProduct.setOnItemSelectedListener(this);
    }

}