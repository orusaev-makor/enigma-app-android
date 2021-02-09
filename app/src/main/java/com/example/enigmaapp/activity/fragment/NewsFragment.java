package com.example.enigmaapp.activity.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.enigmaapp.R;
import com.example.enigmaapp.model.NewsViewModel;
import com.example.enigmaapp.ui.NewsItemAdapter;
import com.google.android.material.chip.Chip;

import java.util.HashMap;


public class NewsFragment extends Fragment {

    static NewsViewModel viewModel;
    static String token;

    private static HashMap<String, String> newsParams = new HashMap<>();
    private static String filterByKeyword;
    private static Chip filterChip;
    private static TextView topBorder;

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

        token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7ImlkIjoxLCJsZXZlbCI6MX0sImlhdCI6MTYxMjg1NzE0NCwiZXhwIjoxNjEyOTQzNTQ0fQ.bv3RTNLOsf1uv3H78iGO74jPbAPby7DY-LfHP8RCfe4";

        RecyclerView recyclerView = v.findViewById(R.id.news_parent_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        final NewsItemAdapter newsAdapter = new NewsItemAdapter(requireContext());
        recyclerView.setAdapter(newsAdapter);

        viewModel = new ViewModelProvider(requireActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(NewsViewModel.class);

        viewModel.getNews().observe(requireActivity(), newsItems -> newsAdapter.submitList(newsItems));

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
            filterChip.setVisibility(View.INVISIBLE);
        } else {
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
        viewModel.fetchNews(token, filterByKeyword);
    }
}