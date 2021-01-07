package com.example.enigmaapp.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.enigmaapp.R;
import com.example.enigmaapp.web.accounts.AccountsItemResult;

public class AccountsItemAdapter extends ListAdapter<AccountsItemResult, AccountsItemAdapter.ItemHolder> {

    private OnItemClickListener listener;
    private Context context;
    private boolean isFiat;


    public AccountsItemAdapter(Context context, Boolean isFiat) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.isFiat = isFiat;
    }

    private static final DiffUtil.ItemCallback<AccountsItemResult> DIFF_CALLBACK = new DiffUtil.ItemCallback<AccountsItemResult>() {
        @Override
        public boolean areItemsTheSame(@NonNull AccountsItemResult oldItem, @NonNull AccountsItemResult newItem) {
            return oldItem.getAccountId() == newItem.getAccountId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull AccountsItemResult oldItem, @NonNull AccountsItemResult newItem) {
            return oldItem.getAccountDetails().equals(newItem.getAccountDetails()) &&
                    oldItem.getAccountName().equals(newItem.getAccountName()) &&
                    oldItem.getAccountType().equals(newItem.getAccountType()) &&
                    oldItem.getIsDefault() == (newItem.getIsDefault()) &&
                    oldItem.getCurrency().equals(newItem.getCurrency());
        }
    };


    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.account_item, parent, false);
        return new ItemHolder(itemView);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        AccountsItemResult currentAccountItem = getItem(position);

        String currency = currentAccountItem.getCurrency();
        String cryptoCurrency = currentAccountItem.getCryptoCurrency();

        if (isFiat && cryptoCurrency != null) return;
        if (!isFiat && currency != null) return;

        holder.accountName.setText(currentAccountItem.getAccountName());

        System.out.println(" in adapter - currency : " + currency + " -- cryptoCurrency: " + cryptoCurrency);

        if (currency != null) {
            switch (currency) {
                case "USD":
                    holder.currencyIcon.setImageResource(R.drawable.ic_usd);
                    break;
                case "EUR":
                    holder.currencyIcon.setImageResource(R.drawable.ic_eur);
                    break;
                default:
                    break;
            }
        } else {
//            switch (cryptoCurrency) {
//                case "USDT":
//                    // TODO: get USDT icon
//                    holder.currencyIcon.setImageResource(R.drawable.ic_usd);
//                    break;
//                case "USDC":
//                    // TODO: get USDC icon
//                    holder.currencyIcon.setImageResource(R.drawable.ic_eur);
//                    break;
//                case "BTC":
                    holder.currencyIcon.setImageResource(R.drawable.ic_btc);
//                    break;
//                default:
//                    break;
//            }
        }

        int starColor;

        if (currentAccountItem.getIsDefault() == 1) {
            starColor = context.getResources().getColor(R.color.starFavoriteMarked);
        } else {
            starColor = context.getResources().getColor(R.color.starFavoriteUnmarked);
        }


        // set bullet point by status colour:
        Drawable unwrappedDrawable = ContextCompat.getDrawable(context, R.drawable.ic_star).mutate();
        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
        DrawableCompat.setTint(wrappedDrawable, starColor);

        holder.favIcon.setBackground(wrappedDrawable);
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        private ImageView favIcon;
        private ImageView currencyIcon;
        private TextView accountName;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            favIcon = itemView.findViewById(R.id.account_item_fav_icon);
            currencyIcon = itemView.findViewById(R.id.account_item_currency_icon);
            accountName = itemView.findViewById(R.id.account_item_name);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getItem(position));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(AccountsItemResult accountsItem);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
