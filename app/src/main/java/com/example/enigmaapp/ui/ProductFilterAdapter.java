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
import com.example.enigmaapp.web.trade.dataset.TradeDatasetProduct;

import static com.example.enigmaapp.activity.fragment.BatchSelectFilterFragment.lastBatchProductPos;
import static com.example.enigmaapp.activity.fragment.MultiSelectFilterFragment.lastTradeProductPos;

public class ProductFilterAdapter extends ListAdapter<TradeDatasetProduct, ProductFilterAdapter.ProductOptionHolder> {

    private int lastCheckedPos = 0;
    private OnItemClickListener listener;
    private Context context;
    private Boolean isTradeFilter;

    public ProductFilterAdapter(Context context, Boolean isTradeFilter) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.isTradeFilter = isTradeFilter;
    }

    public int getLastCheckedPos() {
        return lastCheckedPos;
    }

    public void setLastCheckedPos(int lastCheckedPos) {
        this.lastCheckedPos = lastCheckedPos;
    }

    private static final DiffUtil.ItemCallback<TradeDatasetProduct> DIFF_CALLBACK = new DiffUtil.ItemCallback<TradeDatasetProduct>() {
        @Override
        public boolean areItemsTheSame(@NonNull TradeDatasetProduct oldItem, @NonNull TradeDatasetProduct newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull TradeDatasetProduct oldItem, @NonNull TradeDatasetProduct newItem) {
            return oldItem.getIsChecked() == newItem.getIsChecked();
        }
    };

    @NonNull
    @Override
    public ProductOptionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.filter_item, parent, false);
        return new ProductOptionHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductOptionHolder holder, int position) {
        TradeDatasetProduct currentProduct = getItem(position);

        holder.textViewProductName.setText(currentProduct.getName());
        if (isTradeFilter && currentProduct.getIsChecked() && lastCheckedPos == position || lastTradeProductPos == position) {
            holder.checkedIcon.setVisibility(View.VISIBLE);
            holder.textViewProductName.setTextColor(context.getResources().getColor(R.color.textColor));
        } else if (!isTradeFilter && currentProduct.getIsChecked() && lastCheckedPos == position || lastBatchProductPos == position) {
            holder.checkedIcon.setVisibility(View.VISIBLE);
            holder.textViewProductName.setTextColor(context.getResources().getColor(R.color.textColor));
        }else{
            holder.checkedIcon.setVisibility(View.INVISIBLE);
            holder.textViewProductName.setTextColor(context.getResources().getColor(R.color.textSecondaryColor));
        }
    }

    class ProductOptionHolder extends RecyclerView.ViewHolder {
        private TextView textViewProductName;
        private ImageView checkedIcon;

        public ProductOptionHolder(@NonNull View itemView) {
            super(itemView);
            textViewProductName = itemView.findViewById(R.id.filter_option_name);
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
        void onItemClick(TradeDatasetProduct item, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
