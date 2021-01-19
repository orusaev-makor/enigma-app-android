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

import static com.example.enigmaapp.activity.MainActivity.actionBar;

public class NewBatchSettFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private Button createBtn;
    private Button closeBtn;
    ArrayAdapter<String> counterpartyAdapter;
    ArrayAdapter<String> productAdapter;

    public NewBatchSettFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        actionBar.hide();
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