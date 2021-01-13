package com.example.enigmaapp.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.enigmaapp.R;
import com.example.enigmaapp.web.dataset.DatasetCurrency;

import java.util.ArrayList;

public class CurrencyFilterMultiAdapter extends RecyclerView.Adapter<CurrencyFilterMultiAdapter.CurrencyOptionHolder> {

    private OnItemClickListener listener;
    private Context context;
    private ArrayList<DatasetCurrency> currencies;

    public CurrencyFilterMultiAdapter(Context context, ArrayList<DatasetCurrency> currencies) {
        this.context = context;
        this.currencies = currencies;
    }

    public void setCurrencies(ArrayList<DatasetCurrency> currencies) {
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
        DatasetCurrency currentCurrency = currencies.get(position);
        System.out.println("in bind view holder : " + currentCurrency.getName() + " - is check? " + currentCurrency.getIsChecked());
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
        void onItemClick(DatasetCurrency item, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // Getting all items
    public ArrayList<DatasetCurrency> getAll() {
        return currencies;
    }

    public ArrayList<DatasetCurrency> getSelected() {
        ArrayList<DatasetCurrency> selected = new ArrayList<>();
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
