package com.example.enigmaapp.activity.fragment;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.enigmaapp.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;

import java.util.ArrayList;

import static com.example.enigmaapp.activity.UserActivity.actionBar;


public class StatisticsFragment extends Fragment {
    BarChart statisticsChart;

    public StatisticsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (actionBar != null) {
            actionBar.show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_statistics, container, false);

        final Typeface tfRegular = ResourcesCompat.getFont(getContext(), R.font.poppins_regular);
        statisticsChart = v.findViewById(R.id.statisticsBarChart);

        BarDataSet barDataSet1 = new BarDataSet(barEntries1(), "Nominal (1,000$)");
        barDataSet1.setColor(getResources().getColor(R.color.statisticsNominalBar));
        barDataSet1.setValueTextSize(0f);

        BarDataSet barDataSet2 = new BarDataSet(barEntries2(), "Trade Count (Unit)");
        barDataSet2.setColor(getResources().getColor(R.color.statisticsUnitBar));
        barDataSet2.setValueTextSize(0f);

        Legend l = statisticsChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setTextColor(getResources().getColor(R.color.textColor));
        l.setTypeface(tfRegular);
        l.setYOffset(-5f);
        l.setXOffset(10f);
        l.setXEntrySpace(15f);
        l.setTextSize(12f);

        BarData data = new BarData(barDataSet1, barDataSet2);
        statisticsChart.setData(data);
        statisticsChart.animateY(2000);
        statisticsChart.setHighlightPerTapEnabled(false);
        statisticsChart.setDragEnabled(true);
        statisticsChart.setFitBars(true);

        String[] years = new String[]{"2017", "2018", "2019", "2020"};
        XAxis xAxis = statisticsChart.getXAxis();
        xAxis.setTypeface(tfRegular);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(years));
        xAxis.setTextColor(getResources().getColor(R.color.textColor));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(true);

        YAxis leftAxis = statisticsChart.getAxisLeft();
        leftAxis.setTypeface(tfRegular);
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(false);
        leftAxis.setTextColor(getResources().getColor(R.color.textColor));
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        statisticsChart.getAxisRight().setEnabled(false);

        statisticsChart.setDrawBarShadow(false);
        statisticsChart.setDrawValueAboveBar(false);

        statisticsChart.getDescription().setEnabled(false);
        statisticsChart.setMaxVisibleValueCount(50);
        statisticsChart.setPinchZoom(false);
        statisticsChart.setDrawGridBackground(false);
        statisticsChart.setVisibleXRangeMaximum(10);

        float groupSpace = 0.53f;
        float barSpace = 0.06f;
        float barWidth = 0.14f;
        data.setBarWidth(barWidth);

        //IMPORTANT *****
        statisticsChart.getXAxis().setAxisMaximum(0);
        statisticsChart.getXAxis().setAxisMaximum(0 + statisticsChart.getBarData().getGroupWidth(groupSpace, barSpace) * 4); // 4 is the number of bars
        statisticsChart.getAxisLeft().setAxisMinimum(0);
        statisticsChart.groupBars(0.16f, groupSpace, barSpace);  // perform the "explicit" grouping
        //***** IMPORTANT

        statisticsChart.invalidate();

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