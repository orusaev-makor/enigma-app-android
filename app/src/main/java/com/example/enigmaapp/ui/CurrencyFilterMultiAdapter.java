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
import com.example.enigmaapp.web.dataset.Currency;

import static com.example.enigmaapp.activity.fragment.SettlementFragment.clickedCurrencies;

public class CurrencyFilterMultiAdapter extends ListAdapter<Currency, CurrencyFilterMultiAdapter.CurrencyOptionHolder> {
    private OnItemClickListener listener;
    private Context context;

    public CurrencyFilterMultiAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
    }

    private static final DiffUtil.ItemCallback<Currency> DIFF_CALLBACK = new DiffUtil.ItemCallback<Currency>() {
        @Override
        public boolean areItemsTheSame(@NonNull Currency oldItem, @NonNull Currency newItem) {
            return oldItem.getName() == newItem.getName();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Currency oldItem, @NonNull Currency newItem) {
            return oldItem.getIsChecked() == newItem.getIsChecked();
        }
    };

    @NonNull
    @Override
    public CurrencyOptionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.filter_item, parent, false);
        return new CurrencyOptionHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrencyOptionHolder holder, int position) {
        Currency currentCurrency = getItem(position);
        holder.currency.setText(currentCurrency.getName());
        if (currentCurrency.getIsChecked() || clickedCurrencies.indexOf(currentCurrency.getName()) != -1) {
            // need to update true if currency is in the clicked list due to last search:
            currentCurrency.setIsChecked(true);

            holder.checkedIcon.setVisibility(View.VISIBLE);
            holder.currency.setTextColor(context.getResources().getColor(R.color.textColor));
        } else {
            holder.checkedIcon.setVisibility(View.INVISIBLE);
            holder.currency.setTextColor(context.getResources().getColor(R.color.textSecondaryColor));
        }
    }

    public class CurrencyOptionHolder extends RecyclerView.ViewHolder {
        private TextView currency;
        private ImageView checkedIcon;

        public CurrencyOptionHolder(@NonNull View itemView) {
            super(itemView);
            currency = itemView.findViewById(R.id.filter_option_name);
            checkedIcon = itemView.findViewById(R.id.filter_option_checked_icon);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getItem(position), position);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Currency item, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
