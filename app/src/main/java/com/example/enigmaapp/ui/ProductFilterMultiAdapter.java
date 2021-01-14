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
import com.example.enigmaapp.web.dataset.DatasetProduct;

import java.util.ArrayList;

public class ProductFilterMultiAdapter extends RecyclerView.Adapter<ProductFilterMultiAdapter.ProductOptionHolder> {

    private OnItemClickListener listener;
    private Context context;
    private ArrayList<DatasetProduct> products;

    public ProductFilterMultiAdapter(Context context, ArrayList<DatasetProduct> products) {
        this.context = context;
        this.products = products;
    }

    public void setProducts(ArrayList<DatasetProduct> products) {
        this.products = new ArrayList<>();
        this.products = products;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductOptionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.filter_item, parent, false);
        return new ProductOptionHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductOptionHolder holder, int position) {
        DatasetProduct currentCurrency = products.get(position);
        holder.textViewProductName.setText(currentCurrency.getName());
        if (currentCurrency.getIsChecked()) {
            holder.checkedIcon.setVisibility(View.VISIBLE);
            holder.textViewProductName.setTextColor(context.getResources().getColor(R.color.textColor));
        } else {
            holder.checkedIcon.setVisibility(View.INVISIBLE);
            holder.textViewProductName.setTextColor(context.getResources().getColor(R.color.textSecondaryColor));
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ProductOptionHolder extends RecyclerView.ViewHolder {
        private TextView textViewProductName;
        private ImageView checkedIcon;

        public ProductOptionHolder(@NonNull View itemView) {
            super(itemView);
            textViewProductName = itemView.findViewById(R.id.filter_option_name);
            checkedIcon = itemView.findViewById(R.id.filter_option_checked_icon);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(products.get(position), position);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(DatasetProduct item, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // Getting all items
    public ArrayList<DatasetProduct> getAll() {
        return products;
    }

    public ArrayList<DatasetProduct> getSelected() {
        ArrayList<DatasetProduct> selected = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getIsChecked()) {
                selected.add(products.get(i));
            }
        }
        return selected;
    }

    public void clearSelected() {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getIsChecked()) {
                products.get(i).setIsChecked(false);
                notifyItemChanged(i);
            }
        }
//        notifyDataSetChanged();
    }
}
