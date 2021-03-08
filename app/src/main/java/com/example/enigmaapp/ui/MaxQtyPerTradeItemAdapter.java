package com.example.enigmaapp.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.enigmaapp.R;
import com.example.enigmaapp.web.settings.ExposureLimitItemResult;
import com.example.enigmaapp.web.settings.MaxQtyPerTradeItemResult;

import java.text.DecimalFormat;

public class MaxQtyPerTradeItemAdapter extends ListAdapter<MaxQtyPerTradeItemResult, MaxQtyPerTradeItemAdapter.ItemHolder> {

    private Context context;

    public MaxQtyPerTradeItemAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
    }

    private static final DiffUtil.ItemCallback<MaxQtyPerTradeItemResult> DIFF_CALLBACK = new DiffUtil.ItemCallback<MaxQtyPerTradeItemResult>() {
        @Override
        public boolean areItemsTheSame(@NonNull MaxQtyPerTradeItemResult oldItem, @NonNull MaxQtyPerTradeItemResult newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull MaxQtyPerTradeItemResult oldItem, @NonNull MaxQtyPerTradeItemResult newItem) {
            return oldItem.getProduct().equals(newItem.getProduct()) &&
                    oldItem.getAmount().equals(newItem.getAmount());
        }
    };

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.settings_item, parent, false);
        return new MaxQtyPerTradeItemAdapter.ItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        MaxQtyPerTradeItemResult currentItem = getItem(position);

        holder.name.setText(currentItem.getProduct());
        String coinName = currentItem.getProduct().substring(0, 3).toLowerCase();

        int id = context.getResources().getIdentifier("com.example.enigmaapp:drawable/" + coinName, null, null);
        holder.image.setImageResource(id);

        DecimalFormat decim = new DecimalFormat("#,##0.0000");
        String formattedAmount = decim.format(Double.valueOf(currentItem.getAmount()));

        holder.amount.setText(formattedAmount);
    }

    public class ItemHolder extends RecyclerView.ViewHolder {

        private ImageView image;
        private TextView name, amount;

        public ItemHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.settings_item_image);
            name = itemView.findViewById(R.id.settings_item_name);
            amount = itemView.findViewById(R.id.settings_item_amount);
        }
    }
}
