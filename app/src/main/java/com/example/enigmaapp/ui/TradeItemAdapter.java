package com.example.enigmaapp.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.enigmaapp.R;
import com.example.enigmaapp.web.trade.TradeItemResult;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

//public class TradeItemAdapter extends ListAdapter<TradeItemResult, TradeItemAdapter.ItemHolder> {
public class TradeItemAdapter extends RecyclerView.Adapter<TradeItemAdapter.ItemHolder> {

    private ArrayList<TradeItemResult> dataArrayList;
    private Context context;

    public TradeItemAdapter(Context context, ArrayList<TradeItemResult> dataArrayList) {
        this.context = context;
        this.dataArrayList = dataArrayList;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trade_item, parent, false);
        return new ItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        TradeItemResult currentTradeItem = dataArrayList.get(position);
        holder.textViewProduct.setText(currentTradeItem.getProduct());

        DecimalFormat decim = new DecimalFormat("#,###.00");
        holder.textViewPrice.setText(decim.format(Double.valueOf(currentTradeItem.getPrice())));

        String side = currentTradeItem.getSide();
        if (side.equalsIgnoreCase("BUY")) {
            int buyColor = context.getResources().getColor(R.color.green);
            holder.textViewProduct.setTextColor(buyColor);
            holder.textViewPrice.setTextColor(buyColor);
        } else {
            int sellColor = context.getResources().getColor(R.color.red);
            holder.textViewProduct.setTextColor(sellColor);
            holder.textViewPrice.setTextColor(sellColor);
        }

        holder.textViewDate.setText(String.valueOf(currentTradeItem.getDate()));

        String quantity = "QTY - " + decim.format(Double.valueOf(currentTradeItem.getQuantity()));
        holder.textViewQuantity.setText(quantity);
    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        private TextView textViewProduct;
        private TextView textViewPrice;
        private TextView textViewDate;
        private TextView textViewQuantity;

        public ItemHolder(View itemView) {
            super(itemView);
            textViewProduct = itemView.findViewById(R.id.trade_item_product);
            textViewPrice = itemView.findViewById(R.id.trade_item_price);
            textViewDate = itemView.findViewById(R.id.trade_item_date);
            textViewQuantity = itemView.findViewById(R.id.trade_item_quantity);
        }
    }
}
