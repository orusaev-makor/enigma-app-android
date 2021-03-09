package com.example.enigmaapp.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
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

        holder.product.setText(currentTrade.getProduct());
        holder.price.setText(decim.format(Double.valueOf(currentTrade.getPrice())));

        String side = currentTrade.getSide();
        if (side.equalsIgnoreCase("BUY")) {
            int buyColor = context.getResources().getColor(R.color.green);
            holder.product.setTextColor(buyColor);
            holder.price.setTextColor(buyColor);
        } else {
            int sellColor = context.getResources().getColor(R.color.red);
            holder.product.setTextColor(sellColor);
            holder.price.setTextColor(sellColor);
        }

        holder.date.setText(String.valueOf(currentTrade.getDate()));

        String quantity = "QTY - " + decim.format(Double.valueOf(currentTrade.getQuantity()));
        holder.quantity.setText(quantity);


        // set bullet point by status colour:
        Drawable unwrappedDrawable = ContextCompat.getDrawable(context, R.drawable.ic_bullet_point).mutate();
        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
        DrawableCompat.setTint(wrappedDrawable, desiredColor);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            holder.bulletPoint.setBackground(wrappedDrawable);
        } else {
            holder.bulletPoint.setBackgroundDrawable(wrappedDrawable);
        }
    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        private TextView product, price, date, quantity;
        private View bulletPoint, details;

        // details fields:
        private TextView tradeId, batchId, executionType, status, nominal;

        public ItemHolder(View itemView) {
            super(itemView);
            product = itemView.findViewById(R.id.trade_item_product);
            price = itemView.findViewById(R.id.trade_item_price);
            date = itemView.findViewById(R.id.trade_item_date);
            quantity = itemView.findViewById(R.id.trade_item_quantity);
            bulletPoint = itemView.findViewById(R.id.trade_item_bullet_point);
            details = itemView.findViewById(R.id.trade_item_expand_section);

            // details fields:
            executionType = details.findViewById(R.id.details_trade_execution_type);
            status = details.findViewById(R.id.details_trade_status);
            nominal = details.findViewById(R.id.details_trade_nominal);
        }
    }
}
