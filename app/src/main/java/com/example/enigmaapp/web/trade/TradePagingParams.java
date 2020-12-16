package com.example.enigmaapp.web.trade;

import com.google.gson.annotations.SerializedName;

public class TradePagingParams {

    @SerializedName("total_items")
    private String totalItems;
    @SerializedName("number_of_pages")
    private String numberOfPages;
    @SerializedName("current_page")
    private String currentPage;
    private String sort;
    @SerializedName("items_per_page")
    private String itemsPerPage;

    public TradePagingParams(String totalItems, String numberOfPages, String currentPage, String sort, String itemsPerPage) {
        this.totalItems = totalItems;
        this.numberOfPages = numberOfPages;
        this.currentPage = currentPage;
        this.sort = sort;
        this.itemsPerPage = itemsPerPage;
    }
}
