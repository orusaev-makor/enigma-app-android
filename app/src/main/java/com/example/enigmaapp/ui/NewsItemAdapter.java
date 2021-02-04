package com.example.enigmaapp.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.enigmaapp.R;
import com.example.enigmaapp.web.news.NewsItemResult;

import java.util.ArrayList;

public class NewsItemAdapter extends ListAdapter<NewsItemResult, NewsItemAdapter.ItemHolder> {

    private OnItemClickListener listener;
    private Context context;
    int repeat = 0;
    private ArrayList<String> yourArray = new ArrayList<>();

    public NewsItemAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
        for (int i = 0; i < 6; i++) {
            yourArray.add("BTC-test-" + i);
        }
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

        holder.date.setText(date);
        holder.text.setText(text);
//        holder.keywordsLayout.setLayoutParams(new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//        ));


        if (repeat <= yourArray.size()) {
            for (int i = 0; i < 2; i++) {
                repeat++;
                TextView valueTV = new TextView(context.getApplicationContext());
                valueTV.setText(yourArray.get(i));
                valueTV.setTextSize(13);
                valueTV.setTextColor(context.getResources().getColor(R.color.textColor));
                valueTV.setBackground(context.getResources().getDrawable(R.drawable.underline_text));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    valueTV.setTypeface(context.getResources().getFont(R.font.poppins_regular));
                }
                valueTV.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                holder.keywordsLayout.addView(valueTV);

                if (repeat != yourArray.size()) {
                    Space space = new Space(context.getApplicationContext());
                    space.setMinimumWidth(20);
                    holder.keywordsLayout.addView(space);
                }
            }
        }

        holder.itemView.setOnClickListener(v -> {
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
        private LinearLayout keywordsLayout;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.news_fragment_card_date);
            text = itemView.findViewById(R.id.news_fragment_card_text);
            keywordsLayout = itemView.findViewById(R.id.news_keywords_layout);

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
