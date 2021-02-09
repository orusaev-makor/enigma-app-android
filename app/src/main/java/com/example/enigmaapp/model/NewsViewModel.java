package com.example.enigmaapp.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.enigmaapp.repository.NewsRepository;
import com.example.enigmaapp.web.news.NewsItemResult;

import java.util.HashMap;
import java.util.List;

public class NewsViewModel extends AndroidViewModel {

    private NewsRepository repository;

    public NewsViewModel(@NonNull Application application) {
        super(application);
        repository = new NewsRepository(application);
    }

    public void fetchNews(String token, String keyword) {
        repository.fetchNews(token, keyword);
    }

    public MutableLiveData<List<NewsItemResult>> getNews() {
        return repository.getNews();
    }
}
