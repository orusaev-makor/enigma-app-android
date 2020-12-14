package com.example.enigmaapp.activity.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.enigmaapp.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BalanceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BalanceFragment extends Fragment {

    private PieChart balanceChart;
    private String clickedCoin;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BalanceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BalanceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BalanceFragment newInstance(String param1, String param2) {
        BalanceFragment fragment = new BalanceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //        Hides navbar on "Balance" view:
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

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
        View v = inflater.inflate(R.layout.fragment_balance, container, false);
        final Typeface tfRegular = ResourcesCompat.getFont(getContext(), R.font.poppins_regular);
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
                coinIcon.setImageResource(R.drawable.ic_btc);
                coinIcon.setColorFilter(currentColor);

                // set percentage in center of the chart pie
                String value = String.valueOf(e.getY()) + "%";
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
        return v;
    }
}