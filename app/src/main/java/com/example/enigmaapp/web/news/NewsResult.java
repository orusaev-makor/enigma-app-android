package com.example.enigmaapp.web.news;

import java.util.ArrayList;

public class NewsResult {
    private ArrayList<NewsItemResult> articles;

    public ArrayList<NewsItemResult> getArticles() {
        return articles;
    }

    public void setArticles(ArrayList<NewsItemResult> articles) {
        this.articles = articles;
    }
}
