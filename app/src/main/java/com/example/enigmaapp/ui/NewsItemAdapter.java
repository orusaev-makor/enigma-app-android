package com.example.enigmaapp.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.enigmaapp.R;
import com.example.enigmaapp.web.news.NewsItemResult;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.shape.ShapeAppearanceModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.enigmaapp.activity.fragment.NewsFragment.fetchNews;
import static com.example.enigmaapp.activity.fragment.NewsFragment.toggleFilterChip;
import static com.example.enigmaapp.activity.fragment.NewsFragment.updateFilterKeyword;

public class NewsItemAdapter extends ListAdapter<NewsItemResult, NewsItemAdapter.ItemHolder> {

    public static final long AVERAGE_MONTH_IN_MILLIS = DateUtils.DAY_IN_MILLIS * 30;
    private OnItemClickListener listener;
    private Context context;

    RecyclerView.LayoutManager manager;

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
        ArrayList<String> keywords = new ArrayList<>();
        holder.keywordsChipGroup.removeAllViews();

        NewsItemResult currentNewsItem = getItem(position);

        String dateStr = currentNewsItem.getDate();

        DateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
        Date date = null;
        try {
            date = format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        System.out.println(" date::::::::::::::::: " + date); // Sat Jan 02 00:00:00 GMT 2010
        long mills = date.getTime();
        String formattedTimeAgo = getRelationTime(mills);
//        System.out.println(" formatted::::::::::::::::: " + formattedTimeAgo); // Sat Jan 02 00:00:00 GMT 2010
        String text = currentNewsItem.getTitle();

        holder.date.setText(formattedTimeAgo);
        holder.title.setText(text);

        String[] keys = currentNewsItem.getKeywords().split(",");
        for (String k : keys) {
            if (k.trim().equals("")) break;
            keywords.add(k.trim());
        }

        if (keywords.size() > 0) {
            holder.keywordsTopBorder.setVisibility(View.VISIBLE);

            for (int i = 0; i < keywords.size(); i++) {
                Chip chip = new Chip(context);
                chip.setText(keywords.get(i));
//                chip.setChipBackgroundColorResource(R.color.colorAccent);
//                chip.setHeight(15);
                chip.setCloseIconVisible(false);
                ShapeAppearanceModel shape = new ShapeAppearanceModel().withCornerSize(3);
                chip.setShapeAppearanceModel(shape);
                chip.setChipBackgroundColor(ColorStateList.valueOf(context.getResources().getColor(R.color.colorPrimary)));
                chip.setChipStrokeColor(ColorStateList.valueOf(context.getResources().getColor(R.color.cardBorder)));
                chip.setChipStrokeWidth(1);
                chip.setTextAppearance(R.style.ChipTextAppearance);

                chip.setOnClickListener(v -> {
                    updateFilterKeyword(chip.getText().toString());
                    toggleFilterChip();
                    fetchNews();
                });

                holder.keywordsChipGroup.addView(chip);
            }
        }

        holder.title.setOnClickListener(v -> {
            String webUrl = currentNewsItem.getLink();

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

    private String getRelationTime(long time) {
        final long now = new Date().getTime();
        final long delta = now - time;
        long resolution;
        if (delta <= DateUtils.MINUTE_IN_MILLIS) {
            resolution = DateUtils.SECOND_IN_MILLIS;
        } else if (delta <= DateUtils.HOUR_IN_MILLIS) {
            resolution = DateUtils.MINUTE_IN_MILLIS;
        } else if (delta <= DateUtils.DAY_IN_MILLIS) {
            resolution = DateUtils.HOUR_IN_MILLIS;
        } else if (delta <= DateUtils.WEEK_IN_MILLIS) {
            resolution = DateUtils.DAY_IN_MILLIS;
        } else if (delta <= AVERAGE_MONTH_IN_MILLIS) {
            String ago = ((delta / DateUtils.WEEK_IN_MILLIS) > 1) ? " weeks ago" : " week ago";
            return ((int) (delta / DateUtils.WEEK_IN_MILLIS)) + ago;
        } else if (delta <= DateUtils.YEAR_IN_MILLIS) {
            String ago = ((delta / AVERAGE_MONTH_IN_MILLIS) > 1) ? " months ago" : " month ago";
            return ((int) (delta / AVERAGE_MONTH_IN_MILLIS)) + ago;
        } else {
            String ago = ((delta / DateUtils.YEAR_IN_MILLIS) > 1) ? " years ago" : " year ago";
            return ((int) (delta / DateUtils.YEAR_IN_MILLIS)) + ago;
        }
        return DateUtils.getRelativeTimeSpanString(time, now, resolution).toString();
    }


    public class ItemHolder extends RecyclerView.ViewHolder {
        private TextView date, title, keywordsTopBorder;
        private ChipGroup keywordsChipGroup;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.news_fragment_card_date);
            title = itemView.findViewById(R.id.news_fragment_card_text);
            keywordsTopBorder = itemView.findViewById(R.id.news_keywords_top_border);
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
