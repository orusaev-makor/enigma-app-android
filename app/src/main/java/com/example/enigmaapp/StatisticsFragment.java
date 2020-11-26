package com.example.enigmaapp;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatisticsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatisticsFragment extends Fragment {

    BarChart barChart;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StatisticsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StatisticsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StatisticsFragment newInstance(String param1, String param2) {
        StatisticsFragment fragment = new StatisticsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
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
        View v = inflater.inflate(R.layout.fragment_statistics, container, false);

        barChart = (BarChart) v.findViewById(R.id.statisticsBarChart);

        BarDataSet barDataSet1 = new BarDataSet(barEntries1(), "Nominal (1,000$)");
        barDataSet1.setColor(Color.rgb(50, 171, 209));
        barDataSet1.setValueTextSize(0f);

        BarDataSet barDataSet2 = new BarDataSet(barEntries2(), "Trade Count (Unit)");
        barDataSet2.setColor(Color.rgb(175, 89, 212));
        barDataSet2.setValueTextSize(0f);

        BarData data = new BarData(barDataSet1, barDataSet2);
        barChart.setData(data);

        String[] years = new String[]{"2017", "2018", "2019", "2020"};
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(years));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setCenterAxisLabels(true);
        xAxis.setAxisMaximum(1);
//        xAxis.setGranularityEnabled(true);

        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(false);

//        mpBarChart.setDescription("");
        barChart.getDescription().setEnabled(false);
        barChart.setMaxVisibleValueCount(50);
        barChart.setPinchZoom(false);
        barChart.setDrawGridBackground(false);

        barChart.setDragEnabled(true);
        barChart.setVisibleXRangeMaximum(50);

        float groupSpace = 0.28f;
        float barSpace = 0.02f;
        float barWidth = 0.16f;
        data.setBarWidth(barWidth);


        //IMPORTANT *****
        barChart.getXAxis().setAxisMaximum(0);
        barChart.getXAxis().setAxisMaximum(0 + barChart.getBarData().getGroupWidth(groupSpace, barSpace) * 4); // 4 is the number of bars
        barChart.getAxisLeft().setAxisMinimum(0);
        barChart.groupBars(0, groupSpace, barSpace);  // perform the "explicit" grouping
        //***** IMPORTANT

        barChart.invalidate();

        return v;
    }

    private ArrayList<BarEntry> barEntries1() {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(1, 20));
        barEntries.add(new BarEntry(2, 15));
        barEntries.add(new BarEntry(3, 45));
        barEntries.add(new BarEntry(4, 30));
        return barEntries;
    }

    private ArrayList<BarEntry> barEntries2() {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(1, 22));
        barEntries.add(new BarEntry(2, 30));
        barEntries.add(new BarEntry(3, 25));
        barEntries.add(new BarEntry(4, 50));
        return barEntries;
    }

}