package com.example.enigmaapp.web.news;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.enigmaapp.R;

import java.util.ArrayList;

public class KeywordsAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<String> keywords;

    public KeywordsAdapter(Context context, ArrayList<String> keywords) {
        this.context = context;
        this.keywords = keywords;
        System.out.println("ADAPTER CREATED !!!!!!!!!!!!!!!!!!!! for key words : ) " + keywords);
    }

    @Override
    public int getCount() {
        return keywords.size();
    }

    @Override
    public Object getItem(int position) {
        return keywords.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.keyword_item, null);
        }

        TextView keywordText = convertView.findViewById(R.id.keyword_item_text);
        keywordText.setText(keywords.get(position));
        System.out.println("---------------------------- keywordText in adpter set as: " + keywordText.getText());
        return convertView;
    }
}
