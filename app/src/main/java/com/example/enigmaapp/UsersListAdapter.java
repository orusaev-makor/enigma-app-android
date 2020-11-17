package com.example.enigmaapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

class UsersListAdapter extends ArrayAdapter<String> {
    Context context;
    String rUsername[];
    int rId[];

    UsersListAdapter(Context c, String username[], int id[]) {
        super(c, R.layout.users_list_row, R.id.user_id_text_view, username);
        this.context = c;
        this.rUsername = username;
        this.rId = id;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.users_list_row, parent, false);
        TextView currentUsername = row.findViewById(R.id.user_name_text_view);
        TextView currentId = row.findViewById(R.id.user_id_text_view);

        currentUsername.setText(rUsername[position]);
        currentId.setText(String.valueOf(rId[position]));
        return row;
    }
}