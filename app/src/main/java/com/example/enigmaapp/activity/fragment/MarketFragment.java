package com.example.enigmaapp.activity.fragment;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.enigmaapp.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;

import static com.example.enigmaapp.activity.UserActivity.actionBar;


public class MarketFragment extends Fragment {

    LineChart marketChart;

    public MarketFragment() {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_market, container, false);

        marketChart = (LineChart) v.findViewById(R.id.marketItemLineChart);
        marketChart.getDescription().setEnabled(false);

        marketChart.setPinchZoom(false);
        marketChart.getAxisRight().setEnabled(false);
        marketChart.getAxisLeft().setEnabled(false);
        marketChart.setDrawGridBackground(false);
        marketChart.getXAxis().setEnabled(false);

        Legend l = marketChart.getLegend();
        l.setEnabled(false); // disable legends

        ArrayList<Entry> yValue = new ArrayList<>();

        yValue.add(new Entry(0, 60f));
        yValue.add(new Entry(1, 70f));
        yValue.add(new Entry(2, 65f));
        yValue.add(new Entry(3, 60f));
        yValue.add(new Entry(4, 70f));
        yValue.add(new Entry(5, 75f));

        LineDataSet lineDataSet = new LineDataSet(yValue, "");

        lineDataSet.setValueTextSize(0f);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setLineWidth(2f);
        lineDataSet.setColor(getResources().getColor(R.color.green));
        lineDataSet.setDrawFilled(true);
        if (Utils.getSDKInt() >= 18) {
            // fill drawable only supported on api level 18 and above
            Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.fade_green);
            lineDataSet.setFillDrawable(drawable);
        }
        else {
            lineDataSet.setFillColor(getResources().getColor(R.color.green));
        }

        lineDataSet.setGradientColor(getResources().getColor(R.color.green), Color.WHITE);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);

        LineData data = new LineData(dataSets);

        marketChart.setData(data);
        return v;
    }
}