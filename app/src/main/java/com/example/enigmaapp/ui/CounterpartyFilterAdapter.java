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
import com.example.enigmaapp.web.dataset.DatasetCounterparty;

import static com.example.enigmaapp.activity.fragment.BatchSelectFilterFragment.lastBatchCounterpartyPos;

public class CounterpartyFilterAdapter extends ListAdapter<DatasetCounterparty, CounterpartyFilterAdapter.CounterpartyOptionHolder> {

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

    private static final DiffUtil.ItemCallback<DatasetCounterparty> DIFF_CALLBACK = new DiffUtil.ItemCallback<DatasetCounterparty>() {
        @Override
        public boolean areItemsTheSame(@NonNull DatasetCounterparty oldItem, @NonNull DatasetCounterparty newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull DatasetCounterparty oldItem, @NonNull DatasetCounterparty newItem) {
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
        DatasetCounterparty currentProduct = getItem(position);

        holder.textViewCounterpartyName.setText(currentProduct.getName());

        if (currentProduct.getIsChecked() && lastCheckedPos == position || lastBatchCounterpartyPos == position && isBatchFilter) {
            holder.checkedIcon.setVisibility(View.VISIBLE);
            holder.textViewCounterpartyName.setTextColor(context.getResources().getColor(R.color.textColor));
        } else {
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
        void onItemClick(DatasetCounterparty item, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
