package com.example.enigmaapp.web.trade;

import java.util.ArrayList;
import java.util.List;

public class TradeResult {

    private TradePagingParams paging;
    ArrayList<TradeItemResult> items;

    public List<TradeItemResult> getItems() {
        return items;
    }

    public void setItems(ArrayList<TradeItemResult> items) {
        this.items = items;
    }

    public TradePagingParams getPaging() {
        return paging;
    }

    public void setPaging(TradePagingParams paging) {
        this.paging = paging;
    }
}
