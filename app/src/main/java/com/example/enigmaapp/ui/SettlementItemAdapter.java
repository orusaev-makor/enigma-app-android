package com.example.enigmaapp.ui;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.enigmaapp.R;
import com.example.enigmaapp.repository.SettlementRepository;
import com.example.enigmaapp.web.trade.TradeItemResult;

import java.util.ArrayList;

public class SettlementItemAdapter extends RecyclerView.Adapter<SettlementItemAdapter.ItemHolder> {

    private ArrayList<SettlementRepository.SettlementSummary> dataArrayList;
    private Context context;

    public SettlementItemAdapter(Context context,  ArrayList<SettlementRepository.SettlementSummary> dataArrayList) {
        this.context = context;
        this.dataArrayList = dataArrayList;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.settlement_item, parent, false);
        return new ItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        SettlementRepository.SettlementSummary currentItem = dataArrayList.get(position);

        // check if show Batch ID:
        if (currentItem.isBatch()) {
            holder.textViewBatchId.setText("ID " + currentItem.getBatchId());
            holder.textViewBatchId.setVisibility(View.VISIBLE);
        } else {
            holder.textViewBatchId.setVisibility(View.GONE);
        }

        holder.textViewProduct.setText(currentItem.getName());
        holder.textViewCounterparty.setText(currentItem.getCounterparty());

        // remove time from "sent at" and set the date:
        String date = currentItem.getDate().substring(0, currentItem.getDate().indexOf(" "));
        holder.textViewSentAt.setText(date);

        // set status and it's colour:
        String status = currentItem.getStatus();
        holder.textViewStatus.setText(status);
        int statusColor = 0;
        switch (status) {
            case "pending":
                statusColor = context.getResources().getColor(R.color.statusPending);
                break;
            case "validated":
                statusColor = context.getResources().getColor(R.color.statusValidated);
                break;
            case "rejected":
                statusColor = context.getResources().getColor(R.color.statusRejected);
                break;
            case "in sett":
                statusColor = context.getResources().getColor(R.color.statusInSet);
                break;
            case "settled":
                statusColor = context.getResources().getColor(R.color.statusSettled);
                break;
            default:
                break;
        }
        holder.textViewStatus.setTextColor(statusColor);

        // set bullet point by status colour:
        Drawable unwrappedDrawable = ContextCompat.getDrawable(context, R.drawable.ic_bullet_point).mutate();
        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
        DrawableCompat.setTint(wrappedDrawable, statusColor);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            holder.bulletPoint.setBackground(wrappedDrawable);
        } else {
            holder.bulletPoint.setBackgroundDrawable(wrappedDrawable);
        }
    }

    @Override
    public int getItemCount() { return dataArrayList.size(); }

    class ItemHolder extends RecyclerView.ViewHolder {
        private TextView textViewBatchId;
        private TextView textViewProduct;
        private TextView textViewCounterparty;
        private TextView textViewSentAt;
        private TextView textViewStatus;
        private View bulletPoint;

        public ItemHolder(View itemView) {
            super(itemView);
            textViewBatchId = itemView.findViewById(R.id.settlement_item_id);
            textViewProduct = itemView.findViewById(R.id.settlement_item_product);
            textViewCounterparty = itemView.findViewById(R.id.settlement_item_counterparty);
            textViewSentAt = itemView.findViewById(R.id.settlement_item_sent_at);
            textViewStatus = itemView.findViewById(R.id.settlement_item_status);
            bulletPoint = itemView.findViewById(R.id.settlement_item_bullet_point);
        }
    }
}
