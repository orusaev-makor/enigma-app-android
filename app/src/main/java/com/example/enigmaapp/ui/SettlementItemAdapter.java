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

public class SettlementItemAdapter extends ListAdapter<SettlementRepository.SettlementSummary, SettlementItemAdapter.ItemHolder> {
    private OnItemClickListener listener;
    private Context context;

    public SettlementItemAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
    }

    private static final DiffUtil.ItemCallback<SettlementRepository.SettlementSummary> DIFF_CALLBACK = new DiffUtil.ItemCallback<SettlementRepository.SettlementSummary>() {
        @Override
        public boolean areItemsTheSame(@NonNull SettlementRepository.SettlementSummary oldItem, @NonNull SettlementRepository.SettlementSummary newItem) {
            return oldItem.getId() == newItem.getId() && oldItem.getBatchId() == newItem.getBatchId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull SettlementRepository.SettlementSummary oldItem, @NonNull SettlementRepository.SettlementSummary newItem) {
            return oldItem.getId() == newItem.getId() &&
                    oldItem.getBatchId() == newItem.getBatchId() &&
                    oldItem.getDate().equals(newItem.getDate()) &&
                    oldItem.getCounterparty().equals(newItem.getCounterparty()) &&
                    oldItem.getName().equals(newItem.getName()) &&
                    oldItem.isBatch() == newItem.isBatch() &&
                    oldItem.getStatus().equals(newItem.getStatus());
        }
    };

    @NonNull
    @Override
    public SettlementItemAdapter.ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.settlement_item, parent, false);
        return new ItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        SettlementRepository.SettlementSummary currentBatchItem = getItem(position);

        // check if show Batch ID:
        if (currentBatchItem.isBatch()) {
            holder.textViewBatchId.setText("ID " + currentBatchItem.getBatchId());
            holder.textViewBatchId.setVisibility(View.VISIBLE);
        } else {
            holder.textViewBatchId.setVisibility(View.GONE);
        }

        holder.textViewProduct.setText(currentBatchItem.getName());
        holder.textViewCounterparty.setText(currentBatchItem.getCounterparty());

        // remove time from "sent at" and set the date:
        String date = currentBatchItem.getDate().substring(0, currentBatchItem.getDate().indexOf(" "));
        holder.textViewSentAt.setText(date);

        // set status and it's colour:
        String status = currentBatchItem.getStatus();
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


            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getItem(position));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(SettlementRepository.SettlementSummary tradeItem);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
