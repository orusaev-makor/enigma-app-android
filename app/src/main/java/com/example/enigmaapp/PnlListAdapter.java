package com.example.enigmaapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

class PnlListAdapter extends ArrayAdapter<String> {
    Context context;
    String rDate[];
    float rAmounts[];

    PnlListAdapter (Context c, String date[], float amount[]) {
        super(c, R.layout.pnl_list_row, date);
        this.context = c;
        this.rDate = date;
        this.rAmounts = amount;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.pnl_list_row, parent, false);
        TextView myDate = row.findViewById(R.id.pnl_date_text_view);
        TextView myAmount = row.findViewById(R.id.pnl_amount_text_view);

        myDate.setText(rDate[position]);
        myAmount.setText(String.valueOf(rAmounts[position]));
        return row;
    }
}