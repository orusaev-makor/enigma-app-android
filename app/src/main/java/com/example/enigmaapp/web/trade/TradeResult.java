package com.example.enigmaapp.web.trade;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TradeResult {
    //    private TradeItemResult items;
    private TradePagingParams paging;
    //    private <ArrayList<TradeItemResult>> items;

    List<TradeItemResult> items;

    public List<TradeItemResult> getItems() {
        return items;
    }

    public void setItems(TradeItemResult items) {
        this.items = (List<TradeItemResult>) items;
    }

    public TradePagingParams getPaging() {
        return paging;
    }

    public void setPaging(TradePagingParams paging) {
        this.paging = paging;
    }
}
