package com.example.enigmaapp.activity.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.enigmaapp.R;
import com.example.enigmaapp.model.TradeViewModel;
import com.example.enigmaapp.model.UserViewModel;
import com.example.enigmaapp.ui.TradeItemAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static com.example.enigmaapp.activity.fragment.TradeFilterFragment.paramsToSend;
import static com.example.enigmaapp.activity.fragment.TradeFilterFragment.prefEditor;
import static com.example.enigmaapp.activity.fragment.TradeFilterFragment.resetPrefs;

public class TradeFragment extends Fragment {
    private FloatingActionButton createTradeBtn;
    private ImageView filterBtn;
    private ImageView uploadBtn;
    private ImageView refreshBtn;
    private View topSection;
    SharedPreferences prefs;

    public TradeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //        Show navbar on "Trade" view:
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        prefs = androidx.preference.PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_trade, container, false);

        // TODO: add back create action button after read only version
        // Move fo "New Trade" screen:
//        createTradeBtn = v.findViewById(R.id.trade_create_btn);
//        createTradeBtn.setOnClickListener(v12 -> openNewTradeFragment());

        // Move fo "Filter Trade" screen:
        filterBtn = v.findViewById(R.id.ic_action_filter);
        filterBtn.setOnClickListener(v1 -> openFilterTradeFragment());

        // Refresh "Trade" screen:
        refreshBtn = v.findViewById(R.id.ic_action_refresh);
        refreshBtn.setOnClickListener(v13 -> openTradeFragment());

        // Upload "Trade" screen:
        uploadBtn = v.findViewById(R.id.ic_action_upload);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: add upload process
            }
        });

        topSection = v.findViewById(R.id.layout_top_section);
        TextView fromDate = topSection.findViewById(R.id.trade_from_date);
        fromDate.setText(prefs.getString("startDateTradeFilter", "-"));
        TextView toDate = topSection.findViewById(R.id.trade_to_date);
        toDate.setText(prefs.getString("endDateTradeFilter", "-"));

        RecyclerView recyclerView = v.findViewById(R.id.trade_fragment_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setHasFixedSize(true);

        final TradeItemAdapter adapter = new TradeItemAdapter(requireContext());
        recyclerView.setAdapter(adapter);

        TradeViewModel viewModel = new ViewModelProvider(requireActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(TradeViewModel.class);

        viewModel.getTrades().observe(requireActivity(), tradeItems -> {
            adapter.submitList(tradeItems);
        });

        UserViewModel userViewModel = new ViewModelProvider(requireActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(UserViewModel.class);
        String token = userViewModel.getCurrentUser().getToken();

        viewModel.fetchTrades(token);

        return v;
    }

    private void openFilterTradeFragment() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        TradeFilterFragment fragment = new TradeFilterFragment();
        transaction.replace(R.id.frame_layout, fragment, "Filter Trade");
        transaction.commit();
    }

    private void openTradeFragment() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        TradeFragment fragment = new TradeFragment();
        transaction.replace(R.id.frame_layout, fragment, "Trade");
        transaction.commit();
    }

    // TODO: add back after read only version
    private void openNewTradeFragment() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        NewTradeCreationFragment fragment = new NewTradeCreationFragment();
        transaction.replace(R.id.frame_layout, fragment, "New Trade");
        transaction.commit();
    }
}