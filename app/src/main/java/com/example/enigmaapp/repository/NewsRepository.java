package com.example.enigmaapp.repository;

import android.app.Application;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.enigmaapp.web.news.NewsItemResult;
import com.example.enigmaapp.web.RetrofitNewsClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsRepository {
    private MutableLiveData<List<NewsItemResult>> allNews = new MutableLiveData<>();
    private Application application;

    public NewsRepository(Application application) {
        this.application = application;
    }

    public MutableLiveData<List<NewsItemResult>> getNews() {
        return allNews;
    }

    public void setNews(MutableLiveData<List<NewsItemResult>> allNews) {
        this.allNews = allNews;
    }

    public void fetchNews(String token, String keyword) {
        Call<ArrayList<NewsItemResult>> call = RetrofitNewsClient.getInstance().getRetrofitInterface()
                .executeGetNews(token, keyword);
        call.enqueue(new Callback<ArrayList<NewsItemResult>>() {
            @Override
            public void onResponse(Call<ArrayList<NewsItemResult>> call, Response<ArrayList<NewsItemResult>> response) {
                if (!response.isSuccessful()) {
                    System.out.println("fetchAccounts - Code: " + response.code() + "Error: " + response.message());
                    return;
                }
                allNews.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<NewsItemResult>> call, Throwable t) {
                System.out.println("t.getMessage(): " + t.getMessage());
                Toast.makeText(application, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
