package com.example.enigmaapp.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.enigmaapp.R;
import com.example.enigmaapp.web.news.KeywordsAdapter;
import com.example.enigmaapp.web.news.NewsItemResult;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class NewsItemAdapter extends ListAdapter<NewsItemResult, NewsItemAdapter.ItemHolder> {

    private OnItemClickListener listener;
    private Context context;
    int repeat = 0;
    private ArrayList<String> keywords = new ArrayList<>();

    public NewsItemAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
    }

    private static final DiffUtil.ItemCallback<NewsItemResult> DIFF_CALLBACK = new DiffUtil.ItemCallback<NewsItemResult>() {
        @Override
        public boolean areItemsTheSame(@NonNull NewsItemResult oldItem, @NonNull NewsItemResult newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull NewsItemResult oldItem, @NonNull NewsItemResult newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getKeywords().equals(newItem.getKeywords()) &&
                    oldItem.getDate().equals(newItem.getDate());
        }
    };

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_item, parent, false);
        return new NewsItemAdapter.ItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        NewsItemResult currentNewsItem = getItem(position);

        String date = currentNewsItem.getDate();
        String text = currentNewsItem.getTitle();
        String[] keys = currentNewsItem.getKeywords().split(",");
        for (String k: keys) {
            if (k.trim().equals("")) break;
            keywords.add(k.trim());
        }

        System.out.println("______________________ Keywords.length: __________________________ " + keywords.size());
        System.out.println("______________________ Keywords: __________________________ " + keywords);
        holder.date.setText(date);
        holder.text.setText(text);


//        holder.keywordsLayout.setLayoutParams(new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//        ));

//        if (keywords.size() > 0 && repeat <= keywords.size()) {
//            for (int i = 0; i < keywords.size(); i++) {
//                repeat++;
//                TextView valueTV = new TextView(context.getApplicationContext());
//                valueTV.setText(keywords.get(i));
//                valueTV.setTextSize(13);
//                valueTV.setTextColor(context.getResources().getColor(R.color.textColor));
//                valueTV.setBackground(context.getResources().getDrawable(R.drawable.underline_text));
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    valueTV.setTypeface(context.getResources().getFont(R.font.poppins_regular));
//                }
//                valueTV.setLayoutParams(new LinearLayout.LayoutParams(
//                        LinearLayout.LayoutParams.WRAP_CONTENT,
//                        LinearLayout.LayoutParams.WRAP_CONTENT
//                ));
//                holder.keywordsLayout.addView(valueTV);
//
//                if (repeat != keywords.size()) {
//                    Space space = new Space(context.getApplicationContext());
//                    space.setMinimumWidth(20);
//                    holder.keywordsLayout.addView(space);
//                }
//            }
//        }

//        if (keywords.size() > 0) {
////            GridView keywordsGridView = v.findViewById(R.id.myGrid);
//            ArrayList<String> keywords2 = new ArrayList<>();
//            for (int i = 0; i < 7; i++) {
//                keywords.add("acbced " + Math.random() + " gqf i : " + i );
//            }
//
//            KeywordsAdapter adapter = new KeywordsAdapter(context, keywords2);
//            holder.keywordsGridView.setAdapter(adapter);
//
//            holder.keywordsGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    System.out.println(" ______________ you clicked: ______________ : " + keywords2.get(position));
//                }
//            });
//        }

        if (keywords.size() > 0) {
            for (int i = 0; i < keywords.size(); i++) {

            Chip chip = new Chip(context);
            chip.setText(keywords.get(i));
            chip.setChipBackgroundColorResource(R.color.colorAccent);
            chip.setCloseIconVisible(false);
            chip.setTextColor(context.getResources().getColor(R.color.white));
            chip.setTextAppearance(R.style.ChipTextAppearance);

            holder.keywordsChipGroup.addView(chip);
            }
        }
        keywords.clear();

        holder.text.setOnClickListener(v -> {
            String webUrl = currentNewsItem.getLink();
//            String webUrl = "https://ir.thomsonreuters.com/news-releases/news-release-details/thomson-reuters-announces-closing-sale-refinitiv-london-stock";

            // To open in browser:
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(webUrl));
            context.startActivity(intent);

            // To open in webview in the recycled card:
//            WebView web = new WebView(context.getApplicationContext());
//            web = holder.itemView.findViewById(R.id.myweb);
//            web.loadUrl(webUrl);

//            WebSettings mywebsettings = web.getSettings();
//            mywebsettings.setJavaScriptEnabled(true);

//            web.setWebViewClient(new WebViewClient());

            // improve webview performance:
//            web.getSettings().setAllowFileAccess(true);
//            web.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
//            web.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//            web.getSettings().setAppCacheEnabled(true);
//            web.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
//            mywebsettings.setDomStorageEnabled(true);
//            mywebsettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
//            mywebsettings.setUseWideViewPort(true);
//            mywebsettings.setSavePassword(true);
//            mywebsettings.setSaveFormData(true);
//            mywebsettings.setEnableSmoothTransition(true);
        });
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        private TextView date;
        private TextView text;
        private RelativeLayout keywordsLayout;
        private GridView keywordsGridView;
        private ChipGroup keywordsChipGroup;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.news_fragment_card_date);
            text = itemView.findViewById(R.id.news_fragment_card_text);
//            keywordsLayout = itemView.findViewById(R.id.news_keywords_layout);
//            keywordsGridView = itemView.findViewById(R.id.news_keywords_gridview);
            keywordsChipGroup = itemView.findViewById(R.id.news_keywords_chip_group);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getItem(position));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(NewsItemResult newsItem);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
