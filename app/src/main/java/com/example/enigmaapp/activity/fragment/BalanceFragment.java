package com.example.enigmaapp.activity.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.enigmaapp.R;
import com.example.enigmaapp.model.BalanceViewModel;
import com.example.enigmaapp.model.UserViewModel;
import com.example.enigmaapp.ui.BalanceItemAdapter;
import com.example.enigmaapp.web.balance.BalanceItemResult;
import com.example.enigmaapp.web.login.LoginResult;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BalanceFragment extends Fragment {
    private PieChart balanceChart;

    public BalanceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Show navbar on "Balance" view:
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_balance, container, false);

        final Typeface tfRegular = ResourcesCompat.getFont(getContext(), R.font.poppins_regular);

        UserViewModel userViewModel = new ViewModelProvider(requireActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(UserViewModel.class);

        String username = userViewModel.getCurrentUser().getUsername();
        TextView userGreeting = v.findViewById(R.id.user_greeting_text);
        userGreeting.setText("Hello, " + username);

        TextView coinNameText = v.findViewById(R.id.clicked_coin_name);
        ImageView coinIcon = v.findViewById(R.id.clicked_coin_icon);

        balanceChart = (PieChart) v.findViewById(R.id.balancePieChart);
        balanceChart.setUsePercentValues(true);
        balanceChart.getDescription().setEnabled(false);
        balanceChart.setExtraOffsets(5, 10, 5, 5);
        balanceChart.setDragDecelerationFrictionCoef(0.15f);

        balanceChart.setHoleColor(getResources().getColor(R.color.colorPrimaryDark));
        balanceChart.setCenterTextTypeface(tfRegular);
        balanceChart.setDrawCenterText(true);
        balanceChart.setDrawEntryLabels(false);

        ArrayList<PieEntry> yValues = new ArrayList<>();

        yValues.add(new PieEntry(22f, "LTC", 0));
        yValues.add(new PieEntry(11f, "ETH", 1));
        yValues.add(new PieEntry(56f, "BTC", 2));
        yValues.add(new PieEntry(11f, "ZD token", 3));

        // animate pie on creation:
//        pieChart.animateY(1000);

        PieDataSet dataSet = new PieDataSet(yValues, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(new int[]{
                Color.rgb(230, 32, 26),
                Color.rgb(49, 93, 158),
                Color.rgb(251, 167, 70),
                Color.rgb(139, 147, 180),
                Color.rgb(70, 117, 69),
                Color.rgb(187, 160, 37)
        });

        PieData data = new PieData(dataSet);
        data.setDrawValues(false); // disable values in pie pieces
//        data.setValueTextSize(10f);
//        data.setValueTextColor(Color.WHITE);
//        data.setValueFormatter(new PercentFormatter(balanceChart));

        Legend l = balanceChart.getLegend();
        l.setEnabled(false); // disable legends


        balanceChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            @Override
            public void onValueSelected(Entry e, Highlight h) {
                PieEntry pe = (PieEntry) e;
                int index = (Integer) e.getData();
                int currentColor = dataSet.getColor(index);
//                System.out.println("GET DATA:  " + e.getData());
//                System.out.println("h: " + h);
//                System.out.println("e: " + e);

                coinNameText.setText(pe.getLabel());
                coinNameText.setTextColor(currentColor);
                coinIcon.setImageResource(R.drawable.btc);
                coinIcon.setColorFilter(currentColor);

                // set percentage in center of the chart pie
                String value = e.getY() + "%";
                balanceChart.setCenterText(value);
                balanceChart.setCenterTextColor(currentColor);
                balanceChart.setCenterTextSize(20f);
            }

            @Override
            public void onNothingSelected() {
                balanceChart.setCenterText("");
                coinNameText.setText("");
                coinIcon.setImageResource(0);
            }
        });

        balanceChart.setData(data);

        RecyclerView recyclerView = v.findViewById(R.id.balance_recycled_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        final BalanceItemAdapter balanceAdapter = new BalanceItemAdapter(requireContext());
        recyclerView.setAdapter(balanceAdapter);

        BalanceViewModel viewModel = new ViewModelProvider(requireActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(BalanceViewModel.class);

        viewModel.getBalanceMap().observe(requireActivity(), new Observer<List<BalanceItemResult>>() {
            @Override
            public void onChanged(List<BalanceItemResult> balanceItemResults) {
                System.out.println("in on change - balanceItemResults : " + balanceItemResults);
                balanceAdapter.submitList(balanceItemResults);
            }
        });

        String token = userViewModel.getCurrentUser().getToken();

        viewModel.fetchBalances(token);

        return v;
    }
}