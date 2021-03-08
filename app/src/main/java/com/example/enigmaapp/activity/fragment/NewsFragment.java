package com.example.enigmaapp.activity.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.enigmaapp.R;
import com.example.enigmaapp.model.NewsViewModel;
import com.example.enigmaapp.ui.NewsItemAdapter;
import com.google.android.material.chip.Chip;

import java.util.HashMap;

import static com.example.enigmaapp.repository.LoginRepository.mCurrentUser;


public class NewsFragment extends Fragment {

    static NewsViewModel viewModel;
    static String token;

    public static ProgressBar progressBarNews;
    private static HashMap<String, String> newsParams = new HashMap<>();
    private static String filterByKeyword;
    private static Chip filterChip;
    private static TextView noFilterText;

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

        token = "Bearer " + mCurrentUser.getToken();
        progressBarNews = v.findViewById(R.id.progress_bar_news);
        progressBarNews.setVisibility(View.VISIBLE);

        RecyclerView recyclerView = v.findViewById(R.id.news_parent_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        final NewsItemAdapter newsAdapter = new NewsItemAdapter(requireContext());
        recyclerView.setAdapter(newsAdapter);

        viewModel = new ViewModelProvider(requireActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(NewsViewModel.class);

        viewModel.getNews().observe(requireActivity(), newsItems -> newsAdapter.submitList(newsItems));

        noFilterText = v.findViewById(R.id.news_no_filter_text);
        filterChip = v.findViewById(R.id.news_filter_chip);
        filterChip.setOnCloseIconClickListener(v1 -> {
            updateFilterKeyword("");
            toggleFilterChip();
            fetchNews();
        });

        toggleFilterChip();

        fetchNews();

        return v;
    }

    public static void toggleFilterChip() {
        if (filterByKeyword == null) {
            noFilterText.setVisibility(View.VISIBLE);
            filterChip.setVisibility(View.GONE);
        } else {
            noFilterText.setVisibility(View.GONE);
            filterChip.setText(filterByKeyword);
            filterChip.setVisibility(View.VISIBLE);
        }
    }

    public static void updateFilterKeyword(String keyword) {
        if (keyword.equals("")) {
            filterByKeyword = null;
        } else {
            filterByKeyword = keyword;
        }
    }

    public static void fetchNews() {
        progressBarNews.setVisibility(View.VISIBLE);
        viewModel.fetchNews(token, filterByKeyword);
    }
}