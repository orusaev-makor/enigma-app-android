package com.example.enigmaapp.activity.fragment;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.enigmaapp.R;
import com.example.enigmaapp.model.NewsViewModel;
import com.example.enigmaapp.ui.NewsItemAdapter;
import com.example.enigmaapp.web.news.NewsItemResult;

import java.util.List;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_news, container, false);

        RecyclerView recyclerView = v.findViewById(R.id.news_parent_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        final NewsItemAdapter newsAdapter = new NewsItemAdapter(requireContext());
        recyclerView.setAdapter(newsAdapter);

        NewsViewModel viewModel = new ViewModelProvider(requireActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(NewsViewModel.class);

        viewModel.getNews().observe(requireActivity(), newsItems -> newsAdapter.submitList(newsItems));

        String token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7ImlkIjoxLCJsZXZlbCI6MX0sImlhdCI6MTYxMjg1NzE0NCwiZXhwIjoxNjEyOTQzNTQ0fQ.bv3RTNLOsf1uv3H78iGO74jPbAPby7DY-LfHP8RCfe4";
        viewModel.fetchNews(token);

        return v;
    }
}