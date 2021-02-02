package com.example.enigmaapp.activity.fragment;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.enigmaapp.R;

import static com.example.enigmaapp.activity.UserActivity.actionBar;


public class NewsFragment extends Fragment {

    private String[] mDate = {"Now", "5 Hours ago", "2 Days Ago"};
    private String[] mText = {"In this week's issue, we look at volume and open interest trends on retail spot and futures market, examine the recent regulatory actions against derivatives venues, and assess the implications for BTC in the medium-term.",
            "In this week's issue, we look at previous performances for BTC in Q4, assess to what extent there exists Q4-specific seasonality for BTC, and examine possible trend shifts through to the end . 2020.",
            "In this week's issue, we look at the recent evolution of correlations between BTC, US equities markets (as represented by the SPX index), and gold, and what ongoing trends mean for crypto markets in general as we move into a potentially. tumultous Q4."
    };


    public NewsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (actionBar != null) {
            actionBar.show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_news, container, false);

        return v;
    }
}
//
//    class NewsAdapter extends ArrayAdapter<String> {
//        Context context;
//        String rDate[];
//        String rText[];
//
//        NewsAdapter (Context c, String date[], String text[]) {
//            super(c, R.layout.news_item, date);
//            this.context = c;
//            this.rDate = date;
//            this.rText = text;
//        }
//
//        @NonNull
//        @Override
//        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//            LayoutInflater layoutInflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View row = layoutInflater.inflate(R.layout.news_item, parent, false);
//            TextView myDate = row.findViewById(R.id.news_fragment_card_date);
//            TextView myText = row.findViewById(R.id.news_fragment_card_text);
//
//            myDate.setText(rDate[position]);
//            myText.setText(rText[position]);
//            return row;
//        }
//    }