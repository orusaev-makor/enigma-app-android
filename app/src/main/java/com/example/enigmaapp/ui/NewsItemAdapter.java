package com.example.enigmaapp.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.enigmaapp.R;
import com.example.enigmaapp.web.news.KeywordsAdapter;
import com.example.enigmaapp.web.news.NewsItemResult;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.shape.ShapeAppearanceModel;

import java.util.ArrayList;

import static com.example.enigmaapp.activity.fragment.NewsFragment.fetchNews;
import static com.example.enigmaapp.activity.fragment.NewsFragment.toggleFilterChip;
import static com.example.enigmaapp.activity.fragment.NewsFragment.updateFilterKeyword;

public class NewsItemAdapter extends ListAdapter<NewsItemResult, NewsItemAdapter.ItemHolder> {

    private OnItemClickListener listener;
    private Context context;

    RecyclerView.LayoutManager manager;

    private ArrayList<String> keywords = new ArrayList<>();

    public NewsItemAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
        manager = new GridLayoutManager(context, 7, GridLayoutManager.VERTICAL, false);

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
        keywords.clear();

        NewsItemResult currentNewsItem = getItem(position);

        String date = currentNewsItem.getDate();
        String text = currentNewsItem.getTitle();

        holder.date.setText(date);
        holder.text.setText(text);

        String[] keys = currentNewsItem.getKeywords().split(",");
        for (String k : keys) {
            if (k.trim().equals("")) break;
            keywords.add(k.trim());
        }
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

        System.out.println("______________________ Keywords: __________________________ " + keywords);

//            KeywordsAdapter gridAdapter = new KeywordsAdapter(context, keywords);
//            holder.gridView.setAdapter(gridAdapter);


//            holder.gridView.setLayoutParams(new GridView.LayoutParams(
//                    GridLayoutManager.LayoutParams.MATCH_PARENT,
//                    GridLayoutManager.LayoutParams.MATCH_PARENT
//            ));


        if (keywords.size() > 0) {
            for (int i = 0; i < keywords.size(); i++) {

                Chip chip = new Chip(context);
                chip.setText(keywords.get(i));
//                chip.setChipBackgroundColorResource(R.color.colorAccent);
//                chip.setHeight(15);
                chip.setCloseIconVisible(false);
                ShapeAppearanceModel shape = new ShapeAppearanceModel().withCornerSize(3);
                chip.setShapeAppearanceModel(shape);
                chip.setChipBackgroundColor(ColorStateList.valueOf(context.getResources().getColor(R.color.colorPrimaryDark)));
                chip.setChipStrokeColor(ColorStateList.valueOf(context.getResources().getColor(R.color.navy)));
                chip.setChipStrokeWidth(1);
                chip.setTextAppearance(R.style.ChipTextAppearance);

                chip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println("Chip clicked !!!!!!!!!!!!!!!!!!! " + chip.getText().toString());
                        updateFilterKeyword(chip.getText().toString());
                        toggleFilterChip();
                        fetchNews();
                    }
                });

                holder.keywordsChipGroup.addView(chip);
            }
        }

//        if (keywords.size() > 0) {
//            LinearLayout row = (LinearLayout) holder.keywordsLayout.inflate(context, R.layout.keyword_row, null);
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            row.setLayoutParams(params);
//        }

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
        //        private GridView gridView;
//        private RecyclerView childRecyclerView;
        private ChipGroup keywordsChipGroup;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.news_fragment_card_date);
            text = itemView.findViewById(R.id.news_fragment_card_text);
//            gridView = itemView.findViewById(R.id.news_child_grid_view);
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
