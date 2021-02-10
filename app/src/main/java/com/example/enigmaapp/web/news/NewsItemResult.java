package com.example.enigmaapp.web.news;

import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewsItemResult implements Comparable<NewsItemResult>{
    private int id;
    @SerializedName("source_id")
    private int sourceId;
    private String title;
    private String link;
    @SerializedName("iso_date")
    private String date;
    private String keywords;
    private String guid;
    @SerializedName("img_url")
    private String imgUrl;


    @Override
    public int compareTo(NewsItemResult other) {
        DateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");

        // convert date to compare
        Date myDate = null;
        try {
            myDate = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // convert other's date to compare
        Date otherDate = null;
        try {
            otherDate = format.parse(other.date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // returns in "new to old" order
        return otherDate.compareTo(myDate);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getKeywords() {
        return keywords;
    }

}
