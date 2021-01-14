package com.example.enigmaapp.ui;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.enigmaapp.R;
import com.example.enigmaapp.web.trade.TradeItemResult;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static com.example.enigmaapp.activity.fragment.TradeFragment.mTradeExpandedPosition;
import static com.example.enigmaapp.activity.fragment.TradeFragment.previousTradeExpandedPosition;

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
        TradeItemResult currentTrade = dataArrayList.get(position);

        final boolean isExpanded = position == mTradeExpandedPosition;
        holder.details.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.itemView.setActivated(isExpanded);

        DecimalFormat decim = new DecimalFormat("#,###0.00");

        Resources res = context.getResources();
        String packageName = context.getPackageName();

        int colorId = res.getIdentifier(currentTrade.getStatus(), "color", packageName);
        int desiredColor = res.getColor(colorId);

        if (isExpanded) {
            previousTradeExpandedPosition = position;
            holder.tradeId.setText(currentTrade.getTradeId());
            holder.batchId.setText((currentTrade.getBatchId() != null) ? currentTrade.getBatchId() : "-");
            holder.executionType.setText(currentTrade.getExecutionType());
            holder.status.setText(currentTrade.getStatus());
            holder.status.setTextColor(desiredColor);
            holder.nominal.setText(decim.format(Double.valueOf(currentTrade.getNominal())));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTradeExpandedPosition = isExpanded ? -1 : position;
                notifyItemChanged(previousTradeExpandedPosition);
                notifyItemChanged(position);
            }
        });

        holder.textViewProduct.setText(currentTrade.getProduct());
        holder.textViewPrice.setText(decim.format(Double.valueOf(currentTrade.getPrice())));

        String side = currentTrade.getSide();
        if (side.equalsIgnoreCase("BUY")) {
            int buyColor = context.getResources().getColor(R.color.green);
            holder.textViewProduct.setTextColor(buyColor);
            holder.textViewPrice.setTextColor(buyColor);
        } else {
            int sellColor = context.getResources().getColor(R.color.red);
            holder.textViewProduct.setTextColor(sellColor);
            holder.textViewPrice.setTextColor(sellColor);
        }

        holder.textViewDate.setText(String.valueOf(currentTrade.getDate()));

        String quantity = "QTY - " + decim.format(Double.valueOf(currentTrade.getQuantity()));
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
        private View details;

        // details fields:
        private TextView tradeId;
        private TextView batchId;
        private TextView executionType;
        private TextView status;
        private TextView nominal;

        public ItemHolder(View itemView) {
            super(itemView);
            textViewProduct = itemView.findViewById(R.id.trade_item_product);
            textViewPrice = itemView.findViewById(R.id.trade_item_price);
            textViewDate = itemView.findViewById(R.id.trade_item_date);
            textViewQuantity = itemView.findViewById(R.id.trade_item_quantity);
            details = itemView.findViewById(R.id.trade_item_expand_section);

            // details fields:
            tradeId = details.findViewById(R.id.details_trade_id);
            batchId = details.findViewById(R.id.details_trade_batch_id);
            executionType = details.findViewById(R.id.details_trade_execution_type);
            status = details.findViewById(R.id.details_trade_status);
            nominal = details.findViewById(R.id.details_trade_nominal);
        }
    }
}
