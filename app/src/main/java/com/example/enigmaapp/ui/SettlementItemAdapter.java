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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.enigmaapp.R;
import com.example.enigmaapp.repository.SettlementRepository;
import com.example.enigmaapp.web.trade.TradeItemResult;

import java.util.ArrayList;

import static com.example.enigmaapp.activity.fragment.SettlementFragment.mExpandedPosition;
import static com.example.enigmaapp.activity.fragment.SettlementFragment.previousExpandedPosition;

public class SettlementItemAdapter extends RecyclerView.Adapter<SettlementItemAdapter.ItemHolder> {

    private ArrayList<SettlementRepository.SettlementSummary> dataArrayList;
    private Context context;

    public SettlementItemAdapter(Context context, ArrayList<SettlementRepository.SettlementSummary> dataArrayList) {
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

        final boolean isExpanded = position == mExpandedPosition;

        // open details section for unitary settlements only:
        holder.details.setVisibility((isExpanded && !currentItem.isBatch()) ? View.VISIBLE : View.GONE);
        holder.itemView.setActivated(isExpanded);

        if (isExpanded && !currentItem.isBatch()) {
            previousExpandedPosition = position;
            holder.settlementBatchId.setText(currentItem.getBatchId());
            holder.settlementId.setText(currentItem.getId());
            holder.side.setText(currentItem.getSide());
            holder.type.setText(currentItem.getType());
            holder.amount.setText(currentItem.getAmount());
            holder.wallet.setText("???");
            holder.settledAmount.setText(currentItem.getSettledAmount());
            holder.openAmount.setText("???");
            holder.counterpartyAccount.setText(currentItem.getCounterpartyAccount());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpandedPosition = isExpanded ? -1 : position;
                notifyItemChanged(previousExpandedPosition);
                notifyItemChanged(position);
            }
        });

        // check if show Batch ID:
        holder.textViewBatchId.setText(currentItem.isBatch() ? "ID " + currentItem.getBatchId() : "");
        holder.textViewBatchId.setVisibility(currentItem.isBatch() ? View.VISIBLE : View.GONE);

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
    public int getItemCount() {
        return dataArrayList.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        private TextView textViewBatchId;
        private TextView textViewProduct;
        private TextView textViewCounterparty;
        private TextView textViewSentAt;
        private TextView textViewStatus;
        private View bulletPoint;
        private View details;

        // details fields:
        private TextView settlementBatchId;
        private TextView settlementId;
        private TextView side;
        private TextView type;
        private TextView amount;
        private TextView wallet;
        private TextView settledAmount;
        private TextView openAmount;
        private TextView counterpartyAccount;

        public ItemHolder(View itemView) {
            super(itemView);
            textViewBatchId = itemView.findViewById(R.id.settlement_item_id);
            textViewProduct = itemView.findViewById(R.id.settlement_item_product);
            textViewCounterparty = itemView.findViewById(R.id.settlement_item_counterparty);
            textViewSentAt = itemView.findViewById(R.id.settlement_item_sent_at);
            textViewStatus = itemView.findViewById(R.id.settlement_item_status);
            bulletPoint = itemView.findViewById(R.id.settlement_item_bullet_point);
            details = itemView.findViewById(R.id.settlement_item_expand_section);

            // details fields:
            settlementBatchId = details.findViewById(R.id.details_settlement_batch_id);
            settlementId = details.findViewById(R.id.details_settlement_id);
            side = details.findViewById(R.id.details_settlement_side);
            type = details.findViewById(R.id.details_settlement_type);
            amount = details.findViewById(R.id.details_settlement_amount);
            wallet = details.findViewById(R.id.details_settlement_wallet);
            settledAmount = details.findViewById(R.id.details_settlement_settled_amount);
            openAmount = details.findViewById(R.id.details_settlement_open_amount);
            counterpartyAccount = details.findViewById(R.id.details_settlement_counterparty_account);
        }
    }
}
