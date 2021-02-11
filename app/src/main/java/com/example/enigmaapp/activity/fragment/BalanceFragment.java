package com.example.enigmaapp.activity.fragment;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
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
import com.example.enigmaapp.model.LoginViewModel;
import com.example.enigmaapp.ui.BalanceItemAdapter;
import com.example.enigmaapp.web.balance.BalanceItemResult;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class BalanceFragment extends Fragment {
    private PieChart balanceChart;
//    private final ArrayList<PieEntry> yValues = new ArrayList<>();
    private TextView coinNameText;
    private ImageView coinIcon;
    private float total_values = 0;
    private DecimalFormat decim = new DecimalFormat("#,##0.00");
    private Context context;

    public BalanceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = requireContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_balance, container, false);

        final Typeface tfRegular = ResourcesCompat.getFont(getContext(), R.font.poppins_regular);

        LoginViewModel loginViewModel = new ViewModelProvider(requireActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(LoginViewModel.class);

        String username = loginViewModel.getCurrentUser().getUsername();
        TextView userGreeting = v.findViewById(R.id.user_greeting_text);
        userGreeting.setText("Hello, " + username);

        coinNameText = v.findViewById(R.id.clicked_coin_name);
        coinIcon = v.findViewById(R.id.clicked_coin_icon);

        balanceChart = v.findViewById(R.id.balancePieChart);
        balanceChart.setVisibility(View.INVISIBLE);
        balanceChart.setUsePercentValues(true);
        balanceChart.getDescription().setEnabled(false);
        balanceChart.setExtraOffsets(5, 10, 5, 5);
        balanceChart.setDragDecelerationFrictionCoef(0.15f);

        balanceChart.setHoleColor(getResources().getColor(R.color.colorPrimaryDark));
        balanceChart.setCenterTextTypeface(tfRegular);
        balanceChart.setDrawCenterText(true);
        balanceChart.setDrawEntryLabels(false);

        RecyclerView recyclerView = v.findViewById(R.id.balance_recycled_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        final BalanceItemAdapter balanceAdapter = new BalanceItemAdapter(requireContext());
        recyclerView.setAdapter(balanceAdapter);

        BalanceViewModel viewModel = new ViewModelProvider(requireActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(BalanceViewModel.class);

        viewModel.getBalanceMap().observe(requireActivity(), balanceItemResults -> {
            balanceAdapter.submitList(balanceItemResults);

            // must init values here to avoid duplication of data on device rotation:
            final ArrayList<PieEntry> yValues = new ArrayList<>();
            addDataToChart(yValues, balanceItemResults);
            setChart(yValues);
        });

        String token = loginViewModel.getCurrentUser().getToken();

        viewModel.fetchBalances(token);

        return v;
    }

    private void addDataToChart(ArrayList<PieEntry> yValues, List<BalanceItemResult> balanceItems) {
        for (int i = 0; i < balanceItems.size(); i++) {
            float val = Float.parseFloat(balanceItems.get(i).getValue());
            total_values += val;
            yValues.add(new PieEntry(val, balanceItems.get(i).getName(), i));
        }
    }

    private void setChart(ArrayList<PieEntry> yValues) {
        PieDataSet dataSet = new PieDataSet(yValues, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(Color.rgb(230, 32, 26),
                Color.rgb(49, 93, 158),
                Color.rgb(251, 167, 70),
                Color.rgb(139, 147, 180),
                Color.rgb(70, 117, 69),
                Color.rgb(187, 160, 37));

        PieData data = new PieData(dataSet);
        data.setDrawValues(false); // disables values in pie pieces
        data.setValueTextSize(16);

        Legend l = balanceChart.getLegend();
        l.setEnabled(false); // disable legends

        balanceChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            @Override
            public void onValueSelected(Entry e, Highlight h) {
                PieEntry pe = (PieEntry) e;
                int index = (Integer) e.getData();
                int currentColor = dataSet.getColor(index);

                coinNameText.setText(pe.getLabel());
                coinNameText.setTextColor(currentColor);
                int id = getContext().getResources().getIdentifier("com.example.enigmaapp:drawable/" + pe.getLabel().toLowerCase(), null, null);
                coinIcon.setImageResource(id);

                // set percentage in center of the chart pie
                String value = decim.format(Double.valueOf(e.getY() / total_values * 100)) + "%";
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

        // set initial item to show
        int currentColor = dataSet.getColor(0);

        String label = dataSet.getEntryForIndex(0).getLabel();
        coinNameText.setText(label);
        coinNameText.setTextColor(currentColor);

        if (context != null) {
            int id = context.getResources().getIdentifier("com.example.enigmaapp:drawable/" + label.toLowerCase(), null, null);
            coinIcon.setImageResource(id);

            // set percentage in center of the chart pie
            String value = decim.format(Double.valueOf((dataSet.getEntryForIndex(0).getY()) / total_values * 100)) + "%";
            balanceChart.setCenterText(value);
            balanceChart.setCenterTextColor(currentColor);
            balanceChart.setCenterTextSize(20f);
        }

        balanceChart.setData(data);
        balanceChart.setVisibility(View.VISIBLE);
    }
}