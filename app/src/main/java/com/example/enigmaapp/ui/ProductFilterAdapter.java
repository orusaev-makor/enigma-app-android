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

public class ProductFilterAdapter extends ListAdapter<TradeDatasetProduct, ProductFilterAdapter.ProductOptionHolder> {

    private OnItemClickListener listener;
    private Context context;

    public ProductFilterAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
    }

    private static final DiffUtil.ItemCallback<TradeDatasetProduct> DIFF_CALLBACK = new DiffUtil.ItemCallback<TradeDatasetProduct>() {
        @Override
        public boolean areItemsTheSame(@NonNull TradeDatasetProduct oldItem, @NonNull TradeDatasetProduct newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull TradeDatasetProduct oldItem, @NonNull TradeDatasetProduct newItem) {
            return oldItem.getName().equals(newItem.getName());
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
        if (currentProduct.getIsChecked()) {
            holder.checkedIcon.setVisibility(View.VISIBLE);
            holder.textViewProductName.setTextColor(context.getResources().getColor(R.color.textColor));
        } else {
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
                    listener.onItemClick(getItem(position));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(TradeDatasetProduct tradeItem);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
