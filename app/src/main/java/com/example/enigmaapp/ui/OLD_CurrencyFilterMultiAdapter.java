package com.example.enigmaapp.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.enigmaapp.R;
import com.example.enigmaapp.web.dataset.Currency;

import java.util.ArrayList;

public class OLD_CurrencyFilterMultiAdapter extends RecyclerView.Adapter<OLD_CurrencyFilterMultiAdapter.CurrencyOptionHolder> {
    private static final String TAG = "Currency_Filter_Adapter";
    private OnItemClickListener listener;
    private Context context;
    private ArrayList<Currency> currencies;

    public OLD_CurrencyFilterMultiAdapter(Context context, ArrayList<Currency> currencies) {
        this.context = context;
        this.currencies = currencies;
    }

    public void setCurrencies(ArrayList<Currency> currencies) {
        this.currencies = new ArrayList<>();
        this.currencies = currencies;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CurrencyOptionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.filter_item, parent, false);
        return new CurrencyOptionHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrencyOptionHolder holder, int position) {
        Currency currentCurrency = currencies.get(position);
        Log.d(TAG, "onBindViewHolder - current coin name: " + currentCurrency.getName() + " // is checked? " + currentCurrency.getIsChecked());
        holder.textViewCurrencyName.setText(currentCurrency.getName());
        if (currentCurrency.getIsChecked()) {
            holder.checkedIcon.setVisibility(View.VISIBLE);
            holder.textViewCurrencyName.setTextColor(context.getResources().getColor(R.color.textColor));
        } else {
            holder.checkedIcon.setVisibility(View.INVISIBLE);
            holder.textViewCurrencyName.setTextColor(context.getResources().getColor(R.color.textSecondaryColor));
        }
    }

    @Override
    public int getItemCount() {
        return currencies.size();
    }

    public class CurrencyOptionHolder extends RecyclerView.ViewHolder {
        private TextView textViewCurrencyName;
        private ImageView checkedIcon;

        public CurrencyOptionHolder(@NonNull View itemView) {
            super(itemView);
            textViewCurrencyName = itemView.findViewById(R.id.filter_option_name);
            checkedIcon = itemView.findViewById(R.id.filter_option_checked_icon);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(currencies.get(position), position);
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

    // Getting all items
    public ArrayList<Currency> getAll() {
        return currencies;
    }

    public ArrayList<Currency> getSelected() {
        ArrayList<Currency> selected = new ArrayList<>();
        for (int i = 0; i < currencies.size(); i++) {
            if (currencies.get(i).getIsChecked()) {
                selected.add(currencies.get(i));
            }
        }
        return selected;
    }

    public void clearSelected() {
        for (int i = 0; i < currencies.size(); i++) {
            if (currencies.get(i).getIsChecked()) {
                currencies.get(i).setIsChecked(false);
            }
            notifyDataSetChanged();
        }
    }
}
