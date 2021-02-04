package com.example.enigmaapp.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.enigmaapp.repository.NewsRepository;
import com.example.enigmaapp.web.news.NewsItemResult;

import java.util.List;

public class NewsViewModel extends AndroidViewModel {

    private NewsRepository repository;

    public NewsViewModel(@NonNull Application app) {
        super(app);
        repository = new NewsRepository(app);
    }

    public void fetchNews(String token) {
        repository.fetchNews(token);
    }

    public MutableLiveData<List<NewsItemResult>> getNews() {
        return repository.getNews();
    }
}
