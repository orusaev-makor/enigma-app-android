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


public class NewTradeCreationFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private Button submitBtn;
    private Button closeBtn;
    ArrayAdapter<String> executionTypeAdapter;
    ArrayAdapter<String> productAdapter;
    ArrayAdapter<String> companyAdapter;
    ArrayAdapter<String> clientAdapter;
    ArrayAdapter<String> brokerAdapter;

    public NewTradeCreationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        actionBar.hide();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_new_trade_creation, container, false);
        Spinner spinnerExecutionType = (Spinner) v.findViewById(R.id.new_trade_spinner_execution);
        String[] executionTypeOptions = {"Type 1", "Type 2", "Execution Type"}; // Last option is the hint!
        setExecutionTypeAdapter(spinnerExecutionType, executionTypeOptions);

        Spinner spinnerProduct = (Spinner) v.findViewById(R.id.new_trade_spinner_product);
        String[] productOptions = {"Product 1", "Product 2", "Product"}; // Last option is the hint!
        setProductAdapter(spinnerProduct, productOptions);

        Spinner spinnerCompany = (Spinner) v.findViewById(R.id.new_trade_spinner_company);
        String[] companyOptions = {"Makor", "Enigma", "Company"}; // Last option is the hint!
        setCompanyAdapter(spinnerCompany, companyOptions);

        Spinner spinnerClient = (Spinner) v.findViewById(R.id.new_trade_spinner_client);
        String[] clientOptions = {"Client 1", "Client 2", "Client 3", "Client"}; // Last option is the hint!
        setClientAdapter(spinnerClient, clientOptions);

        Spinner spinnerBroker = (Spinner) v.findViewById(R.id.new_trade_spinner_broker);
        String[] brokerOptions = {"Broker 1", "Broker 2", "Broker 3", "Broker 4","Broker 5", "Broker"}; // Last option is the hint!
        setBrokerAdapter(spinnerBroker, brokerOptions);


        // Creating trade and moving to "Review & Submit Trade" screen
        submitBtn = v.findViewById(R.id.new_trade_submit_btn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: add the creation process
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                NewTradeReviewAndSubmitFragment frg = new NewTradeReviewAndSubmitFragment();
                ft.replace(R.id.frame_layout, frg, "New Trade Review and Submit");
                ft.commit();
            }
        });

        // Close "Trade creation" screen and go back to "Trade Fragment":
        closeBtn = v.findViewById(R.id.new_trade_close_btn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                TradeFragment frg = new TradeFragment();
                ft.replace(R.id.frame_layout, frg, "Trade");
                ft.commit();
            }
        });

        return v;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // sets Text Colour after item selection:
        String spinnerName = parent.toString().substring(parent.toString().indexOf("app:id/"), parent.toString().length() - 1);

        switch (spinnerName) {
            case "app:id/new_trade_spinner_execution":
                if (position != executionTypeAdapter.getCount()) {
                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.textColor));
                }
                break;
            case "app:id/new_trade_spinner_product":
                if (position != productAdapter.getCount()) {
                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.textColor));
                }
                break;
            case "app:id/new_trade_spinner_company":
                if (position != companyAdapter.getCount()) {
                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.textColor));
                }
                break;
            case "app:id/new_trade_spinner_client":
                if (position != clientAdapter.getCount()) {
                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.textColor));
                }
                break;
            case "app:id/new_trade_spinner_broker":
                if (position != brokerAdapter.getCount()) {
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


    private void setBrokerAdapter(Spinner spinnerBroker, String[] brokerOptions) {
        brokerAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_broker) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    TextView textView = (TextView) v.findViewById(R.id.spinner_broker);
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

        brokerAdapter.setDropDownViewResource(R.layout.spinner_broker_dropdown);
        for (int i = 0; i < brokerOptions.length; i++) {
            brokerAdapter.add(brokerOptions[i]);
        }

        spinnerBroker.setAdapter(brokerAdapter);
        spinnerBroker.setSelection(brokerAdapter.getCount()); //set the hint the default selection so it appears on launch.
        spinnerBroker.setOnItemSelectedListener(this);
    }

    private void setClientAdapter(Spinner spinnerClient, String[] clientOptions) {
        clientAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_client) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    TextView textView = (TextView) v.findViewById(R.id.spinner_client);
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

        clientAdapter.setDropDownViewResource(R.layout.spinner_client_dropdown);
        for (int i = 0; i < clientOptions.length; i++) {
            clientAdapter.add(clientOptions[i]);
        }

        spinnerClient.setAdapter(clientAdapter);
        spinnerClient.setSelection(clientAdapter.getCount()); //set the hint the default selection so it appears on launch.
        spinnerClient.setOnItemSelectedListener(this);
    }

    private void setCompanyAdapter(Spinner spinnerCompany, String[] companyOptions) {
        companyAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_company) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    TextView textView = (TextView) v.findViewById(R.id.spinner_company);
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

        companyAdapter.setDropDownViewResource(R.layout.spinner_company_dropdown);
        for (int i = 0; i < companyOptions.length; i++) {
            companyAdapter.add(companyOptions[i]);
        }

        spinnerCompany.setAdapter(companyAdapter);
        spinnerCompany.setSelection(companyAdapter.getCount()); //set the hint the default selection so it appears on launch.
        spinnerCompany.setOnItemSelectedListener(this);
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

    private void setExecutionTypeAdapter(Spinner spinner, String[] executionTypeOptions) {
        executionTypeAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_execution) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    TextView textView = (TextView) v.findViewById(R.id.spinner_execution);
                    textView.setText("");
                    textView.setHint(getItem(getCount())); //"Hint to be displayed"
                    textView.setHintTextColor(getResources().getColor(R.color.textSecondaryColor));
//                    textView.setDrawingCacheBackgroundColor(getResources().getColor(R.color.textSecondaryColor));
                }
                return v;
            }

            @Override
            public int getCount() {
                return super.getCount() - 1; // you don't display last item. It is used as hint.
            }
        };

        executionTypeAdapter.setDropDownViewResource(R.layout.spinner_execution_dropdown);
        for (int i = 0; i < executionTypeOptions.length; i++) {
            executionTypeAdapter.add(executionTypeOptions[i]);
        }
//        executionTypeAdapter.add("Type 1");
//        executionTypeAdapter.add("Type 2");
//        executionTypeAdapter.add("Execution Type"); //This is the text that will be displayed as hint.

        spinner.setAdapter(executionTypeAdapter);
        spinner.setSelection(executionTypeAdapter.getCount()); //set the hint the default selection so it appears on launch.
        spinner.setOnItemSelectedListener(this);
    }

}