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

import java.text.DecimalFormat;

public class ExposureLimitItemAdapter extends ListAdapter<ExposureLimitItemResult, ExposureLimitItemAdapter.ItemHolder> {

    private Context context;

    public ExposureLimitItemAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
    }

    private static final DiffUtil.ItemCallback<ExposureLimitItemResult> DIFF_CALLBACK = new DiffUtil.ItemCallback<ExposureLimitItemResult>() {
        @Override
        public boolean areItemsTheSame(@NonNull ExposureLimitItemResult oldItem, @NonNull ExposureLimitItemResult newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull ExposureLimitItemResult oldItem, @NonNull ExposureLimitItemResult newItem) {
            return oldItem.getCurrency().equals(newItem.getCurrency()) &&
                    oldItem.getAmount().equals(newItem.getAmount());
        }
    };

    @NonNull
    @Override
    public ExposureLimitItemAdapter.ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.settings_item, parent, false);
        return new ExposureLimitItemAdapter.ItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExposureLimitItemAdapter.ItemHolder holder, int position) {
        ExposureLimitItemResult currentItem = getItem(position);

        String coinName = currentItem.getCurrency().toLowerCase();
        holder.name.setText(coinName);

        int id = context.getResources().getIdentifier("com.example.enigmaapp:drawable/" + coinName, null, null);
        holder.image.setImageResource(id);

        DecimalFormat decim = new DecimalFormat("#,##0.##");
        String formattedAmount = decim.format(Double.valueOf(currentItem.getAmount()));

        holder.amount.setText(formattedAmount);

    }

    public class ItemHolder extends RecyclerView.ViewHolder {

        private ImageView image;
        private TextView name;
        private TextView amount;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.settings_item_image);
            name = itemView.findViewById(R.id.settings_item_name);
            amount = itemView.findViewById(R.id.settings_item_amount);
        }
    }
}
