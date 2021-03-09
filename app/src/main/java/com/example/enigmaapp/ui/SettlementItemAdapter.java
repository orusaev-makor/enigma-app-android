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
import com.example.enigmaapp.repository.SettlementRepository;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static com.example.enigmaapp.activity.fragment.SettlementFragment.mSettlementExpandedPosition;
import static com.example.enigmaapp.activity.fragment.SettlementFragment.previousSettlementExpandedPosition;

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

        final boolean isExpanded = position == mSettlementExpandedPosition;

        // open details section for unitary settlements only:
        holder.details.setVisibility((isExpanded && !currentItem.isBatch()) ? View.VISIBLE : View.GONE);
        holder.itemView.setActivated(isExpanded);

        DecimalFormat decim = new DecimalFormat("#,###0.00");

        if (isExpanded && !currentItem.isBatch()) {
            previousSettlementExpandedPosition = position;
            holder.settlementBatchId.setText(currentItem.getBatchId());
            holder.settlementId.setText(currentItem.getId());
            holder.side.setText(currentItem.getSide());
            holder.type.setText(currentItem.getType());
            holder.amount.setText(decim.format(Double.valueOf(currentItem.getAmount())));
            // TODO: check what info need to be rendered here
            holder.wallet.setText("???");
            holder.settledAmount.setText(decim.format(Double.valueOf(currentItem.getSettledAmount())));
            holder.openAmount.setText("???");
            holder.counterpartyAccount.setText(currentItem.getCounterpartyAccount());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSettlementExpandedPosition = isExpanded ? -1 : position;
                notifyItemChanged(previousSettlementExpandedPosition);
                notifyItemChanged(position);
            }
        });

        // check if show Batch ID:
//        holder.batchId.setText(currentItem.isBatch() ? "ID " + currentItem.getBatchId() : "");
//        holder.batchId.setVisibility(currentItem.isBatch() ? View.VISIBLE : View.GONE);

        holder.product.setText(currentItem.getName());
        holder.counterparty.setText(currentItem.getCounterparty());

        // remove time from "sent at" and set the date:
        String date = currentItem.getDate().substring(0, currentItem.getDate().indexOf(" "));
        holder.sentAt.setText(date);

        // set status and it's colour:
        String status = currentItem.getStatus();

        Resources res = context.getResources();
        String packageName = context.getPackageName();
        int desiredColor;

        if (!status.equals("in sett")) {
            int colorId = res.getIdentifier(status, "color", packageName);
            desiredColor = res.getColor(colorId);
        } else {
            desiredColor = res.getColor(R.color.inSet);
        }

        holder.status.setTextColor(desiredColor);
        holder.status.setText(status);

        holder.status.setTextColor(desiredColor);

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
        private TextView batchId, product, counterparty, sentAt, status;
        private View bulletPoint, details;

        // details fields:
        private TextView settlementBatchId, settlementId, side, type, amount, wallet, settledAmount, openAmount, counterpartyAccount;

        public ItemHolder(View itemView) {
            super(itemView);
//            batchId = itemView.findViewById(R.id.settlement_item_id);
            product = itemView.findViewById(R.id.settlement_item_product);
            counterparty = itemView.findViewById(R.id.settlement_item_counterparty);
            sentAt = itemView.findViewById(R.id.settlement_item_sent_at);
            status = itemView.findViewById(R.id.settlement_item_status);
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
