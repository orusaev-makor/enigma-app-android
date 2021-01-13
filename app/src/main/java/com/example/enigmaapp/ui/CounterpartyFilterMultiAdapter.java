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
import com.example.enigmaapp.web.dataset.DatasetCounterparty;

import java.util.ArrayList;

public class CounterpartyFilterMultiAdapter extends RecyclerView.Adapter<CounterpartyFilterMultiAdapter.CounterpartyOptionHolder> {

    private OnItemClickListener listener;
    private Context context;
    private ArrayList<DatasetCounterparty> counterparties;

    public CounterpartyFilterMultiAdapter(Context context, ArrayList<DatasetCounterparty> counterparties) {
        this.context = context;
        this.counterparties = counterparties;
    }

    public void setCounterparties(ArrayList<DatasetCounterparty> counterparties) {
        this.counterparties = new ArrayList<>();
        this.counterparties = counterparties;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CounterpartyOptionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.filter_item, parent, false);
        return new CounterpartyOptionHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CounterpartyOptionHolder holder, int position) {
        DatasetCounterparty currentCounterparty = counterparties.get(position);
        System.out.println("in bind view holder : " + currentCounterparty.getName() + " - is check? " + currentCounterparty.getIsChecked());
        holder.textViewCounterpartyName.setText(currentCounterparty.getName());
        if (currentCounterparty.getIsChecked()) {
            holder.checkedIcon.setVisibility(View.VISIBLE);
            holder.textViewCounterpartyName.setTextColor(context.getResources().getColor(R.color.textColor));
        } else {
            holder.checkedIcon.setVisibility(View.INVISIBLE);
            holder.textViewCounterpartyName.setTextColor(context.getResources().getColor(R.color.textSecondaryColor));
        }
    }

    @Override
    public int getItemCount() { return counterparties.size(); }

    public class CounterpartyOptionHolder extends RecyclerView.ViewHolder {
        private TextView textViewCounterpartyName;
        private ImageView checkedIcon;

        public CounterpartyOptionHolder(@NonNull View itemView) {
            super(itemView);
            textViewCounterpartyName = itemView.findViewById(R.id.filter_option_name);
            checkedIcon = itemView.findViewById(R.id.filter_option_checked_icon);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(counterparties.get(position), position);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(DatasetCounterparty item, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) { this.listener = listener; }

    // Getting all items
    public ArrayList<DatasetCounterparty> getAll() {
        return counterparties;
    }

    public ArrayList<DatasetCounterparty> getSelected() {
        ArrayList<DatasetCounterparty> selected = new ArrayList<>();
        for (int i = 0; i < counterparties.size(); i++) {
            if (counterparties.get(i).getIsChecked()) {
                selected.add(counterparties.get(i));
            }
        }
        return selected;
    }

    public void clearSelected() {
        for (int i = 0; i < counterparties.size(); i++) {
            if (counterparties.get(i).getIsChecked()) {
                counterparties.get(i).setIsChecked(false);
            }
            notifyDataSetChanged();
        }
    }
}
