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
import com.example.enigmaapp.web.trade.dataset.TradeDatasetCounterparty;

public class CounterpartyFilterAdapter extends ListAdapter<TradeDatasetCounterparty, CounterpartyFilterAdapter.CounterpartyOptionHolder> {

    private int lastCheckedPos = 0;
    private OnItemClickListener listener;
    private Context context;

    public CounterpartyFilterAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
    }

    public int getLastCheckedPos() {
        return lastCheckedPos;
    }

    public void setLastCheckedPos(int lastCheckedPos) {
        this.lastCheckedPos = lastCheckedPos;
    }

    private static final DiffUtil.ItemCallback<TradeDatasetCounterparty> DIFF_CALLBACK = new DiffUtil.ItemCallback<TradeDatasetCounterparty>() {
        @Override
        public boolean areItemsTheSame(@NonNull TradeDatasetCounterparty oldItem, @NonNull TradeDatasetCounterparty newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull TradeDatasetCounterparty oldItem, @NonNull TradeDatasetCounterparty newItem) {
            return oldItem.getName().equals(newItem.getName())
                    && oldItem.getIsChecked() == newItem.getIsChecked();
        }
    };


    @NonNull
    @Override
    public CounterpartyOptionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.filter_item, parent, false);
        return new CounterpartyOptionHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CounterpartyOptionHolder holder, int position) {

    }

    public class CounterpartyOptionHolder extends RecyclerView.ViewHolder {

        private TextView textViewCounterpartyName;
        private ImageView checkedIcon;

        public CounterpartyOptionHolder(@NonNull View itemView) {
            super(itemView);
            textViewCounterpartyName = itemView.findViewById(R.id.filter_option_name);
            checkedIcon = itemView.findViewById(R.id.filter_option_checked_icon);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position), position);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(TradeDatasetCounterparty item, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
