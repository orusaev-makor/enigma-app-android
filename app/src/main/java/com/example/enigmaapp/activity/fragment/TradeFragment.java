package com.example.enigmaapp.activity.fragment;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.enigmaapp.R;
import com.example.enigmaapp.model.TradeViewModel;
import com.example.enigmaapp.model.UserViewModel;
import com.example.enigmaapp.ui.TradeItemAdapter;
import com.example.enigmaapp.web.trade.TradeItemResult;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.enigmaapp.activity.fragment.TradeFilterFragment.getTodayDate;

public class TradeFragment extends Fragment {
    private FloatingActionButton createTradeBtn;
    private ImageView filterBtn;
    private ImageView uploadBtn;
    private ImageView refreshBtn;
    private View topSection;
    private int page = 1;
    public static ProgressBar progressBarTrade;
    public static TradeItemAdapter tradeAdapter;
    private NestedScrollView nestedScrollView;
    private RecyclerView recyclerView;
    private HashMap<String, String> pageParams = new HashMap<>();
    SharedPreferences prefs;


    public static int mTradeExpandedPosition = -1;
    public static int previousTradeExpandedPosition = -1;

    public TradeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        resetExpendedItemPos();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Show navbar on "Trade" view:
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

        topSection = v.findViewById(R.id.layout_trade_top_section);
        TextView fromDate = topSection.findViewById(R.id.trade_from_date);
        fromDate.setText(prefs.getString("startDateTradeFilter", "-"));
        TextView toDate = topSection.findViewById(R.id.trade_to_date);
        toDate.setText(prefs.getString("endDateTradeFilter", getTodayDate()));

        UserViewModel userViewModel = new ViewModelProvider(requireActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(UserViewModel.class);
        String token = userViewModel.getCurrentUser().getToken();

        TradeViewModel viewModel = new ViewModelProvider(requireActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(TradeViewModel.class);

        pageParams.put("current_page", String.valueOf(page));
        if (page == 1) {
            viewModel.resetTradesList();
        }
        viewModel.setParams(pageParams);
        viewModel.fetchTrades(token);

        nestedScrollView = v.findViewById(R.id.scroll_trade);
        progressBarTrade = v.findViewById(R.id.progress_bar_trade);
        recyclerView = v.findViewById(R.id.trade_fragment_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ArrayList<TradeItemResult> data = viewModel.getTrades();
        tradeAdapter = new TradeItemAdapter(requireContext(), data);
        recyclerView.setAdapter(tradeAdapter);

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // check if scrolled till bottom
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    // when reach last item position
                    page++;
                    pageParams.put("current_page", String.valueOf(page));
                    viewModel.setParams(pageParams);
                    progressBarTrade.setVisibility(View.VISIBLE);
                    viewModel.fetchTrades(token);
                }
            }
        });
        return v;
    }


    private void resetExpendedItemPos() {
        mTradeExpandedPosition = -1;
        previousTradeExpandedPosition = -1;
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