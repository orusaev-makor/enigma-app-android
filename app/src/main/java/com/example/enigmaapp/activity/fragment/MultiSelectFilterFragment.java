package com.example.enigmaapp.activity.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.enigmaapp.R;
import com.example.enigmaapp.model.TradeFilterViewModel;
import com.example.enigmaapp.ui.TradeFilterAdapter;
import com.example.enigmaapp.web.trade.dataset.TradeDatasetResult;

public class MultiSelectFilterFragment extends Fragment {
    private String mFilterType;
    private TextView titleText;
    private TextView subtitleText;

    public MultiSelectFilterFragment(String filterType) {
        // Required empty public constructor
        this.mFilterType = filterType;
        System.out.println(" *--------**************************** ----------------------- ******************* " + this.mFilterType + " REcived " + filterType);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_multi_select_filter, container, false);
        titleText = v.findViewById(R.id.multi_select_title);
        titleText.setText(mFilterType.substring(0, 1).toUpperCase() + mFilterType.substring(1).toLowerCase());

        subtitleText = v.findViewById(R.id.multi_select_subtitle);
        subtitleText.setText("Select " + mFilterType + " to filter");

        RecyclerView recyclerView = v.findViewById(R.id.multi_select_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setHasFixedSize(true);

        final TradeFilterAdapter adapter = new TradeFilterAdapter();
        recyclerView.setAdapter(adapter);

        TradeFilterViewModel viewModel = new ViewModelProvider(requireActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(TradeFilterViewModel.class);

        viewModel.getTradeDataset().observe(requireActivity(), new Observer<TradeDatasetResult>() {
            @Override
            public void onChanged(TradeDatasetResult tradeDatasetResult) {
//                adapter
            }
        });

        return v;
    }
}