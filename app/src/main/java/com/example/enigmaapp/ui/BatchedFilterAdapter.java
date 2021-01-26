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
import com.example.enigmaapp.web.dataset.Batched;
import com.example.enigmaapp.web.dataset.DatasetBatched;

import static com.example.enigmaapp.activity.fragment.TradeSelectFilterFragment.lastTradeBatchedPos;

public class BatchedFilterAdapter extends ListAdapter<Batched, BatchedFilterAdapter.ItemHolder> {

    private int lastCheckedPos = 0;
    private OnItemClickListener listener;
    private Context context;

    public BatchedFilterAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
    }

    public void setLastCheckedPos(int lastCheckedPos) {
        this.lastCheckedPos = lastCheckedPos;
    }

    private static final DiffUtil.ItemCallback<Batched> DIFF_CALLBACK = new DiffUtil.ItemCallback<Batched>() {
        @Override
        public boolean areItemsTheSame(@NonNull Batched oldItem, @NonNull Batched newItem) {
            return oldItem.getName() == newItem.getName();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Batched oldItem, @NonNull Batched newItem) {
            return oldItem.getIsChecked() == newItem.getIsChecked()
                    && oldItem.getValue().equals(newItem.getValue());
        }
    };

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.filter_item, parent, false);
        return new ItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        Batched currentExecutionType = getItem(position);

        holder.textViewBatchedName.setText(currentExecutionType.getName());
        if (currentExecutionType.getIsChecked() && lastCheckedPos == position || lastTradeBatchedPos == position) {
            holder.checkedIcon.setVisibility(View.VISIBLE);
            holder.textViewBatchedName.setTextColor(context.getResources().getColor(R.color.textColor));
        } else {
            holder.checkedIcon.setVisibility(View.INVISIBLE);
            holder.textViewBatchedName.setTextColor(context.getResources().getColor(R.color.textSecondaryColor));
        }
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        private TextView textViewBatchedName;
        private ImageView checkedIcon;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            textViewBatchedName = itemView.findViewById(R.id.filter_option_name);
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
        void onItemClick(Batched item, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
