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
import com.example.enigmaapp.web.trade.dataset.TradeDatasetProduct;

import static com.example.enigmaapp.activity.fragment.BatchSelectFilterFragment.lastBatchCounterpartyPos;
import static com.example.enigmaapp.activity.fragment.BatchSelectFilterFragment.lastBatchProductPos;
import static com.example.enigmaapp.activity.fragment.MultiSelectFilterFragment.lastTradeProductPos;
import static com.example.enigmaapp.activity.fragment.UnitarySelectFilterFragment.lastUnitaryCounterpartyPos;

public class CounterpartyFilterAdapter extends ListAdapter<TradeDatasetCounterparty, CounterpartyFilterAdapter.CounterpartyOptionHolder> {

    private int lastCheckedPos = 0;
    private OnItemClickListener listener;
    private Context context;
    private Boolean isBatchFilter;

    public CounterpartyFilterAdapter(Context context, Boolean isBatchFilter) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.isBatchFilter = isBatchFilter;
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
        TradeDatasetCounterparty currentProduct = getItem(position);

        holder.textViewCounterpartyName.setText(currentProduct.getName());

        if (currentProduct.getIsChecked() && lastCheckedPos == position || lastBatchCounterpartyPos == position && isBatchFilter) {
            holder.checkedIcon.setVisibility(View.VISIBLE);
            holder.textViewCounterpartyName.setTextColor(context.getResources().getColor(R.color.textColor));


            // TODO: change it to multi select - for unitary filter ONLY
//        }  else if (currentProduct.getIsChecked() && !isBatchFilter)  {
        }  else if (currentProduct.getIsChecked() && lastCheckedPos == position || lastUnitaryCounterpartyPos == position && !isBatchFilter)  {
            holder.checkedIcon.setVisibility(View.VISIBLE);
            holder.textViewCounterpartyName.setTextColor(context.getResources().getColor(R.color.textColor));

        } else{
            holder.checkedIcon.setVisibility(View.INVISIBLE);
            holder.textViewCounterpartyName.setTextColor(context.getResources().getColor(R.color.textSecondaryColor));
        }
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
