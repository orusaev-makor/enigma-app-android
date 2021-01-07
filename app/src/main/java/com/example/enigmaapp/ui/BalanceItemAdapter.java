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
import com.example.enigmaapp.web.accounts.AccountsItemResult;
import com.example.enigmaapp.web.balance.BalanceItemResult;

public class BalanceItemAdapter extends ListAdapter<BalanceItemResult, BalanceItemAdapter.ItemHolder> {

    private Context context;

    public BalanceItemAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
    }

    private static final DiffUtil.ItemCallback<BalanceItemResult> DIFF_CALLBACK = new DiffUtil.ItemCallback<BalanceItemResult>() {
        @Override
        public boolean areItemsTheSame(@NonNull BalanceItemResult oldItem, @NonNull BalanceItemResult newItem) {
            return oldItem.getName() == newItem.getName();
        }

        @Override
        public boolean areContentsTheSame(@NonNull BalanceItemResult oldItem, @NonNull BalanceItemResult newItem) {
            return oldItem.getValue().equals(newItem.getValue());
        }
    };

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.balance_item, parent, false);
        return new ItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        BalanceItemResult currentBalance = getItem(position);

        holder.balanceName.setText(currentBalance.getName());
        String currentValue = currentBalance.getValue();
        String LargeVal = currentValue.substring(0, currentValue.indexOf("."));
        String SmallVal = currentValue.substring(currentValue.indexOf("."));

        holder.balanceValueLarge.setText(LargeVal);
        holder.balanceValueSmall.setText(SmallVal);

        // change negative balance to red:
        if (currentValue.startsWith("-")) {
            holder.balanceValueLarge.setTextColor(context.getResources().getColor(R.color.red));
            holder.balanceValueSmall.setTextColor(context.getResources().getColor(R.color.red));
        }

        String coinName = currentBalance.getName().toLowerCase();
        int id = context.getResources().getIdentifier("com.example.enigmaapp:drawable/" + coinName, null, null);
        holder.balanceIcon.setImageResource(id);
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        private ImageView balanceIcon;
        private TextView balanceName;
        private TextView balanceValueLarge;
        private TextView balanceValueSmall;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            balanceIcon = itemView.findViewById(R.id.balance_item_icon);
            balanceName = itemView.findViewById(R.id.balance_item_name);
            balanceValueLarge = itemView.findViewById(R.id.balance_item_value_large);
            balanceValueSmall = itemView.findViewById(R.id.balance_item_value_small);
        }
    }
}
