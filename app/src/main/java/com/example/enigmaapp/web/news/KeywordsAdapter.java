package com.example.enigmaapp.web.news;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.example.enigmaapp.R;

import java.util.ArrayList;

public class KeywordsAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> keywords = new ArrayList<>();
    LayoutInflater inflater;

    public KeywordsAdapter(Context context, ArrayList<String> keywords) {
        this.context = context;
        this.keywords = keywords;
        inflater = LayoutInflater.from(context);
        System.out.println("Key words in child ::::::::::------:::::::::::::::: " + this.keywords);
    }

    @Override
    public int getCount() {
        System.out.println("Key words in child ::::::::::::----------------------:::::::::::::: " + keywords.size());
        return keywords.size();
    }

    @Override
    public Object getItem(int position) {
        return keywords.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
//        int screenWidth = metrics.widthPixels;

        convertView = inflater.inflate(R.layout.keyword_item, null);
//        convertView.setLayoutParams(new GridView.LayoutParams(screenWidth/2, screenWidth/2));
//        convertView.setPadding(8, 8, 8, 8);

//        int id = convertView.getResources().getIdentifier("com.example.enigmaapp:layout/key" + pe.getLabel().toLowerCase(), null, null);
        TextView keyText = convertView.findViewById(R.id.keyword_item_text);
        keyText.setText(keywords.get(position));
        System.out.println("current key in child :::::::::::::::::::::::::: " + keywords.get(position));
        return convertView;
    }
}
