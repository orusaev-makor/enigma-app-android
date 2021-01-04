package com.example.enigmaapp.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.enigmaapp.R;
import com.example.enigmaapp.web.trade.dataset.TradeDatasetResult;

public class TradeFilterAdapter extends RecyclerView.Adapter<TradeFilterAdapter.FilterOptionHolder> {
    private TradeDatasetResult result = new TradeDatasetResult();

    @NonNull
    @Override
    public FilterOptionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.filter_item, parent, false);
        return new FilterOptionHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterOptionHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class FilterOptionHolder extends RecyclerView.ViewHolder {
        private TextView textViewFilter;

        public FilterOptionHolder(@NonNull View itemView) {
            super(itemView);
            textViewFilter = itemView.findViewById(R.id.filter_option);
        }
    }
}
